package com.example.tenthskate.Clients.ModelClass;

public class OfferZoneModel {

    private String offerName, offerImage;

    public OfferZoneModel() {
    }

    public OfferZoneModel(String offerName, String offerImage) {

        if (offerName.trim().equals("")){
            offerName = "Special offer!\nGrab this now";
        }

        this.offerName = offerName;
        this.offerImage = offerImage;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getOfferImage() {
        return offerImage;
    }

    public void setOfferImage(String offerImage) {
        this.offerImage = offerImage;
    }
}
