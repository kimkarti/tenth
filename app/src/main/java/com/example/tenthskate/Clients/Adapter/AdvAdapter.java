package com.example.tenthskate.Clients.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.tenthskate.Clients.ModelClass.AdvModel;
import com.example.tenthskate.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdvAdapter extends BaseAdapter {

    List<AdvModel> gridAdvList;
    Context mContext;

    public AdvAdapter(List<AdvModel> gridProductsList, Context mContext) {
        this.gridAdvList = gridProductsList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return gridAdvList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 ;
        if(view == null){
            view1 = LayoutInflater.from(mContext).inflate(R.layout.adv_item_layout,null);
            view1.setElevation(3);
            ImageView advImage=view1.findViewById(R.id.adv_image);

            Picasso.get().load(gridAdvList.get(i).getAdvImageUrl())
                    .fit()
                    .into(advImage);
        }
        else view1 = view;
        return view1;
    }
}
