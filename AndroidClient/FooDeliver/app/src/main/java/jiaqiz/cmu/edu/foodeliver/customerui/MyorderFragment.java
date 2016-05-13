package jiaqiz.cmu.edu.foodeliver.customerui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
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
import jiaqiz.cmu.edu.foodeliver.entities.FoodOrder;
import jiaqiz.cmu.edu.foodeliver.remote.DBTask;
import jiaqiz.cmu.edu.foodeliver.utility.DataSearch;
import jiaqiz.cmu.edu.foodeliver.utility.ShowToast;


/**
 * A simple {@link ListFragment} subclass.
 * Use the {@link MyorderFragment#newInstance} factory method to
 * create an instance of this fragment.
 * @author Jiaqi Zhang
 */
public class MyorderFragment extends ListFragment implements DataSearch, ShowToast {

    /**
     * Items in the list view.
     */
    private List<ListViewItem> items;

    /**
     * List view.
     */
    private ListView orderListView;

    private String customerEmail;

    public MyorderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyorderFragment.
     */
    public static MyorderFragment newInstance() {
        MyorderFragment fragment = new MyorderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.customerui_fragment_myorder, container, false);
        Button cancelOrder = (Button) view.findViewById(R.id.cancel_order);
        Button confirmOrder = (Button) view.findViewById(R.id.confirm_order);
        cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // cancel order
                boolean valid = true;
                StringBuilder sb = new StringBuilder("(");
                ArrayList<FoodOrder> updateOrders = new ArrayList<FoodOrder>();
                for (ListViewItem item : items) {
                    if (item.getSelected()) {
                        if (item.order.getStatus().equals("In Delivery")
                                || item.order.getStatus().equals("Cancelled")
                                || item.order.getStatus().equals("Finished")) {
                            valid = false;
                        }
                        updateOrders.add(item.order);
                        item.setSelected(false);
                        sb.append("'").append(item.order.getOrderId()).append("',");
                    }
                }
                if (valid) {
                    sb.deleteCharAt(sb.length() - 1).append(")");
                    String sql = "UPDATE ORDERTABLE SET OrderState='Cancelled' WHERE "
                            + "OrderId IN " + sb.toString() + ";";
                    DBTask dbT = new DBTask();
                    ArrayList<String> list = new ArrayList<String>();
                    list.add(sql);
                    dbT.getResult(list, MyorderFragment.this, "update");
                    for (FoodOrder o : updateOrders) {
                        o.setStatus("Cancelled");
                    }
                    setListAdapter(new ListViewAdapter(getActivity(), items));
                } else {
                    setToast("Invalid cancellation.");
                }
            }
        });
        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // confirm order
                boolean valid = true;
                StringBuilder sb = new StringBuilder("(");
                ArrayList<FoodOrder> updateOrders = new ArrayList<FoodOrder>();
                for (ListViewItem item : items) {
                    if (item.getSelected()) {
                        if (!item.order.getStatus().equals("In Delivery")) {
                            valid = false;
                        }
                        updateOrders.add(item.order);
                        item.setSelected(false);
                        sb.append("'").append(item.order.getOrderId()).append("',");
                    }
                }
                if (valid) {
                    sb.deleteCharAt(sb.length() - 1).append(")");
                    String sql = "UPDATE ORDERTABLE SET OrderState='Finished' WHERE "
                            + "OrderId IN " + sb.toString() + ";";
                    DBTask dbT = new DBTask();
                    ArrayList<String> list = new ArrayList<String>();
                    list.add(sql);
                    dbT.getResult(list, MyorderFragment.this, "update");
                    for (FoodOrder o : updateOrders) {
                        o.setStatus("Finished");
                    }
                    setListAdapter(new ListViewAdapter(getActivity(), items));
                } else {
                    setToast("You cannot confirm an order not in delivery.");
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

        CustomerActivity cActivity = (CustomerActivity) getActivity();
        customerEmail = cActivity.getCustomerEmail();

        DBTask dbT = new DBTask();
        ArrayList<String> list = new ArrayList<String>();
        list.add("SELECT o.OrderID, r.Name, o.OrderState, o.TotalPrice" +
                " FROM ORDERTABLE o, RESTAURANT r WHERE o.CustomerEmail='" + customerEmail
                + "' AND o.RestaurantEmail=r.Email;");
        dbT.getResult(list, MyorderFragment.this, "result");

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

    public void dbResultReady(String sqlResult) {
        String[] results = sqlResult.split("\n");
        items.clear();
        for (String result : results) {
            String[] elements = result.split("\t");
            if (elements.length > 3) {
                items.add(new ListViewItem(elements[0], elements[1], elements[2], elements[3]));
            }
        }
        setListAdapter(new ListViewAdapter(getActivity(), items));
    }

    public void updateReady(String result) {
        if (result.equals("true")) {
            Toast.makeText(getActivity(),
                    "Order updated successfully.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(),
                    "Sorry, operation failed.", Toast.LENGTH_LONG).show();
        }
    }
    public void dbDetailReady(String sqlResult) {
    }

    @Override
    public void deleteReady(String result) {

    }

    /**
     * List View Item Class.
     */
    private static class ListViewItem {
        private FoodOrder order;
        private boolean selected;

        public ListViewItem(String orderId, String name, String status, String p) {
            order = new FoodOrder(orderId, name, status, p, "", "", "");
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

    /**
     * List View Adapter.
     * @author Jiaqi Zhang
     */
    private class ListViewAdapter extends ArrayAdapter<ListViewItem> {

        /**
         * Constructor.
         * @param context context
         * @param items list items.
         */
        public ListViewAdapter(Context context, List<ListViewItem> items) {
            super(context, R.layout.customerui_fragment_myorderitem, items);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.customerui_fragment_myorderitem, parent, false);
            CheckBox orderNumber = (CheckBox) view.findViewById(R.id.orderNumberBox);
            TextView res = (TextView) view.findViewById(R.id.resTextView);
            TextView status = (TextView) view.findViewById(R.id.statusTextView);
            final int p = position;
            ListViewItem item = getItem(position);
            orderNumber.setText(String.valueOf(position + 1));
            res.setText(item.order.getRestaurant());
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
                    System.out.println("Clicked: " + p);
                    System.out.println(items.get(p).order.getOrderId());
                    FoodOrder o = items.get(p).order;
                    o.setShowToast(MyorderFragment.this);
                    o.showOrderDetails();
                }
            });

            return view;
        }
    }

    @Override
    public void setToast(String str) {
        Toast.makeText(getActivity(),
                str, Toast.LENGTH_LONG).show();
    }
}
