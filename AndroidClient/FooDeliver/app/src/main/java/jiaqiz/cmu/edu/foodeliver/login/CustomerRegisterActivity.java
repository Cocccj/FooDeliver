package jiaqiz.cmu.edu.foodeliver.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import jiaqiz.cmu.edu.foodeliver.R;
import jiaqiz.cmu.edu.foodeliver.entities.Customer;
import jiaqiz.cmu.edu.foodeliver.utility.Transition;

/**
 * Customer Register Activity.
 * @author Yu Qiu
 */
public class CustomerRegisterActivity extends AppCompatActivity implements Transition {
    EditText registerEmailText;
    EditText registerPasswordText;
    EditText registerConfirmPasswordText;
    EditText registerCardText;
    EditText registerAddressText;
    EditText registerPhoneText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_customer_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button register = (Button) findViewById(R.id.register_submit_customer_button);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerEmailText = (EditText)findViewById(R.id.register_email_text);
                registerPasswordText = (EditText)findViewById(R.id.register_password_text);
                registerConfirmPasswordText = (EditText)findViewById(R.id.register_confirm_password_text);
                registerCardText = (EditText)findViewById(R.id.register_card_text);
                registerAddressText = (EditText)findViewById(R.id.register_address_text);
                registerPhoneText = (EditText)findViewById(R.id.register_phone_text);
                if (!Patterns.EMAIL_ADDRESS.matcher(registerEmailText.getText()).matches()) {
                    setToast("Please input a valid email address.");
                } else if (!registerPasswordText.getText().toString().trim().
                        equals(registerConfirmPasswordText.getText().toString().trim())) {
                    setToast("Confirmed password is not consistent.");
                } else {
                    String[] params = new String[5];
                    params[0] = registerEmailText.getText().toString();
                    params[1] = registerPhoneText.getText().toString();
                    params[2] = registerPasswordText.getText().toString();
                    params[3] = registerCardText.getText().toString();
                    params[4] = registerAddressText.getText().toString();
                    Customer c = new Customer();
                    c.setTransition(CustomerRegisterActivity.this);
                    c.createProfile(params);
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