package jiaqiz.cmu.edu.foodeliver.restaurantui;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import jiaqiz.cmu.edu.foodeliver.R;

import android.widget.TextView;
import android.widget.Toast;

import jiaqiz.cmu.edu.foodeliver.entities.Food;
import jiaqiz.cmu.edu.foodeliver.remote.DBTask;
import jiaqiz.cmu.edu.foodeliver.remote.GetPicture;
import jiaqiz.cmu.edu.foodeliver.utility.DataSearch;
import jiaqiz.cmu.edu.foodeliver.utility.PictureSearch;

/**
 * Menu Fragment.
 * @author Shuhui Yang
 */
public class MenuFragment extends ListFragment implements DataSearch, PictureSearch{

    /**
     * Items in the list view.
     */
    private List<Food> items;
    private ListView menuListView;
    private String restaurantEmail;

    private EditText new_name;
    private EditText new_price;
    private EditText new_picture;

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MenuFragment.
     */
    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.restaurantui_fragment_menu, container, false);
        new_name = (EditText)view.findViewById(R.id.new_name);
        new_price = (EditText)view.findViewById(R.id.new_price);
        new_picture = (EditText)view.findViewById(R.id.new_picture);
        // add button
        Button add = (Button) view.findViewById(R.id.add_button);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add menu
                String n_name = new_name.getText().toString().trim();
                String n_price = new_price.getText().toString().trim();
                String n_picture= new_picture.getText().toString().trim();
                if (n_name.equals("") || n_price.equals("") || n_picture.equals("")){
                    String show = "Please fill out all fields";
                    Toast.makeText(getActivity(), show.toString(), Toast.LENGTH_LONG).show();
                }else{
                    String sql1 = "INSERT INTO MENU VALUES ('"+restaurantEmail+n_name+"', '"
                            +restaurantEmail+"', '"+n_name+"', '"+n_price+"', '"+n_picture+"');";
                    ArrayList<String> sql_list1 = new ArrayList<String>();
                    sql_list1.add(sql1);
                    DBTask dbT1 = new DBTask();
                    dbT1.getResult(sql_list1, MenuFragment.this, "update");

                    items.clear();
                    String sql2 = "SELECT * FROM MENU WHERE RestaurantEmail='" + restaurantEmail +"';";
                    DBTask dbT2 = new DBTask();
                    ArrayList<String> sql_list2 = new ArrayList<String>();
                    sql_list2.add(sql2);
                    dbT2.getResult(sql_list2, MenuFragment.this, "result");

                    new_name.setText("");
                    new_price.setText("");
                    new_picture.setText("");
                }
            }
        });
        // delete button
        Button delete = (Button) view.findViewById(R.id.delete_button);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete menu
                StringBuilder sb = new StringBuilder("(");
                for (Food item: items){
                    if(item.isSeleted()){
                        sb.append("'").append(restaurantEmail+item.getName()).append("',");
                    }
                }
                sb.deleteCharAt(sb.length()-1).append(")");
                String sql3 = "DELETE FROM MENU WHERE FoodId IN "+sb.toString()+";";
                DBTask dbT3 = new DBTask();
                ArrayList<String> sql_list3 = new ArrayList<String>();
                sql_list3.add(sql3);
                dbT3.getResult(sql_list3, MenuFragment.this, "delete");

                items.clear();
                String sql2 = "SELECT * FROM MENU WHERE RestaurantEmail='" + restaurantEmail +"';";
                DBTask dbT2 = new DBTask();
                ArrayList<String> sql_list2 = new ArrayList<String>();
                sql_list2.add(sql2);
                dbT2.getResult(sql_list2, MenuFragment.this, "result");
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        menuListView = getListView();
        items = new ArrayList<Food>();

        RestaurantActivity rActivity = (RestaurantActivity) getActivity();
        restaurantEmail = rActivity.getRestaurant().getEmail();

        items.clear();
        String sql = "SELECT * FROM MENU WHERE RestaurantEmail='" + restaurantEmail +"';";
        DBTask dbT = new DBTask();
        ArrayList<String> list = new ArrayList<String>();
        list.add(sql);
        dbT.getResult(list, MenuFragment.this, "result");
    }

    @Override
    public void dbResultReady(String result) {
        System.out.println("SQL Result: " + result);
        ArrayList<String> searchPics = new ArrayList<String>();
        String[] foods = result.split("\n");
        for (String food: foods){
            String[] elements = food.split("\t");
            if (elements.length > 3){
                this.items.add(new Food(elements[0], elements[2], elements[3], elements[4]));
                searchPics.add(elements[4]);
            }
        }
        GetPicture gp = new GetPicture();
        gp.search(searchPics, MenuFragment.this);
    }

    @Override
    public void updateReady(String result) {
        String show = "";
        if (result.equals("true")){
            show = "Success: Add New Menu";
        } else {
            show = "Failure: Add New Menu";
        }
        Toast.makeText(getActivity(), show.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void dbDetailReady(String result) {

    }

    @Override
    public void deleteReady(String result) {
        String show = "";
        if (result.equals("true")){
            show = "Success: Delete Menu";
        } else {
            show = "Failure: Delete Menu";
        }
        Toast.makeText(getActivity(), show.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
        menuListView = getListView();
    }

    @Override
    public void onResume() {
        super.onResume();
        setListAdapter(new ListViewAdapter(getActivity(), items));
    }

    @Override
    public void pictureReady(ArrayList<Bitmap> pictures) {
        for (int i = 0; i < pictures.size(); i++){
            items.get(i).setPicture(pictures.get(i));
        }
        setListAdapter(new ListViewAdapter(getActivity(), items));
    }

    private class ListViewAdapter extends ArrayAdapter<Food> {

        public ListViewAdapter(Context context, List<Food> items) {
            super(context, R.layout.restaurantui_fragment_menuitem, items);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.restaurantui_fragment_menuitem, parent, false);

            CheckBox menuNumber = (CheckBox) view.findViewById(R.id.menuNumberBox);
            TextView name = (TextView) view.findViewById(R.id.menu_name);
            TextView price = (TextView) view.findViewById(R.id.m_price);
            ImageView picture = (ImageView) view.findViewById(R.id.menu_pic);

            final int p = position;

            Food item = getItem(position);
            menuNumber.setText(String.valueOf(position + 1));
            name.setText(item.getName());
            price.setText(item.getPrice());
            System.out.println("get_pict" + item.getPicture());
            picture.setImageBitmap(item.getPicture());

            menuNumber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        items.get(p).setSeleted(true);
                    } else {
                        items.get(p).setSeleted(false);
                    }
                }
            });
            return view;
        }
    }
}