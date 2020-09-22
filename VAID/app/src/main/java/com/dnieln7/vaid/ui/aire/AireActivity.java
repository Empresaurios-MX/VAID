package com.dnieln7.vaid.ui.aire;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.dnieln7.vaid.R;
import com.dnieln7.vaid.service.ServiceMaster;
import com.dnieln7.vaid.ui.luces.LucesActivity;
import com.dnieln7.vaid.utils.Directorio;
import com.dnieln7.vaid.utils.Printer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AireActivity extends AppCompatActivity {

    private static final String OBJETIVO = "AIRE";

    private boolean aireHb1;
    private boolean aireHb2;

    private CardView hb1;
    private CardView hb2;

    private ServiceMaster serviceMaster;
    private EscucharThread hb1Thread;
    private EscucharThread hb2Thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aire);

        hb1 = findViewById(R.id.aire_1);
        hb2 = findViewById(R.id.aire_2);

        hb1Thread = new EscucharThread(
                Directorio.IP_SERVIDOR,
                Directorio.PUERTO_SERVIDOR,
                "HB1",
                OBJETIVO,
                hb1,
                10
        );

        hb2Thread = new EscucharThread(
                Directorio.IP_SERVIDOR,
                Directorio.PUERTO_SERVIDOR,
                "HB2",
                OBJETIVO,
                hb2,
                15
        );

        serviceMaster = new ServiceMaster(2);
        serviceMaster.runService(hb1Thread, hb2Thread);

        Toolbar toolbar = findViewById(R.id.aire_appbar).findViewById(R.id.toolbar);
        toolbar.setTitle("Aire acondicionado");
        toolbar.setNavigationIcon(R.drawable.ic_less_arrow_left);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hb1Thread.stop();
        hb2Thread.stop();
        serviceMaster = null;
    }

    public void interactuarHb1(View view) {
        try {
            aireHb1 = new InteractuarTask(
                    Directorio.IP_SERVIDOR,
                    Directorio.PUERTO_SERVIDOR,
                    "HB1",
                    OBJETIVO,
                    !aireHb1
            ).execute().get();
        }
        catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            Printer.snackBar(view, "Ha ocurrido un error");
            Printer.logError(LucesActivity.class.getName(), e);
        }

        cambiarEstado(hb1, aireHb1);
    }

    public void interactuarHb2(View view) {
        try {
            aireHb2 = new InteractuarTask(
                    Directorio.IP_SERVIDOR,
                    Directorio.PUERTO_SERVIDOR,
                    "HB2",
                    OBJETIVO,
                    !aireHb2
            ).execute().get();
        }
        catch (ExecutionException | InterruptedException e) {
            Thread.currentThread().interrupt();
            Printer.snackBar(view, "Ha ocurrido un error");
            Printer.logError(LucesActivity.class.getName(), e);
        }

        cambiarEstado(hb2, aireHb2);
    }

    private static void cambiarEstado(CardView habitacion, boolean orden) {
        if (orden) {
            habitacion.setCardBackgroundColor(Directorio.AIRE_ON);
        }
        else {
            habitacion.setCardBackgroundColor(Directorio.AIRE_OFF);
        }
    }

    static class InteractuarTask extends AsyncTask<Void, Void, Boolean> {

        private final String ip;
        private final int puerto;

        private final String habitacion;
        private final String objetivo;
        private final boolean orden;

        InteractuarTask(String ip, int puerto, String habitacion, String objetivo, boolean orden) {
            this.ip = ip;
            this.puerto = puerto;
            this.habitacion = habitacion;
            this.objetivo = objetivo;
            this.orden = orden;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean estado = false;

            try (Socket cliente = new Socket()) {
                cliente.connect(new InetSocketAddress(ip, puerto), 5000);

                if (cliente.isConnected()) {
                    DataInputStream entrada = new DataInputStream(cliente.getInputStream());
                    DataOutputStream salida = new DataOutputStream(cliente.getOutputStream());

                    salida.writeBoolean(false);
                    salida.writeUTF(habitacion);
                    salida.writeUTF(objetivo);
                    salida.writeBoolean(orden);
                    estado = entrada.readBoolean();
                }
                else {
                    throw new SocketTimeoutException();
                }
            }
            catch (IOException e) {
                Printer.logError(InteractuarTask.class.getName(), e);
            }

            return estado;
        }
    }

    private static class EscucharThread implements Runnable {
        private long inicio;

        private final String IP;
        private final int PUERTO;
        private final String HABITACION;
        private final String OBJETIVO;
        private final CardView VIEW;
        private final int DELAY;

        private boolean ciclar;
        private DataInputStream entrada;
        private DataOutputStream salida;

        EscucharThread(String IP, int PUERTO, String HABITACION, String OBJETIVO, CardView VIEW, int DELAY) {
            this.IP = IP;
            this.PUERTO = PUERTO;
            this.HABITACION = HABITACION;
            this.OBJETIVO = OBJETIVO;
            this.VIEW = VIEW;
            this.DELAY = DELAY;
            this.ciclar = true;
            this.entrada = null;
            this.salida = null;

            inicio = System.currentTimeMillis();
        }

        @Override
        public void run() {
            while (ciclar) {
                long fin = System.currentTimeMillis();
                long diferencia = (fin - inicio) / 1000;

                if (diferencia >= DELAY) {
                    boolean estado = false;

                    try (Socket cliente = new Socket(IP, PUERTO)) {
                        entrada = new DataInputStream(cliente.getInputStream());
                        salida = new DataOutputStream(cliente.getOutputStream());

                        if (entrada == null || salida == null) {
                            break;
                        }

                        salida.writeBoolean(true);
                        salida.writeUTF(HABITACION);
                        salida.writeUTF(OBJETIVO);
                        estado = entrada.readBoolean();
                    }
                    catch (IOException e) {
                        Logger.getLogger(EscucharThread.class.getName()).log(Level.SEVERE, "Ha ocurrido un error!", e);
                        break;
                    }

                    inicio = System.currentTimeMillis();
                    cambiarEstado(this.VIEW, estado);
                }
            }
        }

        void stop() {
            this.ciclar = false;
        }
    }
}
