package Utilities;

import Objects.MixedDrink;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileLoader {
    public static ArrayList<MixedDrink> fileLoaderInitializer() throws FileNotFoundException {
        ArrayList<MixedDrink> temp = new ArrayList<>();
        File directory = new File("Drinks");
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                temp.add(fileToObject(file.getName()));
            }
        }
        return temp;
    }

    private static MixedDrink fileToObject(String fileName) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("Drinks/" + fileName));
        String[] temp = scanner.nextLine().split(";");
        return new MixedDrink(temp[0], temp[1], temp[2]);
    }
}
