package com.example.sipmobileapp.model;

public class AttachParameter {

    private int imageTypeID;
    private int AttachTypeID;
    private int sickID;
    private String Description;
    private String Image;

    public int getImageTypeID() {
        return imageTypeID;
    }

    public void setImageTypeID(int imageTypeID) {
        this.imageTypeID = imageTypeID;
    }

    public int getAttachTypeID() {
        return AttachTypeID;
    }

    public void setAttachTypeID(int attachTypeID) {
        AttachTypeID = attachTypeID;
    }

    public int getSickID() {
        return sickID;
    }

    public void setSickID(int sickID) {
        this.sickID = sickID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
