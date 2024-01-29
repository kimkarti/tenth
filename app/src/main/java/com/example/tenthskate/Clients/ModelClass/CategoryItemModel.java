package com.example.tenthskate.Clients.ModelClass;

public class CategoryItemModel {

    private int categorySrcImage;
    private String categoryName;



    public CategoryItemModel(int categorySrcImage, String categoryName) {
        this.categorySrcImage = categorySrcImage;
        this.categoryName = categoryName;
    }

    public int getCategorySrcImage() {
        return categorySrcImage;
    }

    public void setCategorySrcImage(int categorySrcImage) {
        this.categorySrcImage = categorySrcImage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


}
