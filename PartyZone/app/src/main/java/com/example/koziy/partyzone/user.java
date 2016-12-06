package com.example.koziy.partyzone;

/**
 * Created by mattk on 11/30/2015.
 */
public class user {

    private String first;
    private String last;
    private long id;
    private String pw;
    private String uname;


    public user(Long id, String first, String last, String uname, String pw){
        this.id = id;
        this.first = first;
        this.last = last;
        this.uname = uname;
        this.pw = pw;
    }

    public user(){

    }

    public void setUname(String name){
        this.uname =name;
    }
    public String getUname(){
        return uname;
    }
    public void setId(long id){
        this.id = id;
    }
    public long getId(){
        return id;
    }
    public void setFirst(String first){
        this.first = first;
    }
    public String getFirst(){
        return first;
    }
    public void setLast(String last){
        this.last = last;
    }
    public String getLast(){
        return last;
    }

    public String getPw(){
        return pw;
    }
    public void setPw(String pw){
        this.pw = pw;
    }

}
