package jiaqiz.cmu.edu.foodeliver.remote;

import android.os.AsyncTask;

import java.util.ArrayList;

import jiaqiz.cmu.edu.foodeliver.exception.ConnectionException;
import jiaqiz.cmu.edu.foodeliver.utility.DataSearch;

/**
 * Database operation tasks.
 * @author Jiaqi Zhang
 */
public class DBTask {

    /**
     * DataSearch callback object.
     */
    DataSearch ds = null;

    /**
     * Operation type.
     */
    String type = "result";

    /**
     * Get the result.
     * @param sql query
     * @param ds DataSearch object
     * @param type operation type
     */
    public void getResult(ArrayList<String> sql, DataSearch ds, String type) {
        this.ds = ds;
        this.type = type;
        new GetResTask().execute(sql);
    }

    /**
     * AsyncTask to connect to the server for database operations.
     */
    private class GetResTask extends AsyncTask<ArrayList<String>, Void, String> {
        @Override
        protected String doInBackground(ArrayList<String>... sql) {
            return search(sql[0]);
        }
        @Override
        protected void onPostExecute(String sqlResult) {
            if (type.equals("result")) {
                ds.dbResultReady(sqlResult);
            } else if (type.equals("update")) {
                ds.updateReady(sqlResult);
            } else if (type.equals("delete")){
                ds.deleteReady(sqlResult);
            } else {
                ds.dbDetailReady(sqlResult);
            }
        }

        /**
         * Retrieve the results.
         * @param sql query or update
         * @return result
         */
        private String search(ArrayList<String> sql) {
            String results = "";
            try {
                if (type.equals("update") || type.equals("delete")) {
                    results = "true";
                    for (String s : sql) {
                        if(Client.createData(s).equals("False")) {
                            results = "false";
                        }
                    }
                } else {
                    results = Client.readData(sql.get(0));
                }
            } catch (ConnectionException e) {
                e.printStackTrace();
            }
            System.out.println("***"+results);
            return results;
        }
    }
}
