package Utilities.RecycleViewAdapters;

import Objects.RowType;
import Utilities.SetToArrayList;
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
import java.util.Set;

public class ParentRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public HashMap<String, HashMap<String, ArrayList<String>>> children;
    public ArrayList<String> hashMapKeys;
    public ArrayList<Integer> rowTypes;
    public Context cxt;
    public  int mExpandedPosition = -1;

    public ParentRecycleViewAdapter (HashMap<String, HashMap<String, ArrayList<String>>> children,
                                     ArrayList<Integer> rowTypes,
                                     ArrayList<String> hashMapKeys,
                                     Context cxt) {
        this.children = children;
        this.cxt = cxt;
        this.rowTypes = rowTypes;
        this.hashMapKeys = hashMapKeys;
    }

    @Override
    public int getItemViewType(int position) {
        if (rowTypes.get(position) == 0) {
            return RowType.parentView;
        } else {
            return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row, parent, false);
            return new ParentViewHolder(rowItem);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ParentViewHolder) {
            ParentViewHolder parentViewHolder = (ParentViewHolder) holder;
            String category = hashMapKeys.get(position);

            HashMap<String, ArrayList<String>> subcategories = children.get(category);
            ArrayList<String> subHashMapKey = SetToArrayList.setToArrayList(subcategories.keySet());
            ArrayList<Integer> rowTypes = new ArrayList<>();
            for (int i = 0; i < subcategories.size(); i++) {
                rowTypes.add(2);
            }

            parentViewHolder.categoryHeader.setText(category);
            RecyclerView recyclerView = parentViewHolder.subCategoryHolder;
            LinearLayoutManager layoutManager = new LinearLayoutManager(cxt);
            recyclerView.setLayoutManager(layoutManager);
            ChildRecycleViewAdapter adapter = new ChildRecycleViewAdapter(subcategories, subHashMapKey, rowTypes, cxt);
            recyclerView.setAdapter(adapter);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);

            RecyclerView.OnItemTouchListener mScrollTouchListener = new RecyclerView.OnItemTouchListener() {
                @Override
                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                    int action = e.getAction();
                    switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    }
                    return false;
                }

                @Override
                public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                }
            };

            recyclerView.addOnItemTouchListener(mScrollTouchListener);

//            final boolean isExpanded = position==mExpandedPosition;
//            parentViewHolder.subCategoryHolder.setVisibility(isExpanded?View.VISIBLE:View.GONE);
//            parentViewHolder.itemView.setActivated(isExpanded);
//            parentViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mExpandedPosition = isExpanded ? -1:parentViewHolder.getAdapterPosition();
//                    TransitionManager.beginDelayedTransition(recyclerView);
//                    notifyDataSetChanged();
//                }
//            });
        }
    }

    @Override
    public int getItemCount() {
        return this.children.size();
    }

    public static class ParentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView categoryHeader;
        private RecyclerView subCategoryHolder;
        private ItemClickListener itemClickListener;

        public ParentViewHolder(View view) {
            super(view);
            this.categoryHeader = view.findViewById(R.id.textBoxCategoryHeader2);
            this.subCategoryHolder = view.findViewById(R.id.nestedRecyclerView);
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
