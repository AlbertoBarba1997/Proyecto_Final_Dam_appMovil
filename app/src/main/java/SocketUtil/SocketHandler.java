package SocketUtil;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import de.hdodenhof.circleimageview.CircleImageView;

public class SocketHandler {
    private static String HOST = "192.168.0.11";
    private static int PORT = 9002;
    private final static int PORT_RECIBIR_FOTOS = 9003;
    private static boolean conexionEstablecida=false;

    private static String nombreCarpetaServidor="ProyectoGimnasio_Server";


    private static Socket socket;
    private static BufferedReader inSocket;
    private static PrintWriter outSocket;


    public static String getNombreCarpetaServidor() {
        return nombreCarpetaServidor;
    }

    public static void setNombreCarpetaServidor(String nombreCarpetaServidor) {
        SocketHandler.nombreCarpetaServidor = nombreCarpetaServidor;
    }

    public static boolean isConexionEstablecida() {
        return conexionEstablecida;
    }

    public static void setConexionEstablecida(boolean conexionEstablecida) {
        SocketHandler.conexionEstablecida = conexionEstablecida;
    }

    public static void setPORT(int PORT) {
        SocketHandler.PORT = PORT;
    }

    public static String getHOST() {
        return HOST;
    }

    public static void setHOST(String HOST) {
        SocketHandler.HOST = HOST;
    }

    public static int getPORT() {
        return PORT;
    }

    public static int getPortRecibirFotos() {
        return PORT_RECIBIR_FOTOS;
    }

    public static synchronized Socket getSocket() {
        return socket;
    }

    public static synchronized void setSocket(Socket socket) {
        SocketHandler.socket = socket;
    }

    public static synchronized BufferedReader getInSocket() {
        return inSocket;
    }

    public static synchronized void setInSocket(BufferedReader inSocket) {
        SocketHandler.inSocket = inSocket;
    }

    public static synchronized PrintWriter getOutSocket() {
        return outSocket;
    }

    public static synchronized void setOutSocket(PrintWriter outSocket) {
        SocketHandler.outSocket = outSocket;
    }


    public static void setNewConnection(Socket socket, PrintWriter outputSocket, BufferedReader inputSocket) {
        SocketHandler.socket=socket;
        SocketHandler.inSocket=inputSocket;
        SocketHandler.outSocket=outputSocket;
    }
}
