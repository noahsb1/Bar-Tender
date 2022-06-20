package Utilities.Adapters;

import Screens.Inventory;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.bartender.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SecondLevelAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<ArrayList<String>> data;
    private ArrayList<String> subCategories;
    private String grandParent;
    private int select;

    public SecondLevelAdapter (Context context,
                               ArrayList<ArrayList<String>> data,
                               ArrayList<String> subCategories,
                               String grandParent,
                               int select) {
        this.context = context;
        this.data = data;
        this.subCategories = subCategories;
        this.grandParent = grandParent;
        this.select = select;
    }

    @Override
    public int getGroupCount() {
        return subCategories.size();
    }

    @Override
    public int getChildrenCount(int i) {
        ArrayList<String> children = data.get(i);
        return children.size();
    }

    @Override
    public Object getGroup(int i) {
        return subCategories.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        ArrayList<String> childData;
        childData = data.get(i);
        return childData.get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.expandableviewrow_second, null);
        TextView textView = view.findViewById(R.id.rowSecondText);
        textView.setText(getGroup(i).toString());

        if(b) {
            view.findViewById(R.id.ivGroupIndicator2).setBackground(
                context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp));
        } else {
            view.findViewById(R.id.ivGroupIndicator2).setBackground(
                context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp));
        }

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ArrayList<String> childArray = data.get(i);
        Collections.sort(childArray);
        String txt = childArray.get(i1);

        if (select == 1) {
            view = inflater.inflate(R.layout.expandableviewrow_thirdcheck, null);
            CheckBox checkBox = view.findViewById(R.id.rowThirdCheckBox);

            checkBox.setText(txt);

            if (Inventory.getSelectedLiquors().contains(checkBox.getText().toString())) {
                checkBox.setSelected(true);
                checkBox.setChecked(true);
            } else {
                checkBox.setSelected(false);
                checkBox.setChecked(false);
            }

            checkBox.setOnCheckedChangeListener((compoundButton, b1) -> {
                CheckBox myCheckBox = (CheckBox) compoundButton;
                myCheckBox.setOnCheckedChangeListener(null);
                if (myCheckBox.isChecked()) {
                    Inventory.addToSelectedLiquors(myCheckBox.getText().toString());

                    HashMap<String, HashMap<String, ArrayList<String>>> liquorsInInventory =
                        Inventory.getLiquorsInInventory();
                    if (liquorsInInventory.containsKey(grandParent)) {
                        if (liquorsInInventory.get(grandParent).containsKey((String) getGroup(i))) {
                            liquorsInInventory.get(grandParent).get((String) getGroup(i))
                                .add(myCheckBox.getText().toString());
                        } else {
                            ArrayList<String> temp = new ArrayList<>();
                            temp.add(myCheckBox.getText().toString());
                            liquorsInInventory.get(grandParent).put((String) getGroup(i), temp);
                        }
                    } else {
                        ArrayList<String> temp = new ArrayList<>();
                        temp.add(myCheckBox.getText().toString());

                        HashMap<String, ArrayList<String>> tempHash = new HashMap<>();
                        tempHash.put((String) getGroup(i), temp);

                        liquorsInInventory.put(grandParent, tempHash);
                    }
                } else if (!myCheckBox.isChecked()) {
                    Inventory.removeSelectedLiquors(myCheckBox.getText().toString());

                    HashMap<String, HashMap<String, ArrayList<String>>> liquorsInInventory =
                        Inventory.getLiquorsInInventory();
                    liquorsInInventory.get(grandParent).get((String) getGroup(i)).remove(myCheckBox.getText().toString());
                    if (liquorsInInventory.get(grandParent).get((String) getGroup(i)).isEmpty()) {
                        liquorsInInventory.get(grandParent).remove((String) getGroup(i));
                    }
                    if (liquorsInInventory.get(grandParent).isEmpty()) {
                        liquorsInInventory.remove(grandParent);
                    }
                }
            });
        } else if (select == 2) {
            view = inflater.inflate(R.layout.expandableviewrow_thirdtext, null);
            TextView textView = view.findViewById(R.id.rowThirdTextBox);
            textView.setText(txt);
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
