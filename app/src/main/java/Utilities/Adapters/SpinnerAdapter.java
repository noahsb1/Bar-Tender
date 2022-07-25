package Utilities.Adapters;

import Fragments.DrinkMenu;
import Utilities.StateVO;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.example.bartender.R;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<StateVO> {
    private Context mContext;
    private ArrayList<StateVO> listState;
    private SpinnerAdapter myAdapter;
    private boolean isFromView = false;

    public SpinnerAdapter(Context context, int resource, List<StateVO> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = (ArrayList<StateVO>) objects;
        this.myAdapter = this;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spinner_item, null);
            holder = new ViewHolder();
            holder.mCheckBox = convertView.findViewById(R.id.spinnerCheckbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mCheckBox.setText(listState.get(position).getTitle());


        isFromView = true;
        holder.mCheckBox.setChecked(listState.get(position).isSelected());
        isFromView = false;
        if ((position == 0)) {
            holder.mCheckBox.setButtonDrawable(null);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isFromView) {
                listState.get(position).setSelected(isChecked);
            }
            if (listState.get(position).isSelected()) {
                DrinkMenu.addToFilterButtonArray(listState.get(position).getTitle());
            } else {
                DrinkMenu.removeFromFilterButtonArray(listState.get(position).getTitle());
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private CheckBox mCheckBox;
    }
}
