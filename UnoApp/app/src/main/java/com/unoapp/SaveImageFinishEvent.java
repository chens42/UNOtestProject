package com.unoapp;


import java.util.List;

public class SaveImageFinishEvent {
    private List<DetailObject> detailObjects;

    public SaveImageFinishEvent(List<DetailObject> detailObjects) {
        this.detailObjects = detailObjects;
    }

    public List<DetailObject> getDetailObjects() {
        return detailObjects;
    }
}
