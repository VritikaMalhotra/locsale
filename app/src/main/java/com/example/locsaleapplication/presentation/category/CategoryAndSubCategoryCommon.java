package com.example.locsaleapplication.presentation.category;

import com.example.locsaleapplication.R;

import java.util.ArrayList;

@SuppressWarnings("All")
public class CategoryAndSubCategoryCommon {
    public static ArrayList<CategoryModel> getCategoryModels() {
        ArrayList<CategoryModel>  categoryModels = new ArrayList<>();

        //Bakery Start
        ArrayList<CategoryModel.SubCategoryModel> listBakery = new ArrayList<>();
        listBakery.add(new CategoryModel.SubCategoryModel("Bakery", R.drawable.ic_cat_bakery, false));
        categoryModels.add(new CategoryModel("Bakery", listBakery, false));
        //Bakery End

        //Cafe and Restaurant Start
        ArrayList<CategoryModel.SubCategoryModel> listCafeAndRestaurant = new ArrayList<>();
        listCafeAndRestaurant.add(new CategoryModel.SubCategoryModel("Cafe And Restaurant", R.drawable.ic_cat_cafe, false));
        categoryModels.add(new CategoryModel("Cafe And Restaurant", listCafeAndRestaurant, false));
        //Cafe and Restaurant End

        //Car and Motors Start
        ArrayList<CategoryModel.SubCategoryModel> listCarAndMotors = new ArrayList<>();
        listCarAndMotors.add(new CategoryModel.SubCategoryModel("Battery", R.drawable.ic_cat_battery, false));
        listCarAndMotors.add(new CategoryModel.SubCategoryModel("Service Center", R.drawable.ic_cat_service_center, false));
        listCarAndMotors.add(new CategoryModel.SubCategoryModel("Showroom", R.drawable.ic_cat_showroom, false));
        listCarAndMotors.add(new CategoryModel.SubCategoryModel("Spare Parts", R.drawable.ic_cat_spare_parts, false));
        categoryModels.add(new CategoryModel("Car and Automobile", listCarAndMotors, false));
        //Car and Motors End

        //Clothing and Accessories Start
        ArrayList<CategoryModel.SubCategoryModel> listClothingAndAccessories = new ArrayList<>();
        listClothingAndAccessories.add(new CategoryModel.SubCategoryModel("Jewellery", R.drawable.ic_cat_jewellery, false));
        listClothingAndAccessories.add(new CategoryModel.SubCategoryModel("Kid's Wear", R.drawable.ic_cat_kids_wear, false));
        listClothingAndAccessories.add(new CategoryModel.SubCategoryModel("Men's Wear", R.drawable.ic_cat_mens_wear, false));
        listClothingAndAccessories.add(new CategoryModel.SubCategoryModel("Women's Wear", R.drawable.ic_cat_womens_wear, false));
        listClothingAndAccessories.add(new CategoryModel.SubCategoryModel("Optical Shop", R.drawable.ic_cat_optical_shop, false));
        listClothingAndAccessories.add(new CategoryModel.SubCategoryModel("Tailor", R.drawable.ic_cat_tailor, false));
        categoryModels.add(new CategoryModel("Clothing and Accessories", listClothingAndAccessories, false));
        //Clothing and Accessories End

        //Decoration Start
        ArrayList<CategoryModel.SubCategoryModel> listDecoration = new ArrayList<>();
        listDecoration.add(new CategoryModel.SubCategoryModel("Birthday Decoration", R.drawable.ic_cat_birthday_decoration, false));
        listDecoration.add(new CategoryModel.SubCategoryModel("Wedding Decoration", R.drawable.ic_cat_wedding_decoration, false));
        categoryModels.add(new CategoryModel("Decoration", listDecoration, false));
        //Decoration End

        //Dry Fruits Shop Start
        ArrayList<CategoryModel.SubCategoryModel> listDryFruitsShop = new ArrayList<>();
        listDryFruitsShop.add(new CategoryModel.SubCategoryModel("Dry Fruits Shop", R.drawable.ic_cat_dryfruit, false));
        categoryModels.add(new CategoryModel("Dry Fruits Shop", listDryFruitsShop, false));
        //Dry Fruits Shop End

        //Electronics Shop Start
        ArrayList<CategoryModel.SubCategoryModel> listElectronicsShop = new ArrayList<>();
        listElectronicsShop.add(new CategoryModel.SubCategoryModel("Electronics Shop", R.drawable.ic_cat_electronics_shop, false));
        categoryModels.add(new CategoryModel("Electronics Shop", listElectronicsShop, false));
        //Electronics Shop End

        //Gift Shop Start
        ArrayList<CategoryModel.SubCategoryModel> listGiftShop = new ArrayList<>();
        listGiftShop.add(new CategoryModel.SubCategoryModel("Gift Shop", R.drawable.ic_cat_gift, false));
        categoryModels.add(new CategoryModel("Gift Shop", listGiftShop, false));
        //Gift Shop End

        //Groceries Start
        ArrayList<CategoryModel.SubCategoryModel> listGroceries = new ArrayList<>();
        listGroceries.add(new CategoryModel.SubCategoryModel("Kitchen Supply", R.drawable.ic_cat_kids_wear, false));
        categoryModels.add(new CategoryModel("Groceries", listGroceries, false));
        //Groceries End

        //Hardware Start
        ArrayList<CategoryModel.SubCategoryModel> listHardware = new ArrayList<>();
        listHardware.add(new CategoryModel.SubCategoryModel("Hardware Shop", R.drawable.ic_cat_hardware_shop, false));
        categoryModels.add(new CategoryModel("Hardware", listHardware, false));
        //Hardware End

        //Health And Medical Start
        ArrayList<CategoryModel.SubCategoryModel> listHealthAndMedical = new ArrayList<>();
        listHealthAndMedical.add(new CategoryModel.SubCategoryModel("Dietitian", R.drawable.ic_cat_dietitian, false));
        listHealthAndMedical.add(new CategoryModel.SubCategoryModel("Hospitals", R.drawable.ic_cat_hospitals, false));
        listHealthAndMedical.add(new CategoryModel.SubCategoryModel("Medical Stores", R.drawable.ic_cat_medical_stores, false));
        categoryModels.add(new CategoryModel("Health And Medical", listHealthAndMedical, false));
        //Health And Medical End

        //Home Services Start
        ArrayList<CategoryModel.SubCategoryModel> listHomeServices = new ArrayList<>();
        listHomeServices.add(new CategoryModel.SubCategoryModel("Beauty Parlour Services", R.drawable.ic_cat_beauty_parlour, false));
        listHomeServices.add(new CategoryModel.SubCategoryModel("Cooking Services", R.drawable.ic_cat_cooking_services, false));
        listHomeServices.add(new CategoryModel.SubCategoryModel("Electronic Services", R.drawable.ic_cat_electronic_services, false));
        listHomeServices.add(new CategoryModel.SubCategoryModel("Gas", R.drawable.ic_cat_gas, false));
        listHomeServices.add(new CategoryModel.SubCategoryModel("House Cleaning", R.drawable.ic_cat_house_cleaning, false));
        listHomeServices.add(new CategoryModel.SubCategoryModel("Milk Services", R.drawable.ic_cat_milk_services, false));
        listHomeServices.add(new CategoryModel.SubCategoryModel("Newspaper Services", R.drawable.ic_cat_newspaper_services, false));
        listHomeServices.add(new CategoryModel.SubCategoryModel("Plumber", R.drawable.ic_cat_plumber, false));
        categoryModels.add(new CategoryModel("Home Services", listHomeServices, false));
        //Home Services End

        //Laundry Start
        ArrayList<CategoryModel.SubCategoryModel> listLaundry = new ArrayList<>();
        listLaundry.add(new CategoryModel.SubCategoryModel("Laundry", R.drawable.ic_cat_laundry, false));
        categoryModels.add(new CategoryModel("Laundry", listLaundry, false));
        //Laundry End

        //Photo Studio Start
        ArrayList<CategoryModel.SubCategoryModel> listPhotoStudio = new ArrayList<>();
        listPhotoStudio.add(new CategoryModel.SubCategoryModel("Photo Studio", R.drawable.ic_cat_photo_studio, false));
        categoryModels.add(new CategoryModel("Photo Studio", listPhotoStudio, false));
        //Photo Studio End

        /*//Sweet Studio Start
        ArrayList<CategoryModel.SubCategoryModel> listSweetStudio = new ArrayList<>();
        listSweetStudio.add(new CategoryModel.SubCategoryModel("Sweet Shops", R.drawable.ic_cat_hospital, false));
        categoryModels.add(new CategoryModel("Sweet Shops", listSweetStudio, false));
        //Sweet Studio End*/

        //Stationary Start
        ArrayList<CategoryModel.SubCategoryModel> listStationary = new ArrayList<>();
        listStationary.add(new CategoryModel.SubCategoryModel("Stationary", R.drawable.ic_cat_stationary, false));
        categoryModels.add(new CategoryModel("Stationary", listStationary, false));
        //Stationary End

        //Toys Start
        ArrayList<CategoryModel.SubCategoryModel> listToys = new ArrayList<>();
        listToys.add(new CategoryModel.SubCategoryModel("Toys", R.drawable.ic_cat_toys, false));
        categoryModels.add(new CategoryModel("Toys", listToys, false));
        //Toys End

        return categoryModels;
    }
}
