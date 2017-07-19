package com.example.backupservice;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class BackupnRestore {

	public boolean takeBackup(Context context,String packageName, String dbName, String destinationPath) {
		try {
		//	File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		    File sd = Environment.getExternalStorageDirectory();
		    File data = Environment.getDataDirectory();

		    if (sd.canWrite()) {
		      //  String currentDBPath = "//data//"+packageName+"//databases//"+dbName;
                // File currentDB = new File(data, currentDBPath);

                String currentDBPath =context.getDatabasePath(dbName).getPath();
				File currentDB = new File(currentDBPath);

		        String backupDBPath = destinationPath+"//"+dbName;
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
            Log.d("Exception",e.getMessage());
		}
		
		return false;
	}
	
	public boolean takeEncryptedBackup(Context context,String packageName, String dbName, String destinationPath, String password) {
		try {
	
				File sd = Environment.getExternalStorageDirectory();
				File data = Environment.getDataDirectory();
			
				if (sd.canWrite()) {
				   // String currentDBPath = "//data//"+packageName+"//databases//"+dbName;
                   // File currentDB = new File(data, currentDBPath);
                    String currentDBPath =context.getDatabasePath(dbName).getPath();
                    File currentDB = new File(currentDBPath);

				    String backupDBPath = destinationPath+"//"+dbName;
				    File backupDB = new File(sd, backupDBPath);
				    if (!backupDB.exists()) {
				    	backupDB.createNewFile();
					} 
				    if (currentDB.exists()) {
				        Zipper zipper = new Zipper();
				        File backupDBZIp = new File(sd, backupDBPath+".zip");
				        zipper.pack(currentDB, password, backupDBZIp);
                        return true;
				    }
				}
		    
		} catch (Exception e) {
            Log.d("Exception",e.getMessage());
		}
		
		return false;
	}
	
	public boolean restore(String packageName, String dbName, String destinationPath) {
		try {
		    File sd = Environment.getExternalStorageDirectory();
		    File data = Environment.getDataDirectory();

		    if (sd.canWrite()) {
		    String currentDBPath = "//data//"+packageName+"//databases//"+dbName;
		        String backupDBPath = destinationPath;
		        File currentDB = new File(data, currentDBPath);
		        File backupDB = new File(sd, backupDBPath);
		        if (!currentDB.exists()) {
		        	currentDB.createNewFile();
				}
		        if (currentDB.exists()) {
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
            Log.d("Exception",e.getMessage());
		}
		
		return false;
	}
}
