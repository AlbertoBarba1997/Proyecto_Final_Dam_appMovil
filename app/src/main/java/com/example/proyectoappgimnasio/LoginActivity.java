package com.example.proyectoappgimnasio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, AjustesDialog.ExampleDialogListener {

    //ELEMENTS LAYOUT
    private EditText edtUsuario;
    private EditText edtContraseña;
    private Button buttonLoguin;
    private Button buttonSingIn;
    private TextView ajustes_tv;


    //** ON CREATE()
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        establecerDireccionIp();
        //Referencia Elements Layout
        buttonLoguin= (Button) findViewById(R.id.btn_login);
        buttonSingIn = (Button) findViewById(R.id.btn_singin);
        edtUsuario = (EditText) findViewById(R.id.edt_usuario);
        edtContraseña = (EditText) findViewById(R.id.edt_contraseña);
        ajustes_tv= (TextView) findViewById(R.id.ajustes_tv);

        //Subrayas texto de ajustes
        ajustes_tv.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        //Listeners
        buttonLoguin.setOnClickListener(this);
        buttonSingIn.setOnClickListener(this);
        ajustes_tv.setOnClickListener(this);


        new IniciarConexionAsynkTask().execute();

        /*CircleImageView imagenPrueba=(CircleImageView)findViewById(R.id.imgprueba);
        String url="http://192.168.0.11/ProyectoGimnasio_Server/fotos_usuarios/1.jpg";

        Picasso.get().load(url).placeholder(R.drawable.icons8_user_125px).resize(200, 200).into(imagenPrueba);
        */





    }

    private void establecerDireccionIp() {
        SocketHandler.setHOST(getString(R.string.saved_ip));
        SocketHandler.setPORT(Integer.parseInt(getString(R.string.saved_puerto)));

    }

    @Override
    public void actualizarIpCallBack(String ip, String puerto) {

        //GUARDAR EN MEMORIA LOCAL LOS NUEVOS VALORES
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_ip), ip);
        editor.putString(getString(R.string.saved_puerto), puerto);

        editor.commit();

        //RESETEAR VALORES HANDLER
        SocketHandler.setHOST(ip);
        SocketHandler.setPORT(Integer.parseInt(puerto));

        //RESETEAR CONEXION
        resetConection(true);
    }

    private void resetConection(boolean changeDialog) {
        SocketHandler.setSocket(null);
        SocketHandler.setOutSocket(null);
        SocketHandler.setInSocket(null);
        if(!changeDialog) {
            iniciarConexion();
        }else{
            new IniciarConexionAsynkTask().execute();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private class IniciarConexionAsynkTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            iniciarConexion();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private class LogInAsynkTask extends AsyncTask<Void, Void, String>{
        String resultado="ERROR DESCONOCIDO";
        @Override
        protected String doInBackground(Void... voids) {

            if(SocketHandler.getOutSocket()==null){

                //1. Si no hay conexion establecida, intentar establecerla de nuevo
                try {
                    resetConection(false);

                    //2. Si la conexion es establecida correctamente, comprobar los campos correo y contraseña
                    String correo=edtUsuario.getText().toString();
                    String contraseña=edtContraseña.getText().toString();
                    if(correo.isEmpty() || contraseña.isEmpty()){
                        resultado= "Rellene correctamente los campos 'correo' y 'contraseña";

                    }else {

                        //3. Si estan rellenados correctamente, proceder a hacer el loguin
                        resultado= logIn(correo, contraseña);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    resultado= "Error logeando:No se ha podido establecer conexion con el servidor. Intente introducir la ip y el puerto manualmente.";
                }
            }else{
                //Si  ya hay una conexion establecida, hay que avisar al server para que vuelva a estado inicial y reciba log in correctamente.
                logOut();
                //4. Si la conexion ha sido establecida correctamente, lanzar el login directamente
                try {

                    //Comprobar parametros
                    String correo=edtUsuario.getText().toString();
                    String contraseña=edtContraseña.getText().toString();
                    if(correo.isEmpty() || contraseña.isEmpty()){

                    }else {
                        //Hacer login
                        resultado=logIn(correo, contraseña);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    resultado= "Error logeando:No se ha podido establecer conexion con el servidor. Intente introducir la ip y el puerto manualmente.";
                    iniciarConexion();
                }

            }


            return resultado;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            //Log.e("RESULTADO:",resultado);
            mostrarMensaje(resultado);

        }
    }


    public boolean iniciarConexion() {
        try {

            if (SocketHandler.getSocket() == null) {

                SocketHandler.setSocket(new Socket(SocketHandler.getHOST(), SocketHandler.getPORT()));
                SocketHandler.setOutSocket(new PrintWriter(SocketHandler.getSocket().getOutputStream()));
                SocketHandler.setInSocket(new BufferedReader(new InputStreamReader(SocketHandler.getSocket().getInputStream())));

                //Le pasamos este SOCKET  a su clase ESTATICA  para acceder a el desde otros activities (Ya que no se puede pasar directamente con intent)
                SocketHandler.setConexionEstablecida(true);

                return true;
            }else{
                logOut();
            }


            } catch(IOException e){
                e.printStackTrace();
                SocketHandler.setConexionEstablecida(false);
                Toast.makeText(LoginActivity.this, "No se ha podido establecer conexion con el servidor. Intente introducir la ip y el puerto manualmente.", Toast.LENGTH_LONG).show();
                SocketHandler.setSocket(null);
                return false;
        }
        return true;
    }


    private void logOut() {

            if (SocketHandler.getSocket() != null) {
                try {

                    SocketHandler.getOutSocket().println("M5-LOG_OUT");
                    SocketHandler.getOutSocket().flush();

                    String respuestaServer = SocketHandler.getInSocket().readLine();
                    //Log.e("SALE LOG OUT", respuestaServer);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
    }


    private void mostrarMensaje(String mensaje) {
        try {
            Toast.makeText(LoginActivity.this, mensaje, Toast.LENGTH_LONG).show();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_login:
                new LogInAsynkTask().execute();
                break;

            case R.id.btn_singin:
                Intent intent=new Intent(LoginActivity.this, SingUpActivity.class);
                startActivity(intent);
                break;

            case R.id.ajustes_tv:
                abrirDialogAjustes();
                break;
        }
    }

    private void abrirDialogAjustes() {
        AjustesDialog ajusteDialog = new AjustesDialog();
        ajusteDialog.show(getSupportFragmentManager(), "Ajustes de conexion");
    }

    private String logIn(String correo, String contraseña) {

        int resultado=-1;
        try {
            SocketHandler.getOutSocket().println("M1-LOG_CLIENTE:" + correo + "," + contraseña);
            SocketHandler.getOutSocket().flush();
            String mensajeEntrada = SocketHandler.getInSocket().readLine();
            if (mensajeEntrada.contains("S2-LOG_INCORRECTO")) {
                return"Credenciales incorrectas";

            }else if(mensajeEntrada.contains("S4-LOG_BLOQUEADO")){
                String tiempoEspera = Utilidades.obtenerParametro(mensajeEntrada, 1);
                return "Numero de intentos superados Tiempo de espera:"+tiempoEspera+" segundos";

            }else if(mensajeEntrada.contains("S5-ESPERA")){
                String tiempoEspera = Utilidades.obtenerParametro(mensajeEntrada, 1);
                return "Tiempo de espera:"+tiempoEspera+" segundos";
            }
            else if (mensajeEntrada.contains("S1-LOG_CORRECTO")) {
                String id=Utilidades.obtenerParametro(mensajeEntrada, 1);
                String dni=Utilidades.obtenerParametro(mensajeEntrada, 2);
                String nombre=Utilidades.obtenerParametro(mensajeEntrada, 3);
                String apellido=Utilidades.obtenerParametro(mensajeEntrada, 4);
                correo=Utilidades.obtenerParametro(mensajeEntrada, 5);
                contraseña=Utilidades.obtenerParametro(mensajeEntrada, 6);
                String rutaImg=Utilidades.obtenerParametro(mensajeEntrada, 7);
                String peso=Utilidades.obtenerParametro(mensajeEntrada, 8);
                String altura=Utilidades.obtenerParametro(mensajeEntrada, 9);

                Intent intent= new Intent(LoginActivity.this, HomeActivity.class);


                intent.putExtra("id",id);
                intent.putExtra("dni",dni);
                intent.putExtra("nombre",nombre);
                intent.putExtra("apellido",apellido);
                intent.putExtra("correo",correo);
                intent.putExtra("contraseña",contraseña);
                intent.putExtra("rutaImg",rutaImg);
                intent.putExtra("peso",peso);
                intent.putExtra("altura",altura);

                startActivity(intent);

                return "USUARIO LOGUEADO! DNI:"+dni;
            }
            else{
                return "SERVIDOR EN MANTENIMIENTO. \n Intentelo mas tarde...";
            }

        } catch (IOException ioe) {
            return "ERROR DESCONOCIDO";

        }


    }

}