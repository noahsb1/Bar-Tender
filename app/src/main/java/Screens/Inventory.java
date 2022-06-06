package Screens;

import Objects.MixedDrink;
import Utilities.GitAccess;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.bartender.R;

import java.io.IOException;
import java.util.ArrayList;

public class Inventory extends AppCompatActivity {
    private ArrayList<MixedDrink> drinkObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        drinkObjects = new ArrayList<>();
        gitCall gitCall = new gitCall();
        gitCall.execute();

        TextView text = findViewById(R.id.textView2);
        Button testbutton = findViewById(R.id.testButton);

        testbutton.setOnClickListener(view -> {
            gitCall.cancel(true);
            text.setText(drinkObjects.get(1).getName());
        });
    }

    /**
     * AsyncTask to look at github and pull drink menu
     */
    private class gitCall extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            try {
                return GitAccess.access();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] string) {
            for (String drink : string) {
                String[] temp2 = drink.split(";");
                drinkObjects.add(new MixedDrink(temp2[0], temp2[1], temp2[2]));
            }
        }
    }
}