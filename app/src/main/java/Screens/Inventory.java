package Screens;

import Objects.MixedDrink;
import Utilities.GitAccess;
import Utilities.InternalMemory;
import Utilities.Adapters.BaseRecycleViewAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bartender.R;

import java.io.IOException;
import java.util.*;

public class Inventory extends AppCompatActivity {
    private ArrayList<MixedDrink> drinkObjects;
    private HashMap<String, HashMap<String, ArrayList<String>>> liquorsOnline;
    private ArrayList<String> liquorsInInventory;
    private static ArrayList<String> selectedLiquors;
    private BaseRecycleViewAdapter adapter;
    private ArrayList<String> liquorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        // Initialize variables and copy inventory from memory
        drinkObjects = new ArrayList<>();
        liquorsOnline = new HashMap<>();
        liquorsInInventory = new ArrayList<>();
        selectedLiquors = new ArrayList<>();
        liquorList = new ArrayList<>();
        try {
            String[] temp = InternalMemory.getStoredInventory(this).split("!");
            liquorsInInventory.addAll(Arrays.asList(temp));
        } catch (IOException e) {
            e.printStackTrace();
        }
        selectedLiquors.addAll(liquorsInInventory);
        Button backButton = findViewById(R.id.backButton);
        Button mixedDrinkFinder = findViewById(R.id.mixedDrinkFinder);
        Button addToInventory = findViewById(R.id.addToInventory);

        // Execute AsyncTask to access internet
        gitCall gitCall = new gitCall();
        gitCall.execute();

        // Creates viewTypes array for Recycler Table
        ArrayList<Integer> temp = new ArrayList<>();
        for (int i = 0; i < liquorsInInventory.size(); i++) {
            temp.add(1);
        }

        // Sort liquor list alphabetically and initialize recycler view
        Collections.sort(this.liquorsInInventory);
        RecyclerView recyclerView = findViewById(R.id.inventoryRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new BaseRecycleViewAdapter(liquorsInInventory, temp, this);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                                                                                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

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
            intent.putExtra("liquors", liquorsOnline);
            intent.putExtra("list", liquorList);
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

            for (String category: liquorArray) {
                String[] temp1 = category.split("\\{");
                String categoryName = temp1[0];
                String[] subCategories = temp1[1].substring(0, temp1[1].length() - 1).split("~~~");
                HashMap<String, ArrayList<String>> subCategoriesHashMap = new HashMap<>();
                for (String subCategory : subCategories) {
                    String[] temp2 = subCategory.split(":");
                    String subCategoryName = temp2[0];
                    String[] liquorsInSubCategory = temp2[1].split(";");
                    ArrayList<String> liquorsInSubCategoryArrayList = new ArrayList<>();
                    Collections.addAll(liquorsInSubCategoryArrayList, liquorsInSubCategory);
                    liquorList.addAll(liquorsInSubCategoryArrayList);
                    subCategoriesHashMap.put(subCategoryName, liquorsInSubCategoryArrayList);
                }
                liquorsOnline.put(categoryName, subCategoriesHashMap);
            }
        }
    }

    public static ArrayList<String> getSelectedLiquors() {
        return selectedLiquors;
    }

    public static void addToSelectedLiquors(String str) {
        selectedLiquors.add(str);
    }

    public static void removeSelectedLiquors(String str) {
        selectedLiquors.remove(str);
    }
}