package jiaqiz.cmu.edu.foodeliver.customerui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import jiaqiz.cmu.edu.foodeliver.R;
import jiaqiz.cmu.edu.foodeliver.entities.Customer;

/**
 * Customer Activity.
 * @author Jiaqi Zhang
 */
public class CustomerActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    // Search Fragment
    private SearchFragment searchFragment;

    private MyorderFragment myorderFragment;

    private CustomerProfileFragment customerProfileFragment;
    // Fragment Manager
    private FragmentManager fragmentManager;

    /**
     * Customer profile.
     */
    private Customer customer;

    /**
     * Initial location.
     */
    private String initialLoc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customerui_activity_customer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        customer = new Customer();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            customer.setEmail(extras.getString("customer_email"));
            System.out.println("Customer Email: " + customer.getEmail());
            initialLoc = extras.getString("location");
            System.out.println("Initial Location: " + initialLoc);
        }
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        fragmentManager = getSupportFragmentManager();
        mSectionsPagerAdapter = new SectionsPagerAdapter(fragmentManager);

        searchFragment = new SearchFragment();
        myorderFragment = new MyorderFragment();
        customerProfileFragment = new CustomerProfileFragment();

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.customer_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        customer.readProfile(customer.getEmail());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getCustomerEmail() {
        return customer.getEmail();
    }

    public String getCustomerPhone() {
        return customer.getPhone();
    }

    public String getCustomerAddr() {
        return customer.getAddress();
    }

    public String getPassword() {
        return customer.getPassword();
    }

    public String getCard() {
        return customer.getCard();
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getInitialLoc() {
        return initialLoc;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            if (position == 0) {
                return searchFragment;
            } else if (position == 1) {
                return myorderFragment;
            } else {
                return customerProfileFragment;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SEARCH RESTAURANT";
                case 1:
                    return "MY ORDERS";
                case 2:
                    return "MY PROFILE";
            }
            return null;
        }
    }
}
