package Fragments;

import Objects.MixedDrink;
import Utilities.Adapters.BaseRecycleViewAdapter;
import Utilities.Adapters.SecondLevelAdapter;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bartender.R;

import java.util.ArrayList;
import java.util.Collections;


public class DrinkMenu extends Fragment {
    private static ArrayList<MixedDrink> mixedDrinks;
    private static ArrayList<String> liquorsInInventoryAsList;
    private static ArrayList<String> subcategoriesOfLiquorsInInventoryAsList;
    private static ExpandableListView expandableListView;
    private static SecondLevelAdapter secondLevelAdapter;
    private static ArrayList<String> filterButtonArray = new ArrayList<>();

    public DrinkMenu() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drink_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        SearchView searchView = view.findViewById(R.id.searchBar);

        mixedDrinks = Inventory.getMixedDrinks();
        liquorsInInventoryAsList = Inventory.getLiquorsInInventoryAsList();
        subcategoriesOfLiquorsInInventoryAsList = Inventory.getSubcategoriesOfLiquorsInInventoryAsList();

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
    }

    private static void filter(String text) {
        ArrayList<MixedDrink> filteredlist = new ArrayList<>();

        if (!filterButtonArray.isEmpty()) {
            for (MixedDrink item : mixedDrinks) {
                for (String str : filterButtonArray) {
                    if (item.getCategoriesOfIngredients().contains(str.toLowerCase())) {
                        if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                            filteredlist.add(item);
                        }
                    }
                }
            }
        } else {
            filteredlist.addAll(mixedDrinks);
        }
        secondLevelAdapter.filterList(sortDrinks(filteredlist));
    }

    private void setUpAdapter() {
        expandableListView = getView().findViewById(R.id.expandable_listview3);

        ArrayList<String> categories = new ArrayList<>();
        categories.add("Drinks you can make");
        categories.add("Drinks you can't make");

        ArrayList<ArrayList> mixedDrinks = sortDrinks(this.mixedDrinks);


        secondLevelAdapter = new SecondLevelAdapter(getContext(), mixedDrinks, categories, "", 3);
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

    private static ArrayList<ArrayList> sortDrinks(ArrayList<MixedDrink> mixedDrinksArrays) {
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
        RecyclerView recyclerView = getView().findViewById(R.id.filterRecyclerView);
        recyclerView.requestFocus();
        LinearLayoutManager
            mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false );
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

        BaseRecycleViewAdapter adapter = new BaseRecycleViewAdapter(filters, rowTypes, getContext());
        recyclerView.setAdapter(adapter);
    }

    public static ArrayList<String> getFilterButtonArray() {
        return filterButtonArray;
    }

    public static void addToFilterButtonArray(String str) {
        if (!filterButtonArray.contains(str)) {
            filterButtonArray.add(str);
        }
        filter("");
    }

    public static void removeFromFilterButtonArray(String str) {
        filterButtonArray.remove(str);
        filter("");
    }
}