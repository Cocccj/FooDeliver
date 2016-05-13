package jiaqiz.cmu.edu.foodeliver.entities;

import android.graphics.Bitmap;

import java.util.ArrayList;

import jiaqiz.cmu.edu.foodeliver.remote.DBTask;
import jiaqiz.cmu.edu.foodeliver.remote.GetPicture;
import jiaqiz.cmu.edu.foodeliver.utility.DataSearch;
import jiaqiz.cmu.edu.foodeliver.utility.PictureSearch;

/**
 * Restaurant Class.
 * @author Yu Qiu
 */
public class Restaurant extends User implements DataSearch, PictureSearch {

    private String category; // Category
    private String location; // Location
    private String pictureURL; // Picture url
    private Bitmap picture; // Picture
    private String name; // Name

    private boolean toCreate;

    public Restaurant() {
        super();
    }
    public Restaurant(String email, String name, String category, String location, String picture) {
        this.email = email;
        this.name = name;
        this.category = category;
        this.location = location;
        this.pictureURL = picture;
    }

    // Getter and Setter
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) { this.location = location; }
    public String getPictureURL() { return pictureURL; }
    public void setPictureRUL(String pictureURL) { this.pictureURL = pictureURL; }
    public Bitmap getPicture() {
        return picture;
    }
    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    // Profile Manipulation Functions
    @Override
    public void createProfile(String[] params) {
        // params: Email, Name, Phone, Password, CardNumber, Address, Category, Location, Picture
        String sql = "INSERT INTO RESTAURANT VALUES ('" + params[0] + "','"
                + params[1] + "','" + params[2] + "','" + params[3] + "','"
                + params[4] + "','" + params[5] + "','" + params[6] + "','"
                + params[7] + "','" + params[8] + "')";
        toCreate = true;
        DBTask dbT = new DBTask();
        ArrayList<String> list = new ArrayList<String>();
        list.add(sql);
        dbT.getResult(list, this, "update");
    }

    @Override
    public void readProfile(String email) {
        this.setEmail(email);
        String sql = "SELECT * FROM RESTAURANT WHERE Email='" + email + "';";
        DBTask dbT = new DBTask();
        ArrayList<String> list = new ArrayList<String>();
        list.add(sql);
        dbT.getResult(list, Restaurant.this, "result");
    }

    @Override
    public void updateProfile(String[] params) {
        // params: Email, Name, Phone, Password, CardNumber, Address, Category, Location, Picture
        String sql = "UPDATE RESTAURANT SET Name='" + params[1] + "', Phone='" + params[2]
                + "', Password='" + params[3] + "', CardNumber='" + params[4]
                + "', Address='" + params[5] + "', Category='" + params[6]
                + "', Location='" + params[7] + "', Picture='" + params[8]
                + "' WHERE Email='" + params[0] + "';";
        toCreate = false;
        DBTask dbT = new DBTask();
        ArrayList<String> list = new ArrayList<String>();
        list.add(sql);
        dbT.getResult(list, this, "update");
    }

    @Override
    public void dbResultReady(String result) {
        System.out.println("SQL Result: " + result);
        String[] elements = result.split("\t");
        this.setPassword(elements[3]);
        this.setName(elements[1]);
        this.setAddress(elements[5]);
        this.setPhone(elements[2]);
        this.setCategory(elements[6]);
        this.setLocation(elements[7]);
        this.setCard(elements[4]);
        this.setPictureRUL(elements[8]);
        GetPicture gp = new GetPicture();
        ArrayList<String> searchPics = new ArrayList<>();
        searchPics.add(elements[8]);
        gp.search(searchPics, Restaurant.this);
    }

    @Override
    public void updateReady(String result) {
        if (result.equals("true")) {
            if (toCreate) {
                transition.setToast("New profile created.");
                transition.transit();
            } else {
                showToast.setToast("New profile saved successfully.");
            }
        } else {
            if (toCreate) {
                transition.setToast("Sorry, the account already exists.");
            } else {
                showToast.setToast("Sorry, save failed.");
            }
        }
    }

    @Override
    public void deleteProfile(String email) {}
    @Override
    public void dbDetailReady(String result) {}
    @Override
    public void deleteReady(String result) {}
    @Override
    public void pictureReady(ArrayList<Bitmap> pictures) {
        this.setPicture(pictures.get(0));
    }
}
