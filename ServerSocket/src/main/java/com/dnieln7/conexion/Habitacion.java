package com.dnieln7.conexion;

public class Habitacion {
    private final String id;
    private final String ip;
    private final int puerto;

    public Habitacion(String id, String ip, int puerto) {
        this.id = id;
        this.ip = ip;
        this.puerto = puerto;
    }

    public String getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public int getPuerto() {
        return puerto;
    }

    @Override
    public String toString() {
        return "Habitacion{" +
                "id='" + id + '\'' +
                ", ip='" + ip + '\'' +
                ", puerto=" + puerto +
                '}';
    }
}
