package com.example.springbootmusictgbot.utility;

import java.io.File;

public class StorageUtility
{
    private static final StorageUtility storageUtility = new StorageUtility();
    private StorageUtility() {
    }

    public static StorageUtility getInstance() {
        return storageUtility;
    }

    public boolean deleteMp3File(String filePath) {
        File file = new File(filePath);
        return file.delete();
    }

    public boolean deleteJPGFile(String filePath) {
        File file = new File(filePath);
        return file.delete();
    }
}
