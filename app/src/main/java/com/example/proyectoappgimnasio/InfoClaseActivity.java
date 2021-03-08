package com.example.proyectoappgimnasio;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;

import Modelos.HorarioClase;
import SocketUtil.SocketHandler;

import static java.util.Calendar.*;

public class InfoClaseActivity extends AppCompatActivity implements View.OnClickListener, Serializable {

    //ELEMENTS LAYOUT
    private TextView tv_nombre;
    private TextView tv_descripccion;
    private TextView tv_hora;
    private TextView tv_aforoActual;
    private TextView tv_aforoMaximo;
    private TextView tv_nombreEntrenador;
    private ImageView imagenClase;

    private Button buttonReservar;
    private ImageButton buttonAtras;

    //CONEXION SOCKET
    Socket socket = null;
    public String HOST = "192.168.0.11";
    public int PUERTO = 9002;
    BufferedReader inSocket = null;
    PrintWriter outSocket = null;

    //INFO USUARIO Y CLASE SELECCIONADA
    HorarioClase horario;
    int idCliente;

    //** ON CREATE()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_clase);

        //Obtener datos del cliente, clase seleccionada y el socket.
        horario = (HorarioClase) getIntent().getSerializableExtra("horario");
        idCliente = getIntent().getIntExtra("idCliente", 0);
        obtenerFlujosSocket();


        //Referencia Elements Layout
        buttonReservar = (Button) findViewById(R.id.btn_reservar);
        buttonAtras = (ImageButton) findViewById(R.id.button_atras);
        imagenClase = (ImageView) findViewById(R.id.imagenClaseInfo);

        tv_nombre = (TextView) findViewById(R.id.nombreClaseInfo_tv);
        tv_descripccion = (TextView) findViewById(R.id.descripccionInfo_tv);
        tv_aforoActual = (TextView) findViewById(R.id.aforoActualInfo_tv);
        tv_aforoMaximo = (TextView) findViewById(R.id.aforoMaximo_tv);
        tv_nombreEntrenador = (TextView) findViewById(R.id.nombreEntrenadorInfo_tv);
        tv_hora = (TextView) findViewById(R.id.horaInfo_tv);

        comprobarAforo();
        //Listeners
        buttonReservar.setOnClickListener(this);
        buttonAtras.setOnClickListener(this);

        //Volcar datos horario
        tv_nombre.setText(ponerPrimeraLetraMayuscula(horario.getNombreClase()));
        tv_descripccion.setText(horario.getDescripccion());
        tv_nombreEntrenador.setText(horario.getNombreEntrenador());
        tv_aforoMaximo.setText(Integer.toString(horario.getAforoMaximo()));
        tv_aforoActual.setText(Integer.toString(horario.getAforoActual()));
        tv_hora.setText(horario.getHora());

        if (!horario.getRutaImgCompleta().equals("")) {
            Picasso.get().load(horario.getRutaImgCompleta()).placeholder(R.drawable.clase_default).into(imagenClase);
        }

        //new IniciarConexionAsynkTask().execute();

