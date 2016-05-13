package jiaqiz.cmu.edu.foodeliver.remote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import jiaqiz.cmu.edu.foodeliver.utility.PictureSearch;

/**
 * Picture Retrieving Tasks.
 * @author Jiaqi Zhang
 */
public class GetPicture {

    /**
     * PictureSearch object.
     */
    PictureSearch ps = null;

    /**
     * Retrieve the picture.
     * @param searchUrls picture urls.
     * @param ps PictureSearch object
     */
    public void search(ArrayList<String> searchUrls, PictureSearch ps) {
        this.ps = ps;
        new AsyncPicSearch().execute(searchUrls);
    }

    /**
     * AsyncTask to search the pictures.
     */
    private class AsyncPicSearch extends AsyncTask<ArrayList<String>, Void, ArrayList<Bitmap>> {
        @Override
        protected ArrayList<Bitmap> doInBackground(ArrayList<String>... urls) {
            System.out.println(urls[0]);
            return search(urls[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> pictures) {
            ps.pictureReady(pictures);
        }

        /**
         * Search the pictures.
         * @param searchUrls picture urls
         * @return pictures
         */
        private ArrayList<Bitmap> search(ArrayList<String> searchUrls) {
            try {
                ArrayList<Bitmap> results = new ArrayList<Bitmap>();
                for (String s : searchUrls) {
                    URL u = new URL(s);
                    results.add(getImage(u));
                }
                return results;
            } catch (Exception e) {
                e.printStackTrace();
                return null; // so compiler does not complain
            }
        }

        /**
         * Get the picture Bitmap.
         * @param url picture url
         * @return picture Bitmap
         */
        private Bitmap getImage(final URL url) {
            try {
                URLConnection con = url.openConnection();
                con.connect();
                BufferedInputStream b = new BufferedInputStream(con.getInputStream());
                Bitmap bm = BitmapFactory.decodeStream(b);
                b.close();
                return bm;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
