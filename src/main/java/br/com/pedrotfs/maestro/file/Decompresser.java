package br.com.pedrotfs.maestro.file;

public interface Decompresser {

    void decompress(final String fileLocation, final String fileName, final String zippedName);
}
