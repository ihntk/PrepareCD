package com.af.igor.prepcd.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleHelper {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static String readString() {
        String line = null;

        do {
            try {
                line = reader.readLine();
            } catch (IOException e) {
                System.out.println("Try again");
            }
        } while (line == null);

        return line;
    }
}
