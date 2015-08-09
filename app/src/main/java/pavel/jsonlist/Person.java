package pavel.jsonlist;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by pavel on 06.08.15.
 */
public class Person {
    private String _id;
    private boolean isActive;
    private String picture;
    private int age;
    private String name;
    private String gender;
    private String email;
    private String phone;
    private String address;
    private String registered;

    public Bitmap pictureBitmap;
    private boolean finished;

    public Bitmap getPictureBitmap() {
        return pictureBitmap;
    }

    public void setPictureBitmap(Bitmap pictureBitmap) {
        this.pictureBitmap = pictureBitmap;
    }




    public Person(){
        this._id="";
        this.isActive =false;
        this.picture =null;
        this.age =-1;
        this.name ="N\\A";
        this.gender ="N\\A";
        this.email ="N\\A";
        this.phone ="N\\A";
        this.address="N\\A";
        this.registered ="N\\A";

        this.pictureBitmap =null;
        this.finished=false;


    }

    public Person(String _id, boolean isActive, String picture, int age, String name,
                  String gender, String email, String phone, String address, String registered) {
        this();

        this._id = _id;
        this.isActive = isActive;
        this.picture = picture;
        this.age = age;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.registered = registered;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        return _id.equals(person._id);

    }

    @Override
    public int hashCode() {
        return _id.hashCode();
    }

    /*public void setPictureBitmap(Bitmap pictureBitmap) {
        this.pictureBitmap = pictureBitmap;
    }*/

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String get_id() {
        return _id;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public String getPicture() {
        return picture;
    }

    public Integer getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getRegistered() {
        return registered;
    }
}
