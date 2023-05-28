package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Main {
    public static void main(String[] args) throws IOException {
        String myDirectoryPath = "C:\\Users\\himesh.nandani\\Documents\\Himesh\\results";
        File dir = new File(myDirectoryPath);
        List<Path> paths = new ArrayList<>();
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                paths.add(child.toPath());
            }
        } else {
            System.out.println("Not a folder");
        }

        AtomicInteger tp = new AtomicInteger();
        AtomicInteger fn = new AtomicInteger();
        AtomicInteger fp = new AtomicInteger();
        AtomicInteger tn = new AtomicInteger();

        AtomicInteger tpA = new AtomicInteger();
        AtomicInteger fnA = new AtomicInteger();
        AtomicInteger fpA = new AtomicInteger();
        AtomicInteger tnA = new AtomicInteger();
        for (Path path : paths) {
            System.out.println("Working with File: "+path.getFileName());
            tp.set(0);
            fn.set(0);
            fp.set(0);
            tn.set(0);
            tpA.set(0);
            fnA.set(0);
            fpA.set(0);
            tnA.set(0);


            List<String> stream = Files.readAllLines(path);
            AtomicLong count = new AtomicLong();
            count.set(stream.size());
            count.set(count.get() / 2);
            AtomicLong streamCount = new AtomicLong();
            stream.forEach(s -> {
                String[] tokens = s.split(",");
                try {
                    boolean presented = Boolean.parseBoolean(tokens[2]);
                    boolean responded = Boolean.parseBoolean(tokens[3]);
                    boolean falseNegative = tokens[1].equalsIgnoreCase("none");

                    if (streamCount.get() <= count.get()) {
                        if (presented && responded)
                            tp.addAndGet(1);
                        else if (presented && !responded)
                            fp.addAndGet(1);
                        else if (!presented && responded)
                            fn.addAndGet(1);
                        else
                            tn.addAndGet(1);
                    } else {
                        if (presented && responded)
                            tpA.addAndGet(1);
                        else if (presented && !responded)
                            fpA.addAndGet(1);
                        else if (!presented && responded)
                            fnA.addAndGet(1);
                        else
                            tnA.addAndGet(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Could not parse line number. Skipping line" + tokens[0]);
                }
                streamCount.getAndIncrement();
            });
//            System.out.println("Before values");
//            System.out.println("Tp:"+tp.get()+", fp:"+fp+", fn:"+fn+", tn:"+tn);
//
//            System.out.println("After values");
//            System.out.println("Tp:"+tpA.get()+", fp:"+fpA+", fn:"+fnA+", tn:"+tnA);


            float precision = (float)tp.get() /(float) ((float)tp.get() + (float)fp.get());
            float recall = (float)tp.get() / (float)((float)tp.get() + (float)fn.get());
            float f1 = (2 * precision * recall) / (precision + recall);

            float precisionA = (float)tpA.get() / (float)((float) tpA.get() + (float) fpA.get());
            float recallA = (float) tpA.get() / (float) ((float) tpA.get() + (float) fnA.get());
            float f1A = (2 * precisionA * recallA) / (precisionA + recallA);
//            System.out.println("Precision before : " + precision + " Precision after : " + precisionA);
//            System.out.println("Recall before : " + recall + " Recall after : " + recallA);

            System.out.println("F1 before : " + f1 + " F1 after : " + f1A);
        }
    }

}