        /*CircleImageView imagenPrueba=(CircleImageView)findViewById(R.id.imgprueba);
        String url="http://192.168.0.11/ProyectoGimnasio_Server/fotos_usuarios/1.jpg";


        */


    }

    private class ReservarAsyncTask extends AsyncTask<Void, Void, String> {
        String resultado = "ERROR DESCONOCIDO";
        int idHorario;
        int idCliente;


        public ReservarAsyncTask(int idHorario, int idCliente) {
            this.idHorario = idHorario;
            this.idCliente = idCliente;
        }

        @Override
        protected String doInBackground(Void... voids) {

            if (!SocketHandler.isConexionEstablecida()) {
                resultado="Se ha perdido la conexion con el servidor, reinicie la aplicacion.";
            } else {
                //4. Si la conexion ha sido establecida correctamente, lanzar el login directamente
                try {
                    if (horario.getAforoActual() < horario.getAforoMaximo()) {
                        resultado = reservar(idHorario, idCliente);
                    } else {
                        resultado="No se puede reservar:AFORO MAXIMO SUPERADO.";
                    }


                } catch (Exception e) {
                    e.printStackTrace();

                    resultado = "Error con la conexion al servidor.Reinicie la app e intentelo de nuevo.";

                }

            }


            return resultado;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            //Log.e("Resultado", resultado);
            mostrarMensaje(resultado);

        }
    }

    private String reservar(int idHorario, int idCliente) {

        try {
            outSocket.write("M4-RESERVAR-HORARIOS:"+idCliente+","+idHorario+"\n");
            outSocket.flush();

            String respuestaServidor = inSocket.readLine();


            if (respuestaServidor.contains("S57-RESERVADA")){
                //TODO: Volver atras
                horario.setAforoActual(horario.getAforoActual()+1);
                finish();
                return"RESERVADA!";
            }
            else if(respuestaServidor.contains("S58-AFORO_COMPLETO")){
                horario.setAforoActual(horario.getAforoMaximo());
                tv_aforoActual.setText(horario.getAforoMaximo());
                tv_aforoMaximo.setText(horario.getAforoMaximo()+" ¡Aforo superado!");
                //buttonReservar.setEnabled(false); Peta
                return"Se te han adelantado! El aforo ha llegado a su liminte" ;

            }else if(respuestaServidor.contains("S59-YA_RESERVADA")){


                return "Ya tiene una reserva de esta clase.";
            }
            else return"Error de conexion con el servidor.";

        } catch (IOException ex) {
            return"Error de conexion con el servidor.";
        }


    }
    private void mostrarToast(String mensaje){
        Toast.makeText(InfoClaseActivity.this,mensaje, Toast.LENGTH_LONG).show();
    }


    private String ponerPrimeraLetraMayuscula(String name) {
        String s1 = name.substring(0, 1).toUpperCase();
        String nameCapitalized = s1 + name.substring(1);
        return nameCapitalized;

    }

    private void obtenerFlujosSocket() {
        socket = SocketHandler.getSocket();
        inSocket = SocketHandler.getInSocket();
        outSocket = SocketHandler.getOutSocket();

    }

    private void mostrarMensaje(String mensaje) {
        try {
            Toast.makeText(InfoClaseActivity.this, mensaje, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_reservar:

                if(!comprobarAforo()){

                    Toast.makeText(this,"Ya se ha alcanzado el aforo maximo establecido.", Toast.LENGTH_SHORT).show();
                }else{
                    if(!comprobarDiaValido()){
                        Toast.makeText(this,"Solo puede reservar con un maximo de 1 dia de antelacion la clase.", Toast.LENGTH_LONG).show();
                    }else{
                        new ReservarAsyncTask(horario.getIdHorario(), idCliente).execute();


                    }


                }


                break;
            case R.id.button_atras:
                finish();
                break;


        }
    }

    public boolean comprobarDiaValido() {
        //Solo se pueden reservar clases del mismo dia o un dia antes.
        boolean reservaValida=false;
        int diaActual = getInstance().get(DAY_OF_WEEK);
        int diaClase = horario.getDia();
        switch (diaActual) {
            case MONDAY:
                //Si es lunes, solo podra reservar clases del lunes (dia 1) o del martes (2), y asi sucesivamente
                if (diaClase == 1 || diaClase == 2) return true;
                break;

            case TUESDAY:

                if (diaClase == 2 || diaClase == 3) return true;
                break;

            case THURSDAY:
                if (diaClase == 3 || diaClase == 4) return true;
                break;

            case WEDNESDAY:
                if (diaClase == 4 || diaClase == 5) return true;
                break;

            case FRIDAY:
                if (diaClase == 5 || diaClase == 6) return true;
                break;

            case SATURDAY:
                if (diaClase == 6) return true;
                break;

            case SUNDAY:
                if (diaClase == 1) return true;
                break;

            default:
                return false;
        }

        return reservaValida;
    }

    public boolean comprobarAforo(){
        if(horario.getAforoActual()<horario.getAforoMaximo()){
            return true;
        }else{
            tv_aforoActual.setText(horario.getAforoMaximo());
            tv_aforoMaximo.setText(horario.getAforoMaximo()+" ¡Aforo superado!");
            buttonReservar.setEnabled(false);
            return false;
        }

    }


}