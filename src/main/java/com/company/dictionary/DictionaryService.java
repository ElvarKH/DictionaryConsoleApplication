package com.company.dictionary;

import java.io.*;
import java.util.*;

public class DictionaryService {
    public static void createDictionary() throws IOException {
        MenuService.clearConsole();

        Scanner in = new Scanner(System.in);

        System.out.println("\n\tCreate Dictionary\n");
        System.out.println("Enter dictionary language (from):   |   type \"-back\" to go back");
        String langFrom = in.nextLine();

        if ("-back".equals(langFrom)) {
        } else {

            System.out.println("Enter dictionary language (to): ");
            String langTo = in.nextLine();

            Dictionary dict = new Dictionary(langFrom, langTo);

            // add to dictionaries (map)
            Config.dictionaries.add(dict);

            // add to folder (dictionaries)
            if (!new File(dict.getPath()).createNewFile()) {
                System.err.println("Failed to create file");
                MenuService.sleep2Seconds();
                System.exit(-1);
            }

            System.out.println("\nDictionary created successfully!");
            MenuService.sleep2Seconds();
        }
    }

    public static void showDictionaries(boolean waitAfterShowing) {
        int n = Config.dictionaries.size();

        if (n == 0) {
            System.out.println("There is no dictionary yet");
            MenuService.sleep2Seconds();
            return;
        }

        System.out.println("\n\tDictionaries\n");

        for (int i = 0; i < n; i++) {
            System.out.println(i + ". " +  // dictionary index
                    Config.dictionaries
                            .get(i)
                            .getName());   // dictionary name
        }

        if (waitAfterShowing) {
            System.out.print("\nnext: ENTER");
            new Scanner(System.in).nextLine();
        }
    }

    public static boolean showWords(Dictionary dict, boolean waitAfterShowing) {
        if (dict == null)
            return false;

        int n = dict.words.size();

        if (n == 0) {
            System.out.println("Not a word yet");
            MenuService.sleep2Seconds();
            return false;
        }

        MenuService.clearConsole();

        // Print words

        // Method 1 - with entrySet method in Map:
        System.out.println("\n\t" + dict.getName() + "\n");  // print dictionary name

        for (Map.Entry<String, String> entry : dict.words.entrySet()) {
            System.out.print(entry.getKey() + " ");

            for (int i = 0; i < 27 - entry.getKey().length(); i++)
                System.out.print("-");

            System.out.println(" " + entry.getValue());
        }

        // Method 2 - with java 8 features:
//        System.out.println("\t" + dict.getName() + "\n");  // print dictionary name
//
//        dict.words.forEach((word, translation) -> {
//            System.out.print(word + " ");
//
//            for (int i = 0; i < 27 - word.length(); i++)
//                System.out.print("-");
//
//            System.out.println(" " + translation);
//        });

        if (waitAfterShowing) {
            System.out.print("\nnext: ENTER");
            new Scanner(System.in).nextLine();
        }
        return true;
    }

