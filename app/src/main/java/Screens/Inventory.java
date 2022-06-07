package Screens;

import Objects.MixedDrink;
import Utilities.GitAccess;
import android.content.Intent;
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
        Button backButton = findViewById(R.id.backButton);
        Button mixedDrinkFinder = findViewById(R.id.mixedDrinkFinder);
        Button addToInventory = findViewById(R.id.addToInventory);
        gitCall gitCall = new gitCall();
        gitCall.execute();

        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(Inventory.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        });

        addToInventory.setOnClickListener(view -> {
            Intent intent = new Intent(Inventory.this, LiquorSelect.class);
            startActivity(intent);
            this.finish();
        });
    }

    /**
     * AsyncTask to look at github and pull drink menu
     */
    private class gitCall extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            try {
                return GitAccess.access("https://raw.githubusercontent.com/noahsb1/Bar-Tender/main/app/Text Files/drinks.txt");
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