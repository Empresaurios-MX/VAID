package com.dnieln7.conexion;

public class Habitaciones {
    public static Habitacion hb1;
    public static Habitacion hb2;

    static {
        hb1 = new Habitacion("HB1", "26.93.67.230", 1440);
        hb2 = new Habitacion("HB2", "26.93.67.230", 1441);
    }
}
