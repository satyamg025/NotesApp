package com.example.satyam.myapplication.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by satyam on 9/13/16.
 */
public class DETAILS {
    @SerializedName("USERNAME")
    @Expose
    private String username;

    @SerializedName("NAME")
    @Expose
    private String name;

    @SerializedName("BRANCH")
    @Expose
    private String branch;

    @SerializedName("SEM")
    @Expose
    private Integer sem;


    public String getUsername()
    {
        return username;
    }

    public String getName()
    {
        return name;
    }

    public String getBranch()
    {
        return branch;
    }

    public Integer getSem()
    {
        return sem;
    }
}









