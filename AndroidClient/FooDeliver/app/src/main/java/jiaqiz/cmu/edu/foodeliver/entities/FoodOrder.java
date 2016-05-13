package jiaqiz.cmu.edu.foodeliver.entities;

import java.util.ArrayList;

import jiaqiz.cmu.edu.foodeliver.utility.DataSearch;
import jiaqiz.cmu.edu.foodeliver.remote.DBTask;
import jiaqiz.cmu.edu.foodeliver.utility.Transition;

/**
 * Food Order class.
 * @author Yu Qiu
 */
public class FoodOrder extends Order<Food> implements DataSearch {

    private String restaurant;

    // customer used fields
    private Transition transition;
    private boolean toCreate = false;

    // restaurant used fields
    private String customerPhone;
    private String customerAddress;
    private boolean customerFlag = true;

    public FoodOrder(){}
    public FoodOrder(String orderId, String name, String status, String price, String date, String customerPhone, String customerAddress){
        this.orderId = orderId;
        this.restaurant = name;
        this.status = status;
        this.price = Double.parseDouble(price);
        this.timestamp = date;
        this.customerPhone = customerPhone;
        this.customerAddress = customerAddress;
        items = new ArrayList<>();
    }

    @Override
    public void createOrder(String customerEmail, String restaurantEmail, String orderTime,
                            String phone, String addr, String payment, double totalPrice, ArrayList<Food> items) {
        this.orderId = customerEmail + restaurantEmail + orderTime;
        this.status = "Pending";
        this.timestamp = orderTime;
        this.price = totalPrice;
        toCreate = true;

        ArrayList<String> sqls = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ORDERTABLE VALUES(");
        sb.append("'").append(orderId).append("', ");
        sb.append("'").append(customerEmail).append("', ");
        sb.append("'").append(restaurantEmail).append("', ");
        sb.append("'").append(orderTime).append("', ");
        sb.append("'").append(phone).append("', ");
        sb.append("'").append(addr).append("', ");
        sb.append("'").append(payment).append("', ");
        sb.append("'").append(totalPrice).append("', ");
        sb.append("'").append(status).append("');");
        sqls.add(sb.toString());
        for (Food item : items) {
            if (item.getQuantity() > 0) {
                sb = new StringBuilder();
                sb.append("INSERT INTO ORDERITEM VALUES(");
                sb.append("'").append(orderId).append("', ");
                sb.append("'").append(item.getId()).append("', ");
                sb.append("'").append(item.getQuantity()).append("');");
                item.setQuantity(0);
                sqls.add(sb.toString());
            }
        }
        DBTask dbT = new DBTask();
        dbT.getResult(sqls, this, "update");
    }

    @Override
    public void readOrder() {
        DBTask dbT = new DBTask();
        ArrayList<String> list = new ArrayList<String>();
        list.add("SELECT m.FoodId, m.FoodName, m.FoodPrice, oi.Quantity" +
                " FROM ORDERITEM oi, MENU m WHERE oi.OrderId='"
                + orderId + "' AND oi.FoodId=m.FoodId;");
        dbT.getResult(list, this, "detail");
    }

    @Override
    public void updateOrder(String orderId, String name, String status, String p) {
        this.orderId = orderId;
        this.restaurant = name;
        this.status = status;
        this.price = Double.parseDouble(p);
        toCreate = false;
    }

    @Override
    public void deleteOrder(String orderNumber) {
    }

    @Override
    public void showOrderDetails() {
        System.out.println(customerFlag);
        if (customerFlag){
            StringBuilder show = new StringBuilder();
            for (Food f : items) {
                show.append("Item: ").append(f.getName()).append(",\tPrice: ").append(f.getPrice())
                        .append(",\tQuantity: ").append(f.getQuantity()).append("\n");
                System.out.println(show.toString());
            }
            showToast.setToast(show.toString());
        } else {
            StringBuilder show = new StringBuilder();
            for (Food f: items) {
                show.append("Item: ").append(f.getName()).append(", \tPrice: ").append(f.getPrice())
                        .append(", \tQuantity: ").append(f.getQuantity()).append("\n");
            }
            show.append("Delivary Address: ").append(this.getCustomerAddress()).append("\n");
            show.append("Customer Phone: ").append(this.getCustomerPhone());
            showToast.setToast(show.toString());
        }
    }

    @Override
    public void dbResultReady(String sqlResult) {
    }

    @Override
    public void updateReady(String result) {
        if (result.equals("true")) {
            if (toCreate) {
                transition.setToast("Order Placed.");
                transition.transit();
            } else {
                showToast.setToast("Order updated successfully.");
            }
        } else {
            if (toCreate) {
                transition.setToast("Sorry, operation failed.");
            } else {
                showToast.setToast("Sorry, operation failed.");
            }
        }
    }

    @Override
    public void dbDetailReady(String sqlResult) {
        String[] results = sqlResult.split("\n");
        StringBuilder show = new StringBuilder();
        for (String result : results) {
            String[] elements = result.split("\t");
            if (elements.length > 3) {
                Food food = new Food(elements[0], elements[1], elements[2], Integer.parseInt(elements[3]));
                System.out.println(food.getQuantity());
                items.add(food);
            }
        }
    }

    public boolean isCustomerFlag() {
        return customerFlag;
    }

    public void setCustomerFlag(boolean customerFlag) {
        this.customerFlag = customerFlag;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public void setTransition(Transition transition) {
        this.transition = transition;
    }

    @Override
    public void deleteReady(String result) {

    }

    public String getRestaurant() {
        return restaurant;
    }
}
