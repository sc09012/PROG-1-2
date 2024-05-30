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

    public CollectionManager(String collectionPath) throws IOException {
        this.jsonCollection = new File(collectionPath);
        this.movies = new TreeSet<>();
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
                update(Integer.parseInt(parts[1]), parts[2]);
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
                save();
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
        Movie movie = Movie.fromJson(movieJson);
        movies.add(movie);
        System.out.println("Movie added.");
        save();
    }

    private void update(int id, String movieJson) {
        Movie newMovie = Movie.fromJson(movieJson);
        movies.removeIf(movie -> movie.getId().equals(id));
        movies.add(newMovie);
        System.out.println("Movie updated.");
        save();
    }

    private void removeById(int id) {
        movies.removeIf(movie -> movie.getId().equals(id));
        System.out.println("Movie removed.");
        save();
    }

    private void clear() {
        movies.clear();
        System.out.println("Collection cleared.");
        save();
    }

    private void executeScript(String fileName) {
        // Implement script execution logic here
    }

    private void addIfMin(String movieJson) {
        Movie movie = Movie.fromJson(movieJson);
        if (movies.stream().allMatch(m -> m.compareTo(movie) > 0)) {
            movies.add(movie);
            System.out.println("Movie added.");
            save();
        } else {
            System.out.println("Movie not added. It is not the minimum.");
        }
    }

    private void removeGreater(String movieJson) {
        Movie movie = Movie.fromJson(movieJson);
        movies.removeIf(m -> m.compareTo(movie) > 0);
        System.out.println("Movies greater than the given movie removed.");
        save();
    }

    private void removeLower(String movieJson) {
        Movie movie = Movie.fromJson(movieJson);
        movies.removeIf(m -> m.compareTo(movie) < 0);
        System.out.println("Movies lower than the given movie removed.");
        save();
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
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
                    .create();
            writer.write(gson.toJson(movies.toArray()));
            System.out.println("connection terminated.");
        } catch (IOException e) {
            System.err.println("Error saving collection: " + e.getMessage());
        }
    }
}
