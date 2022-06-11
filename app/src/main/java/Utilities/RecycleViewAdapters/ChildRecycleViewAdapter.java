package Utilities.RecycleViewAdapters;

import Objects.RowType;
import android.content.Context;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bartender.R;
import java.util.ArrayList;
import java.util.HashMap;

public class ChildRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public HashMap<String, ArrayList<String>> children;
    public ArrayList<String> hashMapKeys;
    public ArrayList<Integer> rowTypes;
    public Context cxt;
    public  int mExpandedPosition = -1;

    public ChildRecycleViewAdapter (HashMap<String, ArrayList<String>> children,
                                    ArrayList<String> hashMapKeys,
                                    ArrayList<Integer> rowTypes,
                                    Context cxt) {
        this.children = children;
        this.hashMapKeys = hashMapKeys;
        this.rowTypes = rowTypes;
        this.cxt = cxt;
    }

    @Override
    public int getItemViewType(int position) {
        if (rowTypes.get(position) == 2) {
            return RowType.childView;
        } else {
            return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 2) {
            View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row, parent, false);
            return new ChildViewHolder(rowItem);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChildViewHolder) {
            ChildViewHolder childViewHolder = (ChildViewHolder) holder;
            String subCategory = hashMapKeys.get(position);
            ArrayList<String> liquorsInCategoryArray = children.get(subCategory);
            ArrayList<Integer> rowTypes = new ArrayList<>();
            for (int i = 0; i < liquorsInCategoryArray.size(); i++) {
                rowTypes.add(3);
            }

            childViewHolder.subCategoryHeader.setText(subCategory);
            RecyclerView recyclerView = childViewHolder.liquorsInCategory;
            LinearLayoutManager layoutManager = new LinearLayoutManager(cxt);
            recyclerView.setLayoutManager(layoutManager);
            BaseRecycleViewAdapter adapter = new BaseRecycleViewAdapter(liquorsInCategoryArray, rowTypes, cxt);
            recyclerView.setAdapter(adapter);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);

            final boolean isExpanded = position==mExpandedPosition;
            childViewHolder.liquorsInCategory.setVisibility(isExpanded?View.VISIBLE:View.GONE);
            childViewHolder.itemView.setActivated(isExpanded);
            childViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExpandedPosition = isExpanded ? -1:childViewHolder.getAdapterPosition();
                    TransitionManager.beginDelayedTransition(recyclerView);
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.children.size();
    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView subCategoryHeader;
        private RecyclerView liquorsInCategory;
        private ParentRecycleViewAdapter.ParentViewHolder.ItemClickListener itemClickListener;

        public ChildViewHolder(View view) {
            super(view);
            this.subCategoryHeader = view.findViewById(R.id.textBoxCategoryHeader2);
            this.liquorsInCategory = view.findViewById(R.id.nestedRecyclerView);
        }

        public void setItemClickListener(
            ParentRecycleViewAdapter.ParentViewHolder.ItemClickListener ic) {
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
