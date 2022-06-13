package Utilities.Adapters;

import Objects.RowType;
import Screens.Inventory;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bartender.R;

import java.util.ArrayList;

public class BaseRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<String> children;
    private final ArrayList<Integer> rowTypes;
    public Context cxt;

    public BaseRecycleViewAdapter (ArrayList<String> children,
                                   ArrayList<Integer> rowTypes,
                                   Context cxt) {
        this.children = children;
        this.rowTypes = rowTypes;
        this.cxt = cxt;
    }

    @Override
    public int getItemViewType(int position) {
        if (rowTypes.get(position) == 1) {
            return RowType.normalTextBox;
        } else if(rowTypes.get(position) == 3) {
            return RowType.checkBox;
        } else {
            return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1){
            View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_rowtextbox, parent, false);
            return new NormalTextBoxViewHolder(rowItem);
        } else if (viewType == 3) {
            View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_rowcheckbox, parent, false);
            return new CheckBoxViewHolder(rowItem);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CheckBoxViewHolder) {
            CheckBoxViewHolder checkBoxHolder = (CheckBoxViewHolder) holder;
            checkBoxHolder.checkBox.setText(this.children.get(position));
            if (Inventory.getSelectedLiquors().contains(checkBoxHolder.checkBox.getText().toString())) {
                checkBoxHolder.checkBox.setSelected(true);
                checkBoxHolder.checkBox.setChecked(true);
            } else {
                checkBoxHolder.checkBox.setSelected(false);
                checkBoxHolder.checkBox.setChecked(false);
            }
            checkBoxHolder.setItemClickListener((v, pos) -> {
                CheckBox myCheckBox = (CheckBox) v;

                myCheckBox.setOnCheckedChangeListener(null);
                if(myCheckBox.isChecked()) {
                    Inventory.addToSelectedLiquors(myCheckBox.getText().toString());
                }
                else if(!myCheckBox.isChecked()) {
                    Inventory.removeSelectedLiquors(myCheckBox.getText().toString());
                }
            });
        } else if (holder instanceof NormalTextBoxViewHolder) {
            NormalTextBoxViewHolder normalTextBoxHolder = (NormalTextBoxViewHolder) holder;
            normalTextBoxHolder.textView.setText(this.children.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return this.children.size();
    }

    public static class NormalTextBoxViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public NormalTextBoxViewHolder(View view) {
            super(view);
            this.textView = view.findViewById(R.id.textBox);
        }
    }

    public static class CheckBoxViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CheckBox checkBox;
        private ItemClickListener itemClickListener;

        public CheckBoxViewHolder(View view) {
            super(view);
            this.checkBox = view.findViewById(R.id.checkBox);
            this.checkBox.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener ic) {
            this.itemClickListener = ic;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v, getLayoutPosition());
        }

        public interface ItemClickListener {
            void onItemClick(View v, int pos);
        }
    }
}