    public static void addWord(Dictionary dict) {
        if (dict == null)
            return;

        Scanner in = new Scanner(System.in);

        System.out.println("Enter the new word:   |   type \"-back\" to go back");
        String word = in.nextLine();

        if ("-back".equals(word)) {
        } else {

            if (word.length() > 25) {
                MenuService.clearConsole();
                System.out.println("Word length can not greater than 25");
                addWord(dict);
            } else {
                System.out.print("Enter the translation of the word: ");
                String translation = in.nextLine();

                // add to the appropriate dictionary
                dict.words.put(word, translation);

                // update dictionary on file
                try (FileWriter fw =
                             new FileWriter(
                                     new File(dict.getPath()),
                                     true)) {   // append = true

                    fw.write(word + "=" + translation + "\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("\nWord added successfully!");
                MenuService.sleep2Seconds();
            }
        }
    }

    public static void modifyWord(Dictionary dict) {
        if (dict == null)
            return;

        Map<String, String> map = dict.words;

        if (map.isEmpty()) {
            System.out.println("Not a word yet");
            return;
        }

        Scanner in = new Scanner(System.in);

        showWords(dict, false); // print words

        System.out.println("\nEnter the word to be modified:   |   type \"-back\" to go back");
        String wordToModify = in.nextLine();

        if ("-back".equals(wordToModify)) {
        } else {
            System.out.print("\nEnter new word: ");
            String newWord = in.nextLine();

            System.out.print("Enter translation: ");
            String newTranslation = in.nextLine();

            if (!map.containsKey(wordToModify)) {    // if word does not exist
                System.out.println("Wrong! This word does not exist in the dictionary. Please, re-enter");
                MenuService.sleep2Seconds();
                modifyWord(dict);
            } else {
                // modify on map
                map.remove(wordToModify);
                map.put(newWord, newTranslation);

                // modify on file
                try (BufferedWriter fw = new BufferedWriter(
                        new FileWriter(
                                new File(dict.getPath()),
                                false))) {   // append = false  // by default = false

                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        fw.write(entry.getKey() + "=" + entry.getValue() + "\n");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("\nWord modified successfully!");
            }
        }
    }

    public static void deleteWord(Dictionary dict) {
        if (dict == null)
            return;

        // print words
        if (showWords(dict, false)) {
            Map<String, String> map = dict.words;

            System.out.println("\nEnter the word to be deleted:   |   type \"-back\" to go back");
            String word = new Scanner(System.in).nextLine();

            if ("-back".equals(word)) {
            } else {
                if (!map.containsKey(word)) {
                    System.out.println("Wrong! This word does not exist in the dictionary. Please, re-enter");
                    deleteWord(dict);
                } else {
                    // modify on map
                    map.remove(word);

                    // modify on file
                    try (BufferedWriter fw = new BufferedWriter(
                            new FileWriter(
                                    new File(dict.getPath()),
                                    false))) {   // append = false  // by default = false

                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            fw.write(entry.getKey() + "=" + entry.getValue() + "\n");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("\nWord deleted successfully!");
                    MenuService.sleep2Seconds();
                }
            }
        }
    }

    public static void deleteDictionary() {
        Dictionary dict = selectDictionary();

        if (dict == null)
            return;

        // delete on list
        Config.dictionaries.remove(dict);

        // delete from directory
        if (new File(dict.getPath()).delete()) {
            System.out.println("\nFile deleted successfully!");
            MenuService.sleep2Seconds();
        }
    }

    public static Dictionary selectDictionary() {
        MenuService.clearConsole();
        Scanner in = new Scanner(System.in);
        int dictIndex = 0;
        int counter = 0;
        boolean checker = false;

        while (!checker) {
            if (counter >= 1)
                System.out.print("\nWrong! Please, re-enter index: ");

            System.out.println("Select (index) dictionary:   |   type \"-1\" to go back");

            showDictionaries(false);
            dictIndex = in.nextInt();

            if (-1 == dictIndex)
                return null;

            if (dictIndex >= 0 && dictIndex <= Config.dictionaries.size() - 1)
                checker = true;

            ++counter;
        }

        return Config.dictionaries.get(dictIndex);
    }

    public static void search(Dictionary dict) {
        if (dict == null)
            return;

        MenuService.clearConsole();

        System.out.println("\t\n" + dict.getName() + "\n");  // print dictionary name

        System.out.println("Enter the word you are looking for:   |   type \"-back\" to go back");
        String word = new Scanner(System.in).nextLine();

        if ("-back".equals(word)) {
        } else {
            String translation = dict.words.get(word);

            if (translation == null) {


                System.out.println("This word does not exist in the dictionary");
                search(dict);
            } else {
                System.out.println(word + " = " + translation);
            }

            new Scanner(System.in).nextLine();
            MenuService.clearConsole();
        }
    }
}
