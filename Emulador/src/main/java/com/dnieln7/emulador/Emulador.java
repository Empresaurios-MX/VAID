package com.dnieln7.emulador;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Emulador {
    private static final int puerto = 1440;

    private boolean luces;
    private boolean cerradura;
    private boolean aire;

    public static void main(String[] args) {
        Emulador hb1 = new Emulador();

        hb1.iniciar();
    }

    public Emulador() {
        this.luces = true;
        this.cerradura = false;
        this.aire = true;
    }

    private void iniciar() {
        try (ServerSocket servidor = new ServerSocket(puerto)) {

            servidor.getInetAddress();
            System.out.println("Servidor iniciado en la dirección: " + InetAddress.getLocalHost().getHostAddress()
                    + " puerto: " + puerto);

            while (true) {
                Socket cliente = servidor.accept();

                System.out.println("Conexión entrante... IP: " + cliente.getInetAddress().getHostAddress() + " Puerto: "
                        + cliente.getPort());

                DataInputStream entrada = new DataInputStream(cliente.getInputStream());
                DataOutputStream salida = new DataOutputStream(cliente.getOutputStream());

                boolean auto = entrada.readBoolean();
                String objetivo = entrada.readUTF();

                if (auto) {
                    switch (objetivo) {
                        case "LUCES":
                            salida.writeUTF(objetivo);
                            salida.writeBoolean(luces);
                            break;
                        case "CERRADURA":
                            salida.writeUTF(objetivo);
                            salida.writeBoolean(cerradura);
                            break;
                        case "AIRE":
                            salida.writeUTF(objetivo);
                            salida.writeBoolean(aire);
                            break;
                    }
                } else {
                    boolean orden = entrada.readBoolean();

                    switch (objetivo) {
                        case "LUCES":
                            if (orden) {
                                System.out.println("Las luces se han encendido");
                            } else {
                                System.out.println("Las luces se han apagado");
                            }

                            luces = orden;
                            salida.writeUTF(objetivo);
                            salida.writeBoolean(luces);
                            break;
                        case "CERRADURA":
                            if (orden) {
                                System.out.println("La cerradura se ha desbloqueado");
                            } else {
                                System.out.println("La cerradura se ha bloqueado");
                            }

                            cerradura = orden;
                            salida.writeUTF(objetivo);
                            salida.writeBoolean(cerradura);
                            break;
                        case "AIRE":
                            if (orden) {
                                System.out.println("El aire acondicionado se ha encendido");
                            } else {
                                System.out.println("El aire acondicionado se ha apagado");
                            }

                            aire = orden;
                            salida.writeUTF(objetivo);
                            salida.writeBoolean(aire);
                            break;
                    }
                }
            }
        } catch (IOException e) {
            Logger.getLogger(Emulador.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
