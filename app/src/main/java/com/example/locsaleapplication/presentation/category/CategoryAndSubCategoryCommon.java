package com.example.locsaleapplication.presentation.category;

import com.example.locsaleapplication.R;

import java.util.ArrayList;

@SuppressWarnings("All")
public class CategoryAndSubCategoryCommon {
    public static ArrayList<CategoryModel> getCategoryModels() {
        ArrayList<CategoryModel>  categoryModels = new ArrayList<>();

        //Bakery Start
        ArrayList<CategoryModel.SubCategoryModel> listBakery = new ArrayList<>();
        categoryModels.add(new CategoryModel("Bakery", listBakery, false));
        //Bakery End

        //Cafe and Restaurant Start
        ArrayList<CategoryModel.SubCategoryModel> listCafeAndRestaurant = new ArrayList<>();
        categoryModels.add(new CategoryModel("Cafe And Restaurant", listCafeAndRestaurant, false));
        //Cafe and Restaurant End

        //Car and Motors Start
        ArrayList<CategoryModel.SubCategoryModel> listCarAndMotors = new ArrayList<>();
        listCarAndMotors.add(new CategoryModel.SubCategoryModel("Battery", R.drawable.ic_cat_hospital, false));
        listCarAndMotors.add(new CategoryModel.SubCategoryModel("Service Center", R.drawable.ic_cat_hospital, false));
        listCarAndMotors.add(new CategoryModel.SubCategoryModel("Showroom", R.drawable.ic_cat_hospital, false));
        listCarAndMotors.add(new CategoryModel.SubCategoryModel("Spare Parts", R.drawable.ic_cat_hospital, false));
        categoryModels.add(new CategoryModel("Car and Motors", listCarAndMotors, false));
        //Car and Motors End

        //Clothing and Accessories Start
        ArrayList<CategoryModel.SubCategoryModel> listClothingAndAccessories = new ArrayList<>();
        listClothingAndAccessories.add(new CategoryModel.SubCategoryModel("Jewellery", R.drawable.ic_cat_hospital, false));
        listClothingAndAccessories.add(new CategoryModel.SubCategoryModel("Kid's Wear", R.drawable.ic_cat_hospital, false));
        listClothingAndAccessories.add(new CategoryModel.SubCategoryModel("Men's Wear", R.drawable.ic_cat_hospital, false));
        listClothingAndAccessories.add(new CategoryModel.SubCategoryModel("Women's Wear", R.drawable.ic_cat_hospital, false));
        listClothingAndAccessories.add(new CategoryModel.SubCategoryModel("Optical Shop", R.drawable.ic_cat_hospital, false));
        listClothingAndAccessories.add(new CategoryModel.SubCategoryModel("Tailor", R.drawable.ic_cat_hospital, false));
        categoryModels.add(new CategoryModel("Clothing and Accessories", listClothingAndAccessories, false));
        //Clothing and Accessories End

        //Decoration Start
        ArrayList<CategoryModel.SubCategoryModel> listDecoration = new ArrayList<>();
        listDecoration.add(new CategoryModel.SubCategoryModel("Birthday Decoration", R.drawable.ic_cat_hospital, false));
        listDecoration.add(new CategoryModel.SubCategoryModel("Wedding Decoration", R.drawable.ic_cat_hospital, false));
        categoryModels.add(new CategoryModel("Decoration", listDecoration, false));
        //Decoration End

        //Dry Fruits Shop Start
        ArrayList<CategoryModel.SubCategoryModel> listDryFruitsShop = new ArrayList<>();
        categoryModels.add(new CategoryModel("Dry Fruits Shop", listDryFruitsShop, false));
        //Dry Fruits Shop End

        //Electronics Shop Start
        ArrayList<CategoryModel.SubCategoryModel> listElectronicsShop = new ArrayList<>();
        listElectronicsShop.add(new CategoryModel.SubCategoryModel("Electronics Shop", R.drawable.ic_cat_hospital, false));
        categoryModels.add(new CategoryModel("Electronics Shop", listElectronicsShop, false));
        //Electronics Shop End

        //Gift Shop Start
        ArrayList<CategoryModel.SubCategoryModel> listGiftShop = new ArrayList<>();
        categoryModels.add(new CategoryModel("Gift Shop", listGiftShop, false));
        //Gift Shop End

        //Groceries Start
        ArrayList<CategoryModel.SubCategoryModel> listGroceries = new ArrayList<>();
        listGroceries.add(new CategoryModel.SubCategoryModel("Kitchen Supply", R.drawable.ic_cat_hospital, false));
        categoryModels.add(new CategoryModel("Groceries", listGroceries, false));
        //Groceries End

        //Hardware Start
        ArrayList<CategoryModel.SubCategoryModel> listHardware = new ArrayList<>();
        listHardware.add(new CategoryModel.SubCategoryModel("Hardware Shop", R.drawable.ic_cat_hospital, false));
        categoryModels.add(new CategoryModel("Hardware", listHardware, false));
        //Hardware End

        //Health And Medical Start
        ArrayList<CategoryModel.SubCategoryModel> listHealthAndMedical = new ArrayList<>();
        listHealthAndMedical.add(new CategoryModel.SubCategoryModel("Dietitian", R.drawable.ic_cat_hospital, false));
        listHealthAndMedical.add(new CategoryModel.SubCategoryModel("Hospitals", R.drawable.ic_cat_hospital, false));
        listHealthAndMedical.add(new CategoryModel.SubCategoryModel("Medical Stores", R.drawable.ic_cat_hospital, false));
        categoryModels.add(new CategoryModel("Health And Medical", listHealthAndMedical, false));
        //Health And Medical End

        //Home Services Start
        ArrayList<CategoryModel.SubCategoryModel> listHomeServices = new ArrayList<>();
        listHomeServices.add(new CategoryModel.SubCategoryModel("Beauty Parlour Services", R.drawable.ic_cat_hospital, false));
        listHomeServices.add(new CategoryModel.SubCategoryModel("Cooking Services", R.drawable.ic_cat_hospital, false));
        listHomeServices.add(new CategoryModel.SubCategoryModel("Electronic Services", R.drawable.ic_cat_hospital, false));
        listHomeServices.add(new CategoryModel.SubCategoryModel("Gas", R.drawable.ic_cat_hospital, false));
        listHomeServices.add(new CategoryModel.SubCategoryModel("House Cleaning", R.drawable.ic_cat_hospital, false));
        listHomeServices.add(new CategoryModel.SubCategoryModel("Milk Services", R.drawable.ic_cat_hospital, false));
        listHomeServices.add(new CategoryModel.SubCategoryModel("Newspaper Services", R.drawable.ic_cat_hospital, false));
        listHomeServices.add(new CategoryModel.SubCategoryModel("Plumber", R.drawable.ic_cat_hospital, false));
        categoryModels.add(new CategoryModel("Home Services", listHomeServices, false));
        //Home Services End

        //Laundry Start
        ArrayList<CategoryModel.SubCategoryModel> listLaundry = new ArrayList<>();
        categoryModels.add(new CategoryModel("Laundry", listLaundry, false));
        //Laundry End

        //Photo Studio Start
        ArrayList<CategoryModel.SubCategoryModel> listPhotoStudio = new ArrayList<>();
        listPhotoStudio.add(new CategoryModel.SubCategoryModel("Photo Studio", R.drawable.ic_cat_hospital, false));
        categoryModels.add(new CategoryModel("Photo Studio", listPhotoStudio, false));
        //Photo Studio End

        /*//Sweet Studio Start
        ArrayList<CategoryModel.SubCategoryModel> listSweetStudio = new ArrayList<>();
        listSweetStudio.add(new CategoryModel.SubCategoryModel("Sweet Shops", R.drawable.ic_cat_hospital, false));
        categoryModels.add(new CategoryModel("Sweet Shops", listSweetStudio, false));
        //Sweet Studio End*/

        //Stationary Start
        ArrayList<CategoryModel.SubCategoryModel> listStationary = new ArrayList<>();
        categoryModels.add(new CategoryModel("Stationary", listStationary, false));
        //Stationary End

        //Toys Start
        ArrayList<CategoryModel.SubCategoryModel> listToys = new ArrayList<>();
        categoryModels.add(new CategoryModel("Toys", listToys, false));
        //Toys End

        return categoryModels;
    }
}
