package jiaqiz.cmu.edu.foodeliver.restaurantui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import jiaqiz.cmu.edu.foodeliver.R;
import jiaqiz.cmu.edu.foodeliver.customerui.CustomerActivity;
import jiaqiz.cmu.edu.foodeliver.entities.Restaurant;
import jiaqiz.cmu.edu.foodeliver.exception.ConnectionException;
import jiaqiz.cmu.edu.foodeliver.remote.Client;
import jiaqiz.cmu.edu.foodeliver.utility.ShowToast;

/**
 * Restaurant Profile Fragment.
 * @author Shuhui Yang
 */
public class RestaurantProfileFragment extends Fragment implements ShowToast{

    private Restaurant restaurant;

    private EditText passwordText;
    private EditText categoryText;
    private EditText phoneText;
    private EditText cardText;
    private EditText addressText;
    private EditText locationText;
    private EditText pictureURLText;

    private TextView nameTextView;
    private TextView accountTextView;

    private ImageView picture;

    public RestaurantProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RestaurantProfileFragment.
     */
    public static RestaurantProfileFragment newInstance() {
        RestaurantProfileFragment fragment = new RestaurantProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RestaurantActivity rActivity = (RestaurantActivity) getActivity();
        restaurant = rActivity.getRestaurant();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.restaurantui_fragment_profile, container, false);

        // Retrieve info
        RestaurantActivity rActivity = (RestaurantActivity) getActivity();
        restaurant = rActivity.getRestaurant();

        // Text View
        accountTextView = (TextView) view.findViewById(R.id.resId);
        nameTextView = (TextView) view.findViewById(R.id.resName);
        accountTextView.setText(restaurant.getEmail());
        nameTextView.setText(restaurant.getName());
        picture = (ImageView) view.findViewById(R.id.res_picture);
        picture.setImageBitmap(restaurant.getPicture());

        // Edit View
        passwordText = (EditText) view.findViewById(R.id.resPassword);
        categoryText = (EditText) view.findViewById(R.id.resCategory);
        phoneText = (EditText) view.findViewById(R.id.resPhone);
        cardText = (EditText) view.findViewById(R.id.resCard);
        addressText = (EditText) view.findViewById(R.id.resAddress);
        locationText = (EditText) view.findViewById(R.id.resLocation);
        pictureURLText = (EditText) view.findViewById(R.id.pic_url);

        // Set Edit View
        passwordText.setText(restaurant.getPassword());
        categoryText.setText(restaurant.getCategory());
        phoneText.setText(restaurant.getPhone());
        cardText.setText(restaurant.getCard());
        addressText.setText(restaurant.getAddress());
        locationText.setText(restaurant.getLocation());
        pictureURLText.setText(restaurant.getPictureURL());

        // Set Edit View Enable
        passwordText.setEnabled(false);
        categoryText.setEnabled(false);
        phoneText.setEnabled(false);
        cardText.setEnabled(false);
        addressText.setEnabled(false);
        locationText.setEnabled(false);
        pictureURLText.setEnabled(false);

        // Edit Button
        Button editRestaurant = (Button) view.findViewById(R.id.editButton);
        editRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // edit restaurant
                passwordText.setEnabled(true);
                categoryText.setEnabled(true);
                phoneText.setEnabled(true);
                cardText.setEnabled(true);
                addressText.setEnabled(true);
                locationText.setEnabled(true);
                pictureURLText.setEnabled(true);
            }
        });

        // Save Button
        Button saveRestaurant = (Button) view.findViewById(R.id.saveButton);
        saveRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // params: Email, Name, Phone, Password, CardNumber, Address, Category, Location, Picture
                String[] params = new String[9];
                params[0] = restaurant.getEmail();
                params[1] = restaurant.getName();
                params[2] = phoneText.getText().toString().trim();
                params[3] = passwordText.getText().toString().trim();
                params[4] = cardText.getText().toString().trim();
                params[5] = addressText.getText().toString().trim();
                params[6] = categoryText.getText().toString().trim();
                params[7] = locationText.getText().toString().trim();
                params[8] = pictureURLText.getText().toString().trim();

                restaurant.updateProfile(params);
                restaurant.setShowToast(RestaurantProfileFragment.this);

                // Set Edit View Enable
                passwordText.setEnabled(false);
                categoryText.setEnabled(false);
                phoneText.setEnabled(false);
                cardText.setEnabled(false);
                addressText.setEnabled(false);
                locationText.setEnabled(false);
                pictureURLText.setEnabled(false);
            }
        });
        return view;
    }

    @Override
    public void setToast(String str) {
        Toast.makeText(getActivity(),
                str, Toast.LENGTH_LONG).show();
    }

    private class UpdateRestaurant extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... sql) {
            return create(sql[0]);
        }
        protected void onPostExecute(String results) {
            if (results.equals("true")) {
                Toast.makeText(getActivity(),
                        "New profile saved successfully.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(),
                        "Sorry, save failed.", Toast.LENGTH_LONG).show();
            }
        }
        private String create(String sql) {
            String results = "";
            try {
                results = Client.createData(sql);
            } catch (ConnectionException e) {
                e.printStackTrace();
            }
            return results;
        }
    }
}