package jiaqiz.cmu.edu.foodeliver.customerui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jiaqiz.cmu.edu.foodeliver.R;
import jiaqiz.cmu.edu.foodeliver.entities.Restaurant;
import jiaqiz.cmu.edu.foodeliver.remote.DBTask;
import jiaqiz.cmu.edu.foodeliver.remote.GetPicture;
import jiaqiz.cmu.edu.foodeliver.utility.DataSearch;
import jiaqiz.cmu.edu.foodeliver.utility.PictureSearch;

/**
 * Search restaurant fragment.
 * @author Jiaqi Zhang
 */
public class SearchFragment extends ListFragment
        implements OnItemClickListener, PictureSearch, DataSearch {

    /**
     * List view.
     */
    private ListView searchListView;

    /**
     * Items in the list view.
     */
    private List<Restaurant> items;

    /**
     * Custoemr Email.
     */
    private String customerEmail = "";

    /**
     * Initial location.
     */
    private String initialLoc = "";

    /**
     * Search term.
     */
    private EditText searchTerm;

    /**
     * Category spinner.
     */
    private Spinner spinnerCategory;

    /**
     * Location spinner.
     */
    private Spinner spinnerLocation;

    /**
     * Location areas.
     */
    private static final String[] LOCATIONS = new String[]{"All", "ShadySide",
            "Squirrel Hill", "North Oakland", "South Oakland"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customerui_fragment_search, container, false);
        Button search = (Button) view.findViewById(R.id.search_submit);
        searchTerm = (EditText) view.findViewById(R.id.editTextSearch);
        spinnerCategory = (Spinner) view.findViewById(R.id.spinnerCategory);
        spinnerLocation = (Spinner) view.findViewById(R.id.spinnerLocation);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // search restaurants
                String nameSearch = searchTerm.getText().toString().trim();
                String category = (String) spinnerCategory.getSelectedItem();
                String location = (String) spinnerLocation.getSelectedItem();
                StringBuilder sql = new StringBuilder("SELECT Email, Name, Category, Location, Picture FROM RESTAURANT");
                int count = 0;
                if (nameSearch.length() != 0 || !category.equals("All") || !location.equals("All")) {
                    sql.append(" WHERE");
                    if (nameSearch.length() != 0) {
                        sql.append(" Name='").append(nameSearch).append("'");
                        count++;
                    }
                    if (!category.equals("All")) {
                        if (count > 0) {
                            sql.append(" AND");
                        }
                        sql.append(" Category='").append(category).append("'");
                        count++;
                    }
                    if (!location.equals("All")) {
                        if (count > 0) {
                            sql.append(" AND");
                        }
                        sql.append(" Location='").append(location).append("'");
                        count++;
                    }
                }
                sql.append(";");
                DBTask dbT = new DBTask();
                ArrayList<String> list = new ArrayList<String>();
                list.add(sql.toString());
                dbT.getResult(list, SearchFragment.this, "result");
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchListView = getListView();
        CustomerActivity cActivity = (CustomerActivity) getActivity();
        customerEmail = cActivity.getCustomerEmail();
        initialLoc = cActivity.getInitialLoc();
        items = new ArrayList<Restaurant>();

        int index = -1;
        for (int i = 0; initialLoc != null && i < LOCATIONS.length; i++) {
            if(initialLoc.equals(LOCATIONS[i])) {
                index = i;
            }
        }
        DBTask dbT = new DBTask();
        if (index <= 0) {
            ArrayList<String> list = new ArrayList<String>();
            list.add("SELECT Email, Name, Category, Location, Picture FROM RESTAURANT;");
            dbT.getResult(list, SearchFragment.this, "result");
        } else {
            spinnerLocation.setSelection(index);
            ArrayList<String> list = new ArrayList<String>();
            list.add("SELECT Email, Name, Category, Location, Picture" +
                    " FROM RESTAURANT WHERE Location='" + initialLoc + "';");
            dbT.getResult(list, SearchFragment.this, "result");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
    }

    @Override
    public void pictureReady(ArrayList<Bitmap> pictures) {
        for (int i = 0; i < pictures.size() && i < items.size(); i++) {
            items.get(i).setPicture(pictures.get(i));
        }
        setListAdapter(new ListViewAdapter(getActivity(), items));

        searchListView.setOnItemClickListener(SearchFragment.this);
    }

    @Override
    public void dbResultReady(String result) {
        items.clear();
        ArrayList<String> searchPics = new ArrayList<String>();
        String[] results = result.split("\n");
        for (String r : results) {
            String[] elements = r.split("\t");
            if (elements.length > 4) {
                items.add(new Restaurant(elements[0], elements[1], elements[2], elements[3], elements[4]));
                searchPics.add(elements[4]);
            }
        }

        GetPicture gp = new GetPicture();
        gp.search(searchPics, SearchFragment.this);
    }

    @Override
    public void updateReady(String result) {}

    @Override
    public void dbDetailReady(String result) {}

    @Override
    public void deleteReady(String result) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity().getApplicationContext(), PlaceOrderActivity.class);
        CustomerActivity cActivity = (CustomerActivity) getActivity();
        intent.putExtra("customer_email", customerEmail);
        intent.putExtra("restaurant_email", items.get(position).getEmail());
        intent.putExtra("customer_phone", cActivity.getCustomerPhone());
        intent.putExtra("customer_addr", cActivity.getCustomerAddr());
        startActivity(intent);
    }

    /**
     * List View Adapter.
     * @author Jiaqi Zhang
     */
    private class ListViewAdapter extends ArrayAdapter<Restaurant> {

        /**
         * Constructor.
         * @param context context
         * @param items list items
         */
        public ListViewAdapter(Context context, List<Restaurant> items) {
            super(context, R.layout.customerui_fragment_searchitem, items);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.customerui_fragment_searchitem, parent, false);
            TextView name = (TextView) view.findViewById(R.id.resNameTextView);
            ImageView pic = (ImageView) view.findViewById(R.id.resPicView);
            TextView category = (TextView) view.findViewById(R.id.categoryTextView);
            TextView location = (TextView) view.findViewById(R.id.locTextView);

            Restaurant item = getItem(position);
            name.setText(item.getName());
            pic.setImageBitmap(item.getPicture());
            category.setText(item.getCategory());
            location.setText(item.getLocation());

            return view;
        }
    }
}
