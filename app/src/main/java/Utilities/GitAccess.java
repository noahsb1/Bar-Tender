package Utilities;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class GitAccess {
    public static String[] access() throws IOException {
        URL url = new URL("https://raw.githubusercontent.com/noahsb1/Bar-Tender/main/app/Drinks/drinks.txt");
        Scanner s = new Scanner(url.openStream());
        String[] temp = s.nextLine().split("!");
        return temp;
    }
}
