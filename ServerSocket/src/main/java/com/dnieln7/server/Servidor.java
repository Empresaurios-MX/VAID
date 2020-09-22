package com.dnieln7.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {

    private static final int puerto = 1444;
    private ServerSocket servidor;

    public static void main(String[] args) {
        Servidor servidor = new Servidor();

        servidor.iniciar();
    }

    public Servidor() {
        try {
            servidor = new ServerSocket(puerto);

            System.out.println("Servidor iniciado en la dirección: " + InetAddress.getLocalHost().getHostAddress()
                    + " puerto: " + puerto);
        } catch (IOException e) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, "Ha ocurrido un error", e);
        }
    }

    private void iniciar() {
        System.out.println("Servidor esta escuchando...");

        while (true) {
            Socket cliente = null;

            try {
                cliente = servidor.accept();

                System.out.println("Cliente conectado... IP: " + cliente.getInetAddress().getHostAddress()
                        + " Puerto: " + cliente.getPort()
                );

                DataInputStream entrada = new DataInputStream(cliente.getInputStream());
                DataOutputStream salida = new DataOutputStream(cliente.getOutputStream());

                Thread hiloCliente = new HiloCliente(cliente, entrada, salida);
                hiloCliente.start();

            } catch (Exception e) {
                if (cliente != null) {
                    try {
                        cliente.close();
                    } catch (IOException ex) {
                        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, e);
                        break;
                    }
                }
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
}
