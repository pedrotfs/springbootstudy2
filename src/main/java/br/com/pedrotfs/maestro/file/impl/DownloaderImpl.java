package br.com.pedrotfs.maestro.file.impl;

import br.com.pedrotfs.maestro.file.Downloader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URL;

@Component
public class DownloaderImpl implements Downloader {

    private static Logger LOG = LoggerFactory.getLogger(DownloaderImpl.class);

    public void download(final String fileLocation, final String fileName)
    {
        LOG.info("attempting update from " + fileLocation);

        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        BufferedInputStream in;
        FileOutputStream fileOutputStream;
        File file = new File(fileName);
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
            in = new BufferedInputStream(new URL(fileLocation).openStream());
            fileOutputStream = new FileOutputStream(fileName, false);
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            in.close();
            fileOutputStream.close();

            LOG.info("update stored on " + fileName);
        } catch (IOException e) {
            LOG.info("source update failed. check connection or resource availability", e);
        }
    }
}
