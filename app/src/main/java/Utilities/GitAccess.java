package Utilities;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class GitAccess {
    /**
     * Read text from websites
     * @param str URL input
     * @return string from website
     * @throws IOException if website can't be reached
     */
    public static String access(String str) throws IOException {
        URL url = new URL(str);
        Scanner s = new Scanner(url.openStream());
        return s.nextLine();
    }
}
