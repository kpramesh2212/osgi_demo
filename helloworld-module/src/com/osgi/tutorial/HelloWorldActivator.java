package com.osgi.tutorial;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Created by IEI78363 on 20/06/2017.
 */
public class HelloWorldActivator implements BundleActivator {
    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println("Hello World!");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("GoodBye World!");
    }
}
