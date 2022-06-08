package Utilities;

import android.content.Context;
import android.widget.Toast;

public class ErrorDisplay {
    /**
     * Display toast messages
     * @param context
     * @param errorMessage
     */
    public static void displayError(Context context, String errorMessage) {
        if (context != null) {
            int popUpDuration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, errorMessage, popUpDuration);
            toast.show();
        }
    }
}
