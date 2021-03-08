package com.example.proyectoappgimnasio;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.TextView;

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

import ADAPTER.HorarioAdapter;
import ADAPTER.ReservasAdapter;
import Modelos.HorarioClase;
import Modelos.Reserva;
import SocketUtil.SocketHandler;
import Util.Utilidades;
import de.hdodenhof.circleimageview.CircleImageView;

public class ReservasActivity extends AppCompatActivity implements View.OnClickListener, Serializable {
    //ELEMENTS LAYOUT

    //CONEXION SOCKET
    Socket socket = null;
    BufferedReader inSocket = null;
    PrintWriter outSocket = null;

    //DATOS DEL USUARIO
    int idCliente;
    String nombreCliente;


    //LISTAS HORARIOS CLASES
    ArrayList<Reserva> lReservas = new ArrayList<>();


    //RECYCLER VIEWS
    RecyclerView recycler_reservas;
    RecyclerView.LayoutManager layoutManager;
    ReservasAdapter reservasAdapter;
    ReservasAdapter rAdapter=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservas);


        //Obtener el socket y los dos flujos y el id del cliente
        obtenerFlujosSocket();
        idCliente=getIntent().getIntExtra("idCliente",0);
        nombreCliente=getIntent().getStringExtra("nombreCliente");


        //Referencia Elements Layout
        recycler_reservas=(RecyclerView) findViewById(R.id.recicler_reserva);
        layoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_reservas.setLayoutManager(layoutManager);


        //Peticion LISTAR CLASES servidor.
        new ListarReservasAsynkTask().execute();


    }

    private class ListarReservasAsynkTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            listarClases();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(rAdapter!=null){
                recycler_reservas.setAdapter(rAdapter);
            }
        }
    }

    private void listarClases() {
        try {
            //1 Solicitar al servidor el listado de trabajadores (String)
            outSocket.write("M6-LISTAR_RESERVAS:"+idCliente+"\n");
            outSocket.flush();

            String respuestaServidor = inSocket.readLine();

            if (respuestaServidor.contains("S60-ERROR")) {

            } else if (respuestaServidor.contains("S61-LISTA_RESERVAS")) {

                int numeroDeReservas = Utilidades.contarParametros(respuestaServidor);
                for (int i = 0; i < numeroDeReservas; i++) {
                    String cadenaDatosReserva = Utilidades.obtenerParametro(respuestaServidor, i + 1);
                    Reserva reserva = Utilidades.obtenerReserva(cadenaDatosReserva);

                    if (reserva != null) {
                        //Log.e("RESERVA " + i, reserva.toString());

                        lReservas.add(reserva);
                    }
                }
                rAdapter=new ReservasAdapter(lReservas, R.layout.item_reserva, this, idCliente,nombreCliente);

                //recycler_reservas.setAdapter(rAdapter);

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


    public void cargarFotoPerfil(CircleImageView contenedor, String rutaSimple) {
        if (rutaSimple.length() > 0) {
            String url = "http://" + SocketHandler.getHOST() + "/" + SocketHandler.getNombreCarpetaServidor() + "/" + rutaSimple.substring(2, rutaSimple.length());  //El substring para quitarle el "./" inicial

            Picasso.get().load(url).placeholder(R.drawable.icons8_user_125px).resize(300, 300).centerCrop().into(contenedor);

        }
    }


}