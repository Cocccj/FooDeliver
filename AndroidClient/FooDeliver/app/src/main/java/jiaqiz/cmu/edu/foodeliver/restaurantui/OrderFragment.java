package jiaqiz.cmu.edu.foodeliver.restaurantui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import jiaqiz.cmu.edu.foodeliver.R;
import jiaqiz.cmu.edu.foodeliver.customerui.MyorderFragment;
import jiaqiz.cmu.edu.foodeliver.entities.FoodOrder;
import jiaqiz.cmu.edu.foodeliver.exception.ConnectionException;
import jiaqiz.cmu.edu.foodeliver.remote.Client;
import jiaqiz.cmu.edu.foodeliver.remote.DBTask;
import jiaqiz.cmu.edu.foodeliver.utility.DataSearch;
import jiaqiz.cmu.edu.foodeliver.utility.ShowToast;

/**
 * Order Fragment.
 * @author Shuhui Yang
 */
public class OrderFragment extends ListFragment implements DataSearch, ShowToast {

    /**
     * Items in the list view.
     */
    private List<ListViewItem> items;
    private ListView orderListView;
    private String restaurantEmail;

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment OrderFragment.
     */
    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.restaurantui_fragment_order, container, false);
        // accept button
        Button accept = (Button) view.findViewById(R.id.accept_button);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // accept order
                boolean valid = true;
                StringBuilder sb = new StringBuilder("(");
                ArrayList<FoodOrder> updateOrders = new ArrayList<>();
                for (ListViewItem item: items){
                    if (item.getSelected()) {
                        if (!item.order.getStatus().equals("Pending")){
                            valid = false;
                        }
                        updateOrders.add(item.order);
                        item.setSelected(false);
                        sb.append("'").append(item.order.getOrderId()).append("',");
                    }
                }
                if (valid){
                    sb.deleteCharAt(sb.length()-1).append(")");
                    String sql = "UPDATE ORDERTABLE SET OrderState='Accepted' WHERE " +
                            "OrderId IN "+sb.toString()+";";
                    DBTask dbT = new DBTask();
                    ArrayList<String> list = new ArrayList<String>();
                    list.add(sql);
                    dbT.getResult(list, OrderFragment.this, "update");
                    for (FoodOrder o: updateOrders) {
                        o.setStatus("Accepted");
                    }
                    setListAdapter(new ListViewAdapter(getActivity(), items));
                } else {
                    setToast("The order is not available for acceptance.");
                }
            }
        });
        // deliver button
        Button deliver = (Button) view.findViewById(R.id.deliver_button);
        deliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // deliver
                boolean valid = true;
                StringBuilder sb = new StringBuilder("(");
                ArrayList<FoodOrder> updateOrders = new ArrayList<>();
                for (ListViewItem item : items) {
                    if (item.getSelected()) {
                        if (!item.order.getStatus().equals("Accepted")) {
                            valid = false;
                        }
                        updateOrders.add(item.order);
                        item.setSelected(false);
                        sb.append("'").append(item.order.getOrderId()).append("',");
                    }
                }
                if (valid) {
                    sb.deleteCharAt(sb.length() - 1).append(")");
                    String sql = "UPDATE ORDERTABLE SET OrderState='In Delivery' WHERE " +
                            "OrderId IN " + sb.toString() + ";";
                    DBTask dbT = new DBTask();
                    ArrayList<String> list = new ArrayList<String>();
                    list.add(sql);
                    dbT.getResult(list, OrderFragment.this, "update");
                    SmsManager smsManager = SmsManager.getDefault();
                    for (FoodOrder o : updateOrders) {
                        System.out.println(o.getOrderId());
                        o.setStatus("In Delivery");
                        smsManager.sendTextMessage(o.getCustomerPhone(), null, "Your order is in delivery.", null, null);
                    }
                    updateOrders.clear();
                    // send text to customers
                    setListAdapter(new ListViewAdapter(getActivity(), items));
                } else {
                    setToast("The order is not available for delivery");
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        orderListView = getListView();
        items = new ArrayList<ListViewItem>();

        RestaurantActivity rActivity = (RestaurantActivity) getActivity();
        restaurantEmail = rActivity.getRestaurant().getEmail();

        DBTask dbT = new DBTask();
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT * FROM ORDERTABLE o WHERE o.RestaurantEmail='" + restaurantEmail + "';";
        list.add(sql);
        dbT.getResult(list, OrderFragment.this, "result");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
        orderListView = getListView();
    }

    @Override
    public void onResume() {
        super.onResume();
        setListAdapter(new ListViewAdapter(getActivity(), items));
    }

    @Override
    public void dbResultReady(String sqlResult) {

        String[] results = sqlResult.split("\n");
        items.clear();
        for (String result: results){
            String[] elements = result.split("\t");
            if (elements.length > 3){
                items.add(new ListViewItem(elements[0], elements[3], elements[7], elements[8], elements[4], elements[5]));
            }
        }
        setListAdapter(new ListViewAdapter(getActivity(), items));
    }

    @Override
    public void updateReady(String result) {
        if (result.equals("true")) {
            this.setToast( "Order updated successfully.");
        } else {
            this.setToast("Sorry, operation failed.");
        }
    }

    @Override
    public void dbDetailReady(String result) {

    }

    @Override
    public void deleteReady(String result) {

    }

    @Override
    public void setToast(String str) {
        Toast.makeText(getActivity(),
                str, Toast.LENGTH_LONG).show();
    }

    /**
     * List View Item Class.
     */
    private static class ListViewItem {
        private FoodOrder order;
        private boolean selected;

        public ListViewItem(String orderId, String date, String price, String status, String customerPhone, String customerAddress) {
            order = new FoodOrder(orderId, "", status, price, date, customerPhone, customerAddress);
            order.setCustomerFlag(false);
            order.readOrder();
            selected = false;
        }

        public boolean getSelected() {
            return selected;
        }
        public void setSelected(boolean b) {
            selected = b;
        }
    }

    private class ListViewAdapter extends ArrayAdapter<ListViewItem> {

        public ListViewAdapter(Context context, List<ListViewItem> items) {
            super(context, R.layout.restaurantui_fragment_orderitem, items);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.restaurantui_fragment_orderitem, parent, false);

            CheckBox orderNumber = (CheckBox) view.findViewById(R.id.orderNumberBoxRes);
            TextView date = (TextView) view.findViewById(R.id.dateTextView);
            TextView price = (TextView) view.findViewById(R.id.priceTextViewRes);
            TextView status = (TextView) view.findViewById(R.id.statusTextViewRes);

            final int p = position;
            ListViewItem item = getItem(position);

            orderNumber.setText(String.valueOf(position + 1));
            date.setText(item.order.getTimestamp());
            price.setText(String.valueOf(item.order.getPrice()));
            status.setText(item.order.getStatus());

            orderNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        items.get(p).setSelected(true);
                    } else {
                        items.get(p).setSelected(false);
                    }
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // show order details
                    System.out.println(items.get(p).order.getOrderId());
                    FoodOrder o = items.get(p).order;
                    o.setShowToast(OrderFragment.this);
                    o.showOrderDetails();
                }
            });
            return view;
        }
    }
}
