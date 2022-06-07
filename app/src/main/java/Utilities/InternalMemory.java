package Utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;

public class InternalMemory {
    public static String getStoredInventory(Activity activity) throws IOException {
        String file_name = "inventory.txt";

        FileInputStream fileObj = activity.openFileInput(file_name);
        int read = -1;
        StringBuilder buffer = new StringBuilder();
        while((read = fileObj.read()) != -1) {
            buffer.append((char) read);
        }

        fileObj.close();
        return buffer.toString();
    }

    public static void addToInventory(Activity activity, String[] string)
        throws IOException {
        String file_name = "inventory.txt";

        FileOutputStream fileObj = activity.openFileOutput(file_name, Context.MODE_APPEND);
        for (String str : string) {
            fileObj.write(str.getBytes());
        }

        fileObj.close();
    }
}
