package client;

import java.util.Scanner;

public class ScannerWrapper {

    private static Scanner scanner = new Scanner(System.in);

    public static Scanner getInstance() {
        return scanner;
    }
}
