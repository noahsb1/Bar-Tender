package Utilities.Adapters;

import Fragments.Inventory;
import Objects.MixedDrink;
import Objects.RowType;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bartender.R;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

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
        } else if (rowTypes.get(position) == 0) {
            return RowType.filterBox;
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
        } else if (viewType == 2) {
            View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_cardview, parent, false);
            return new CardViewHolder(rowItem);
        } else if (viewType == 0) {
            View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_filterbutton, parent, false);
            return new FilterButtonViewHolder(rowItem);
        }else {
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
                LayoutInflater inflater = (LayoutInflater)
                    ((Activity)cxt).getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.drink_popup, null);
                TextView popupName = (TextView) popupView.findViewById(R.id.drinkNamePopup);
                ImageView popupImage = (ImageView) popupView.findViewById(R.id.drinkImagePopUp);
                TextView ingredientsList = (TextView) popupView.findViewById(R.id.ingredientsList);
                TextView recipe = (TextView) popupView.findViewById(R.id.recipe);

                popupName.setText(mixedDrink.getName());
                popupImage.setImageBitmap(bmresized);
                ingredientsList.setText(mixedDrink.getIngredients().replaceAll("~~~", "\n"));
                recipe.setText(mixedDrink.getRecipe().replaceAll("~~~", "\n"));

                int width = (int) (screenWidth*.5);
                int height = (int) (screenHeight*.75);
                boolean focusable = true;
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
            });
        } else if (holder instanceof FilterButtonViewHolder) {
            FilterButtonViewHolder filterButtonViewHolder = (FilterButtonViewHolder) holder;
            filterButtonViewHolder.button.setBackgroundColor(Color.WHITE);
            filterButtonViewHolder.button.setText((String) this.children.get(position));
            filterButtonViewHolder.button.setBackgroundResource(R.drawable.pillbutton_background);

            filterButtonViewHolder.setItemClickListener((v, pos) -> {

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
            this.textView = view.findViewById(R.id.filterButton);
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

    public static class FilterButtonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button button;
        private View view;
        private ItemClickListener itemClickListener;

        public FilterButtonViewHolder(View view) {
            super(view);
            this.button = view.findViewById(R.id.filterButton);
            this.button.setOnClickListener(this);
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
