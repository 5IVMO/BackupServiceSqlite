package com.example.backupservice;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;

public class Zipper {
    private String password;

    public Zipper(String password) {
        this.password = password;
    }

    public Zipper() {

    }

    public void pack(File filePath, String password, File destinationPath) throws ZipException {
        ZipParameters zipParameters = new ZipParameters();
        zipParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        zipParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);
        zipParameters.setEncryptFiles(true);
        zipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
        zipParameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
        zipParameters.setPassword(password);
        //      String baseFileName = FilenameUtils.getBaseName(filePath);
        //      String destinationZipFilePath = destinationPath + "." + EXTENSION;
        ZipFile zipFile = new ZipFile(destinationPath);
        zipFile.addFile(filePath, zipParameters);
    }

    public boolean unpack(String sourceZipFilePath, String extractedZipFilePath, String Password) throws ZipException {
        ZipFile zipFile = new ZipFile(sourceZipFilePath);

        if (zipFile.isEncrypted()) {
            zipFile.setPassword(Password);
        }
        zipFile.extractAll(extractedZipFilePath);
        return true;
    }
}