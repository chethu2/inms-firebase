package com.invent.inms.helper;

public class IngredientsData {

    private int imageId;
    private String checkText;

    public IngredientsData(int imageId, String checkText) {
        this.imageId = imageId;
        this.checkText = checkText;
        setCheckedText(checkText);
        setImageId(imageId);
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setCheckedText(String checkText ) {
        this.checkText = checkText;
    }

    public int getImageId() {
        return imageId;
    }

    public String getCheckedText() {
        return checkText;
    }

    public String getExpiry(String ingredient) {
        String expiry = "5 days";
        if(ingredient.contains(Constants.TOMATO)){
            expiry = "5 days";
        }
        if(ingredient.contains(Constants.ONION)){
            expiry = "2 weeks";
        }
        if(ingredient.contains(Constants.BELLPEPPER)){
            expiry = "5 days";
        }
        if(ingredient.contains(Constants.CARROT)){
            expiry = "1.5 weeks";
        }
        if(ingredient.contains(Constants.EGG)){
            expiry = "3 weeks";
        }
return expiry;
    }
}
