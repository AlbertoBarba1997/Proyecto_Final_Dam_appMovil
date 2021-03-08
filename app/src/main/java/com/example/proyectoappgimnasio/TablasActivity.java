package com.example.proyectoappgimnasio;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

import ADAPTER.ReservasAdapter;
import ADAPTER.TablasAdapter;
import Modelos.Reserva;
import Modelos.Tabla;
import SocketUtil.SocketHandler;
import Util.Utilidades;
import de.hdodenhof.circleimageview.CircleImageView;

public class TablasActivity extends AppCompatActivity implements View.OnClickListener, Serializable {
    //ELEMENTS LAYOUT

    //CONEXION SOCKET
    Socket socket = null;
    BufferedReader inSocket = null;
    PrintWriter outSocket = null;

    //DATOS DEL USUARIO
    int idCliente;
    String nombreCliente;


    //LISTAS HORARIOS CLASES
    ArrayList<Tabla> lTablas = new ArrayList<>();


    //RECYCLER VIEWS
    RecyclerView recycler_tablas;
    RecyclerView.LayoutManager layoutManager;
    TablasAdapter tAdapter=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablas);


        //Obtener el socket y los dos flujos y el id del cliente
        obtenerFlujosSocket();
        idCliente=getIntent().getIntExtra("idCliente",0);
        nombreCliente=getIntent().getStringExtra("nombreCliente");



        //Referencia Elements Layout
        recycler_tablas =(RecyclerView) findViewById(R.id.recicler_tablas);
        layoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_tablas.setLayoutManager(layoutManager);

        //Peticion LISTAR CLASES servidor.
        new ListarTablasAsynkTask().execute();

    }

    private class ListarTablasAsynkTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            listarTablasUsuario();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(tAdapter!=null) {
                recycler_tablas.setAdapter(tAdapter);
            }

        }
    }

    private void listarTablasUsuario() {

        try {
            //1 Solicitar al servidor el listado de trabajadores (String)
            outSocket.write("M8-LISTAR_TABLAS_EJERCICIOS:"+idCliente+"\n");
            outSocket.flush();

            String respuestaServidor = inSocket.readLine();

            if (respuestaServidor.contains("S60-ERROR")) {

            } else if (respuestaServidor.contains("S63-LISTA_TABLAS")) {

                mostrarMensajeConsola(String.valueOf(Utilidades.contarParametros(respuestaServidor)));

                int numeroDeTablas = Utilidades.contarParametros(respuestaServidor);
                for (int i = 0; i < numeroDeTablas; i++) {
                    String cadenaDatosReserva = Utilidades.obtenerParametro(respuestaServidor, i + 1);
                    Tabla tabla = Utilidades.obtenerTabla(cadenaDatosReserva);

                    if (tabla != null) {
                        lTablas.add(tabla);
                    }
                }
                tAdapter=new TablasAdapter(lTablas, R.layout.item_tabla, this);

                //recycler_tablas.setAdapter(tAdapter);

            } else {
                Log.e("Respuesta erronea del server:" , respuestaServidor);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void obtenerFlujosSocket() {
        socket = SocketHandler.getSocket();
        inSocket = SocketHandler.getInSocket();
        outSocket = SocketHandler.getOutSocket();

    }

    @Override
    public void onClick(View v) {
    }

    private void mostrarMensajeConsola(String mensaje) {
        Log.e("---------CONSOLA RAPIDA:", mensaje);
    }




}