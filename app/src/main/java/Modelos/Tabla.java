package Modelos;

import java.io.Serializable;

public class Tabla implements Serializable {

    int idTabla;
    String nombre;
    int nDias;

    public Tabla(int idTabla, String nombre, int nDias) {
        this.idTabla = idTabla;
        this.nombre = nombre;
        this.nDias = nDias;
    }

    public Tabla() {
    }

    @Override
    public String toString() {
        return "TablaEjercicios{" +
                "idTabla=" + idTabla +
                ", nombre='" + nombre + '\'' +
                ", nDias=" + nDias +
                '}';
    }

    public int getIdTabla() {
        return idTabla;
    }

    public void setIdTabla(int idTabla) {
        this.idTabla = idTabla;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getnDias() {
        return nDias;
    }

    public void setnDias(int nDias) {
        this.nDias = nDias;
    }
}
