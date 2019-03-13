package org.randcam.service;

import org.randcam.model.RandCam;
import org.randcam.utils.WebcamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Service
public class RandCamService {


    @Autowired
    private HttpServletRequest request;

    @Autowired
    private WebcamService webcamService;


    public ResponseEntity<String> getStatus() {

        if (this.webcamService.webcamActive())
            return ResponseEntity.ok(this.webcamService.getName());
        else
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }


    public RandCam generateNumber(Integer min, Integer max) throws NoSuchAlgorithmException, IOException, URISyntaxException {

        String location = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/" + this.webcamService.requestImage();


        String hash = this.webcamService.getHashFromImage();


        RandCam randCam = new RandCam();
        randCam.setMin(min);
        randCam.setMax(max);
        randCam.setImageUrl(location);
        randCam.setHash(hash);
        randCam.setGeneratedNumber(generateNewNumber(min, max, hash));


        return randCam;

    }

    public byte[] getImage(String file) {
        try {
            return this.webcamService.getImage(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private int generateNewNumber(int min, int max, String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }

        SecureRandom generator = new SecureRandom(data);
        return generator.nextInt(max - min + 1) + min;
    }
}
