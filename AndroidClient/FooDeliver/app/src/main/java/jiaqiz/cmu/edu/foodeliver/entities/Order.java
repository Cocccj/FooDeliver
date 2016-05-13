package jiaqiz.cmu.edu.foodeliver.entities;

import java.util.ArrayList;

import jiaqiz.cmu.edu.foodeliver.utility.ShowToast;

/**
 * Abstract class Order.
 * @param <T> item type
 */
public abstract class Order<T> {
    String orderId; // Order number
    String timestamp; // Timestamp
    double price; // Food price
    String status; // Order status
    ArrayList<T> items;
    ShowToast showToast;

    abstract void createOrder(String customerEmail, String restaurantEmail, String orderTime,
                              String phone, String addr, String payment, double totalPrice, ArrayList<Food> items);

    abstract void readOrder();

    abstract void updateOrder(String orderId, String name, String status, String p);

    /**
     * Delete order.
     * @param orderID order number
     * @return true if successfully, false otherwise
     */
    abstract void deleteOrder(String orderID);

    abstract void showOrderDetails();

    /**
     * Get items.
     * @return items
     */
    public ArrayList<T> getItems() {
        return items;
    }

    /**
     * Set items.
     * @param items items
     */
    public void setItems(ArrayList<T> items) {
        this.items = items;
    }

    /**
     * Get order number.
     * @return order number
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * Set order number.
     * @param orderId order number
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * Get timestamp.
     * @return timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Set timestamp.
     * @param timestamp timestamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Get price.
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Set price.
     * @param price price
     */
    public void setPrice(double price) {
        this.price = price;
    }
    // Get status

    /**
     * Get status.
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set status.
     * @param status status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public void setShowToast (ShowToast st) {
        showToast = st;
    }
}

