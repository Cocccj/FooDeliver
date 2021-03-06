package jiaqiz.cmu.edu.foodeliver.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import jiaqiz.cmu.edu.foodeliver.R;
import jiaqiz.cmu.edu.foodeliver.customerui.CustomerActivity;
import jiaqiz.cmu.edu.foodeliver.exception.ConnectionException;
import jiaqiz.cmu.edu.foodeliver.remote.Client;
import jiaqiz.cmu.edu.foodeliver.restaurantui.RestaurantActivity;

/**
 * Log in Activity.
 * @author Yu Qiu
 */
public class LoginActivity extends AppCompatActivity {

    EditText emailTextView;
    EditText passwordTextView;
    LocationManager locationManager;
    Location location;
    private static final double shadysideLatitude = 40.4556477;
    private static final double shadysideLongitude = -79.9276651;
    private static final double squirrelHillLatitude = 40.4381246;
    private static final double squirrelHillLongitude = -79.9192175;
    private static final double northOaklandLatitude = 40.4466296;
    private static final double northOaklandLongitude = -79.9542438;
    private static final double southOaklandLatitude = 40.4319875;
    private static final double southOaklandLongitude = -79.9598383;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws SecurityException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},1);
        CustomLocationListener test = new CustomLocationListener();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, test);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        Button loginCustomerButton = (Button) findViewById(R.id.email_sign_in_customer_button);
        Button loginRestaurantButton = (Button) findViewById(R.id.email_sign_in_restaurant_button);
        Button customerReg = (Button) findViewById(R.id.email_register_customer_button);
        Button restaurantReg = (Button) findViewById(R.id.email_register_restaurant_button);

        emailTextView = (EditText)findViewById(R.id.email);
        passwordTextView = (EditText)findViewById(R.id.password);

        loginCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sql = "SELECT password FROM CONSUMER WHERE Email = '" + emailTextView.getText().toString() + "'";
                new GetPassword().execute(sql + ":Consumer");
            }
        });

        loginRestaurantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sql = "SELECT password FROM RESTAURANT WHERE Email = '" + emailTextView.getText().toString() + "'";
                new GetPassword().execute(sql + ":Restaurant");
            }
        });

        customerReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CustomerRegisterActivity.class);
                startActivity(intent);
            }
        });

        restaurantReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RestaurantRegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private class GetPassword extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... sql) {
            return search(sql[0]);
        }
        protected void onPostExecute(String sql) throws SecurityException {
            String[] results = sql.split(":");
            
            if (results[0].trim().equals(passwordTextView.getText().toString())) {
                
                if (results[1].trim().equals("Restaurant")) {
                    
                    Intent intent = new Intent(getApplicationContext(), RestaurantActivity.class);
                    intent.putExtra("restaurant_email", emailTextView.getText().toString());
                    startActivity(intent);
                } 
                
                else {
                    Intent intent = new Intent(getApplicationContext(), CustomerActivity.class);
                    intent.putExtra("customer_email", emailTextView.getText().toString());
                    intent.putExtra("location", getLocation());
                    startActivity(intent);
                }
            } 
            
            else {
                Toast.makeText(LoginActivity.this,
                        "Sorry, we could not find an account for this email address.", Toast.LENGTH_LONG).show();
            }
        }
        private String search(String sql) {
            
            String[] sqls = sql.split(":"); // Splits the results by a colon
            String results = ""; // Empty string of results initially
            
            try {
                results = Client.readData(sqls[0]);
            } 
            
            catch (ConnectionException e) {
                e.printStackTrace();
            }
            
            return results + ":" + sqls[1]; // Returns the search results
        }
    }

    public class CustomLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {}
        @Override
        public void onProviderDisabled(String provider) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

    public String getLocation() {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        String[] location = {"ShadySide", "Squirrel Hill", "North Oakland", "South Oakland"};
        double[] distance = new double[4];
        distance[0] = Math.pow((latitude - shadysideLatitude), 2) + Math.pow((longitude - shadysideLongitude), 2);
        distance[1] = Math.pow((latitude - squirrelHillLatitude), 2) + Math.pow((longitude - squirrelHillLongitude), 2);
        distance[2] = Math.pow((latitude - northOaklandLatitude), 2) + Math.pow((longitude - northOaklandLongitude), 2);
        distance[3] = Math.pow((latitude - southOaklandLatitude), 2) + Math.pow((longitude - southOaklandLongitude), 2);
        int choice = 0;
        double minDistance = distance[0];
        for (int i = 1; i < 4; i++) {
            
            if (distance[i] < minDistance) {
                choice = i;
            }
        }
        
        return location[choice];
    }
}
