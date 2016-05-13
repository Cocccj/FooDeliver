package jiaqiz.cmu.edu.foodeliver.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import jiaqiz.cmu.edu.foodeliver.exception.ConnectionException;

/**
 * Remote Client Class.
 * @author Shuhui Yang
 */
public class Client {

    // Server URL
    private static String SERVER_URL = "10.0.2.2:8080/Foodeliver/ServerServlet";

    /**
     * Send request to create data record.
     * @return true if success, false otherwise
     * @throws ConnectionException connection exception
     */
    public static String createData(String sql) throws ConnectionException {
        sql = sql.replaceAll(" ", "%20");
        sql = sql.replaceAll("'", "%27");
        String url = "http://" + SERVER_URL + "?sql=" + sql + "&method=create";
        URL requestUrl = null;
        HttpURLConnection con = null;
        try {
            requestUrl = new URL(url);
            con = (HttpURLConnection) requestUrl.openConnection();
            // if create successful
            if (con.getResponseCode() == 200){
                // retrieve response and judge
                BufferedReader read = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String input = read.readLine().trim();
                if (input.equals("False")) {
                    return "false";
                }
                return "true";
            } else {
                throw new ConnectionException(1);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "false";
    }

    /**
     * Send request to read data record.
     * @return true if success, false otherwise
     * @throws ConnectionException connection exception
     */
    public static String readData(String sql) throws ConnectionException {
        sql = sql.replaceAll(" ", "%20");
        String url = "http://" + SERVER_URL + "?sql=" + sql + "&method=read";
        URL requestUrl = null;
        HttpURLConnection con = null;
        StringBuilder sb = new StringBuilder();
        try {
            requestUrl = new URL(url);
            con = (HttpURLConnection) requestUrl.openConnection();
            // if create successful
            if (con.getResponseCode() == 200){
                // retrieve response
                BufferedReader read = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String input = null;
                while ((input = read.readLine()) != null){
                    sb.append(input).append("\n");
                }
            } else {
                throw new ConnectionException(1);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * Update data.
     * @param tablename table name
     * @param values values
     * @return true if success, false otherwise
     * @throws ConnectionException connection exception
     */
    public static boolean updateData(String tablename, HashMap<String, String> values)
            throws ConnectionException {
        // form parameter including tablename and values
        StringBuilder parameter = new StringBuilder();
        parameter.append("tablename").append("=").append(tablename);
        for (String key: values.keySet()){
            parameter.append("&").append(key).append("=").append(values.get(key));
        }
        parameter.append("&").append("method").append("=").append("update");
        // form GET request url and open connection
        String url = "http://" + SERVER_URL + "?" + parameter.toString();
        URL requestUrl = null;
        HttpURLConnection con = null;
        try {
            requestUrl = new URL(url);
            con = (HttpURLConnection) requestUrl.openConnection();
            // if create successful
            if (con.getResponseCode() == 200){
                // retrieve response and judge
                return true;
            } else {
                throw new ConnectionException(1);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete data from the database.
     * @param tablename table name
     * @param conditions conditions
     * @return true if success, false otherwise
     * @throws ConnectionException connection exception
     */
    public static boolean deleteData(String tablename, HashMap<String, String> conditions)
            throws ConnectionException {
        // form parameter including tablename and values
        StringBuilder parameter = new StringBuilder();
        parameter.append("tablename").append("=").append(tablename);
        for (String key: conditions.keySet()){
            parameter.append("&").append(key).append("=").append(conditions.get(key));
        }
        parameter.append("&").append("method").append("=").append("delete");
        // form GET request url and open connection
        String url = "http://" + SERVER_URL + "?" + parameter.toString();
        URL requestUrl = null;
        HttpURLConnection con = null;
        try {
            requestUrl = new URL(url);
            con = (HttpURLConnection) requestUrl.openConnection();
            // if create successful
            if (con.getResponseCode() == 200){
                // retrieve response and judge
                return true;
            } else {
                throw new ConnectionException(1);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}