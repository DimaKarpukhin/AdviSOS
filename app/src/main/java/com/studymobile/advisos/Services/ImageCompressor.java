package com.studymobile.advisos.Services;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

public class ImageCompressor
{
    public static File Commpress(File i_ActualImage, Context i_Context)
    {
        File compressedImage = null;
        if (i_ActualImage == null) {
           return null;
        }
        else {
            // Compress image in main thread using custom Compressor
            try {
                compressedImage = new Compressor(i_Context)
                        .setMaxWidth(620)
                        .setMaxHeight(460)
                        .setQuality(72)
                        .setCompressFormat(Bitmap.CompressFormat.WEBP)
                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES).getAbsolutePath())
                        .compressToFile(i_ActualImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return compressedImage;
    }
}
