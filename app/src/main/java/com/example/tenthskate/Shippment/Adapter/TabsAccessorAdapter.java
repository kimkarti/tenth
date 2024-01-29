package com.example.tenthskate.Shippment.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.tenthskate.Shippment.Fragment.DelivererdSto;
import com.example.tenthskate.Shippment.Fragment.OrdersFragment;
import com.example.tenthskate.Shippment.Fragment.ProductsFragment;

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
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Incoming";
            case 1:
                return "Intransit";
                case 2:
                return "Delivered";


            default:
                return null;
        }
    }
}
