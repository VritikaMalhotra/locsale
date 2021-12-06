package com.example.locsaleapplication.Fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locsaleapplication.Adapter.SearchCategoryAdapter;
import com.example.locsaleapplication.Adapter.UserAdapter;
import com.example.locsaleapplication.Model.User;
import com.example.locsaleapplication.R;
import com.example.locsaleapplication.presentation.category.CategoryAndSubCategoryCommon;
import com.example.locsaleapplication.presentation.category.CategoryModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("All")
public class SearchFragment extends Fragment {

    private RecyclerView recyclerViewBusiness;
    private List<User> mUsers;
    private List<User> mListBusiness = new ArrayList<>();
    private ArrayList<CategoryModel.SubCategoryModel> mListOtherCategory = new ArrayList<>();
    private UserAdapter userAdapter;
    private SocialAutoCompleteTextView search_bar;

    private LinearLayout lvBusiness, lvCategories;
    private AppCompatTextView tvBusiness, tvCategories;
    private View viewBusiness, viewCategories;

    private RecyclerView recyclerViewCategories;
    private SearchCategoryAdapter searchCategoryAdapter;
    private ArrayList<CategoryModel> listCategory = new ArrayList<>();

    private ProgressBar progressBar;
    private AppCompatTextView tvNoData;
    private String stSearchType = "";
    private String selectedTab = "category";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvNoData = view.findViewById(R.id.tvSearchNoData);
        progressBar = view.findViewById(R.id.progressBarLoading);

        lvBusiness = view.findViewById(R.id.linearSearchBusiness);
        lvCategories = view.findViewById(R.id.linearSearchCategories);

        tvBusiness = view.findViewById(R.id.tvSearchBusiness);
        tvCategories = view.findViewById(R.id.tvSearchCategories);

        viewBusiness = view.findViewById(R.id.viewSearchBusiness);
        viewCategories = view.findViewById(R.id.viewSearchCategories);

        recyclerViewCategories = view.findViewById(R.id.recycler_view_categories);

        listCategory = new ArrayList<>();
        listCategory = CategoryAndSubCategoryCommon.getCategoryModels();
        searchCategoryAdapter = new SearchCategoryAdapter(getActivity(), listCategory, new SearchCategoryAdapter.OnItemClickCategorySub() {
            @Override
            public void onItemClickCategorySub(CategoryModel model, CategoryModel.SubCategoryModel subCategoryModel) {
                lvBusiness.performClick();
                if (subCategoryModel != null) {
                    stSearchType = "subCategory";
                    searchUserForCategory(subCategoryModel.getStSubCategoryName());
                } else {
                    stSearchType = "category";
                    searchUserForCategory(model.getStCategoryName());
                }

                //search_bar.setText(subCategoryModel.getStSubCategoryName());
            }
        });
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewCategories.setAdapter(searchCategoryAdapter);

        setClickEvent();

