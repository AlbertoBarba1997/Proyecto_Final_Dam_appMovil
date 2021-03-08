package com.example.proyectoappgimnasio;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import SocketUtil.SocketHandler;
import Util.Utilidades;

import static android.os.Build.HOST;

public class SingUpActivity extends AppCompatActivity implements View.OnClickListener{

    //ELEMENTOS LAYOUT
    private EditText edtUsuario;
    private EditText edtContraseña;
    private EditText edtContraseña2;
    private Button buttonRegister;
    private ImageButton buttonAtras;

    //ELEMENTOS SOCKET
    private Socket socket;
    private BufferedReader inputSocket;
    private PrintWriter outputSocket;




    //** ON CREATE()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        //Referencia Elements Layout
        buttonRegister= (Button) findViewById(R.id.btn_register);
        buttonAtras = (ImageButton) findViewById(R.id.btn_atrasRegistro);
        edtUsuario = (EditText) findViewById(R.id.correo_et);
        edtContraseña = (EditText) findViewById(R.id.contraseña_ett);
        edtContraseña2= (EditText) findViewById(R.id.edt_contraseña2);

        buttonRegister.setOnClickListener(this);
        buttonAtras.setOnClickListener(this);


    }
    //** CLICK LISTENER
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_register:
                String correo= String.valueOf(edtUsuario.getText());
                String contraseña= String.valueOf(edtContraseña.getText());
                String contraseña2= String.valueOf(edtContraseña2.getText());
                if(correo.equals("") || contraseña.equals("") || contraseña2.equals("")){
                    mostrarMensaje("Debe de rellenar los 3 campos.");
                }else{
                    if(!contraseña.equals(contraseña2)){
                        mostrarMensaje("Las dos contraseñas deben de coincidir.");
                    }else{
                        mostrarMensaje("Registrando...");
                        new RegisterAsynkTask(correo,contraseña,contraseña2).execute();
                    }

                }

                break;

            case R.id.btn_atrasRegistro:
                finish();
                break;
        }
    }


    private void mostrarMensaje(String mensaje) {
        try {
            Toast.makeText(SingUpActivity.this, mensaje, Toast.LENGTH_LONG).show();

        }catch(Exception e){
            e.printStackTrace();
        }
    }




    private class RegisterAsynkTask extends AsyncTask<Void, Void, String>{
        String resultado="ERROR DESCONOCIDO";

        String usuario;
        String contraseña;
        String contraseña2;
        public RegisterAsynkTask(String correo,String contraseña, String contraseña2){

            this.usuario=correo;
            this.contraseña=contraseña;
            this.contraseña2=contraseña2;
        }
        @Override
        protected String doInBackground(Void... voids) {

            if(establecerConexion()){
                logOut();
                resultado=registrar(usuario,contraseña,contraseña2);
            }

            return resultado;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            Log.e("RESULTADO",resultado);
            mostrarMensaje(resultado);

        }
    }


    public boolean establecerConexion() {
        //Comprueba si ya hay una conexion establecida (SocketHandler) para obtenerla, o si no abrir una nueva.

        if (SocketHandler.getSocket() == null || SocketHandler.getInSocket() == null || SocketHandler.getOutSocket() == null) {
            try {
                String HOST = getString(R.string.saved_ip);
                int PUERTO = Integer.parseInt(getString(R.string.saved_puerto));
                socket = new Socket(HOST, PUERTO);
                outputSocket = new PrintWriter(socket.getOutputStream());
                inputSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                SocketHandler.setNewConnection(socket, outputSocket, inputSocket);
                return true;
            } catch (Exception e) {
                socket = null;
                outputSocket = null;
                inputSocket = null;
                SocketHandler.setNewConnection(socket, outputSocket, inputSocket);
                mostrarMensaje("No se ha podido establecer conexion. Reajuste la direccion IP en el menu Login e intentelo de nuevo. Quizas el servidor este en mantenimiento.");
                return false;
            }
        } else {
            socket = SocketHandler.getSocket();
            inputSocket = SocketHandler.getInSocket();
            outputSocket = SocketHandler.getOutSocket();
            return true;
        }


    }
    private void logOut() {

        if (SocketHandler.getSocket() != null) {
            try {
                SocketHandler.getOutSocket().println("M5-LOG_OUT");
                SocketHandler.getOutSocket().flush();
                String respuestaServer = SocketHandler.getInSocket().readLine();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }





    private String registrar(String correo, String contraseña, String contraseña2){
        String resultado="ERROR EN EL REGISTRO";

        try {
            outputSocket.println("M10-SING_UP_CLIENTE:" + correo + "," + contraseña);
            outputSocket.flush();
            String mensajeEntrada = inputSocket.readLine();

            //Log.e("registrar() respuesta del server", mensajeEntrada);

            if (mensajeEntrada.contains("S65-REGISTRO_CORRECTO")) {
                finish();
                return"Registrado correctamente!";

            }else if(mensajeEntrada.contains("S66-ERROR_REGISTRO")){
                if(Utilidades.contarParametros(mensajeEntrada)>0){
                    String mensajeError=Utilidades.obtenerParametro(mensajeEntrada,1);
                    return mensajeError;
                }else {
                    return "Error de registro.";
                }

            }else{
                return "Error de registro.";
            }

        } catch (IOException ioe) {
            return "ERROR DESCONOCIDO";

        }
    }



}