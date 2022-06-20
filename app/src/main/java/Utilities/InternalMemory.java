package Utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;

public class InternalMemory {
    /**
     * Reads stored internal memory of inventory
     * @param activity
     * @return string of stored inventory
     * @throws IOException
     */
    public static String getStoredInventory(Activity activity, String filename) throws IOException {
        FileInputStream fileObj = activity.openFileInput(filename);
        int read = -1;
        StringBuilder buffer = new StringBuilder();
        while((read = fileObj.read()) != -1) {
            buffer.append((char) read);
        }

        fileObj.close();
        return buffer.toString();
    }

    /**
     * Saves inventory to internal memory
     * @param activity
     * @param string
     * @throws IOException
     */
    public static void addToInventory(Activity activity, String string, String filename)
        throws IOException {

        FileOutputStream fileObj = activity.openFileOutput(filename, Context.MODE_PRIVATE);
        fileObj.write(string.getBytes());
        fileObj.close();
    }
}
