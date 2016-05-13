package jiaqiz.cmu.edu.foodeliver.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import jiaqiz.cmu.edu.foodeliver.R;
import jiaqiz.cmu.edu.foodeliver.entities.Restaurant;
import jiaqiz.cmu.edu.foodeliver.utility.Transition;

/**
 * Restaurant Register Activity.
 * @author Yu Qiu
 */
public class RestaurantRegisterActivity extends AppCompatActivity implements Transition{
    EditText registerRestaurantNameText;
    EditText registerRestaurantEmailText;
    EditText registerRestaurantPhoneText;
    EditText registerRestaurantPasswordText;
    EditText registerRestaurantConfirmPasswordText;
    EditText registerRestaurantCardText;
    EditText registerRestaurantAddressText;
    Spinner spinnerLocation;
    Spinner spinnerCategory;
    EditText registerImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_restaurant_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button register = (Button) findViewById(R.id.register_restaurant_submit_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerRestaurantNameText = (EditText)findViewById(R.id.register_restaurant_name_text);
                registerRestaurantEmailText = (EditText)findViewById(R.id.register_restaurant_email_text);
                registerRestaurantPhoneText = (EditText)findViewById(R.id.register_restaurant_phone_text);
                registerRestaurantPasswordText = (EditText)findViewById(R.id.register_restaurant_password_text);
                registerRestaurantConfirmPasswordText = (EditText)findViewById(R.id.register_restaurant_confirm_password_text);
                registerRestaurantCardText = (EditText)findViewById(R.id.register_restaurant_card_text);
                registerRestaurantAddressText = (EditText)findViewById(R.id.register_restaurant_address_text);
                spinnerLocation = (Spinner)findViewById(R.id.spinner_location);
                spinnerCategory = (Spinner)findViewById(R.id.spinner_category);
                registerImageUrl = (EditText)findViewById(R.id.register_image_url);
                if (!Patterns.EMAIL_ADDRESS.matcher(registerRestaurantEmailText.getText()).matches()) {
                    setToast("Please input a valid email address.");
                } else if (!registerRestaurantPasswordText.getText().toString().trim().
                        equals(registerRestaurantConfirmPasswordText.getText().toString().trim())) {
                    setToast("Confirmed password is not consistent.");
                } else {
                    String[] params = new String[9];
                    params[0] = registerRestaurantEmailText.getText().toString();
                    params[1] = registerRestaurantNameText.getText().toString();
                    params[2] = registerRestaurantPhoneText.getText().toString();
                    params[3] = registerRestaurantPasswordText.getText().toString();
                    params[4] = registerRestaurantCardText.getText().toString();
                    params[5] = registerRestaurantAddressText.getText().toString();
                    params[6] = spinnerCategory.getSelectedItem().toString();
                    params[7] = spinnerLocation.getSelectedItem().toString();
                    params[8] = registerImageUrl.getText().toString();
                    Restaurant r = new Restaurant();
                    r.setTransition(RestaurantRegisterActivity.this);
                    r.createProfile(params);
                }
            }
        });
    }

    public void transit() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void setToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }
}
