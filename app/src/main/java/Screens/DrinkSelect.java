package Screens;

import Objects.MixedDrink;
import Utilities.Adapters.BaseRecycleViewAdapter;
import Utilities.Adapters.SecondLevelAdapter;
import Utilities.ErrorDisplay;
import android.content.Intent;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bartender.R;

import javax.crypto.AEADBadTagException;
import java.util.ArrayList;

public class DrinkSelect extends AppCompatActivity {
    private ArrayList<MixedDrink> mixedDrinks;
    private ArrayList<String> liquorsInInventoryAsList;
    private ArrayList<String> subcategoriesOfLiquorsInInventoryAsList;
    private ArrayList<String> categories;
    private ExpandableListView expandableListView;
    private SecondLevelAdapter secondLevelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_select);
        Bundle extras = getIntent().getExtras();

        Button backButton = findViewById(R.id.backButton3);
        SearchView searchView = findViewById(R.id.searchBar);


        mixedDrinks = (ArrayList<MixedDrink>) extras.get("mixedDrinks");
        liquorsInInventoryAsList = (ArrayList<String>) extras.get("inventoryList");
        subcategoriesOfLiquorsInInventoryAsList = (ArrayList<String>) extras.get("categoryList");
        categories = (ArrayList<String>) extras.get("categories");
        ArrayList<Integer> rowTypes = new ArrayList<>();
        for (int i = 0; i < mixedDrinks.size(); i++) {
            rowTypes.add(i, 2);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        setUpAdapter();
        setUpRecycler();

        // Define action on back button press
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(DrinkSelect.this, Inventory.class);
            startActivity(intent);
            this.finish();
        });
    }

    private void filter(String text) {
        ArrayList<MixedDrink> filteredlist = new ArrayList<>();
        ArrayList<Integer> rowTypes = new ArrayList<>();

        for (MixedDrink item : mixedDrinks) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
                rowTypes.add(2);
            }
        }
        secondLevelAdapter.filterList(sortDrinks(filteredlist));
    }

    private void setUpAdapter() {
        expandableListView = findViewById(R.id.expandable_listview3);

        ArrayList<String> categories = new ArrayList<>();
        categories.add("Drinks you can make");
        categories.add("Drinks you can't make");

        ArrayList<ArrayList> mixedDrinks = sortDrinks(this.mixedDrinks);


        secondLevelAdapter = new SecondLevelAdapter(this, mixedDrinks, categories, "", 3);
        expandableListView.setAdapter(secondLevelAdapter);
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

    private ArrayList<ArrayList> sortDrinks(ArrayList<MixedDrink> mixedDrinksArrays) {
        ArrayList<ArrayList> mixedDrinks =  new ArrayList<>();
        mixedDrinks.add(0, new ArrayList<>());
        mixedDrinks.get(0).add(new ArrayList<>());
        mixedDrinks.add(1, new ArrayList<>());
        mixedDrinks.get(1).add(new ArrayList<>());

        for (int i = 0; i < liquorsInInventoryAsList.size(); i++) {
            String tmpStr = liquorsInInventoryAsList.remove(i);
            liquorsInInventoryAsList.add(i, tmpStr.toLowerCase());
        }

        for (MixedDrink drink : mixedDrinksArrays) {
            boolean canBeMade = true;
            String[] categoriesOfIngredients = drink.getCategoriesOfIngredients().split(",");
            for (String categoryOfIngredient : categoriesOfIngredients) {
                if (!liquorsInInventoryAsList.contains(categoryOfIngredient.toLowerCase()) && !subcategoriesOfLiquorsInInventoryAsList.contains(categoryOfIngredient.toLowerCase())) {
                    canBeMade = false;
                    break;
                }
            }

            if (canBeMade) {
                ((ArrayList) mixedDrinks.get(0).get(0)).add(drink);
            } else {
                ((ArrayList) mixedDrinks.get(1).get(0)).add(drink);
            }
        }

        return mixedDrinks;
    }

    private void setUpRecycler() {
        RecyclerView recyclerView = findViewById(R.id.filterRecyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false );
        recyclerView.setLayoutManager(mLayoutManager);

        ArrayList<String> filters = new ArrayList<>();
        filters.add("Vodka");
        filters.add("Whiskey");
        filters.add("Rum");
        filters.add("Tequila");
        filters.add("Liqueur");
        ArrayList<Integer> rowTypes = new ArrayList<>();
        for (int j = 0; j < filters.size(); j++) {
            rowTypes.add(0);
        }

        BaseRecycleViewAdapter adapter = new BaseRecycleViewAdapter(filters, rowTypes, this);
        recyclerView.setAdapter(adapter);
    }
}