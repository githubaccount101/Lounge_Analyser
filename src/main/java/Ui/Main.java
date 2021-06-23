package Ui;

import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Ui ui = new Ui(scanner);

        RaceDao.resetTables();

        RaceDao.generateRandom(100);

    }
}
