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
    private List<String> currentInventory;
    private int selecter;

    public RecycleViewAdapter (List<String> data, List<String> currentInventory, int selecter){
        this.data = data;
        this.currentInventory = currentInventory;
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

            if (currentInventory.contains(this.data.get(position))) {
                holder.checkBox.setChecked(true);
            }
        } else {
            holder.textView.setText(this.data.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private TextView textView;

        public ViewHolder(View view, int selector) {
            super(view);
            if (selector == 0) {
                this.checkBox = view.findViewById(R.id.checkBox);
                this.textView = null;
            } else {
                this.checkBox = null;
                this.textView = view.findViewById(R.id.textBox);
            }
        }
    }
}
