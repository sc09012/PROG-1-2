package com.project;

import com.project.util.CollectionManager;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java com.project.Main <filename>");
            return;
        }

        String filename = args[0];
        try {
            CollectionManager manager = new CollectionManager(filename);
            manager.interactiveMode();
        } catch (IOException e) {
            System.err.println("Error inicializando la aplicación: " + e.getMessage());
        }
    }
}
