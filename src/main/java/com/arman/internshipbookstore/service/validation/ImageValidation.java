package com.arman.internshipbookstore.service.validation;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageValidation {

    public static boolean isImageUrlValid(String imageUrl) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(imageUrl).openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(3000);
            connection.connect();
            return connection.getResponseCode() == 200;
        } catch (IOException e) {
            return false;
        }
    }
}
