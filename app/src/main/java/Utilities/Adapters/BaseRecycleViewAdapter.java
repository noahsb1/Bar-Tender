package Utilities.Adapters;

import Objects.MixedDrink;
import Objects.RowType;
import Screens.Inventory;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bartender.R;

import java.util.ArrayList;

public class BaseRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList children;
    private ArrayList<Integer> rowTypes;
    public Context cxt;

    public BaseRecycleViewAdapter (ArrayList children,
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
        } else if (rowTypes.get(position) == 2) {
            return RowType.cardView;
        }else {
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
        } else if (viewType == 2) {
            View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_cardview, parent, false);
            return new CardViewHolder(rowItem);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CheckBoxViewHolder) {
            CheckBoxViewHolder checkBoxHolder = (CheckBoxViewHolder) holder;
            checkBoxHolder.checkBox.setText((String) this.children.get(position));
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
            normalTextBoxHolder.textView.setText((String) this.children.get(position));
        } else if (holder instanceof CardViewHolder) {
            CardViewHolder cardViewHolder = (CardViewHolder) holder;
            MixedDrink mixedDrink = ((ArrayList<MixedDrink>) children).get(position);
            Bitmap bm = BitmapFactory.decodeByteArray(mixedDrink.getImage(), 0, (mixedDrink.getImage().length));

            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity)cxt).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int screenHeight = displaymetrics.heightPixels;
            int screenWidth = displaymetrics.widthPixels;

            Bitmap bmresized = Bitmap.createScaledBitmap(bm, (int) (screenWidth*.5), (int) (screenHeight*.25), true);
            cardViewHolder.textView.setText(mixedDrink.getName());
            cardViewHolder.imageView.setImageBitmap(bmresized);

            cardViewHolder.setItemClickListener((v, pos) -> {

            });
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

    public static class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView textView;
        public CardView cardView;
        private CardViewHolder.ItemClickListener itemClickListener;


        public CardViewHolder(View view) {
            super(view);
            this.imageView = view.findViewById(R.id.drinkImg);
            this.textView = view.findViewById(R.id.drinkName);
            this.cardView = view.findViewById(R.id.card_view);
            this.cardView.setOnClickListener(this);
        }

        public void setItemClickListener(CardViewHolder.ItemClickListener ic) {
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
