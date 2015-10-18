package org.app4j.site.module.file.service;


import org.app4j.site.util.Scalr;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

/**
 * @author chi
 */
public class ImageScalar {
    public static void writeJPG(BufferedImage bufferedImage, OutputStream outputStream, double quality) throws IOException {
        Iterator<ImageWriter> iterator =
            ImageIO.getImageWritersByFormatName("jpg");
        ImageWriter imageWriter = iterator.next();
        ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();
        imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        imageWriteParam.setCompressionQuality((float) quality);
        ImageOutputStream imageOutputStream = new MemoryCacheImageOutputStream(outputStream);
        imageWriter.setOutput(imageOutputStream);
        IIOImage iioimage = new IIOImage(bufferedImage, null, null);
        imageWriter.write(null, iioimage, imageWriteParam);
        imageOutputStream.flush();
    }

    public byte[] scale(byte[] content, String imageType, int width, int height) {
        try {
            BufferedImage src = ImageIO.read(new ByteArrayInputStream(content));
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            BufferedImage result = Scalr.resize(chop(src, height, width), Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT,
                width, height, Scalr.OP_ANTIALIAS);

            if ("jpg".equals(imageType) || "jpeg".equals(imageType)) {
                writeJPG(result, out, 0.5);
            } else {
                ImageIO.write(result, imageType, out);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    final BufferedImage chop(BufferedImage src, int height, int width) {
        if (isChopY(src.getHeight(), src.getWidth(), height, width)) {
            double rate = (double) height / width;
            int y = (int) (src.getWidth() * rate);
            return src.getSubimage(0, (src.getHeight() - y) / 2, src.getWidth(), y);
        } else {
            double rate = (double) width / height;
            int x = (int) (src.getHeight() * rate);
            return src.getSubimage((src.getWidth() - x) / 2, 0, x, src.getHeight());
        }
    }

    final boolean isChopY(int height, int width, int targetHeight, int targetWidth) {
        double srcRate = (double) height / width;
        double targetRate = (double) targetHeight / targetWidth;
        return srcRate > targetRate;
    }
}
