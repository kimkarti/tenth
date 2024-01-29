package com.example.tenthskate.Clients.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tenthskate.Clients.Activities.MainActivity;
import com.example.tenthskate.Clients.ModelClass.CategoryItemModel;
import com.example.tenthskate.R;

import java.util.List;


public class CategoryModelAdapter extends RecyclerView.Adapter<CategoryModelAdapter.CategoryViewHolder> {


    private List<CategoryItemModel> categoryItemModelList;
    private Context mContext;


    public CategoryModelAdapter(List<CategoryItemModel> categoryItemModelList,Context mContext) {
        this.categoryItemModelList = categoryItemModelList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_item,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, final int position) {
        final CategoryItemModel categoryItemModel = categoryItemModelList.get(holder.getAdapterPosition());
        holder.setValues(categoryItemModel.getCategorySrcImage(),categoryItemModel.getCategoryName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startFragment(position);
            }
        });
    }

    private void startFragment(int position) {

        if(position == 0){
            MainActivity.openHomeApp =1;
            Intent intent = new Intent(mContext, MainActivity.class );
            mContext.startActivity(intent);
            ((Activity)mContext).finish();
        }
        else if(position == 1){
            MainActivity.openRoller =1;
            Intent intent = new Intent(mContext, MainActivity.class );
            mContext.startActivity(intent);
            ((Activity)mContext).finish();
        }
        else if(position == 2){
            MainActivity.openHelmet =1;
            Intent intent = new Intent(mContext, MainActivity.class );
            mContext.startActivity(intent);
            ((Activity)mContext).finish();
        }
        else if(position == 3){
            MainActivity.openKnee =1;
            Intent intent = new Intent(mContext, MainActivity.class );
            mContext.startActivity(intent);
            ((Activity)mContext).finish();
        }
        else if(position == 4){
            MainActivity.openElbow =1;
            Intent intent = new Intent(mContext, MainActivity.class );
            mContext.startActivity(intent);
            ((Activity)mContext).finish();
        }
        else if(position == 5){
            MainActivity.openWrist =1;
            Intent intent = new Intent(mContext, MainActivity.class );
            mContext.startActivity(intent);
            ((Activity)mContext).finish();
        }
        else if(position == 6){
            MainActivity.openOthers =1;
            Intent intent = new Intent(mContext, MainActivity.class );
            mContext.startActivity(intent);
            ((Activity)mContext).finish();
        }
    }


    @Override
    public int getItemCount() {
        return categoryItemModelList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryTitle;
        private ImageView categoryImage;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.category_icon);
            categoryTitle = itemView.findViewById(R.id.category_title);

        }
        void setValues(int img, String name){
            categoryImage.setImageResource(img);
            categoryTitle.setText(name);
        }
    }

}
