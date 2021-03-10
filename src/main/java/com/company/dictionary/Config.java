package com.company.dictionary;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Config {
    public static Map<String, String> admins = new HashMap<>();
    public static List<Dictionary> dictionaries = new ArrayList<>();
    public static final String BASE_PATH = System.getProperty("user.dir") + "\\src\\main\\java\\com\\company\\dictionary\\files\\";
    public static volatile boolean isLoggedIn;

    private Config() {
    }

    static {
        // load admins (map) from file (admins.txt)
        try (BufferedReader reader = new BufferedReader(
                new FileReader(
                        new File(BASE_PATH + "admins.txt")))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                admins.put(
                        line.split(":")[0],   // userName
                        line.split(":")[1]);  // password
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // load dictionaries (list) from file (dictionaries)
        File dir = new File(BASE_PATH + "dictionaries\\");  // dictionaries dir.
        File[] files = dir.listFiles();

        if (files != null)
            for (File file : files)
                try (BufferedReader reader = new BufferedReader(
                        new FileReader(file))) {

                    String fileName = stripExtension(file.getName());

                    String langFrom = fileName.split("_to_")[0];
                    String langTo = fileName.split("_to_")[1];

                    Dictionary dict = new Dictionary(langFrom, langTo);

                    String line;
                    while ((line = reader.readLine()) != null) {

                        dict.words.put(
                                line.split("=")[0],   // word
                                line.split("=")[1]);  // translation
                    }

                    dictionaries.add(dict);

                } catch (Exception e) {
                    e.printStackTrace();
                }
    }

    private static String stripExtension(String fileName) {
        if (fileName.indexOf(".") > 0)
            return fileName.substring(0, fileName.lastIndexOf("."));

        return fileName;
    }
}
