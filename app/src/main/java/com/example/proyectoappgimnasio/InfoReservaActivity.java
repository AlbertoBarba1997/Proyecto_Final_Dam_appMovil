package com.example.proyectoappgimnasio;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;

import SocketUtil.SocketHandler;

import static java.util.Calendar.getInstance;

public class InfoReservaActivity extends AppCompatActivity implements View.OnClickListener, Serializable {

    //ELEMENTS LAYOUT
    private TextView tv_nombreClase;
    private TextView tv_horaClase;
    private TextView tv_diaClase;
    private Button buttonCancelar;

    //CONEXION SOCKET
    Socket socket = null;
    public String HOST = "192.168.0.11";
    public int PUERTO = 9002;
    BufferedReader inSocket = null;
    PrintWriter outSocket = null;

    //INFO USUARIO Y CLASE SELECCIONADA
    int idReserva;
    String nombreClase;
    String horaClase;
    String diaClase;


    //** ON CREATE()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_reserva);

        //Obtener datos del cliente, clase seleccionada y el socket.
        idReserva = getIntent().getIntExtra("idReserva", 0);
        nombreClase= getIntent().getStringExtra("nombreClase");
        horaClase= getIntent().getStringExtra("horaClase");
        diaClase= getIntent().getStringExtra("diaClase");

        //Obtener el socket y sus flujos
        obtenerFlujosSocket();

        //Referenciar los elementos del layout
        buttonCancelar = (Button) findViewById(R.id.btn_cancelar);


        tv_nombreClase = (TextView) findViewById(R.id.nombreClaseInfoReserva_tv);
        tv_horaClase = (TextView) findViewById(R.id.horaInfoReserva_tv);
        tv_diaClase = (TextView) findViewById(R.id.diaReserva_tv);

        //Listeners
        buttonCancelar.setOnClickListener(this);

        //Volcar datos reserva
        tv_nombreClase.setText(nombreClase);
        tv_diaClase.setText(diaClase);
        tv_horaClase.setText(horaClase);


    }

    private class CancelarReservaAsyncTask extends AsyncTask<Void, Void, String> {
        String resultado = "ERROR DESCONOCIDO";
        int idReserva;

        public CancelarReservaAsyncTask(int idReserva) {
            this.idReserva = idReserva;

        }

        @Override
        protected String doInBackground(Void... voids) {
            Log.e("Empieza doInBackground","si");
            if (!SocketHandler.isConexionEstablecida()) {
                resultado = "Se ha perdido la conexion con el servidor, reinicie la aplicacion.";
            } else {

                //4. Si la conexion ha sido establecida correctamente, lanzar el login directamente
                try {
                    resultado = reservar(idReserva);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("ERROR CONEXION: XDDD", e.getMessage() + "|| " + e.getLocalizedMessage());
                    resultado = "Error con la conexion al servidor.Reinicie la app e intentelo de nuevo.";
                }
            }

            return resultado;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            //Log.e("RESULTADO:", resultado);
            mostrarMensaje(resultado);

        }
    }


    private String reservar(int idReserva) {

        try {
            outSocket.write("M7-CANCELAR_RESERVA:"+idReserva+"\n");
            outSocket.flush();

            String respuestaServidor = inSocket.readLine();
            //Log.e("Respuesta server reservar()", respuestaServidor);

            if (respuestaServidor.contains("S62-CANCELACION_CORRECTA")){

                //horario.setAforoActual(horario.getAforoActual()+1);

                finish();
                return"Reserva cancelada.";
            }

            else return "Error en la cancelacion. ";

        } catch (IOException ex) {
            ex.printStackTrace();
            return"Error de conexion con el servidor.";
        }


    }
    private void mostrarToast(String mensaje){
        Toast.makeText(InfoReservaActivity.this,mensaje, Toast.LENGTH_LONG).show();
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
            Toast.makeText(InfoReservaActivity.this, mensaje, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_cancelar:
                new CancelarReservaAsyncTask(idReserva).execute();
                break;

        }
    }






}