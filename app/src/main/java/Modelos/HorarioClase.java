package Modelos;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import SocketUtil.SocketHandler;

public class HorarioClase implements Serializable {

    int idHorario;
    String hora;
    int dia;

    int aforoMaximo;
    int aforoActual;

    String nombreClase;
    String descripccion;
    String rutaImgBase;
    String rutaImgCompleta;
    String nombreEntrenador;

    String horaSt;

    //CONSTRUCTORES
    public HorarioClase() {
    }

    public HorarioClase(int idHorario, int dia, int aforoMaximo, int aforoActual, String nombreClase, String descripccion, String rutaImgBase, String nombreEntrenador, String horaSt) {
        this.idHorario = idHorario;
        this.dia = dia;
        this.aforoMaximo = aforoMaximo;
        this.aforoActual = aforoActual;
        this.nombreClase = nombreClase;
        this.descripccion = descripccion;
        this.rutaImgBase = rutaImgBase;
        this.nombreEntrenador = nombreEntrenador;
        this.horaSt = horaSt;
        this.hora=horaSt;
        generarRutaCompleta();

    }
    // METODOS
    private void generarRutaCompleta() {

        if(rutaImgBase!=null && !rutaImgBase.equalsIgnoreCase("")){
            rutaImgCompleta="http://" + SocketHandler.getHOST() + "/" +SocketHandler.getNombreCarpetaServidor()+"/"+ rutaImgBase.substring(2, rutaImgBase.length());
        }else{
            rutaImgCompleta="";
        }
    }

    @Override
    public String toString() {
        return "HorarioClase{" +
                "idHorario=" + idHorario +
                ", hora=" + hora +
                ", dia=" + dia +
                ", aforoMaximo=" + aforoMaximo +
                ", aforoActual=" + aforoActual +
                ", nombreClase='" + nombreClase + '\'' +
                ", descripccion='" + descripccion + '\'' +
                ", rutaImgBase='" + rutaImgBase + '\'' +
                ", rutaImgCompleta='" + rutaImgCompleta + '\'' +
                ", nombreEntrenador='" + nombreEntrenador + '\'' +
                ", horaSt='" + horaSt + '\'' +
                '}';
    }

    //GETTER AND SETTERS
    public String getRutaImgCompleta() {
        return rutaImgCompleta;
    }

    public void setRutaImgCompleta(String rutaImgCompleta) {
        this.rutaImgCompleta = rutaImgCompleta;
    }

    public int getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(int idHorario) {
        this.idHorario = idHorario;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getAforoMaximo() {
        return aforoMaximo;
    }

    public void setAforoMaximo(int aforoMaximo) {
        this.aforoMaximo = aforoMaximo;
    }

    public int getAforoActual() {
        return aforoActual;
    }

    public void setAforoActual(int aforoActual) {
        this.aforoActual = aforoActual;
    }

    public String getNombreClase() {
        return nombreClase;
    }

    public void setNombreClase(String nombreClase) {
        this.nombreClase = nombreClase;
    }

    public String getDescripccion() {
        return descripccion;
    }

    public void setDescripccion(String descripccion) {
        this.descripccion = descripccion;
    }

    public String getRutaImgBase() {
        return rutaImgBase;
    }

    public void setRutaImgBase(String rutaImgBase) {
        this.rutaImgBase = rutaImgBase;
    }

    public String getNombreEntrenador() {
        return nombreEntrenador;
    }

    public void setNombreEntrenador(String nombreEntrenador) {
        this.nombreEntrenador = nombreEntrenador;
    }

    public String getHoraSt() {
        return horaSt;
    }

    public void setHoraSt(String horaSt) {
        this.horaSt = horaSt;
    }
}
