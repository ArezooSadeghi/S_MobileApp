package com.example.sipmobileapp.eventbus;

public class UpdateEvent {
    private int oldAttachID;
    private int newAttachID;

    public UpdateEvent(int oldAttachID, int newAttachID) {
        this.oldAttachID = oldAttachID;
        this.newAttachID = newAttachID;
    }

    public int getOldAttachID() {
        return oldAttachID;
    }

    public void setOldAttachID(int oldAttachID) {
        this.oldAttachID = oldAttachID;
    }

    public int getNewAttachID() {
        return newAttachID;
    }

    public void setNewAttachID(int newAttachID) {
        this.newAttachID = newAttachID;
    }
}
