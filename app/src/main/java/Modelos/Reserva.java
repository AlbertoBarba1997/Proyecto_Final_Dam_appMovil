package Modelos;

import java.io.Serializable;

public class Reserva implements Serializable {

    int idReserva;
    int idClase;
    String nombreClase;
    String hora;
    String dia;

    public Reserva(int idReserva, String nombreClase, int nDia, String hora) {
        this.idReserva = idReserva;
        this.nombreClase = nombreClase;
        this.hora = hora;
        this.dia = transformarDia(nDia);
    }

    private String transformarDia(int nDia){
        String dia="";
        switch (nDia){
            case 1:
                dia="Lunes";
                break;
            case 2:
                dia="Martes";
                break;
            case 3:
                dia="Miercoles";
                break;
            case 4:
                dia="Jueves";
                break;
            case 5:
                dia="Viernes";
                break;
            case 6:
                dia="Sabado";
                break;
            case 7:
                dia="Domingo";
                break;
            default:
                dia="";
                break;

        }
        return dia;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public int getIdClase() {
        return idClase;
    }

    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    public String getNombreClase() {
        return nombreClase;
    }

    public void setNombreClase(String nombreClase) {
        this.nombreClase = nombreClase;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "idReserva=" + idReserva +
                ", idClase=" + idClase +
                ", nombreClase='" + nombreClase + '\'' +
                ", hora='" + hora + '\'' +
                ", dia='" + dia + '\'' +
                '}';
    }
}
