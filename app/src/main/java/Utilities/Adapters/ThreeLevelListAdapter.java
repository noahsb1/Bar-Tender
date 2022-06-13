package Utilities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.example.bartender.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ThreeLevelListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<String> categories;
    private ArrayList<ArrayList<String>> subCategories;
    private ArrayList<HashMap<String, ArrayList<String>>> data;

    public ThreeLevelListAdapter (Context context,
                                  ArrayList<String> categories,
                                  ArrayList<ArrayList<String>> subCategories,
                                  ArrayList<HashMap<String, ArrayList<String>>> data) {
        this.context = context;
        this.categories = categories;
        this.subCategories = subCategories;
        this.data = data;
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

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final SecondLevelExpandableListView secondLevelExpandableListView = new SecondLevelExpandableListView(context);

        ArrayList<String> subCategoryList = subCategories.get(i);

        ArrayList<ArrayList<String>> childData = new ArrayList<>();
        HashMap<String, ArrayList<String>> secondLevelData = data.get(i);

        for(String str : secondLevelData.keySet()) {
            childData.add(secondLevelData.get(str));
        }

        secondLevelExpandableListView.setAdapter(new SecondLevelAdapter(context, childData, subCategoryList));
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
