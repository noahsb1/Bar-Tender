package Fragments;

import Objects.MixedDrink;
import Screens.Combined;
import Screens.LiquorSelect;
import Utilities.Adapters.ThreeLevelListAdapter;
import Utilities.ErrorDisplay;
import Utilities.GitAccess;
import Utilities.InternalMemory;
import Utilities.SetToArrayList;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SearchView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.bartender.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Inventory extends Fragment {
    private static final ArrayList<MixedDrink> mixedDrinks = new ArrayList<>();
    private final HashMap<String, HashMap<String, ArrayList<String>>> liquorsOnline = new HashMap<>();
    private final ArrayList<String> liquorOnlineAsList = new ArrayList<>();
    private static final HashMap<String, HashMap<String, ArrayList<String>>> liquorsInInventory = new HashMap<>();
    private static final ArrayList<String> liquorsInInventoryAsList = new ArrayList<>();
    private static final ArrayList<String> subcategoriesOfLiquorsInInventoryAsList = new ArrayList<>();
    private static final ArrayList<String> selectedLiquors = new ArrayList<>();

    private final ArrayList<HashMap<String, ArrayList<String>>> data = new ArrayList<>();
    private ArrayList<String> categories = new ArrayList<>();
    private final ArrayList<ArrayList<String>> secondLevel = new ArrayList<>();
    private ExpandableListView expandableListView;
    private ThreeLevelListAdapter threeLevelListAdapter;

    public Inventory() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mixedDrinks.clear();
        liquorsInInventory.clear();
        liquorsInInventoryAsList.clear();
        subcategoriesOfLiquorsInInventoryAsList.clear();
        liquorsOnline.clear();
        liquorOnlineAsList.clear();

        // Copy inventory from memory and turn into hashmap
        loadFromMemory();

        // Add liquors in inventory to selected liquors
        if (liquorsInInventoryAsList != null) {
            selectedLiquors.clear();
            selectedLiquors.addAll(liquorsInInventoryAsList);
        }

        // Define buttons
        Button addToInventory = view.findViewById(R.id.addToInventory);
        SearchView searchView = view.findViewById(R.id.inventorySearchBar);

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

        // Execute AsyncTask to access internet
        gitCall gitCall = new gitCall();
        gitCall.execute();

        // Set up inventory adapter
        categories = SetToArrayList.setToArrayList(liquorsInInventory.keySet());
        setUpAdapter();

        // Define action on add to inventory button press
        addToInventory.setOnClickListener(view2 -> {
            Intent intent = new Intent(getActivity(), LiquorSelect.class);
            gitCall.cancel(true);
            intent.putExtra("liquors", liquorsOnline);
            intent.putExtra("list", liquorOnlineAsList);
            startActivity(intent);
            getActivity().finish();
        });
    }

    /**
     * AsyncTask to look at github and pull drink menu
     */
    private class gitCall extends AsyncTask<Void, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            ArrayList<String> rtn = new ArrayList<>();
            String temp1 = "";
            String temp2 = "";
            try {
                temp1 = GitAccess.access("https://raw.githubusercontent.com/noahsb1/Bar-Tender/main/app/Text%20Files/drinks.txt");
                temp2 = GitAccess.access("https://raw.githubusercontent.com/noahsb1/Bar-Tender/main/app/Text%20Files/liquorList.txt");
                InternalMemory.addToInventory(getActivity(), temp1, "drinks.txt");
                InternalMemory.addToInventory(getActivity(), temp2, "liquorList.txt");
                rtn.add(temp1);
                rtn.add(temp2);

                return rtn;
            } catch (IOException e) {
                try {
                    temp1 = InternalMemory.getStoredInventory(getActivity(), "drinks.txt");
                    temp2 = InternalMemory.getStoredInventory(getActivity(), "liquorList.txt");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                rtn.add(temp1);
                rtn.add(temp2);
            }
            return rtn;
        }

        @SuppressLint("WrongThread")
        @Override
        protected void onPostExecute(ArrayList<String> string) {
            String[] drinks = string.get(0).split("!");
            String[] liquorArray = string.get(1).split("!");

            // Turn drink strings into mixed drink objects
            for (String drink : drinks) {
                String[] temp2 = drink.split(";");
                if (temp2.length == 5) {
                    mixedDrinks.add(new MixedDrink(temp2[0], temp2[1], temp2[2], temp2[3],
                                                   Base64.decode(temp2[4], Base64.DEFAULT)));
                } else {
                    Resources res = getResources();
                    Drawable d = res.getDrawable(R.drawable.ic_question_mark_desktop_wallpaper_grey_computer_icon__2_);
                    Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] bitmapdata = stream.toByteArray();

                    mixedDrinks.add(new MixedDrink(temp2[0], temp2[1], temp2[2], temp2[3], bitmapdata));
                }
            }

            // Turn liquor string into hashmap of categoreis and subcategories
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
        if (!selectedLiquors.contains(str)) {
            selectedLiquors.add(str);
        }
    }

    public static void removeSelectedLiquors(String str) {
        selectedLiquors.remove(str);
    }

    public static HashMap<String, HashMap<String, ArrayList<String>>> getLiquorsInInventory() {
        return liquorsInInventory;
    }

    private void filter(String text) {
        ArrayList<ArrayList<String>> filteredlist = new ArrayList<>();

        for (int i = 0; i < secondLevel.size(); i++) {
            filteredlist.add(i, new ArrayList<>());
            for (int j = 0; j < secondLevel.get(i).size(); j++) {
                if (secondLevel.get(i).get(j).toLowerCase().contains(text.toLowerCase())) {
                    filteredlist.get(i).add(secondLevel.get(i).get(j));
                }
            }
        }
        threeLevelListAdapter.filterList(filteredlist);
    }

    private void setUpAdapter() {
        for (String str : categories) {
            secondLevel.add(SetToArrayList.setToArrayList(liquorsInInventory.get(str).keySet()));
            data.add(liquorsInInventory.get(str));
        }

        expandableListView = getView().findViewById(R.id.expandable_listview1);
        threeLevelListAdapter = new ThreeLevelListAdapter(getContext(), categories, secondLevel, data, 2);
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

    private void loadFromMemory() {
        try {
            String temp = InternalMemory.getStoredInventory(getActivity(), "LiquorsInInventory.txt");
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
                        subcategoriesOfLiquorsInInventoryAsList.add(subCategoryName.toLowerCase());
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
    }

    public static ArrayList<MixedDrink> getMixedDrinks() {
        return mixedDrinks;
    }

    public static ArrayList<String> getLiquorsInInventoryAsList() {
        return liquorsInInventoryAsList;
    }

    public static ArrayList<String> getSubcategoriesOfLiquorsInInventoryAsList() {
        return subcategoriesOfLiquorsInInventoryAsList;
    }
}