package Utilities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.recyclerview.widget.*;
import com.example.bartender.R;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    private List<String> data;
    private List<String> selectedLiquors;
    private int selecter;

    public RecycleViewAdapter (List<String> data, List<String> currentInventory, int selecter){
        this.data = data;
        this.selectedLiquors = currentInventory;
        this.selecter = selecter;
    }

    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem;
        if (selecter == 0) {
            rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_rowcheckbox, parent, false);
        } else {
            rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_rowtextbox, parent, false);
        }
        return new ViewHolder(rowItem, selecter);
    }

    @Override
    public void onBindViewHolder(RecycleViewAdapter.ViewHolder holder, int position) {
        if (selecter == 0) {
            holder.checkBox.setText(this.data.get(position));
            if (selectedLiquors.contains(holder.checkBox.getText().toString())) {
                holder.checkBox.setSelected(true);
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setSelected(false);
                holder.checkBox.setChecked(false);
            }
            holder.setItemClickListener((v, pos) -> {
                CheckBox myCheckBox = (CheckBox) v;

                myCheckBox.setOnCheckedChangeListener(null);
                if(myCheckBox.isChecked()) {
                     selectedLiquors.add(myCheckBox.getText().toString());
                }
                else if(!myCheckBox.isChecked()) {
                    selectedLiquors.remove(myCheckBox.getText().toString());
                }
            });
        } else {
            holder.textView.setText(this.data.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public List<String> getSelectedLiquors() {
        return this.selectedLiquors;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CheckBox checkBox;
        private TextView textView;
        private ItemClickListener itemClickListener;

        public ViewHolder(View view, int selector) {
            super(view);
            if (selector == 0) {
                this.checkBox = view.findViewById(R.id.checkBox);
                this.textView = null;
                this.checkBox.setOnClickListener(this);
            } else {
                this.checkBox = null;
                this.textView = view.findViewById(R.id.textBox);
            }
        }
        
        public void setItemClickListener(ItemClickListener ic)
        {
            this.itemClickListener = ic;
        }
        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v,getLayoutPosition());
        }
        interface ItemClickListener {

            void onItemClick(View v,int pos);
        }
    }
}
