package jiaqiz.cmu.edu.foodeliver.entities;

import java.util.ArrayList;

/**
 * Abstract Class Menu.
 * @param <T> item type
 */
public abstract class Menu<T> {
    ArrayList<T> items = new ArrayList<T>();
    String restaurant;

    /**
     * Create item in menu
     * @param name name
     * @param price price
     * @param picture picture
     * @return true if successfully
     */
    abstract public void createItem(String name, double price, Byte[] picture);

    /**
     * Read item in menu
     * @param name name
     * @return item information
     */
    abstract public void readItem(String name);

    /**
     * Update item in menu
     * @param name name
     * @param price price
     * @param picture picture
     * @return true if update successfully, false otherwise
     */
    abstract public void updateItem(String name, double price, Byte[] picture);

    /**
     * Delete item in menu
     * @param name name
     * @return true if delete successfully, false otherwise
     */
    abstract public void deleteItem(String name);

    /**
     * Get items.
     * @return items
     */
    public ArrayList<T> getItems() {
        return items;
    }

    /**
     * Set items
     * @param items items
     */
    public void setItems(ArrayList<T> items) {
        this.items = items;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }
}
