package Screens;

import Objects.MixedDrink;
import Utilities.Adapters.ThreeLevelListAdapter;
import Utilities.GitAccess;
import Utilities.InternalMemory;
import Utilities.SetToArrayList;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ExpandableListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.bartender.R;

import java.io.IOException;
import java.util.*;

public class Inventory extends AppCompatActivity {
    private ArrayList<MixedDrink> drinkObjects = new ArrayList<>();
    private HashMap<String, HashMap<String, ArrayList<String>>> liquorsOnline = new HashMap<>();
    private ArrayList<String> liquorOnlineAsList = new ArrayList<>();
    private static HashMap<String, HashMap<String, ArrayList<String>>> liquorsInInventory = new HashMap<>();
    private ArrayList<String> liquorsInInventoryAsList = new ArrayList<>();
    private static ArrayList<String> selectedLiquors = new ArrayList<>();

    private ArrayList<HashMap<String, ArrayList<String>>> data = new ArrayList<>();
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<ArrayList<String>> secondLevel = new ArrayList<>();
    private ExpandableListView expandableListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        // Copy inventory from memory and turn into hashmap
        try {
            String temp = InternalMemory.getStoredInventory(this, "LiquorsInInventory.txt");
            if(!temp.equals("")) {
                String[] temp1 = temp.split("!");
                for (String category : temp1) {
                    String[] temp2 = category.split("\\{");
                    String categoryName = temp2[0];
                    String[] subCategories =
                        temp2[1].substring(0, temp2[1].length() - 1).split("~~~");
                    HashMap<String, ArrayList<String>> subCategoriesHashMap = new HashMap<>();
                    for (String subCategory : subCategories) {
                        String[] temp3 = subCategory.split(":");
                        String subCategoryName = temp3[0];
                        String[] liquorsInSubCategory = temp3[1].split(";");
                        ArrayList<String> liquorsInSubCategoryArrayList = new ArrayList<>();
                        Collections.addAll(liquorsInSubCategoryArrayList, liquorsInSubCategory);
                        liquorsInInventoryAsList.addAll(liquorsInSubCategoryArrayList);
                        subCategoriesHashMap.put(subCategoryName, liquorsInSubCategoryArrayList);
                    }
                    liquorsInInventory.put(categoryName, subCategoriesHashMap);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (liquorsInInventoryAsList != null) {
            selectedLiquors.addAll(liquorsInInventoryAsList);
        }
        Button backButton = findViewById(R.id.backButton);
        Button mixedDrinkFinder = findViewById(R.id.mixedDrinkFinder);
        Button addToInventory = findViewById(R.id.addToInventory);

        // Execute AsyncTask to access internet
        gitCall gitCall = new gitCall();
        gitCall.execute();

        // Set up inventory adapter
        categories = SetToArrayList.setToArrayList(liquorsInInventory.keySet());
        setUpAdapter();

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
            intent.putExtra("list", liquorOnlineAsList);
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
                    liquorOnlineAsList.addAll(liquorsInSubCategoryArrayList);
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

    public static HashMap<String, HashMap<String, ArrayList<String>>> getLiquorsInInventory() {
        return liquorsInInventory;
    }

    private void setUpAdapter() {
        for (String str : categories) {
            secondLevel.add(SetToArrayList.setToArrayList(liquorsInInventory.get(str).keySet()));
            data.add(liquorsInInventory.get(str));
        }

        expandableListView = findViewById(R.id.expandable_listview1);
        ThreeLevelListAdapter
            threeLevelListAdapter = new ThreeLevelListAdapter(this, categories, secondLevel, data, 2);
        expandableListView.setAdapter(threeLevelListAdapter);
        expandableListView.setOnGroupExpandListener(
            new ExpandableListView.OnGroupExpandListener() {
                int previousGroup = -1;
                @Override
                public void onGroupExpand(int i) {
                    if (i != previousGroup) {
                        expandableListView.collapseGroup(previousGroup);
                    }
                    previousGroup = i;
                }
            });
    }
}