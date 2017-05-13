package com.example.satyam.myapplication;

/**
 * Created by satyam on 9/24/16.
 */
public interface UploadProgressListener {
    /**
     * This method updated how much data size uploaded to server
     * @param num
     */
    void transferred(long num);
}