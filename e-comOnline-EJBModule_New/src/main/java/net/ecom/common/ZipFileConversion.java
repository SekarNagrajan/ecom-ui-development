/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ecom.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author gobinath.t
 */
public class ZipFileConversion {

    public void convertZipFile(String filename, String filepath, HashMap<String, byte[]> filemap) {
        try {
            System.out.println("[ECOM] ZipFileConversion convertZipFile");
            File zipFile = new File(filepath + File.separator + filename + ".zip");
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);

            Iterator it = filemap.keySet().iterator();       //keyset is a method  
            String key = null;
            byte[] filedata = null;
            while (it.hasNext()) {
                key = (String) it.next();
                filedata = filemap.get(key);
                zipFile(key, zos, filedata);
            }
            //zipFile(fileName1, zos);
            //zipFile(fileName2, zos);
            zos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void zipFile(String fileName, ZipOutputStream zos, byte filedata[]) throws IOException {
        try {
            ZipEntry zipEntry = new ZipEntry(fileName);
            zos.putNextEntry(zipEntry);
            zos.write(filedata);

            zos.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
