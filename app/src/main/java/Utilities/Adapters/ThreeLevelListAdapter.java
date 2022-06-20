package Utilities.Adapters;

import Utilities.SetToArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.example.bartender.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class ThreeLevelListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<String> categories;
    private ArrayList<ArrayList<String>> subCategories;
    private ArrayList<HashMap<String, ArrayList<String>>> data;
    private int select;

    public ThreeLevelListAdapter (Context context,
                                  ArrayList<String> categories,
                                  ArrayList<ArrayList<String>> subCategories,
                                  ArrayList<HashMap<String, ArrayList<String>>> data,
                                  int select) {
        this.context = context;
        this.categories = categories;
        this.subCategories = subCategories;
        this.data = data;
        this.select = select;
    }

    @Override
    public int getGroupCount() {
        return categories.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return 1;
    }

    @Override
    public Object getChild(int i, int i1) {
        return i1;
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
        view = inflater.inflate(R.layout.expandableviewrow_first, null);
        TextView text = view.findViewById(R.id.rowParentText);
        text.setText(this.categories.get(i));

        if(b) {
            view.findViewById(R.id.ivGroupIndicator1).setBackground(
                context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_black_24dp));
        } else {
            view.findViewById(R.id.ivGroupIndicator1).setBackground(
                context.getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_black_24dp));
        }

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final SecondLevelExpandableListView secondLevelExpandableListView = new SecondLevelExpandableListView(context);

        ArrayList<String> subCategoryList = subCategories.get(i);
        Collections.sort(subCategoryList);

        ArrayList<ArrayList<String>> childData = new ArrayList<>();
        HashMap<String, ArrayList<String>> secondLevelData = data.get(i);
        ArrayList<String> temp1 = SetToArrayList.setToArrayList(secondLevelData.keySet());
        Collections.sort(temp1);

        for(String str : temp1) {
            childData.add(secondLevelData.get(str));
        }

        secondLevelExpandableListView.setAdapter(new SecondLevelAdapter(context, childData, subCategoryList,
                                                                        categories.get(i), select));
        secondLevelExpandableListView.setGroupIndicator(null);

        secondLevelExpandableListView.setOnGroupExpandListener(
            new ExpandableListView.OnGroupExpandListener() {
                int previousGroup = -1;
                @Override
                public void onGroupExpand(int i) {
                    if (i != previousGroup) {
                        secondLevelExpandableListView.collapseGroup(previousGroup);
                    }
                    previousGroup = i;
                }
            });

        return secondLevelExpandableListView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
