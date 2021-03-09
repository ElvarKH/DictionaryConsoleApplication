package com.company.dictionary;

import java.io.*;
import java.util.Scanner;

public class AdminService {
    public static boolean adminExists(String userName) {
        return Config.admins.containsKey(userName);
    }

    public static void createLogin() {
        System.out.println("\n\tCreate Login\n");
        System.out.println("Enter new user name:   |   type \"-back\" to go back");
        String userName = new Scanner(System.in).nextLine();

        if ("-back".equals(userName)) {
        } else {
            System.out.println("Enter new password:");
            String password = new Scanner(System.in).nextLine();

            if (adminExists(userName)) {
                System.out.println("\nThis login is already exists");
                createLogin();
            } else {
                // add to logins (map)
                Config.admins.put(userName, password);

                // write to file (admins.txt)
                try (FileWriter fw = new FileWriter(
                        new File(Config.BASE_PATH + "admins.txt"),
                        true)) {  // append = true

                    fw.write(userName + ":" + password + "\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println("\nLogin created successfully!");
                MenuService.sleep2Seconds();
            }
        }
    }
}
