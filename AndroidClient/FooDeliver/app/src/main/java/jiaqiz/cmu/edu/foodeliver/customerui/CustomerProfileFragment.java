package jiaqiz.cmu.edu.foodeliver.customerui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import jiaqiz.cmu.edu.foodeliver.R;
import jiaqiz.cmu.edu.foodeliver.entities.Customer;
import jiaqiz.cmu.edu.foodeliver.utility.ShowToast;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomerProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 * @author Jiaqi Zhang
 */
public class CustomerProfileFragment extends Fragment implements ShowToast {

    private Customer customer;
    private TextView emailText;
    private EditText passwordText;
    private EditText cPasswordText;
    private EditText phoneText;
    private EditText cardText;
    private EditText addrText;

    public CustomerProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CustomerProfileFragment.
     */
    public static CustomerProfileFragment newInstance() {
        CustomerProfileFragment fragment = new CustomerProfileFragment();
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

        CustomerActivity cActivity = (CustomerActivity) getActivity();
        customer = cActivity.getCustomer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.customerui_fragment_profile, container, false);
        Button editProfile = (Button) view.findViewById(R.id.customer_edit_button);
        CustomerActivity cActivity = (CustomerActivity) getActivity();

        customer = cActivity.getCustomer();
        emailText = (TextView) view.findViewById(R.id.register_email_text);
        passwordText = (EditText) view.findViewById(R.id.register_password_text);
        cPasswordText = (EditText) view.findViewById(R.id.register_confirm_password_text);
        cardText = (EditText) view.findViewById(R.id.register_card_text);
        addrText = (EditText) view.findViewById(R.id.register_address_text);
        phoneText = (EditText) view.findViewById(R.id.register_phone_text);

        emailText.setText(customer.getEmail());
        phoneText.setText(customer.getPhone());
        passwordText.setText(customer.getPassword());
        cPasswordText.setText(customer.getPassword());
        cardText.setText(customer.getCard());
        addrText.setText(customer.getAddress());

        passwordText.setEnabled(false);
        cPasswordText.setEnabled(false);
        cardText.setEnabled(false);
        addrText.setEnabled(false);
        phoneText.setEnabled(false);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // enable edit customer profile
                passwordText.setEnabled(true);
                cPasswordText.setEnabled(true);
                cardText.setEnabled(true);
                addrText.setEnabled(true);
                phoneText.setEnabled(true);
            }
        });

        Button saveProfile = (Button) view.findViewById(R.id.customer_save_button);
        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // save customer profile
                if (!passwordText.getText().toString().trim().
                        equals(cPasswordText.getText().toString().trim())) {
                    setToast("Confirmed password is not consistent.");
                    passwordText.setText(customer.getPassword());
                    cPasswordText.setText(customer.getPassword());
                } else {
                    String[] params = new String[4];
                    params[0] = phoneText.getText().toString().trim();
                    params[1] = passwordText.getText().toString().trim();
                    params[2] = cardText.getText().toString().trim();
                    params[3] = addrText.getText().toString().trim();
                    customer.setShowToast(CustomerProfileFragment.this);
                    customer.updateProfile(params);
                }
                passwordText.setEnabled(false);
                cPasswordText.setEnabled(false);
                cardText.setEnabled(false);
                addrText.setEnabled(false);
                phoneText.setEnabled(false);
            }
        });
        return view;
    }

    @Override
    public void setToast(String str) {
        Toast.makeText(getActivity(),
                str, Toast.LENGTH_LONG).show();
    }
}
