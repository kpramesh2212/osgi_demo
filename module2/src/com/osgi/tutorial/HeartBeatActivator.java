package com.osgi.tutorial;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Created by IEI78363 on 20/06/2017.
 */
public class HeartBeatActivator implements BundleActivator {
    Thread t;

    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println("Starting the Heart Beat thread");
        t = new Thread(new HeartBeat());
        t.start();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("Stopping the Heart beat thread");
        t.interrupt();
    }
}

class HeartBeat implements Runnable {
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("Heart beat signal");
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            System.out.println("I am dead");
        }
    }
}
