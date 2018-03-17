package com.example.ddk.benefits;

import com.google.firebase.database.*;
import com.google.firebase.database.DatabaseReference;

public class User {

    public String user_id;
    public String email;
    public String password;
    public String name;
    public String birthday;
    public String gender;
    public String weight;
    private DatabaseReference mDatabase;
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(com.example.ddk.benefits.User.class)
    }

    public User(String email, String password, String usr_name, String birthday, String gender, String weight) {
        this.email = email;
        this.password = password;
        this.name = usr_name;
        this.birthday = birthday;
        this.gender = gender;
        this.weight = weight;
        this.mDatabase = FirebaseDatabase.getInstance().getReference();

    }
    public User writeNewUser(String email,String password,String usr_name,String birthday, String gender, String weight) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("user");
        String userId = mDatabase.push().getKey();
        User new_user = new User(email,password, usr_name,birthday, gender,weight);
        mDatabase.child(userId).setValue(new_user);
        return new_user;
}
    public String getPassword(){
        return this.password;
    }


}


































