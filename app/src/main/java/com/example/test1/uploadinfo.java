package com.example.test1;
public class uploadinfo {
    public String food_name;
    public String food_image_URL;
    public String food_quantity;
    public String food_validity;
    public String user_email;
    public String pickup_location;
    public uploadinfo(){}

    public uploadinfo(String fname,String foodquantity,String foodvalidity,String email,String loc, String url) {
        this.food_name = fname;
        this.food_quantity= foodquantity;
        this.food_image_URL = url;
        this.food_validity = foodvalidity;
        this.user_email = email;
        this.pickup_location = loc;

    }
    public String getFood_name() {
        return food_name;
    }
    public String getFood_image_URL() {
        return food_image_URL;
    }
    public String getFood_validity() {
        return food_validity;
    }
    public String getFood_quantity() {
        return food_quantity;
    }
    public String getUser_email() {
        return user_email;
    }
    public String getPickup_location() {
        return pickup_location;
    }
    private String mName;
    private String mImageUrl;

    public uploadinfo(String name, String imageUrl) {
        if (name.trim().equals("")) {
            name = "No Name";
        }

        mName = name;
        mImageUrl = imageUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }


}
