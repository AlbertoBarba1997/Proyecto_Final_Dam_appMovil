package com.example.proyectoappgimnasio;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import ADAPTER.EjerciciosAdapter;
import ADAPTER.TablasAdapter;
import Modelos.Ejercicio;
import Modelos.Tabla;
import SocketUtil.SocketHandler;
import Util.Utilidades;

public class EjerciciosActivity extends AppCompatActivity implements View.OnClickListener, Serializable {
    //ELEMENTS LAYOUT

    //CONEXION SOCKET
    Socket socket = null;
    BufferedReader inSocket = null;
    PrintWriter outSocket = null;



    //SPINER
    Spinner spinnerDias;


    //LISTAS DE EJERCICIOS
    ArrayList<Ejercicio> ejerciciosDia1=new ArrayList<>();
    ArrayList<Ejercicio> ejerciciosDia2=new ArrayList<>();
    ArrayList<Ejercicio> ejerciciosDia3=new ArrayList<>();
    ArrayList<Ejercicio> ejerciciosDia4=new ArrayList<>();
    ArrayList<Ejercicio> ejerciciosDia5=new ArrayList<>();
    ArrayList<Ejercicio> ejerciciosDia6=new ArrayList<>();
    ArrayList<Ejercicio> ejerciciosDia7=new ArrayList<>();
    ArrayList<ArrayList<Ejercicio>> lDiasEjercicios=new ArrayList<ArrayList<Ejercicio>>();

    //RECYCLERS
    RecyclerView recyclerViewEjercicios;
    RecyclerView.LayoutManager layoutManager;
    EjerciciosAdapter eAdapter=null;

    //DATOS TABLA
    int idTabla;
    int nDias=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicios);

        obtenerFlujosSocket();
        idTabla=getIntent().getIntExtra("idTabla",0);
        nDias=getIntent().getIntExtra("nDias", 1);


        //Referencia Elements Layout
        recyclerViewEjercicios =(RecyclerView) findViewById(R.id.recicler_ejercicios);
        layoutManager=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewEjercicios.setLayoutManager(layoutManager);

        //Peticion LISTAR CLASES servidor.
        new DescargarListasEjerciciosAsynkTask().execute();

        spinnerDias= findViewById(R.id.spinner2);
        RellenarSpinner();

        lDiasEjercicios.add(ejerciciosDia1);
        lDiasEjercicios.add(ejerciciosDia2);
        lDiasEjercicios.add(ejerciciosDia3);
        lDiasEjercicios.add(ejerciciosDia4);
        lDiasEjercicios.add(ejerciciosDia5);
        lDiasEjercicios.add(ejerciciosDia6);
        lDiasEjercicios.add(ejerciciosDia7);




    }

    private void RellenarSpinner() {

        List<Integer> lDias=new ArrayList<Integer>();
        for(int i=0;i<nDias;i++) {
            lDias.add(i+1);

        }
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this, R.layout.spinner_item, lDias);
        spinnerDias.setAdapter(arrayAdapter);
        spinnerDias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int diaSeleccionado=(int)spinnerDias.getSelectedItem();
                recyclerViewEjercicios.setAdapter(new EjerciciosAdapter(lDiasEjercicios.get(diaSeleccionado-1), R.layout.item_tabla, EjerciciosActivity.this));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                int diaSeleccionado=(int)spinnerDias.getSelectedItem();
            }
        });
    }

    private class DescargarListasEjerciciosAsynkTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            listarTablasEjercicios();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(eAdapter!=null) {
                recyclerViewEjercicios.setAdapter(eAdapter);
            }
        }
    }

    private void listarTablasEjercicios() {

        try {
            //1 Solicitar al servidor el listado de trabajadores (String)
            outSocket.write("M9-LISTAR_EJERCICIOS_TABLA:"+idTabla+"\n");
            outSocket.flush();

            String respuestaServidor = inSocket.readLine();
            //Log.e("-------Ejercicio RESPUESTA SERVIDOR:",respuestaServidor);
            if (respuestaServidor.contains("S60-ERROR")) {

            } else if (respuestaServidor.contains("S64-LISTA_EJERCICIOS_DE_TABLA")) {

                int numeroDeTablas = Utilidades.contarParametros(respuestaServidor);
                for (int i = 0; i < numeroDeTablas; i++) {
                    String cadenaDatosReserva = Utilidades.obtenerParametro(respuestaServidor, i + 1);
                    Ejercicio ejercicio = Utilidades.obtenerEjercicio(cadenaDatosReserva);

                    if (ejercicio != null) {
                        //Log.e("EJERCICIO " + i, ejercicio.toString());
                        int dia=ejercicio.getDia();
                        lDiasEjercicios.get(dia-1).add(ejercicio);

                    }
                }

                //Cargar los ejercicios del dia 1
                eAdapter=new EjerciciosAdapter(ejerciciosDia1, R.layout.item_tabla, this);

                //recyclerViewEjercicios.setAdapter(eAdapter);




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






}