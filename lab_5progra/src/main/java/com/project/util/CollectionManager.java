package com.project.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.project.model.Movie;
import java.io.*;
import java.time.ZonedDateTime;
import java.util.Scanner;
import java.util.TreeSet;

public class CollectionManager {
    private TreeSet<Movie> movies;
    private File jsonCollection;
    private Gson gson;

    public CollectionManager(String collectionPath) throws IOException {
        this.jsonCollection = new File(collectionPath);
        this.movies = new TreeSet<>();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
                .create();
        load();
    }

    public void interactiveMode() {
        try (Scanner commandReader = new Scanner(System.in)) {
            String userCommand;
            while (true) {
                System.out.print("> ");
                userCommand = commandReader.nextLine();
                handleCommand(userCommand);
            }
        }
    }

    private void handleCommand(String command) {
        String[] parts = command.trim().split(" ", 2);
        switch (parts[0]) {
            case "help":
                showHelp();
                break;
            case "info":
                showInfo();
                break;
            case "show":
                show();
                break;
            case "add":
                add(parts[1]);
                break;
            case "update":
                if (parts.length < 2) {
                System.out.println("Usage: update <id> <movieJson>");
                break;
            }
            String[] updateParts = parts[1].split(" ", 2);
            if (updateParts.length < 2) {
                System.out.println("Usage: update <id> <movieJson>");
                break;
            }
            int updateId = Integer.parseInt(updateParts[0]); // Convierte el primer elemento a ID
            String movieJson = updateParts[1]; // Obtiene el JSON
            update(updateId, movieJson);
            break;
            case "remove_by_id":
                removeById(Integer.parseInt(parts[1]));
                break;
            case "clear":
                clear();
                break;
            case "save":
                save();
                break;
            case "execute_script":
                executeScript(parts[1]);
                break;
            case "exit":
                System.exit(0);
                break;
            case "add_if_min":
                addIfMin(parts[1]);
                break;
            case "remove_greater":
                removeGreater(parts[1]);
                break;
            case "remove_lower":
                removeLower(parts[1]);
                break;
            case "min_by_oscars_count":
                minByOscarsCount();
                break;
            case "print_ascending":
                printAscending();
                break;
            case "print_field_ascending_oscars_count":
                printFieldAscendingOscarsCount();
                break;
            default:
                System.out.println("Unknown command. Type 'help' for a list of commands.");
        }
    }

    private void showHelp() {
        System.out.println("Available commands: help, info, show, add {element}, update id {element}, remove_by_id id, clear, save, execute_script file_name, exit, add_if_min {element}, remove_greater {element}, remove_lower {element}, min_by_oscars_count, print_ascending, print_field_ascending_oscars_count");
    }

    private void showInfo() {
        System.out.println("Collection type: " + movies.getClass());
        System.out.println("Number of movies: " + movies.size());
    }

    private void show() {
        movies.forEach(movie -> System.out.println(movie));
    }

    private void add(String movieJson) {
        Movie movie = gson.fromJson(movieJson, Movie.class); // Usar la instancia de Gson
        movies.add(movie);
        System.out.println("Movie added.");
    }

  private void update(int id, String movieJson) {
    Movie newMovie = Movie.fromJson(movieJson);
    
    // Verifica si la película existe
    Movie existingMovie = movies.stream()
                                 .filter(movie -> movie.getId().equals(id))
                                 .findFirst()
                                 .orElse(null);
    
    if (existingMovie != null) {
        movies.remove(existingMovie); // Elimina la película existente
        movies.add(newMovie); // Agrega la nueva película
        System.out.println("Movie updated.");
    } else {
        System.out.println("Movie not found for the given ID.");
    }
}

    private void removeById(int id) {
        movies.removeIf(movie -> movie.getId().equals(id));
        System.out.println("Movie removed.");
    }

    private void clear() {
        movies.clear();
        System.out.println("Collection cleared.");
    }

  private void executeScript(String fileName) {
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
        String command;
        while ((command = reader.readLine()) != null) {
            System.out.println("Executing: " + command);
            handleCommand(command);
        }
    } catch (IOException e) {
        System.out.println("Error reading the script file: " + e.getMessage());
    } catch (Exception e) {
        System.out.println("An error occurred while executing the command: " + e.getMessage());
    }
}

  private void addIfMin(String movieJson) {
    Movie movie = Movie.fromJson(movieJson);
    // Comparar usando el conteo de Oscars
    boolean isMin = movies.stream().allMatch(m -> m.getOscarsCount() > movie.getOscarsCount());
    
    if (isMin) {
        movies.add(movie);
        System.out.println("Movie added.");
        save();
    } else {
        System.out.println("Movie not added. It is not the minimum.");
    }
}

  private void removeGreater(String movieJson) {
    Movie movie = Movie.fromJson(movieJson);
    movies.removeIf(m -> m.getOscarsCount() > movie.getOscarsCount());
    System.out.println("Movies greater than the given movie removed.");
}


   private void removeLower(String movieJson) {
    Movie movie = Movie.fromJson(movieJson);
    movies.removeIf(m -> m.getOscarsCount() < movie.getOscarsCount());
    System.out.println("Movies lower than the given movie removed.");
}

    private void minByOscarsCount() {
        movies.stream().min((m1, m2) -> Integer.compare(m1.getOscarsCount(), m2.getOscarsCount()))
                .ifPresent(movie -> System.out.println(movie));
    }

    private void printAscending() {
        movies.forEach(movie -> System.out.println(movie));
    }

    private void printFieldAscendingOscarsCount() {
        movies.stream().sorted((m1, m2) -> Integer.compare(m1.getOscarsCount(), m2.getOscarsCount()))
                .forEach(movie -> System.out.println(movie.getOscarsCount()));
    }

    private void load() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(jsonCollection)))) {
            StringBuilder jsonText = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonText.append(line);
            }
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
                    .create();
            Movie[] movieArray = gson.fromJson(jsonText.toString(), Movie[].class);
            for (Movie movie : movieArray) {
                movies.add(movie);
            }
            System.out.println("Collection loaded. Number of movies: " + movies.size());
        }
    }

    private void save() {
        try (Writer writer = new BufferedWriter(new FileWriter(jsonCollection))) {
            writer.write(gson.toJson(movies.toArray())); // Usar la instancia de Gson
            System.out.println("Collection saved.");
        } catch (IOException e) {
            System.err.println("Error saving collection: " + e.getMessage());
        }
    }

}
