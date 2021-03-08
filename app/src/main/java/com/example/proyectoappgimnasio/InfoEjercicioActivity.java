package com.example.proyectoappgimnasio;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import Modelos.Ejercicio;
import SocketUtil.SocketHandler;

import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.FRIDAY;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SUNDAY;
import static java.util.Calendar.THURSDAY;
import static java.util.Calendar.TUESDAY;
import static java.util.Calendar.WEDNESDAY;
import static java.util.Calendar.getInstance;

public class InfoEjercicioActivity extends AppCompatActivity implements View.OnClickListener, Serializable {

    //ELEMENTS LAYOUT
    private TextView tv_nombre;
    private TextView tv_descripccion;
    private TextView tv_tipo;
    private TextView tv_repeticiones;
    private TextView tv_series;
    private TextView tv_tiempo;
    private ImageView imagenClase;


    private ImageButton buttonAtras;

    //CONEXION SOCKET
    Socket socket = null;
    public String HOST = "192.168.0.11";
    public int PUERTO = 9002;
    BufferedReader inSocket = null;
    PrintWriter outSocket = null;

    //INFO USUARIO Y CLASE SELECCIONADA
    Ejercicio ejercicio;
    int idCliente;

    //** ON CREATE()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_ejercicio);

        //Obtener datos del cliente, clase seleccionada y el socket.
        ejercicio = (Ejercicio) getIntent().getSerializableExtra("ejercicio");

        obtenerFlujosSocket();


        //Referencia Elements Layout
        buttonAtras = (ImageButton) findViewById(R.id.btn_atras1);
        imagenClase = (ImageView) findViewById(R.id.imagenEjercicioInfo);

        tv_nombre = (TextView) findViewById(R.id.nombreEjercicioInfo_tv);
        tv_descripccion = (TextView) findViewById(R.id.descripccionEjercicioInfo_tv);
        tv_repeticiones = (TextView) findViewById(R.id.repeticionesEjercicioInfo_tv);
        tv_series = (TextView) findViewById(R.id.seriesEjercicioInfo_tv);
        tv_tiempo = (TextView) findViewById(R.id.tiempoInfoEjercicios_tv);
        tv_tipo = (TextView) findViewById(R.id.tipoInfoEjercicio_tv);

        //Listeners
        buttonAtras.setOnClickListener(this);

        //Volcar datos horario
        tv_nombre.setText(ponerPrimeraLetraMayuscula(ejercicio.getNombre()));
        tv_descripccion.setText(ponerPrimeraLetraMayuscula(ejercicio.getDescripcion()));
        tv_tiempo.setText(ejercicio.getTiempo());
        tv_series.setText(Integer.toString(ejercicio.getSeries()));
        tv_repeticiones.setText(Integer.toString(ejercicio.getRepeticiones()));
        tv_tipo.setText(ponerPrimeraLetraMayuscula(ejercicio.getTipo()));

        if (!ejercicio.getRutaImgCompleta().equals("")) {
            Picasso.get().load(ejercicio.getRutaImgCompleta()).placeholder(R.drawable.clase_default).into(imagenClase);
        }

        //new IniciarConexionAsynkTask().execute();

        /*CircleImageView imagenPrueba=(CircleImageView)findViewById(R.id.imgprueba);
        String url="http://192.168.0.11/ProyectoGimnasio_Server/fotos_usuarios/1.jpg";


        */


    }

    private void mostrarToast(String mensaje){
        Toast.makeText(InfoEjercicioActivity.this,mensaje, Toast.LENGTH_LONG).show();
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



    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_atras1:
                finish();
                break;


        }
    }

    public boolean comprobarDiaValido() {
        //Solo se pueden reservar clases del mismo dia o un dia antes.
        boolean reservaValida=false;
        int diaActual = getInstance().get(DAY_OF_WEEK);
        int diaClase = ejercicio.getDia();
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



}