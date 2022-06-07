package Utilities;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class GitAccess {
    public static String[] access(String str) throws IOException {
        URL url = new URL(str);
        Scanner s = new Scanner(url.openStream());
        String[] temp = s.nextLine().split("!");
        return temp;
    }
}
