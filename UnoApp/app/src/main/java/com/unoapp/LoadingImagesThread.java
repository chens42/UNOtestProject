package com.unoapp;

import android.content.Context;
import android.graphics.Bitmap;

import com.squareup.picasso.Picasso;
import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;

import java.io.IOException;
import java.util.List;

public class LoadingImagesThread extends Thread {
    private List<DetailObject> detailObjects;
    private Context context;
    private FileService fileService;
    private final Storage internalStorage;
    private String assetFolder = "images";


    public LoadingImagesThread(List<DetailObject> detailObjects, Context context) {
        this.detailObjects = detailObjects;
        this.context = context;
        internalStorage = SimpleStorage.getInternalStorage(context);
        fileService = new FileService(context);
    }

    @Override
    public void run() {
        super.run();
        for (int i = 0; i < detailObjects.size(); i++) {
            if (i != 0) {
                try {
                    Bitmap bitmap = Picasso.with(context).load(detailObjects.get(i).getImage()).get();
                    fileService.createFile(internalStorage, assetFolder, detailObjects.get(i).getId() + ".jpg", bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //use EventBus to give activity a notification to update the imageView From Local storage
        UnoApplication.getInstance().getBus().post(new SaveImageFinishEvent(detailObjects));
    }
}
