package com.company.dictionary;

import java.io.IOException;
import java.util.Scanner;

public class MenuService {

    public static boolean isFirstTime = true;

    enum Role {
        USER, ADMIN
    }

    public static void mainMenu() throws IOException {
        Role role = !Config.isLoggedIn ? Role.USER : Role.ADMIN;

        switch (role) {
            case USER:
                userMenu();
                break;

            case ADMIN:
                if (isFirstTime)
                    login();

                isFirstTime = false;

                if (Config.isLoggedIn)
                    adminMenu();

                break;
        }
    }

    public static void userOrAdmin() {
        System.out.println("Are you User or Admin (1 | 2): ");

        Scanner in = new Scanner(System.in);
        int result = in.nextInt();

        while (result != 1 && result != 2) {
            clearConsole();
            System.out.println("Wrong! Please, re-enter - User or Admin (1 | 2): ");
            result = in.nextInt();
        }

        Config.isLoggedIn = result != 1;
    }

    public static void userMenu() {
        clearConsole();

        System.out.println("\n\tUSER\n");
        System.out.println(
                "Enter the menu number:\n" +
                        "1. Show dictionaries\n" +
                        "2. Show words\n" +
                        "3. Search\n" +
                        "4. Login"
        );

        Scanner in = new Scanner(System.in);
        int menu = in.nextInt();

        if (menu < 1 || menu > 4)
            return;

        switch (menu) {
            case 1:
                DictionaryService.showDictionaries(true);
                break;

            case 2:
                DictionaryService
                        .showWords(DictionaryService.selectDictionary(), true);
                break;

            case 3:
                DictionaryService
                        .search(DictionaryService.selectDictionary());
                break;

            case 4:
                Config.isLoggedIn = true;
                isFirstTime = true;
                break;
        }
    }

    public static void login() {
        clearConsole();

        Scanner in = new Scanner(System.in);

        boolean result = false;
        int counter = 0;

        while (!result) {
            if (counter >= 1)
                if (counter >= 3) {
                    System.err.println("\nLogin failed! Exited the System...");
                    sleep2Seconds();

                    System.exit(-1);
                } else {
                    clearConsole();
                    System.out.println("Wrong! Please, re-enter: ");
                }

            System.out.println("Enter the user name:    |   type \"-back\" to go back");
            String userName = in.nextLine();

            if ("-back".equals(userName)) {
                Config.isLoggedIn = false;
                return;
            }

            System.out.println("Enter the password:");
            String password = in.nextLine();

            result = password.equals(Config.admins.get(userName));

            ++counter;
        }

        Config.isLoggedIn = true;
        System.out.println("\nSuccessfully logged in!");
        sleep2Seconds();
    }

    public static void adminMenu() throws IOException {
        clearConsole();

        System.out.println("\n\tADMIN\n");
        System.out.println(
                "Enter the menu number:\n" +
                        "1. Create login\n" +
                        "2. Create dictionary\n" +
                        "3. Show dictionaries\n" +
                        "4. Show words\n" +
                        "5. Search\n" +
                        "6. Add word\n" +
                        "7. Modify word\n" +
                        "8. Delete word\n" +
                        "9. Delete dictionary\n" +
                        "10. Logout"
        );

        Scanner in = new Scanner(System.in);
        int menu = in.nextInt();

        if (menu < 1 || menu > 10)
            return;

        switch (menu) {
            case 1:
                AdminService.createLogin();
                break;

            case 2:
                DictionaryService.createDictionary();
                break;

            case 3:
                DictionaryService.showDictionaries(true);
                break;

            case 4:
                DictionaryService
                        .showWords(DictionaryService.selectDictionary(), true);
                break;

            case 5:
                DictionaryService
                        .search(DictionaryService.selectDictionary());
                break;

            case 6:
                DictionaryService
                        .addWord(DictionaryService.selectDictionary());
                break;

            case 7:
                DictionaryService
                        .modifyWord(DictionaryService.selectDictionary());
                break;

            case 8:
                DictionaryService.deleteWord(DictionaryService.selectDictionary());
                break;

            case 9:
                DictionaryService.deleteDictionary();
                break;

            case 10:
                Config.isLoggedIn = false;
                isFirstTime = true;
                break;
        }
    }

    public static void sleep2Seconds() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
    }

    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033\143");
            }
        } catch (IOException | InterruptedException ignored) {
        }
    }
}
