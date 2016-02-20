package com.unoapp;

import android.content.Context;
import android.graphics.Bitmap;

import com.sromku.simple.storage.InternalStorage;
import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;

import java.io.File;

public class FileService {
    private final InternalStorage internalStorage;
    private final String assetFolder;

    public FileService(Context context) {
        internalStorage = SimpleStorage.getInternalStorage(context);
        assetFolder = "images";
    }

    public void createFile(Storage storage, String folderName, String fileName, Bitmap data) {
        storage.deleteFile(folderName, fileName);
        storage.createFile(folderName, fileName, data);
    }

    public File getFileFromStorage(String imageId) {
        return internalStorage.getFile(assetFolder, imageId + ".jpg");
    }

}
