package jiaqiz.cmu.edu.foodeliver.entities;

import jiaqiz.cmu.edu.foodeliver.utility.ShowToast;
import jiaqiz.cmu.edu.foodeliver.utility.Transition;

/**
 * Abstract Class User.
 * @author Yu Qiu
 */
public abstract class User {
    String email; // User email
    String phone; // User phone number
    String password; // User password
    String card; // User card number
    String address; // User address
    ShowToast showToast;
    Transition transition;

    abstract void createProfile(String[] params);
    /**
     * Read profile.
     * @param email email
     * @return profile information
     */
    abstract void readProfile(String email);

    abstract void updateProfile(String[] params);

    /**
     * Delete profile.
     * @param email email address as the key
     * @return true if delete successfully, false otherwise
     */
    abstract void deleteProfile(String email);

    // Get e-mail
    public String getEmail() {
        return email;
    }
    // Set e-mail
    public void setEmail(String email) {
        this.email = email;
    }
    // Get phone
    public String getPhone() {
        return phone;
    }
    // Set phone
    public void setPhone(String phone) {
        this.phone = phone;
    }
    // Get password
    public String getPassword() {
        return password;
    }
    // Set password
    public void setPassword(String password) {
        this.password = password;
    }
    // Get card
    public String getCard() {
        return card;
    }
    // Set card
    public void setCard(String card) {
        this.card = card;
    }
    // Get address
    public String getAddress() {
        return address;
    }
    // Set address
    public void setAddress(String address) {
        this.address = address;
    }

    public void setShowToast (ShowToast st) {
        showToast = st;
    }

    public void setTransition(Transition r) {
        transition = r;
    }
}
