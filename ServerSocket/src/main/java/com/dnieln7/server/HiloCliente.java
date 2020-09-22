package com.dnieln7.server;


import com.dnieln7.conexion.Habitacion;
import com.dnieln7.conexion.Habitaciones;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

class HiloCliente extends Thread {

    private final Socket cliente;

    private final DataInputStream entrada;
    private final DataOutputStream salida;

    public HiloCliente(Socket cliente, DataInputStream entrada, DataOutputStream salida) {
        this.cliente = cliente;
        this.entrada = entrada;
        this.salida = salida;
    }

    @Override
    public void run() {
        String idHabitacion;
        String objetivo;

        while (true) {
            try {
                System.out.println("---------------------------------------------------------------------------------");
                // Se verifica si es una llamada del usuario o de un proceso en segundo plano
                boolean auto = entrada.readBoolean();
                System.out.println("El valor del modo automático seleccionado es: " + auto + "\n");

                // Se obtiene el id de la habitación, el objetivo y la orden
                idHabitacion = entrada.readUTF();
                objetivo = entrada.readUTF();
                System.out.println("Habitación entrante: " + idHabitacion);
                System.out.println("Objetivo entrante: " + objetivo);

                if (auto) {
                    // Según la habitación se hace la llamada
                    switch (idHabitacion) {
                        case "HB1":
                            escuchar(Habitaciones.hb1, objetivo);
                            break;
                        case "HB2":
                            escuchar(Habitaciones.hb2, objetivo);
                            break;
                    }
                    cerrar();
                    System.out.println("-----------------------------------------------------------------------------");
                    break;
                } else {
                    boolean orden = entrada.readBoolean();
                    System.out.println("Orden entrante: " + orden + "\n");

                    // Según la habitación se hace la llamada
                    switch (idHabitacion) {
                        case "HB1":
                            interactuar(Habitaciones.hb1, objetivo, orden);
                            break;
                        case "HB2":
                            interactuar(Habitaciones.hb2, objetivo, orden);
                            break;
                    }

                    cerrar();
                    System.out.println("-----------------------------------------------------------------------------");
                    break;
                }
            } catch (IOException e) {
                Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, null, e);
                cerrar();
                break;
            }
        }
    }

    private void interactuar(final Habitacion habitacion, final String objetivo, final boolean orden) throws IOException {
        boolean estado = false;

        System.out.println("Se ha seleccionado la habitación: " + habitacion.getId()
                + " Con objectivo: " + objetivo + " y orden: " + orden
        );

        System.out.println("Se esta conectando a la habitación: " + habitacion.getId()
                + " IP: " + habitacion.getIp() + " Puerto: " + habitacion.getPuerto()
        );

        try (Socket socket = new Socket(habitacion.getIp(), habitacion.getPuerto())) {

            DataInputStream entrada = new DataInputStream(socket.getInputStream());
            DataOutputStream salida = new DataOutputStream(socket.getOutputStream());

            salida.writeBoolean(false);
            salida.writeUTF(objetivo);
            salida.writeBoolean(orden);

            entrada.readUTF();

            estado = entrada.readBoolean();

            System.out.println("\nEl objetivo: " + objetivo
                    + " de la habitacion: " + habitacion.getId()
                    + " ha cambiado su estado a: " + estado
            );
        } catch (IOException e) {
            Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, "ha ocurrido un error", e);
        }

        // Se envian el estado actual del objetivo
        salida.writeBoolean(estado);
    }

    private void escuchar(final Habitacion habitacion, final String objetivo) throws IOException {
        boolean estado = false;

        System.out.println("Se ha seleccionado la habitación: " + habitacion.getId()
                + " Con objectivo: " + objetivo
        );

        System.out.println("Se esta conectando a la habitación: " + habitacion.getId()
                + " IP: " + habitacion.getIp() + " Puerto: " + habitacion.getPuerto()
        );

        try (Socket socket = new Socket(habitacion.getIp(), habitacion.getPuerto())) {

            DataInputStream entrada = new DataInputStream(socket.getInputStream());
            DataOutputStream salida = new DataOutputStream(socket.getOutputStream());

            salida.writeBoolean(true);
            salida.writeUTF(objetivo);

            entrada.readUTF();

            estado = entrada.readBoolean();

            System.out.println("\nEl objetivo: " + objetivo
                    + " de la habitacion: " + habitacion.getId()
                    + " tiene un estado: " + estado
            );
        } catch (IOException e) {
            Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, "ha ocurrido un error", e);
        }

        // Se envian el estado actual del objetivo
        salida.writeBoolean(estado);
    }

    private void cerrar() {
        System.out.println("El cliente:  " + this.cliente + " ha salido");
        System.out.println("Cerrando la conexión...");

        try {
            this.cliente.close();
            this.entrada.close();
            this.salida.close();

            System.out.println("Conexión cerrada");
        } catch (IOException e) {
            Logger.getLogger(HiloCliente.class.getName()).log(Level.SEVERE, "ha ocurrido un error", e);
        }
    }
}
