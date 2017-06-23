package com.osgi.tutorial;

import com.ramesh.BundleInstallUpdate;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class BundleUpdateActivator implements BundleActivator {
    Thread bundleInstaller;

    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println("Starting the bundle update activator");
        bundleInstaller = new Thread(new BundleInstallUpdate(context, "output"));
        bundleInstaller.start();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("Stopping the bundle update activator");
        bundleInstaller.interrupt();
    }
}
