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
import java.util.Arrays;

public class Inventory extends AppCompatActivity {
    private ArrayList<MixedDrink> drinkObjects;
    private ArrayList<String> liquors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        drinkObjects = new ArrayList<>();
        liquors = new ArrayList<>();
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
            intent.putExtra("liquors", liquors);
            startActivity(intent);
            this.finish();
        });
    }

    /**
     * AsyncTask to look at github and pull drink menu
     */
    private class gitCall extends AsyncTask<Void, Void, ArrayList<String[]>> {
        @Override
        protected ArrayList<String[]> doInBackground(Void... params) {
            try {
                ArrayList<String[]> rtn = new ArrayList<>();
                String[] temp1 = GitAccess.access("https://raw.githubusercontent.com/noahsb1/Bar-Tender/main/app/Text%20Files/drinks.txt");
                String[] temp2 = GitAccess.access("https://raw.githubusercontent.com/noahsb1/Bar-Tender/main/app/Text%20Files/liquors.txt");
                rtn.add(temp1);
                rtn.add(temp2);

                return rtn;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String[]> string) {
            String[] drinks = string.get(0);
            String[] liquorArray = string.get(1);
            for (String drink : drinks) {
                String[] temp2 = drink.split(";");
                drinkObjects.add(new MixedDrink(temp2[0], temp2[1], temp2[2]));
            }
            liquors.addAll(Arrays.asList(liquorArray));
        }
    }
}