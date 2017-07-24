package com.example.backupservice;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.joda.time.LocalDate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class BackupAndRestore {

    public boolean takeBackup(Context context, String appName, String packageName, String dbName, String destinationPath) {
        try {
            //	File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            LocalDate today = LocalDate.now();
            if (sd.canWrite()) {
                //  String currentDBPath = "//data//"+packageName+"//databases//"+dbName;
                // File currentDB = new File(data, currentDBPath);

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

    public boolean takeEncryptedBackup(Context context, String appName, String packageName, String dbName, String destinationPath, String password) {
        try {

            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                // String currentDBPath = "//data//"+packageName+"//databases//"+dbName;
                // File currentDB = new File(data, currentDBPath);
                LocalDate today = LocalDate.now();
                String currentDBPath = context.getDatabasePath(dbName).getPath();
                File currentDB = new File(currentDBPath);

                String backupDBPath = destinationPath + "//" + dbName + "-" + today;
                File backupDB = new File(sd, backupDBPath);
                if (!backupDB.exists()) {
                    backupDB.createNewFile();
                }
                if (currentDB.exists()) {
                    Zipper zipper = new Zipper();
                    File backupDBZIp = new File(sd, backupDBPath + ".zip");
                    zipper.pack(currentDB, password, backupDBZIp);
                    return true;
                }
            }

        } catch (Exception e) {
            Log.d("Exception", e.getMessage());
        }

        return false;
    }

    public boolean restore(String packageName, String dbName, String destinationPath) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + packageName + "//databases//" + dbName;
                String backupDBPath = destinationPath;
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);
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

    public boolean restore(String FilePath, String DBPath)  {

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

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

       /* InputStream mInput = new FileInputStream(FilePath);
        String outFileName = DBPath;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();*/
    }

    public boolean decryptBackup(Context context,String FilePath, String DBPath,String Password){
        try {

            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                LocalDate today = LocalDate.now();
                String currentDBPath = DBPath;
                File currentDB = new File(currentDBPath);

                String backupDBPath = FilePath;
                File backupDB = new File(backupDBPath);
                if (!currentDB.exists()) {
                    currentDB.createNewFile();
                }
                if (backupDB.exists()) {
                    Zipper zipper = new Zipper();
                    File backupDBZIp = new File(sd, backupDBPath);
                    //Give path where to extract zip file
                    //check if extracted file is .db file
                    //if yes
                    //replace with current db
                  //  zipper.unpack(backupDBPath,);
                    return true;
                }
            }
        } catch (Exception e) {
            Log.d("Exception", e.getMessage());
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
            }
            else if (encryptedBackupDB.exists()) {
                encryptedBackupDB.delete();
                return true;
            }
        } catch (Exception e) {
            Log.d("Exception", e.getMessage());
        }
        return false;
    }
}
