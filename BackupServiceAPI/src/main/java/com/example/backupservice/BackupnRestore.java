package com.example.backupservice;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.joda.time.LocalDate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class BackupnRestore {

	public boolean takeBackup(Context context,String appName,String packageName, String dbName, String destinationPath) {
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
	
	public boolean takeEncryptedBackup(Context context,String appName,String packageName, String dbName, String destinationPath, String password) {
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
            Log.d("Exception",e.getMessage());
		}
		
		return false;
	}

	public void importDB(String FilePath,String DBPath) throws IOException {
		InputStream mInput = new FileInputStream(FilePath);
		String outFileName = DBPath;
		OutputStream mOutput = new FileOutputStream(outFileName);
		byte[] mBuffer = new byte[1024];
		int mLength;
		while ((mLength = mInput.read(mBuffer))>0)
		{
			mOutput.write(mBuffer, 0, mLength);
		}
		mOutput.flush();
		mOutput.close();
		mInput.close();
	}

	public boolean expire(LocalDate expiryDate){
		try {

		}
		catch (Exception e) {
			Log.d("Exception",e.getMessage());
		}
		return false;
	}
}
