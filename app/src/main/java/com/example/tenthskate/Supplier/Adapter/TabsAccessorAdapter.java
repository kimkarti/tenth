package com.example.tenthskate.Supplier.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.tenthskate.Supplier.Fragment.ApprovedFragment;
import com.example.tenthskate.Supplier.Fragment.OrdersFragment;

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
                return new ApprovedFragment();
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
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Requests";
            case 1:
                return "Deliveries";

            default:
                return null;
        }
    }
}
