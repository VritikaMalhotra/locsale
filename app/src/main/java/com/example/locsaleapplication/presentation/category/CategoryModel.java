package com.example.locsaleapplication.presentation.category;

import java.io.Serializable;
import java.util.ArrayList;

public class CategoryModel implements Serializable {

    String stCategoryName;
    ArrayList<SubCategoryModel> subCategoryList;
    boolean isSelect = false;

    public CategoryModel(String stCategoryName, ArrayList<SubCategoryModel> subCategoryList, boolean isSelect) {
        this.stCategoryName = stCategoryName;
        this.subCategoryList = subCategoryList;
        this.isSelect = isSelect;
    }

    public String getStCategoryName() {
        return stCategoryName;
    }

    public void setStCategoryName(String stCategoryName) {
        this.stCategoryName = stCategoryName;
    }

    public ArrayList<SubCategoryModel> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(ArrayList<SubCategoryModel> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public static class SubCategoryModel implements Serializable {
        String stSubCategoryName;
        int drawableIds;
        boolean isSelect = false;

        public SubCategoryModel(String stSubCategoryName, int drawableIds, boolean isSelect) {
            this.stSubCategoryName = stSubCategoryName;
            this.drawableIds = drawableIds;
            this.isSelect = isSelect;
        }

        public String getStSubCategoryName() {
            return stSubCategoryName;
        }

        public void setStSubCategoryName(String stSubCategoryName) {
            this.stSubCategoryName = stSubCategoryName;
        }

        public int getDrawableIds() {
            return drawableIds;
        }

        public void setDrawableIds(int drawableIds) {
            this.drawableIds = drawableIds;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }
    }
}
