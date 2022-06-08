package Screens;

import Objects.MixedDrink;
import Utilities.GitAccess;
import Utilities.InternalMemory;
import Utilities.RecycleViewAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bartender.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Inventory extends AppCompatActivity {
    private ArrayList<MixedDrink> drinkObjects;
    private ArrayList<String> liquors;
    private ArrayList<String> liquorsInInventory;
    private RecycleViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        // Initialize variables and copy inventory from memory
        drinkObjects = new ArrayList<>();
        liquors = new ArrayList<>();
        liquorsInInventory = new ArrayList<>();
        try {
            String[] temp = InternalMemory.getStoredInventory(this).split("!");
            for (String str : temp) {
                liquorsInInventory.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Button backButton = findViewById(R.id.backButton);
        Button mixedDrinkFinder = findViewById(R.id.mixedDrinkFinder);
        Button addToInventory = findViewById(R.id.addToInventory);

        // Execute AsyncTask to access internet
        gitCall gitCall = new gitCall();
        gitCall.execute();

        // Sort liquor list alphabetically and initialize recycler view
        Collections.sort(this.liquorsInInventory);
        RecyclerView recyclerView = findViewById(R.id.inventoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecycleViewAdapter(liquorsInInventory, liquorsInInventory, 1);
        recyclerView.setAdapter(adapter);

        // Define action on back button press
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(Inventory.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        });

        // Define action on add to inventory button press
        addToInventory.setOnClickListener(view -> {
            Intent intent = new Intent(Inventory.this, LiquorSelect.class);
            gitCall.cancel(true);
            intent.putExtra("liquors", liquors);
            intent.putExtra("inventory", liquorsInInventory);
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
                String[] temp2 = GitAccess.access("https://raw.githubusercontent.com/noahsb1/Bar-Tender/main/app/Text%20Files/liquorList.txt");
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
            for (String liquor: liquorArray) {
                liquors.add(liquor);
            }
        }
    }
}