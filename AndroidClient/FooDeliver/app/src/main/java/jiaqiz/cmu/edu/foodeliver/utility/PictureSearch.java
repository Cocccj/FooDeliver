package jiaqiz.cmu.edu.foodeliver.utility;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Picture Search Interface.
 * @author Jiaqi Zhang
 */
public interface PictureSearch {

    /**
     * Operations when pictures are ready.
     * @param pictures pictures
     */
    void pictureReady(ArrayList<Bitmap> pictures);
}
