package com.example.locsaleapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locsaleapplication.R;
import com.example.locsaleapplication.presentation.category.CategoryModel;
import com.example.locsaleapplication.utils.OnItemClick;

import java.util.List;

@SuppressWarnings("All")
public class SearchSubCategoryAdapter extends RecyclerView.Adapter<SearchSubCategoryAdapter.ViewHolder> {

    private Context mContext;
    private List<CategoryModel.SubCategoryModel> listCategories;
    private OnItemClick<CategoryModel.SubCategoryModel> mListener;

    public SearchSubCategoryAdapter(Context mContext, List<CategoryModel.SubCategoryModel> listCategories, OnItemClick<CategoryModel.SubCategoryModel> mListener) {
        this.mContext = mContext;
        this.listCategories = listCategories;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_sub_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        CategoryModel.SubCategoryModel model = listCategories.get(position);

        holder.tvTitle.setText(model.getStSubCategoryName());

        holder.icCategoryDp.setImageResource(model.getDrawableIds());

        holder.lvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(model, position);
                }
            }
        });
    }

    //public int mSelectedPos = -1;

    @Override
    public int getItemCount() {
        return listCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        LinearLayout lvMain;
        AppCompatImageView icCategoryDp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lvMain = itemView.findViewById(R.id.linearItemSubCategory);

            tvTitle = itemView.findViewById(R.id.tvItemSubCategoryTitle);

            icCategoryDp = itemView.findViewById(R.id.icCategoryDp);
        }
    }
}
