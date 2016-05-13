package jiaqiz.cmu.edu.foodeliver.entities;

import android.graphics.Bitmap;

import java.util.ArrayList;

import jiaqiz.cmu.edu.foodeliver.remote.GetPicture;
import jiaqiz.cmu.edu.foodeliver.utility.PictureSearch;

/**
 * Food Class.
 * @author Yu Qiu
 */
public class Food{

    String id;
    String name;
    String price;
    String pictureURL;
    Bitmap picture;
    boolean seleted;
    int quantity;


    public Food(){}
    public Food(String id, String name, String price){
        this.id = id;
        this.name = name;
        this.price = price;
    }
    public Food(String id, String name, String price, int quantity){
        this(id, name, price);
        this.quantity = quantity;
    }


    public Food(String id, String name, String price, String pictureURL){
        this(id, name, price);
        this.pictureURL = pictureURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public boolean isSeleted() {
        return seleted;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSeleted(boolean seleted) {
        this.seleted = seleted;
    }

    /**
     * Get name.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get price.
     * @return price
     */
    public String getPrice() {
        return price;
    }

    /**
     * Get picture.
     * @return picture
     */
    public Bitmap getPicture() {
        return picture;
    }

    /**
     * Set food name.
     * @param name food name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set price
     * @param price price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * Set picture
     * @param picture picture
     */
    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

}
