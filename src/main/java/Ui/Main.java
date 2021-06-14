package Ui;

import java.io.File;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        RaceDao dao  =  new RaceDao();
        Ui ui = new Ui(scanner, dao);

        dao.setUp();
        ui.start();
    }
}