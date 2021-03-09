package com.company.dictionary;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        MenuService.clearConsole();

        System.out.println("---------------------------------------\n" +
                "--- Welcome Dictionary Application! ---\n" +
                "---------------------------------------\n");
        MenuService.userOrAdmin();

        while (true)
            MenuService.mainMenu();
    }
}
