package br.com.pedrotfs.maestro.file.impl;


import br.com.pedrotfs.maestro.file.Decompresser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class DecompresserImpl implements Decompresser {

    private static Logger LOG = LoggerFactory.getLogger(DecompresserImpl.class);

    @Override
    public void decompress(final String fileLocation, final String fileName, final String targetName) {
        LOG.info("starting decompresser on " + fileName);
        FileInputStream fileInputStream;
        byte[] buffer = new byte[1024];

        try {
            fileInputStream = new FileInputStream(fileName);
            ZipInputStream zip = new ZipInputStream(fileInputStream);
            ZipEntry zipEntry = zip.getNextEntry();

            while(zipEntry != null) {
                if(zipEntry.getName().equalsIgnoreCase(targetName)) {
                    LOG.info("target fileName found in zip " + fileName);
                    File file = new File(fileLocation);
                    treatTargetDirectory(file);
                    FileOutputStream fileOutputStream = new FileOutputStream(file);

                    LOG.info("Extracting compressed file " + fileName);
                    int len;
                    while ((len = zip.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, len);
                    }
                    fileOutputStream.close();
                    zip.closeEntry();
                    break;
                }
            }

            LOG.info("Extraction complete. closing streams.");
            zip.closeEntry();
            zip.close();
            fileInputStream.close();
        } catch (IOException e) {
            LOG.error("Error extracting " + fileName + " to " + fileLocation, e);
        }
    }

    private void treatTargetDirectory(File dir) throws IOException {
        if(!dir.exists()) {
            LOG.info("creating folder.");
            dir.getParentFile().mkdirs();
        }
        LOG.info("creating blank file.");
        dir.createNewFile();
    }
}
