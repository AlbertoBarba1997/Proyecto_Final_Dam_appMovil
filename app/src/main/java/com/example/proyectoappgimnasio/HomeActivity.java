package com.example.proyectoappgimnasio;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.RenderScript;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import SocketUtil.SocketHandler;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    //ELEMENTS LAYOUT
    private TextView nombreApellidos_tf;
    private TextView correo_tf;
    private CircleImageView fotoUsuario;
    private ImageButton buttonEjercicios;
    private ImageButton buttonHorario;
    private ImageButton buttonReservas;

    //CONEXION SOCKET
    Socket socket=null;
    BufferedReader inputSocket=null;
    PrintWriter outputSocket=null;

    //DATOS DEL USUARIO
    int idCliente;
    String nombreCliente;
    String apellidosCliente;
    String correoCliente;
    String rutaImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        idCliente=Integer.parseInt(getIntent().getStringExtra("id"));
        nombreCliente=getIntent().getStringExtra("nombre");
        apellidosCliente=getIntent().getStringExtra("apellido");
        correoCliente=getIntent().getStringExtra("correo");
        rutaImagen=getIntent().getStringExtra("rutaImg");

        //Referencia Elements Layout
        buttonEjercicios= (ImageButton) findViewById(R.id.ejercicios_bt);
        buttonHorario= (ImageButton) findViewById(R.id.reservas_bt);
        buttonReservas = (ImageButton) findViewById(R.id.horario_bt);
        nombreApellidos_tf = (TextView) findViewById(R.id.nombre_tf);
        correo_tf = (TextView) findViewById(R.id.correo_tf);
        fotoUsuario= (CircleImageView) findViewById(R.id.foto_img);

        //Listeners
        buttonEjercicios.setOnClickListener(this);
        buttonHorario.setOnClickListener(this);
        buttonReservas.setOnClickListener(this);

        //Cargar datos del cliente
        correo_tf.setText(correoCliente);
        nombreApellidos_tf.setText(nombreCliente +" "+ apellidosCliente);
        cargarFotoPerfil(fotoUsuario,rutaImagen);


        //Obtener el socket y los dos flujos
        obtenerFlujosSocket();



    }

    private void obtenerFlujosSocket() {
        socket=SocketHandler.getSocket();
        inputSocket= SocketHandler.getInSocket();
        outputSocket=SocketHandler.getOutSocket();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.horario_bt:
                Intent intent= new Intent(HomeActivity.this,HorarioActivity.class);
                intent.putExtra("idCliente",idCliente);
                startActivity(intent);
                break;
            case R.id.reservas_bt:
                Intent intentReservas= new Intent(HomeActivity.this,ReservasActivity.class);
                intentReservas.putExtra("idCliente",idCliente);
                intentReservas.putExtra("nombreCliente",nombreCliente);
                startActivity(intentReservas);
                break;
            case R.id.ejercicios_bt:
                Intent intentTablas= new Intent(HomeActivity.this,TablasActivity.class);
                intentTablas.putExtra("idCliente",idCliente);
                startActivity(intentTablas);
                break;
        }
    }





    public void cargarFotoPerfil(CircleImageView contenedor, String rutaSimple){
        if(rutaSimple.length()>0) {
            String url = "http://" + SocketHandler.getHOST() + "/" +SocketHandler.getNombreCarpetaServidor()+"/"+ rutaSimple.substring(2, rutaSimple.length());  //El substring para quitarle el "./" inicial

            /*OkHttpClient client = new OkHttpClient();
            Picasso picasso = new Picasso.Builder(this)
                    .downloader(new OkHttp3Downloader(client))
                    .build();
            picasso.load(url).placeholder(R.drawable.icons8_user_125px).into(contenedor);*/
            Picasso.get().load(url).placeholder(R.drawable.icons8_user_125px).resize(300,300).centerCrop().into(contenedor);

        }
    }
}