/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import android.util.Log;
import android.widget.Toast;

import java.awt.font.TextAttribute;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import Modelos.Ejercicio;
import Modelos.HorarioClase;
import Modelos.Reserva;
import Modelos.Tabla;


/**
 *
 * @author juana
 */
public class Utilidades {

    public Utilidades() {
    }

    public static String obtenerParametro(String theInput, int nParametroBuscado) {
        //OBTIENE EL PARAMETRO indicado en la cadena indicada

        //Si es el primero estara entre los ":" y la ","
        //A partir del segundo se iran controlando por las ",", cuando lea una coma sabra que lo siguiente a leer es el siguiente parametro.
        int nParametro = 0;       //Mide por que parametro va, cuando lea un ":" o una "," -> nParametro++;

        char[] caracteres = theInput.toCharArray();
        String parametro = "";
        boolean comenzo = false;

        for (char c : caracteres) {

            if (c == ':' && comenzo == false) {
                nParametro++;
                comenzo = true;

            } else if (c == '&') {
                nParametro++;
            } else if (nParametro == nParametroBuscado) {
                parametro += c;
            }

        }
        return parametro;
    }

    public static int contarParametros(String respuestaServidor) {
        //CUENTA CUANTOS PARAMETROS HAY EN UNA SECUENCIA String
        final char caracterDelimitador = '&';
        int nParametros = 0;

        if (respuestaServidor.contains(":")) {
            nParametros = 1;

            if (respuestaServidor.contains(""+caracterDelimitador)) {
                char[] arrayCaracteres = respuestaServidor.toCharArray();
                for (char c : arrayCaracteres) {
                    if (c == caracterDelimitador) {
                        nParametros++;
                    }
                }
            }
        }
        return nParametros;

    }

    public static HorarioClase obtenerHorario(String parametroClase) {
        final String separador = "$";
        HorarioClase horario;
        //try {
            int idHorario = Integer.parseInt(obtenerAtributo(parametroClase, 0, separador));  //id_horario
            String nombreClase = obtenerAtributo(parametroClase, 1, separador);                 //
            String nombreEntrenador = obtenerAtributo(parametroClase, 2, separador);
            int dia = Integer.parseInt(obtenerAtributo(parametroClase, 3, separador));  //id_horario
            String hora = obtenerAtributo(parametroClase, 4, separador);
            String rutaImg = obtenerAtributo(parametroClase, 5, separador);
            int aforoMaximo = Integer.parseInt(obtenerAtributo(parametroClase, 6, separador));
            int aforoActual = Integer.parseInt(obtenerAtributo(parametroClase, 7, separador));
            String descripccion = obtenerAtributo(parametroClase, 8, separador);


            horario = new HorarioClase(idHorario, dia, aforoMaximo, aforoActual, nombreClase, descripccion, rutaImg, nombreEntrenador, hora);


        return horario;

    }
    public static Reserva obtenerReserva(String parametroClase) {
        final String separador = "$";
        Reserva reserva;
        int idHorario = Integer.parseInt(obtenerAtributo(parametroClase, 0, separador));  //id_horario
        String nombreClase = obtenerAtributo(parametroClase, 1, separador);
        int dia = Integer.parseInt(obtenerAtributo(parametroClase, 2, separador));  //id_horario
        String hora = obtenerAtributo(parametroClase, 3, separador);

        reserva = new Reserva(idHorario, nombreClase, dia , hora);
        return reserva;

    }



    public static Ejercicio obtenerEjercicio(String parametroEjercicio) {

        Ejercicio ejercicio=null;
        final String separador = "$";

        int idEjercicio = Integer.parseInt(obtenerAtributo(parametroEjercicio, 0, separador));
        int dia = Integer.parseInt(obtenerAtributo(parametroEjercicio, 1, separador));
        String nombre= obtenerAtributo(parametroEjercicio, 2, separador);
        String descripcion= obtenerAtributo(parametroEjercicio, 3, separador);
        String tipo= obtenerAtributo(parametroEjercicio, 4, separador);
        String rutaImg= obtenerAtributo(parametroEjercicio, 5, separador);

        int series;
        int repeticiones;
        try {
            series = Integer.parseInt(obtenerAtributo(parametroEjercicio, 6, separador));
            repeticiones = Integer.parseInt(obtenerAtributo(parametroEjercicio, 7, separador));
        }catch(Exception e){
            series=0;
            repeticiones=0;
        }
        String tiempo= obtenerAtributo(parametroEjercicio, 8, separador);

        try{
            ejercicio=new Ejercicio(idEjercicio,dia,nombre,descripcion,tipo,rutaImg,series,repeticiones,tiempo);
        }catch(Exception e){
            return null;
        }


        return ejercicio;
    }







    public static String obtenerAtributo(String cadena, int nAtributoBuscado, String separador) {
        int nParametro = 0;       //Mide por que parametro va
        final char caracterDelimitador = separador.charAt(0);
        char[] caracteres = cadena.toCharArray();
        String parametro = "";

        for (char c : caracteres) {

            if (c == caracterDelimitador) {
                nParametro++;
            } else if (nParametro == nAtributoBuscado) {
                parametro += c;
            }

        }
        return parametro;

    }

    public static String primeraLetraMayuscula(String input) {
        String output;
        try {
            output = input.substring(0, 1).toUpperCase() + input.substring(1);
        } catch (Exception e) {
            output = "";
        }
        return output;
    }

    public static boolean comprobarComa(String... textos){
        boolean contieneComa=false;
        for(String texto:textos){
            if(texto.contains(",")){
                contieneComa=true;
            }
        }
        return contieneComa;
    }

    public static String remplazarComaPorArroba(String texto){
        texto.replace(',', '@');
        return texto;
    }

    public static String remplazarArrobaPorComa(String texto){
        texto.replace('@', ',');
        return texto;
    }


    public static Tabla obtenerTabla(String parametroTabla) {
        final String separador = "$";
        Tabla tabla;
        int idTabla = Integer.parseInt(obtenerAtributo(parametroTabla, 0, separador));  //id_horario
        String nombreTabla = obtenerAtributo(parametroTabla, 1, separador);
        int nDias = Integer.parseInt(obtenerAtributo(parametroTabla, 2, separador));  //id_horario

        tabla= new Tabla(idTabla, nombreTabla, nDias);
        return tabla;

    }
}
