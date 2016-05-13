package jiaqiz.cmu.edu.foodeliver.exception;

import android.util.Log;

/**
 * Connection Exception class.
 * @Author Jiaqi Zhang
 */
public class ConnectionException extends Exception {

    /**
     * Error Number.
     */
    private int errorNo;

    /**
     * Error Message.
     */
    private String errorMsg;

    /**
     * Error number set.
     */
    private static final int [] ERROR_NO_SET = new int [] {1};

    /**
     * Error message set.
     */
    private static final String [] ERROR_MSG_SET
            = new String [] {"Connection Error."};

    /**
     * Default serialize ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor with no argument.
     */
    public ConnectionException() {
        super();
        printEmptyException();
    }

    /**
     * Constructor.
     * @param errorNo error number
     */
    public ConnectionException(int errorNo) {
        super();
        int index = findErrorNo(errorNo);
        if (index != -1) {
            this.errorNo = errorNo;
        }
        errorMsg = ERROR_MSG_SET[index];
        printEmptyException();
    }

    /**
     * Constructor.
     * @param errorMsg error message
     */
    public ConnectionException(String errorMsg) {
        super();
        int index = findErrorMsg(errorMsg);
        if (index != -1) {
            this.errorMsg = errorMsg;
        }
        errorNo = ERROR_NO_SET[index];
        printEmptyException();
    }

    /**
     * Constructor.
     * @param errorNo error number
     * @param errorMsg error message
     */
    public ConnectionException(int errorNo, String errorMsg) {
        super();
        if (validateError(errorNo, errorMsg)) {
            this.errorNo = errorNo;
            this.errorMsg = errorMsg;
        }
        printEmptyException();
    }

    /**
     * Get error number.
     * @return error number
     */
    public int getErrorNo() {
        return errorNo;
    }

    /**
     * Set error number.
     * @param errorNo error number
     */
    public void setErrorno(int errorNo) {
        if (findErrorNo(errorNo) != -1) {
            this.errorNo = errorNo;
        }
    }

    /**
     * Get the error message.
     * @return error message
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * Set error message.
     * @param errorMsg error message
     */
    public void setErrormsg(String errorMsg) {
        if (findErrorMsg(errorMsg) != -1) {
            this.errorMsg = errorMsg;
        }
    }

    /**
     * Print Connection exception.
     */
    public void printEmptyException() {
        Log.e("Exception", "ConnectionException: [errorNo=" + errorNo
                + ", errorMsg=" + errorMsg);
    }

    /**
     * Fix the exception.
     * @param errorNo error number
     */
    public String fix(int errorNo) {
        StringBuilder errorMsg = new StringBuilder();
        switch (errorNo) {
            case 1:
                errorMsg.append("Exception: ").append(errorNo)
                        .append("\nPlease check your internect connection.");
                break;
            default: break;
        }
        return errorMsg.toString();
    }

    /**
     * Find the corresponding error number index.
     * @param no error number
     * @return index
     */
    private int findErrorNo(int no) {
        for(int i = 0; i < ERROR_NO_SET.length; i++) {
            if (no == ERROR_NO_SET[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Find the corresponding error message index.
     * @param msg error message
     * @return index
     */
    private int findErrorMsg(String msg) {
        for (int i = 0; i < ERROR_MSG_SET.length; i++) {
            if (msg.equals(ERROR_MSG_SET[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Validate the error information.
     * @param no error number
     * @param msg error message
     * @return true if valid, false otherwise
     */
    private boolean validateError(int no, String msg) {
        for (int i = 0; i < ERROR_NO_SET.length; i++) {
            if (ERROR_NO_SET[i] == no && ERROR_MSG_SET[i].equals(msg)) {
                return true;
            }
        }
        return false;
    }
}
