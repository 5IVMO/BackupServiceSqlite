package com.example.backupservice;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.joda.time.LocalDate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import static com.example.backupservice.Util.parseException;

public class BackupAndRestore {

    public boolean takeBackup(Context context, String dbName, String destinationPath) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            LocalDate today = LocalDate.now();
            if (sd.canWrite()) {
                String currentDBPath = context.getDatabasePath(dbName).getPath();
                File currentDB = new File(currentDBPath);

                String backupDBPath = destinationPath + "//" + dbName + "-" + today + ".db";
                File backupDB = new File(sd, backupDBPath);
                if (!backupDB.exists()) {
                    backupDB.createNewFile();
                }
                if (currentDB.exists()) {
                    FileInputStream srcInputStream = new FileInputStream(currentDB);
                    FileOutputStream dstOutputStream = new FileOutputStream(backupDB);
                    FileChannel src = srcInputStream.getChannel();
                    FileChannel dst = dstOutputStream.getChannel();
                    dst.transferFrom(src, 0, src.size());
                    dst.close();
                    src.close();
                    dstOutputStream.close();
                    srcInputStream.close();
                    return true;
                }
            }
        } catch (Exception e) {
            Log.d("Exception", e.getMessage());
        }
        return false;
    }

    public boolean takeEncryptedBackup(Context context, String dbName, String destinationPath, String password) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            LocalDate today = LocalDate.now();

            String backupDBPath = destinationPath + "//" + dbName + "-" + today + ".db";
            File backupDB = new File(sd, backupDBPath);

            boolean response = takeBackup(context, dbName, destinationPath);
            if (response && sd.canWrite()) {
                if (backupDB.exists()) {
                    Zipper zipper = new Zipper();
                    File backupDBZIp = new File(sd, backupDBPath + ".zip");
                    boolean responseZip = zipper.pack(backupDB, password, backupDBZIp);
                    if (responseZip) {
                        backupDB.delete();
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public boolean restore(String FilePath, String DBPath) {

        try {
            File sd = Environment.getExternalStorageDirectory();

            if (sd.canWrite()) {
                String currentDBPath = DBPath;
                String backupDBPath = FilePath;
                File currentDB = new File(currentDBPath);
                File backupDB = new File(backupDBPath);
                if (!currentDB.exists()) {
                    currentDB.createNewFile();
                }
                if (backupDB.exists()) {
                    FileInputStream srcInputStream = new FileInputStream(backupDB);
                    FileOutputStream dstOutputStream = new FileOutputStream(currentDB);
                    FileChannel src = srcInputStream.getChannel();
                    FileChannel dst = dstOutputStream.getChannel();
                    dst.transferFrom(src, 0, src.size());
                    dst.close();
                    src.close();
                    dstOutputStream.close();
                    srcInputStream.close();
                    return true;
                }
            }
        } catch (Exception e) {
            Log.d("Exception", e.getMessage());
        }
        return false;
    }

    public boolean decryptBackup(Context context, String FilePath, String DBPath, String Password) {
        try {

            File sd = Environment.getExternalStorageDirectory();

            if (sd.canWrite()) {
                String backupDBPath = FilePath;
                File backupDB = new File(backupDBPath);

                String extractedFilePath = FilePath.substring(0, FilePath.lastIndexOf('/'));
                String extractedFile = FilePath.substring(0, FilePath.lastIndexOf('.'));
                File extractedDB = new File(extractedFile);

                if (backupDB.exists()) {
                    Zipper zipper = new Zipper();
                    boolean responseUnpack = zipper.unpack(backupDBPath, extractedFilePath, Password);
                    if (responseUnpack && extractedDB.exists()) {
                        boolean response = restore(extractedFile, DBPath);
                        extractedDB.delete();
                        if (response) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            parseException(context, e);
        }
        return false;
    }

    public boolean expire(LocalDate expiryDate, String dbName, String destinationPath) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            String backupDBPath = destinationPath + "//" + dbName + "-" + expiryDate + ".db";
            File backupDB = new File(sd, backupDBPath);

            String encryptedBackupDBPath = destinationPath + "//" + dbName + "-" + expiryDate + ".db.zip";
            File encryptedBackupDB = new File(sd, encryptedBackupDBPath);

            if (backupDB.exists()) {
                backupDB.delete();
                return true;
            } else if (encryptedBackupDB.exists()) {
                encryptedBackupDB.delete();
                return true;
            }
        } catch (Exception e) {
            Log.d("Exception", e.getMessage());
        }
        return false;
    }
}
