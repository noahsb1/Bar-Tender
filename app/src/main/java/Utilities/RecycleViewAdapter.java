package Utilities;

import Objects.RowType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.recyclerview.widget.*;
import com.example.bartender.R;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> data;
    private List<String> selectedLiquors;
    private List<Integer> rowTypes;

    public RecycleViewAdapter (List<String> data, List<String> currentInventory, List<Integer> rowTypes){
        this.data = data;
        this.selectedLiquors = currentInventory;
        this.rowTypes = rowTypes;
    }

    @Override
    public int getItemViewType(int position) {
        if (rowTypes.get(position) == 0) {
            return RowType.categoryHeader;
        } else if (rowTypes.get(position) == 1) {
            return RowType.checkBoxInCategory;
        } else if (rowTypes.get(position) == 2) {
            return RowType.normalTextBox;
        } else {
            return -1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 0) {
            View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_rowcategoryheader, parent, false);
            return new CategoryHeaderViewHolder(rowItem);
        } else if (viewType == 1){
            View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_rowcheckbox, parent, false);
            return new CheckBoxInCategoryViewHolder(rowItem);
        } else if (viewType == 2) {
            View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_rowtextbox, parent, false);
            return new NormalTextBoxViewHolder(rowItem);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CheckBoxInCategoryViewHolder) {
            CheckBoxInCategoryViewHolder checkBoxHolder = (CheckBoxInCategoryViewHolder) holder;
            checkBoxHolder.checkBox.setText(this.data.get(position));
            if (selectedLiquors.contains(checkBoxHolder.checkBox.getText().toString())) {
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
                     selectedLiquors.add(myCheckBox.getText().toString());
                }
                else if(!myCheckBox.isChecked()) {
                    selectedLiquors.remove(myCheckBox.getText().toString());
                }
            });
        } else if (holder instanceof CategoryHeaderViewHolder) {
            CategoryHeaderViewHolder categoryHeaderHolder = (CategoryHeaderViewHolder) holder;
            categoryHeaderHolder.textView.setText(this.data.get(position));
        } else if (holder instanceof NormalTextBoxViewHolder) {
            NormalTextBoxViewHolder normalTextBoxHolder = (NormalTextBoxViewHolder) holder;
            normalTextBoxHolder.textView.setText(this.data.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public List<String> getSelectedLiquors() {
        return this.selectedLiquors;
    }

    public static class CategoryHeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public CategoryHeaderViewHolder(View view) {
            super(view);
            this.textView = view.findViewById(R.id.textBoxCategoryHeader);
        }
    }

    public static class CheckBoxInCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CheckBox checkBox;
        private ItemClickListener itemClickListener;

        public CheckBoxInCategoryViewHolder(View view) {
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

    public static class NormalTextBoxViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public NormalTextBoxViewHolder(View view) {
            super(view);
            this.textView = view.findViewById(R.id.textBox);
        }
    }

}
