package org.randcam;

import org.randcam.service.WebcamService;
import org.randcam.view.View;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class RandcamApplication {


    public static void main(String[] args) throws IOException {

        WebcamService webcamService = new WebcamService();
        if (webcamService.webcamActive()) {
            JOptionPane.showMessageDialog(null, "La Webcam " + webcamService.getName() + " a été trouvée et sera utilisée.", "Information", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Aucune webcam trouvée. L'application va s'arreter", "Erreur", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        View view = new View();
    }

}
