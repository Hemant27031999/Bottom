package com.example.hemant.bottom;

public class Userinforetrieval {
   private String User_Name;
   private String User_Email;
   private String User_Phn;

    public Userinforetrieval(){

    }

    public Userinforetrieval(String user_Name, String user_Email, String user_Phn) {
        User_Name = user_Name;
        User_Email = user_Email;
        User_Phn = user_Phn;
    }

    public String getUser_Name() {
        if(User_Name == null){
            return "No Name";
        }
        else
        return User_Name;
    }

    public String getUser_Email() {
            if(User_Email==null){
                return "No E-mail";
            }
            else
        return User_Email;
    }

    public String getUser_Phn() {
        if(User_Phn==null){
            return "No Phone";
        }
        else
        return User_Phn;
    }
}
