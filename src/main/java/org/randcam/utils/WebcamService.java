package org.randcam.utils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface WebcamService {
    boolean webcamActive();

    String getName();

    String requestImage() throws IOException;

    String saveImage() throws IOException;

    byte[] getImage(String file) throws IOException;

    String getHashFromImage() throws NoSuchAlgorithmException;
}
