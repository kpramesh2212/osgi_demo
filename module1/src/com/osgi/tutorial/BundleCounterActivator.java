package com.osgi.tutorial;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

public class BundleCounterActivator implements BundleActivator, BundleListener {
    private BundleContext context;

    @Override
    public void start(BundleContext context) throws Exception {
        this.context = context;
        context.addBundleListener(this);
        printCount();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        context.removeBundleListener(this);
    }

    @Override
    public void bundleChanged(BundleEvent event) {
        switch (event.getType()) {
            case BundleEvent.INSTALLED:
                System.out.println("Installed a new bundle");
                printCount();
                break;
            case BundleEvent.UNINSTALLED:
                System.out.println("Removed a bundle");
                printCount();
                break;
        }
    }

    private void printCount() {
        int length = this.context.getBundles().length;
        System.out.println("There are currently " + length + " bundles.");
    }
}
