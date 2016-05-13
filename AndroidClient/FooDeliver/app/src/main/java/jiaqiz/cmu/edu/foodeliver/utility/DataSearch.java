package jiaqiz.cmu.edu.foodeliver.utility;

/**
 * Data search interface.
 * @author Jiaqi Zhang
 */
public interface DataSearch {

    /**
     * Operations when the information query result ready.
     * @param result query result
     */
    void dbResultReady(String result);

    /**
     * Operations when the update finished.
     * @param result successful or not
     */
    void updateReady(String result);

    /**
     * Operations when the detail query result ready.
     * @param result query result
     */
    void dbDetailReady(String result);

    /**
     * Operations when the delete finished.
     * @param result successful or not
     */
    void deleteReady(String result);
}
