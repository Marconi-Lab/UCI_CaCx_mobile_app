package com.ug.air.uci_cacx.Models;

public class Image {
    
    String image_path, prediction, result;

    public Image(String image_path, String prediction, String result) {
        this.image_path = image_path;
        this.prediction = prediction;
        this.result = result;
    }

    public String getImage_path() {
        return image_path;
    }

    public String getPrediction() {
        return prediction;
    }

    public String getResult() {
        return result;
    }
}
