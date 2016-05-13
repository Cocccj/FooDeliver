package jiaqiz.cmu.edu.foodeliver.customerui;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jiaqiz.cmu.edu.foodeliver.R;
import jiaqiz.cmu.edu.foodeliver.entities.Food;
import jiaqiz.cmu.edu.foodeliver.entities.FoodMenu;
import jiaqiz.cmu.edu.foodeliver.entities.FoodOrder;
import jiaqiz.cmu.edu.foodeliver.entities.Restaurant;
import jiaqiz.cmu.edu.foodeliver.exception.ConnectionException;
import jiaqiz.cmu.edu.foodeliver.remote.Client;
import jiaqiz.cmu.edu.foodeliver.utility.Transition;

public class PlaceOrderActivity extends ListActivity implements Transition {

    /**
     * Food menu.
     */
    private FoodMenu foodMenu;

    /**
     * Customer email.
     */
    private String customerEmail = "";

    /**
     * Phone number.
     */
    private String customerPhone = "";

    /**
     * Deliver address.
     */
    private String customerAddr = "";

    /**
     * Restaurant profile.
     */
    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customerui_activity_place_order);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            customerEmail = extras.getString("customer_email");
            restaurant = new Restaurant();
            restaurant.readProfile(extras.getString("restaurant_email"));
            new GetInfoTask().execute("SELECT Name, Address, Phone" +
                    " FROM RESTAURANT WHERE Email='" + extras.getString("restaurant_email") + "';");
            customerPhone = extras.getString("customer_phone");
            customerAddr = extras.getString("customer_addr");
            EditText addrText = (EditText) findViewById(R.id.editTextAddr);
            addrText.setText(customerAddr);
            EditText phoneText = (EditText) findViewById(R.id.editTextPhone);
            phoneText.setText(customerPhone);
            foodMenu = new FoodMenu();
            foodMenu.readItem(restaurant.getEmail());
            setListAdapter(new ListViewAdapter(PlaceOrderActivity.this, foodMenu.getItems()));
        }
        Button placeOrder = (Button) findViewById(R.id.place_order);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // place order operations
                boolean validOrder = false;
                double totalPrice = 0;
                EditText addrText = (EditText) findViewById(R.id.editTextAddr);
                String addr = addrText.getText().toString();
                EditText phoneText = (EditText) findViewById(R.id.editTextPhone);
                String phone = phoneText.getText().toString();
                Spinner spinnerOnOff = (Spinner) findViewById(R.id.spinnerOnOff);
                for (Food item : foodMenu.getItems()) {
                    if (item.getQuantity() > 0) {
                        validOrder = true;
                        totalPrice += item.getQuantity() * Double.parseDouble(item.getPrice());
                    }
                }
                if (addr == null || addr.length() == 0 || phone == null || phone.length() == 0) {
                    validOrder = false;
                }
                if (!validOrder) {
                    Toast.makeText(PlaceOrderActivity.this, "Invalid order information.", Toast.LENGTH_SHORT).show();
                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    String orderTime = dateFormat.format(date);
                    String payment = spinnerOnOff.getSelectedItem().toString();
                    System.out.println("Order Time: " + orderTime);
                    FoodOrder foodOrder = new FoodOrder();
                    foodOrder.setTransition(PlaceOrderActivity.this);
                    foodOrder.createOrder(customerEmail, restaurant.getEmail(), orderTime,
                            phone, addr, payment, totalPrice, foodMenu.getItems());
                }
            }
        });
    }

    @Override
    public void transit() {
        Intent intent = new Intent(getApplicationContext(), CustomerActivity.class);
        intent.putExtra("customer_email", customerEmail);
        startActivity(intent);
    }

    @Override
    public void setToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    private class ListViewAdapter extends ArrayAdapter<Food> {

        public ListViewAdapter(Context context, List<Food> items) {
            super(context, R.layout.customerui_placeorderitem, items);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            final Food item = getItem(position);
            view = inflater.inflate(R.layout.customerui_placeorderitem, parent, false);
            TextView idView = (TextView) view.findViewById(R.id.menuidTextView);
            TextView nameView = (TextView) view.findViewById(R.id.menuNameTextView);
            ImageView picView = (ImageView) view.findViewById(R.id.menuPicView);
            TextView priceView = (TextView) view.findViewById(R.id.priceTextView);
            Spinner spinner = (Spinner) view.findViewById(R.id.quantityEdit);

            idView.setText(String.valueOf(position + 1));
            nameView.setText(item.getName());
            picView.setImageBitmap(item.getPicture());
            priceView.setText(String.valueOf(item.getPrice()));
            spinner.setSelection(item.getQuantity());

            spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    item.setQuantity(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    setSelection(0);
                }

            });

            return view;
        }
    }

    /**
     * AsyncTask to get restaurant information.
     */
    private class GetInfoTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... sql) {
            return search(sql[0]);
        }
        @Override
        protected void onPostExecute(String sqlResult) {
            String[] results = sqlResult.split("\n");
            for (String result : results) {
                String[] elements = result.split("\t");
                if (elements.length > 2) {
                    TextView resName = (TextView) findViewById(R.id.resNameTextView);
                    TextView resAddr = (TextView) findViewById(R.id.resAddrTextView);
                    TextView resPhone = (TextView) findViewById(R.id.resPhoneTextView);
                    resName.setText(elements[0]);
                    resAddr.setText(elements[1]);
                    resPhone.setText(elements[2]);
                }
            }
        }

        /**
         * Get the data from server.
         * @param sql query
         * @return query results
         */
        private String search(String sql) {
            String results = "";
            try {
                results = Client.readData(sql);
            } catch (ConnectionException e) {
                e.printStackTrace();
            }
            return results;
        }
    }
}