        recyclerViewBusiness = view.findViewById(R.id.recycler_view_users);
        recyclerViewBusiness.setHasFixedSize(true);
        recyclerViewBusiness.setLayoutManager(new LinearLayoutManager(getContext()));
        mUsers = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(), mUsers, true);
        recyclerViewBusiness.setAdapter(userAdapter);

        search_bar = view.findViewById(R.id.search_bar);

        search_bar.addTextChangedListener(new TextWatcher() {

            CountDownTimer timer = null;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                stSearchType = "text";
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }

                timer = new CountDownTimer(1000, 100) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        //if (selectedTab.equalsIgnoreCase("business")) {
                            searchUserForCategory(s.toString());
                        //} else {
                            searchFromCategory(s.toString());
                        //}
                    }
                }.start();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //readUsers();
        getOtherCategories();
    }

    private void setClickEvent() {
        lvBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTab = "business";
                viewBusiness.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));
                viewCategories.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));

                tvBusiness.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
                tvCategories.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));

                recyclerViewBusiness.setVisibility(View.VISIBLE);
                recyclerViewCategories.setVisibility(View.GONE);

                tvNoData.setVisibility(View.GONE);
            }
        });

        lvCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTab = "category";
                viewBusiness.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.white));
                viewCategories.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red));

                tvBusiness.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                tvCategories.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));

                recyclerViewBusiness.setVisibility(View.GONE);
                recyclerViewCategories.setVisibility(View.VISIBLE);

                tvNoData.setVisibility(View.GONE);
            }
        });
    }

    private void getOtherCategories() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mListOtherCategory = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (checkStrinValueReturn(user.getType(), "").equals("1")
                            && checkStrinValueReturn(user.getBusiness_field(), "").equals("Others")) {
                        if (checkStrinValue(user.getBusiness_sub_category())) {
                            mListOtherCategory.add(new CategoryModel.SubCategoryModel(user.getBusiness_sub_category(), R.drawable.ic_cat_other, false));
                        }
                    }
                }

                /*if (mListOtherCategory.size() > 0) {
                    listCategory.add(new CategoryModel("Other", mListOtherCategory, false));
                } else {
                    listCategory.add(new CategoryModel("Other", new ArrayList<>(), false));
                }*/

                searchCategoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*private void SearchUser(String s) {
        Query query = FirebaseDatabase.getInstance().getReference().child("Users")
                .orderByChild("business_name").startAt(s).endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user.getType() != null && user.getType().equals("1")) {
                        mUsers.add(user);
                    }
                    userAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

    private void searchFromCategory(String s) {
        //listCategory.clear();
        ArrayList<CategoryModel> listCategoryFilter = new ArrayList<>();
        if (checkStrinValue(s)) {
            for (CategoryModel modelCategory : listCategory) {

                ArrayList<CategoryModel.SubCategoryModel> listSubCategory = new ArrayList<>();
                if (modelCategory.getSubCategoryList().size() > 0) {
                    for (CategoryModel.SubCategoryModel subCategoryModel : modelCategory.getSubCategoryList()) {
                        if (subCategoryModel.getStSubCategoryName().toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT))) {
                            listSubCategory.add(subCategoryModel);
                        }
                    }
                    if (listSubCategory.size() > 0) {
                        listCategoryFilter.add(new CategoryModel(modelCategory.getStCategoryName(), listSubCategory, false));
                    } else {
                        if (modelCategory.getStCategoryName().toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT))) {
                            listCategoryFilter.add(new CategoryModel(modelCategory.getStCategoryName(), listSubCategory, false));
                        }
                    }
                } else {
                    if (modelCategory.getStCategoryName().toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT))) {
                        listCategoryFilter.add(modelCategory);
                    }
                }
            }

            ArrayList<CategoryModel.SubCategoryModel> listSubCategory = new ArrayList<>();
            for (CategoryModel.SubCategoryModel modelSearch : mListOtherCategory) {
                if (modelSearch.getStSubCategoryName().toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT))) {
                    listSubCategory.add(modelSearch);
                }
            }

            if (listSubCategory.size() > 0) {
                listCategoryFilter.add(new CategoryModel("Other", listSubCategory, false));
            } else {
                if ("Other".toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT))) {
                    listCategoryFilter.add(new CategoryModel("Other", new ArrayList<>(), false));
                }
            }

        } else {
            listCategoryFilter = listCategory;
        }

        searchCategoryAdapter.updateList(listCategoryFilter);
    }

    private void searchUserForCategory(String s) {
        if (checkStrinValue(s)) {
            progressBar.setVisibility(View.VISIBLE);
            Query query = FirebaseDatabase.getInstance().getReference().child("Users");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        if (user.getType() != null && user.getType().equals("1")) {

                            if (stSearchType.equalsIgnoreCase("category") &&
                                    checkStrinValueReturn(user.getBusiness_field(), "").toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT))) {
                                mUsers.add(user);
                            } else if (stSearchType.equalsIgnoreCase("subCategory") &&
                                    checkStrinValueReturn(user.getBusiness_sub_category(), "").toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT))) {
                                mUsers.add(user);
                            } else {
                                if (checkStrinValueReturn(user.getBusiness_name(), "").toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT)) ||
                                        checkStrinValueReturn(user.getName(), "").toLowerCase(Locale.ROOT).contains(s.toLowerCase(Locale.ROOT))) {
                                    mUsers.add(user);
                                }
                            }

                        /*if (checkStrinValueReturn(user.getBusiness_name(), "").contains(s) ||
                                checkStrinValueReturn(user.getBusiness_field(), "").contains(s)||
                                checkStrinValueReturn(user.getBusiness_sub_category(), "").contains(s)) {
                            mUsers.add(user);
                        }*/
                        }
                    }

                    if (mUsers != null && mUsers.size() > 0) {
                        userAdapter.notifyDataSetChanged();
                        if (selectedTab.equalsIgnoreCase("business")) {
                            recyclerViewBusiness.setVisibility(View.VISIBLE);
                            tvNoData.setVisibility(View.GONE);
                        }
                    } else {
                        if (selectedTab.equalsIgnoreCase("business")) {
                            recyclerViewBusiness.setVisibility(View.GONE);
                            tvNoData.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            mUsers.clear();
            userAdapter.notifyDataSetChanged();
            recyclerViewBusiness.setVisibility(View.VISIBLE);
        }
    }

    public boolean checkStrinValue(String value) {
        if (value != null && !value.isEmpty() && !value.equalsIgnoreCase("null")) {
            return true;
        } else {
            return false;
        }
    }

    public String checkStrinValueReturn(String value, String returnvalue) {
        if (value != null && !value.isEmpty() && !value.equalsIgnoreCase("null")) {
            return value;
        } else {
            return returnvalue;
        }
    }
}