package com.example.tenthskate.stockinventor.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.tenthskate.stockinventor.Fragment.DelivererdSto;
import com.example.tenthskate.stockinventor.Fragment.OrdersFragment;
import com.example.tenthskate.stockinventor.Fragment.ProductsFragment;
import com.example.tenthskate.stockinventor.Fragment.deliveredReq;

public class TabsAccessorAdapter extends FragmentPagerAdapter {

    public TabsAccessorAdapter(@NonNull FragmentManager fm) {
        super(fm);


    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new OrdersFragment();
            case 1:
                return new ProductsFragment();
                //messages uncomment
            case 2:
                return new DelivererdSto();
//            case 3:
//                return new RequestFragment();
            case 3:
                return new deliveredReq();
            //messages uncomment

//            case 2:
//                return new MessageFragment();
            default:
                return new OrdersFragment();
        }
    }

    @Override
    public int getCount() {
//change to 3 for messages
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Orders";
            case 1:
                return "Products";
                case 2:
                return "Delivered";

//            case 3:
//                return "Request Supplies";
            case 3:
                return "Delivered Supplies";
            default:
                return null;
        }
    }
}
