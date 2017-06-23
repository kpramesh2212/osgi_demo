package com.ramesh;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BundleInstallUpdate implements Runnable {
    private final String dirToScan;
    private final BundleContext context;
    private Map<String, Long> bundlesMap = new HashMap<>();

    public BundleInstallUpdate(BundleContext context, String dirToScan) {
        this.context = context;
        this.dirToScan = dirToScan;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Thread.sleep(5000);
                List<Path> jars = scanForJars();
                if (bundlesMap.isEmpty()) {
                    for (Bundle b : context.getBundles()) {
                        bundlesMap.put(b.getLocation(), b.getBundleId());
                    }
                }
                for (Path jar: jars) {
                    final String bundleLocation = "file:" + jar.toString();
                    if (bundlesMap.containsKey(bundleLocation)) {
                        long bundleId = bundlesMap.get(bundleLocation);
                        if (jar.toFile().lastModified() > context.getBundle(bundleId).getLastModified()) {
                            System.out.println("Updating bundle " + jar + " with id " + bundleId + " to a newer " +
                                    "version");
                            try {
                                context.getBundle(bundleId).update();
                            } catch (BundleException e) {
                                System.out.println("unable to update bundle with id " + bundleId);
                            }
                        }
                    } else {
                        try {
                            Bundle addedBundle = context.installBundle(bundleLocation);
                            System.out.println("Added bundle " + jar + " with id " + addedBundle.getBundleId() + " " +
                                    "from location " + addedBundle.getLocation());
                            bundlesMap.put(addedBundle.getLocation(), addedBundle.getBundleId());
                        } catch (BundleException e) {
                            System.out.println("unable to add bundle " + jar);
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            System.out.println("JarFileScanner is now shutting down");
        }
    }

    private List<Path> scanForJars() {
        Path path = Paths.get(dirToScan);
        List<Path> jarFiles = new ArrayList<>();
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path fileName = file.getFileName();
                    if (fileName.toString().endsWith(".jar")) {
                        jarFiles.add(file);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jarFiles;
    }

}
