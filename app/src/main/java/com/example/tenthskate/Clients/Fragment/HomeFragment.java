package com.example.tenthskate.Clients.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.tenthskate.Clients.Activities.MainActivity;
import com.example.tenthskate.Clients.Adapter.AdvAdapter;
import com.example.tenthskate.Clients.Adapter.CategoryModelAdapter;
import com.example.tenthskate.Clients.Adapter.GridProductLayoutAdapter;
import com.example.tenthskate.Clients.Adapter.HorizontalPopularProductsAdapter;
import com.example.tenthskate.Clients.Adapter.MyOrderAdapter;
import com.example.tenthskate.Clients.Adapter.SliderAdapter;
import com.example.tenthskate.Clients.ModelClass.AdvModel;
import com.example.tenthskate.Clients.ModelClass.CategoryItemModel;
import com.example.tenthskate.Clients.ModelClass.HorizontalPopularProductModel;
import com.example.tenthskate.Clients.ModelClass.MyOrderItemModel;
import com.example.tenthskate.Clients.ModelClass.SliderModel;
import com.example.tenthskate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
    }

    //TextView searchBar;
    RecyclerView recyclerView,myLastOrderRecyclerView;
    List<CategoryItemModel> categoryItemModelList;
    CategoryModelAdapter categoryModelAdapter;
    ViewPager bannerSlider;
    List<SliderModel> sliderModelList;
    MyOrderAdapter myOrderAdapter;
    List<MyOrderItemModel> myOrderItemModelList;
    TextView viewAllOrders;
    SliderAdapter sliderAdapter;
    HorizontalPopularProductsAdapter horizontalPopularProductsAdapter;
    List<HorizontalPopularProductModel> horizontalPopularProductModelList;
    List<HorizontalPopularProductModel> gridProductsModelList;
    List<AdvModel> gridAdvList;
    GridView gridLayout,advGrid;
    GridProductLayoutAdapter gridProductLayoutAdapter;
    AdvAdapter AdvAdapter;
    RecyclerView horizontalProductRecyclerView;
    int currentPage = 2;
    Timer timer;
    long DELAY_TIME = 3000;
    long PERIOD_TIME = 3000;
    ImageView stripAdImage;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        final FragmentActivity fragmentActivity = getActivity();

        //////////////////////////////////// HOME CATEGORY VIEW ///////////////////////////////////////

        LinearLayoutManager layoutManager = new LinearLayoutManager(fragmentActivity);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView = view.findViewById(R.id.category_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        progressBar = view.findViewById(R.id.progress_circular_home);

        categoryItemModelList = new ArrayList<>();
        categoryItemModelList.add(new CategoryItemModel(R.drawable.skatepacked,"Packed Kit"));
        categoryItemModelList.add(new CategoryItemModel(R.drawable.elbowpad ,"Elbow Pad"));
        categoryItemModelList.add(new CategoryItemModel(R.drawable.helmet ,"Helmet"));
        categoryItemModelList.add(new CategoryItemModel(R.drawable.rollerskates ,"Roller Skate"));
        categoryItemModelList.add(new CategoryItemModel(R.drawable.kneepad ,"Knee Pad"));
        categoryItemModelList.add(new CategoryItemModel(R.drawable.wristpad ,"Wrist Guard"));
        categoryItemModelList.add(new CategoryItemModel(R.drawable.skateboard ,"Other Needs"));

        categoryModelAdapter = new CategoryModelAdapter(categoryItemModelList, fragmentActivity);
        recyclerView.setAdapter(categoryModelAdapter);
        categoryModelAdapter.notifyDataSetChanged();



        new Thread(new Runnable() {
            @Override
            public void run() {
                categoryModelAdapter=new CategoryModelAdapter(categoryItemModelList, fragmentActivity);
                assert fragmentActivity != null;
                fragmentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(categoryModelAdapter);
                    }
                });
            }
        }).start();

 ///////////////////////////////////// HORIZONTAL POPULAR PRODUCTS ////////////////////////////////////////////////

        horizontalProductRecyclerView = view.findViewById(R.id.horizontal_products_recycler_view);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(fragmentActivity);
        layoutManager2.setOrientation(RecyclerView.HORIZONTAL);
        horizontalProductRecyclerView.setLayoutManager(layoutManager2);
        horizontalPopularProductModelList = new ArrayList<>();

        DatabaseReference horizontalProducts = FirebaseDatabase.getInstance().getReference()
                .child("HorizontalPopularProducts");
        horizontalProducts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    progressBar.setVisibility(View.GONE);
                    view.findViewById(R.id.horizontal_product_popular).setVisibility(View.VISIBLE);
                    horizontalPopularProductModelList.clear();
                    for(DataSnapshot products:snapshot.getChildren()){

                        HorizontalPopularProductModel popularProducts = new
                                HorizontalPopularProductModel(Objects.requireNonNull(products.child("productImage").getValue()).toString(),
                                Objects.requireNonNull(products.child("productTitle").getValue()).toString(),
                                Objects.requireNonNull(products.child("productCuttedPrice").getValue()).toString(),
                                Objects.requireNonNull(products.child("productPrice").getValue()).toString(),
                                Objects.requireNonNull(products.child("productID").getValue()).toString());
                        horizontalPopularProductModelList.add(popularProducts);

                    }
                    horizontalPopularProductsAdapter
                            = new HorizontalPopularProductsAdapter(horizontalPopularProductModelList,fragmentActivity);
                    horizontalProductRecyclerView.setAdapter(horizontalPopularProductsAdapter);
                    horizontalPopularProductsAdapter.notifyDataSetChanged();
                }

                else {
                    view.findViewById(R.id.horizontal_product_popular).setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //////////////////////////////// GRID PRODUCT LAYOUT ////////////////////////////////////

        gridProductsModelList = new ArrayList<>();
        gridLayout = view.findViewById(R.id.product_grid_view);

        DatabaseReference gridProducts = FirebaseDatabase.getInstance().getReference()
                .child("GridLayoutPopularProducts");
        gridProducts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    progressBar.setVisibility(View.GONE);
                    view.findViewById(R.id.grid_trending_product_layout).setVisibility(View.VISIBLE);
                    gridProductsModelList.clear();
                    for(DataSnapshot products:snapshot.getChildren()){

                        HorizontalPopularProductModel popularProducts = new
                                HorizontalPopularProductModel(products.child("productImage").getValue().toString(),
                                products.child("productTitle").getValue().toString(),
                                products.child("productCuttedPrice").getValue().toString(),
                                products.child("productPrice").getValue().toString(),
                                products.child("productID").getValue().toString());
                        gridProductsModelList.add(popularProducts);

                    }
                    gridProductLayoutAdapter
                            = new GridProductLayoutAdapter(gridProductsModelList,fragmentActivity);
                    gridLayout.setAdapter(gridProductLayoutAdapter);
                    gridProductLayoutAdapter.notifyDataSetChanged();
                }
                else {
                    view.findViewById(R.id.grid_trending_product_layout).setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



////////////////////////////////////////// BANNER SLIDER ////////////////////////////////////////////////

        bannerSlider = view.findViewById(R.id.banner_slider_view_pager);
        sliderModelList = new ArrayList<SliderModel>();

        DatabaseReference sliderModelRef = FirebaseDatabase.getInstance().getReference().child("SliderModel1");
        sliderModelRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    progressBar.setVisibility(View.GONE);
                    view.findViewById(R.id.slider_layout).setVisibility(View.VISIBLE);
                    sliderModelList.clear();
                    for (DataSnapshot images:snapshot.getChildren()){
                        SliderModel image = images.getValue(SliderModel.class);
                        sliderModelList.add(image);
                    }

                    sliderAdapter = new SliderAdapter(sliderModelList);
                    bannerSlider.setAdapter(sliderAdapter);
                    sliderAdapter.notifyDataSetChanged();
                }
                else {
                    view.findViewById(R.id.slider_layout).setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        bannerSlider.setClipToPadding(false);
        bannerSlider.setPageMargin(20);
        bannerSlider.setCurrentItem(currentPage);
        bannerSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == ViewPager.SCROLL_STATE_IDLE) {
                    //pageLooper();
                }
            }
        });

        startBannerSlideShow();
        bannerSlider.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                    stopBannerSlideShow();
                else if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    startBannerSlideShow();
                }

                return false;
            }
        });

        ////////////////////////////////// STRIP AD //////////////////////////////////////////

        final DatabaseReference stripAdRef = FirebaseDatabase.getInstance().getReference();
        stripAdImage = view.findViewById(R.id.strip_ad_img);
        stripAdRef.child("StripAdv").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    progressBar.setVisibility(View.GONE);
                    view.findViewById(R.id.strip_ad).setVisibility(View.VISIBLE);
                    stripAdRef.child("StripAdv").child("imageUrl").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                Picasso.get()
                                        .load(Objects.requireNonNull(snapshot.getValue()).toString())
                                        .placeholder(R.drawable.image_preview)
                                        .fit()
                                        .centerInside()
                                        .into(stripAdImage);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else {
                    view.findViewById(R.id.strip_ad).setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //////////////////////////////////// MY LAST ORDER /////////////////////////////////////

        viewAllOrders = view.findViewById(R.id.viewMyOrders);
        viewAllOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.openMyOrders = 1;
                Intent intent = new Intent(fragmentActivity, MainActivity.class);
                startActivity(intent);
                ((Activity)fragmentActivity).finish();

            }
        });

        DatabaseReference myOrders =  FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("orders");
        myLastOrderRecyclerView = view.findViewById(R.id.my_last_order_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(fragmentActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        myLastOrderRecyclerView.setLayoutManager(linearLayoutManager);
        myOrderItemModelList = new ArrayList<>();

        myOrders.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChildren()){
                    progressBar.setVisibility(View.GONE);
                    view.findViewById(R.id.my_last_item_ordered_home_view).setVisibility(View.VISIBLE);
                    myOrderItemModelList.clear();
                    MyOrderItemModel myOrderItemModel = null;
                    for(DataSnapshot items:snapshot.getChildren()){
                        myOrderItemModel = items.getValue(MyOrderItemModel.class);
                    }
                    myOrderItemModelList.add(myOrderItemModel);
                    myOrderAdapter=new MyOrderAdapter(fragmentActivity,myOrderItemModelList);
                    myLastOrderRecyclerView.setAdapter(myOrderAdapter);
                    myOrderAdapter.notifyDataSetChanged();
                }
                else {
                    view.findViewById(R.id.my_last_item_ordered_home_view).setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //////////////////////////////// ADVERTISE LAYOUT ////////////////////////////////////

        gridAdvList = new ArrayList<>();
        advGrid = view.findViewById(R.id.adv_grid);

        DatabaseReference adv = FirebaseDatabase.getInstance().getReference()
                .child("Advertisement");
        adv.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    progressBar.setVisibility(View.GONE);
                    view.findViewById(R.id.grid_advertise_layout).setVisibility(View.VISIBLE);
                    gridAdvList.clear();
                    for(DataSnapshot advt:snapshot.getChildren()){
                        AdvModel adv = new AdvModel(Objects.requireNonNull(advt.child("imageUrl").getValue()).toString());
                        gridAdvList.add(adv);

                    }

                    AdvAdapter = new AdvAdapter(gridAdvList,fragmentActivity);
                    advGrid.setAdapter(AdvAdapter);
                    AdvAdapter.notifyDataSetChanged();
                }
                else {
                    view.findViewById(R.id.grid_advertise_layout).setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ///////////////////////////////////////////////////////////////////////////////////////
        return view;
    }

    ////////////////////////////////////////// BANNER SLIDER ////////////////////////////////////////////////


    private  void startBannerSlideShow(){
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if(currentPage == sliderModelList.size()-1)
                    currentPage = 0;
                else
                    currentPage++;
                bannerSlider.setCurrentItem(currentPage++,true);

            }
        };
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        },DELAY_TIME,PERIOD_TIME);
    }
    private  void stopBannerSlideShow(){
        timer.cancel();
    }
    ////////////////////////////////////////////////////////////////////////////////////////
}
