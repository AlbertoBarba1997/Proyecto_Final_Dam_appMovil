package com.example.proyectoappgimnasio;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import ADAPTER.HorarioAdapter;
import Modelos.HorarioClase;
import SocketUtil.SocketHandler;
import Util.Utilidades;
import de.hdodenhof.circleimageview.CircleImageView;

public class HorarioActivity extends AppCompatActivity implements View.OnClickListener {
    //ELEMENTS LAYOUT
    private TextView nombreApellidos_tf;
    private TextView correo_tf;
    private CircleImageView fotoUsuario;
    private ImageButton buttonEjercicios;
    private ImageButton buttonHorario;
    private ImageButton buttonReservas;

    //CONEXION SOCKET
    Socket socket = null;
    BufferedReader inSocket = null;
    PrintWriter outSocket = null;

    //DATOS DEL USUARIO
    int idCliente;
    String nombreCliente;
    String apellidosCliente;
    String correoCliente;
    String rutaImagen;

    //LISTAS HORARIOS CLASES
    ArrayList<HorarioClase> clasesLunes = new ArrayList<>();
    ArrayList<HorarioClase> clasesMartes = new ArrayList<>();
    ArrayList<HorarioClase> clasesMiercoles = new ArrayList<>();
    ArrayList<HorarioClase> clasesJueves = new ArrayList<>();
    ArrayList<HorarioClase> clasesViernes = new ArrayList<>();
    ArrayList<HorarioClase> clasesSabado = new ArrayList<>();
    ArrayList<ArrayList<HorarioClase>> lClases = new ArrayList<ArrayList<HorarioClase>>();

    //RECYCLER VIEWS
    RecyclerView recycler_lunes;
    RecyclerView recycler_martes;
    RecyclerView recycler_miercoles;
    RecyclerView recycler_jueves;
    RecyclerView recycler_viernes;
    RecyclerView recycler_sabado;
    ArrayList<RecyclerView> recyclerViews = new ArrayList<RecyclerView>();

    RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
    RecyclerView.LayoutManager layoutManager2=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
    RecyclerView.LayoutManager layoutManager3=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
    RecyclerView.LayoutManager layoutManager4=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
    RecyclerView.LayoutManager layoutManager5=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
    RecyclerView.LayoutManager layoutManager6=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);

    ArrayList<HorarioAdapter> lAdapters = new ArrayList<HorarioAdapter>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios);

        //Obtener el socket y los dos flujos y el id del cliente
        obtenerFlujosSocket();
        idCliente=getIntent().getIntExtra("idCliente",0);

        //Agrupar lista clases y recyclers en una lista
        lClases.add(clasesLunes);
        lClases.add(clasesMartes);
        lClases.add(clasesMiercoles);
        lClases.add(clasesJueves);
        lClases.add(clasesViernes);
        lClases.add(clasesSabado);


        //Referencia Elements Layout
        recycler_lunes = (RecyclerView) findViewById(R.id.recicler_lunes);
        recycler_martes = (RecyclerView) findViewById(R.id.recicler_martes);
        recycler_miercoles = (RecyclerView) findViewById(R.id.recicler_miercoles);
        recycler_jueves = (RecyclerView) findViewById(R.id.recicler_jueves);
        recycler_viernes = (RecyclerView) findViewById(R.id.recicler_viernes);
        recycler_sabado = (RecyclerView) findViewById(R.id.recicler_sabado);

        layoutManager=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_lunes.setLayoutManager(layoutManager);
        recycler_martes.setLayoutManager(layoutManager2);
        recycler_miercoles.setLayoutManager(layoutManager3);
        recycler_jueves.setLayoutManager(layoutManager4);
        recycler_viernes.setLayoutManager(layoutManager5);
        recycler_sabado.setLayoutManager(layoutManager6);

        recyclerViews.add(recycler_lunes);
        recyclerViews.add(recycler_martes);
        recyclerViews.add(recycler_miercoles);
        recyclerViews.add(recycler_jueves);
        recyclerViews.add(recycler_viernes);
        recyclerViews.add(recycler_sabado);

        //Listeners

        //Peticion LISTAR CLASES servidor.
        new ListarClasesAsynkTask().execute();


    }

    private class ListarClasesAsynkTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            listarClases();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //No se puede inflar el Adapter desde el doInBackground(), asi que lo inflamos en oPostExecute.
            for(int j=0; j<6 ;j++){
                if(lAdapters.get(j)!=null) {
                    recyclerViews.get(j).setAdapter(lAdapters.get(j));
                }
            }
        }
    }

    private boolean listarClases() {
        boolean resultado=false;
        try {
            //1 Solicitar al servidor el listado de trabajadores (String)
            outSocket.write("M3-LISTAR_HORARIOS\n");
            outSocket.flush();

            String respuestaServidor = inSocket.readLine();
            if (respuestaServidor.contains("S12-ERROR_QUERY")) {

            } else if (respuestaServidor.contains("S35-HORARIO")) {
                //Log.e("listarClases() respuesta servidor", respuestaServidor);
                int numeroDeHorarios = Utilidades.contarParametros(respuestaServidor);
                for (int i = 0; i < numeroDeHorarios; i++) {
                    String cadenaDatosHorario = Utilidades.obtenerParametro(respuestaServidor, i + 1);
                    HorarioClase horarioClase = Utilidades.obtenerHorario(cadenaDatosHorario);

                    if (horarioClase != null) {
                        //Segun el dia (numero del 1-6) meterlo en una de las listas de clases
                        int nDia = horarioClase.getDia();
                        lClases.get(nDia - 1).add(horarioClase);
                        //(Las listas de horarios Lunes-Jueves estan recogidas ordenadamente en otra lista, la lClasesLunes es la 0, y en la BD el lunes es el 1, por eso se resta -1)
                        //Log.e("listarClase() clasesLunes.lenth()=", String.valueOf(clasesLunes.size()));

                        //Crear ADAPTER
                        for(int j=0; j<6 ;j++){
                            lAdapters.add(new HorarioAdapter(lClases.get(j),R.layout.item_clase,this,new HorarioAdapter.OnClickListener() {
                                @Override
                                public void onClick(HorarioClase horarioClase, int pos) {
                                }

                            }, idCliente));
                            //recyclerViews.get(j).setAdapter(lAdapters.get(j));   Hacer en onPostExecute() para evitar bug.
                        }
                        resultado= true;



                    }
                }

            } else {
                System.out.println("Respuesta erronea del server:" + respuestaServidor);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultado;
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