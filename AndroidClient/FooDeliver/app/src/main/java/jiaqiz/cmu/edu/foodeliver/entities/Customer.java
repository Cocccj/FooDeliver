package jiaqiz.cmu.edu.foodeliver.entities;

import java.util.ArrayList;

import jiaqiz.cmu.edu.foodeliver.utility.DataSearch;
import jiaqiz.cmu.edu.foodeliver.remote.DBTask;

/**
 * Customer Class.
 * @author Yu Qiu
 */
public class Customer extends User implements DataSearch {

    boolean toCreate = false;

    @Override
    public void createProfile(String[] params) {
        this.email = params[0];
        this.phone = params[1];
        this.password = params[2];
        this.card = params[3];
        this.address = params[4];
        String sql = "INSERT INTO CONSUMER VALUES ('" + email + "','"
                + phone + "','" + password + "','"
                + card + "','" + address + "')";
        toCreate = true;
        DBTask dbT = new DBTask();
        ArrayList<String> list = new ArrayList<String>();
        list.add(sql);
        dbT.getResult(list, this, "update");
    }

    @Override
    public void readProfile(String email) {
        this.email = email;
        DBTask dbT = new DBTask();
        ArrayList<String> list = new ArrayList<String>();
        list.add("SELECT Email, Password, CardNumber, Address, Phone" +
                " FROM CONSUMER WHERE Email='" + email + "';");
        dbT.getResult(list, this, "result");
    }

    @Override
    public void updateProfile(String[] params) {
        if (params.length > 3) {
            this.phone = params[0];
            this.password = params[1];
            this.card = params[2];
            this.address = params[3];
            toCreate = false;
            DBTask dbT = new DBTask();
            ArrayList<String> list = new ArrayList<String>();
            list.add("UPDATE CONSUMER SET Password='" + password
                    + "', Phone='" + phone + "', CardNumber='" + card + "', Address='"
                    + address + "' WHERE Email='" + email + "';");
            dbT.getResult(list, this, "update");
        }
    }

    @Override
    public void deleteProfile(String email) {
    }

    public void dbResultReady(String result) {
        System.out.println("SQL Result: " + result);
        String[] elements = result.split("\t");
        password = elements[1];
        card = elements[2];
        address = elements[3];
        phone = elements[4];
    }

    public void dbDetailReady(String result) {}

    @Override
    public void deleteReady(String result) {

    }


    public void updateReady(String result) {
        if (result.equals("true")) {
            if (toCreate) {
                transition.setToast("New profile created.");
                transition.transit();
            } else {
                showToast.setToast("New profile saved successfully.");
            }
        } else {
            if (toCreate) {
                transition.setToast("Sorry, the account already exists.");
            } else {
                showToast.setToast("Sorry, save failed.");
            }
        }
    }
}
