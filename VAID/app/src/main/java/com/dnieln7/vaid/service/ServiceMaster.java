package com.dnieln7.vaid.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceMaster {
    private final ExecutorService EXECUTOR_SERVICE;

    public ServiceMaster(int habitaciones) {
        this.EXECUTOR_SERVICE = Executors.newFixedThreadPool(habitaciones);
    }

    public void runService(Runnable... process) {
        for (Runnable runnable : process) {
            EXECUTOR_SERVICE.submit(runnable);
        }
    }
}
