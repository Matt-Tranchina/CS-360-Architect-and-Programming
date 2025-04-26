package com.zybooks.matt_tranchina_project_two_weight_app.ui.profile;


public class ProfileDataModel {

    int id;
    String user_username;
    String user_password;
    String user_firstName;
    String user_lastName;
    String user_email;
    String user_phonenumber;
    String user_height;
    String user_start_weight;
    String user_goal_weight;
    String user_age;
    String user_gender;

    public ProfileDataModel(){
        super();
    }

    public ProfileDataModel(int i, String username, String password, String firstName,
                            String lastName, String email, String phonenumber, String height,
                            String startWeight,String goalWeight, String age, String gender){
        super();
        this.id = i;
        this.user_username = username;
        this.user_password = password;
        this.user_firstName = firstName;
        this.user_lastName = lastName;
        this.user_email = email;
        this.user_phonenumber = phonenumber;
        this.user_height = height;
        this.user_start_weight = startWeight;
        this.user_goal_weight = goalWeight;
        this.user_age = age;
        this.user_gender = gender;
    }

    // Constructor for user profile
    public ProfileDataModel(String username, String password, String firstName, String lastName,
                            String email, String phonenumber, String height, String startWeight,
                            String goalWeight, String age, String gender){
        this.user_username = username;
        this.user_password = password;
        this.user_firstName = firstName;
        this.user_lastName = lastName;
        this.user_email = email;
        this.user_phonenumber = phonenumber;
        this.user_height = height;
        this.user_start_weight = startWeight;
        this.user_goal_weight = goalWeight;
        this.user_age = age;
        this.user_gender = gender;
    }
    public ProfileDataModel(String firstName, String lastName,
                            String email, String phonenumber, String height, String startWeight,
                            String goalWeight, String age, String gender){
        this.user_firstName = firstName;
        this.user_lastName = lastName;
        this.user_email = email;
        this.user_phonenumber = phonenumber;
        this.user_height = height;
        this.user_start_weight = startWeight;
        this.user_goal_weight = goalWeight;
        this.user_age = age;
        this.user_gender = gender;
    }


    // Getters and Setters for user profile
    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getUser_username(){ return user_username;}

    public void setUser_username(String username){ this.user_username = username;}

    public String getUser_password() { return this.user_password;}

    public void setUser_password(String password) {this.user_password = password;}

    public String getUser_firstName(){
        return user_firstName;
    }

    public void setUser_firstName(String firstName){
        this.user_firstName = firstName;
    }

    public String getUser_lastName(){
        return user_lastName;
    }

    public void setUser_lastName(String lastName){
        this.user_lastName = lastName;
    }

    public String getUser_email(){
        return user_email;
    }

    public void setUser_email(String email){
        this.user_email = email;
    }

    public String getUser_phonenumber(){
        return user_phonenumber;
    }

    public void setUser_phonenumber(String number){
        this.user_phonenumber = number;
    }

    public String getUser_height(){
        return user_height;
    }

    public void setUser_height(String height){
        this.user_height = height;
    }

    public String getUser_start_weight(){
        return user_start_weight;
    }

    public void setUser_start_weight(String startWeight){
        this.user_start_weight = startWeight;
    }

    public String getUser_goal_weight(){ return this.user_goal_weight;}
    public void setUser_goal_weight(String goalWeight){ this.user_goal_weight = goalWeight;}

    public String getUser_age(){
        return user_age;
    }

    public void setUser_age(String age){
        this.user_age = age;
    }

    public String getUser_gender(){
        return user_gender;
    }

    public void setUser_gender(String gender){
        this.user_gender = gender;
    }


}
