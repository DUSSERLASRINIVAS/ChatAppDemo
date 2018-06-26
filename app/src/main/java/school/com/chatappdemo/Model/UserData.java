package school.com.chatappdemo.Model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by hp on 24-06-2018.
 */
@IgnoreExtraProperties
public class UserData {


    String name;
    String email;
    String phone;
    String image;

    public UserData(String name, String email, String phone, String image) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.image = image;
    }

    public UserData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }
}
