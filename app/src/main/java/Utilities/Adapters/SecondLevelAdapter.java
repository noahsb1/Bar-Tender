package Utilities.Adapters;

import Screens.Inventory;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.bartender.R;

import java.util.ArrayList;

public class SecondLevelAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<ArrayList<String>> data;
    private ArrayList<String> subCategories;
    private ImageView ivGroupIndicator;

    public SecondLevelAdapter (Context context,
                               ArrayList<ArrayList<String>> data,
                               ArrayList<String> subCategories) {
        this.context = context;
        this.data = data;
        this.subCategories = subCategories;
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
        view = inflater.inflate(R.layout.expandableviewrow_third, null);

        CheckBox checkBox = view.findViewById(R.id.rowThirdCheckBox);

        ArrayList<String> childArray = data.get(i);
        String txt = childArray.get(i1);
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
            if(myCheckBox.isChecked()) {
                Inventory.addToSelectedLiquors(myCheckBox.getText().toString());
            }
            else if(!myCheckBox.isChecked()) {
                Inventory.removeSelectedLiquors(myCheckBox.getText().toString());
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
