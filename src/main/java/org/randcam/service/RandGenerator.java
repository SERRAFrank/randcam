package org.randcam.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class RandGenerator {

    private byte[] data;

    public byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }

        this.data = data;
        return this.data;
    }


    public int generateNewNumber(int min, int max)
    {
        SecureRandom generator = new SecureRandom(this.data);
        return generator.nextInt(max-min+1)+min;
    }


}
