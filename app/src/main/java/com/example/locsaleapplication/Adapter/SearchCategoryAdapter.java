package com.example.locsaleapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locsaleapplication.R;
import com.example.locsaleapplication.presentation.category.CategoryModel;
import com.example.locsaleapplication.utils.OnItemClick;

import java.util.List;

@SuppressWarnings("All")
public class SearchCategoryAdapter extends RecyclerView.Adapter<SearchCategoryAdapter.ViewHolder> {

    private Context mContext;
    private List<CategoryModel> listCategories;
    private OnItemClickCategorySub mListener;

    public SearchCategoryAdapter(Context mContext, List<CategoryModel> listCategories, OnItemClickCategorySub mListener) {
        this.mContext = mContext;
        this.listCategories = listCategories;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        CategoryModel model = listCategories.get(position);

        holder.tvTitle.setText(model.getStCategoryName());

        if (model.getSubCategoryList().size() > 0) {
            SearchSubCategoryAdapter adapterSubCategory = new SearchSubCategoryAdapter(mContext, model.getSubCategoryList(), new OnItemClick<CategoryModel.SubCategoryModel>() {
                @Override
                public void onItemClick(CategoryModel.SubCategoryModel data, int position) {
                    if (mListener != null) {
                        mListener.onItemClickCategorySub(model, data);
                    }
                }
            });
            holder.recyclerViewSubCategory.setLayoutManager(new GridLayoutManager(mContext, 2));
            holder.recyclerViewSubCategory.setAdapter(adapterSubCategory);
            holder.recyclerViewSubCategory.setVisibility(View.VISIBLE);
        } else {
            holder.recyclerViewSubCategory.setVisibility(View.GONE);
        }

        holder.lvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClickCategorySub(model, null);
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
        RecyclerView recyclerViewSubCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lvMain = itemView.findViewById(R.id.linearItemCategory);

            tvTitle = itemView.findViewById(R.id.tvItemCategoryTitle);
            recyclerViewSubCategory = itemView.findViewById(R.id.recyclerViewItemCategory);
        }
    }

    public interface OnItemClickCategorySub{
        void onItemClickCategorySub(CategoryModel model, CategoryModel.SubCategoryModel subCategoryModel);
    }
}
