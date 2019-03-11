package org.randcam.service;

import com.github.sarxos.webcam.Webcam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Service de gestion de la webcam
 */
@Component
@Slf4j
public class WebcamService {

    private Webcam webcam;


    //Format de l'image sauvegardée
    private final static String IMAGE_FORMAT = "JPG";

    //Nom de l'image sauvegardée
    private final static String IMAGE_FILENAME = "image.jpg";

    //Hash par defaut utilisé si le hashage de l'image échoue
    private final static String DEFAULT_HASH = "ab127d5f19adc3009dca86927ca2c4717d9a85db";

    //Image en memoire
    private BufferedImage image;

    /**
     * Constructeur
     */
    public WebcamService() {
        //Initialisation de webcam
        this.webcam = Webcam.getDefault();
        this.webcam.close();
        this.webcam.setViewSize(new Dimension(320, 240));
        this.webcam.open();
    }


    /**
     * Retourn True si la camera est active
     * @return
     */
    public boolean webcamActive() {
        return (this.webcam.isOpen() && this.webcam != null);
    }


    /**
     * Retourne le nom de la camera
     * @return
     */
    public String getName() {
        return this.webcam.getName();
    }


    /**
     * Acquiert une image de la camera et l'enregistre
     * @return
     * @throws IOException
     */
    public ImageIcon requestImage() throws IOException {

        //Acquisition de l'image
        this.image = this.webcam.getImage();

        //Enregistrement du BufferedImage image
        this.saveImage();

        //Chargement en tant que ImageIcon et la flush
        ImageIcon imageIcon = new ImageIcon(this.image);
        imageIcon.getImage().flush();

        //Retourne l' ImageIcon
        return imageIcon;

    }


    /**
     * Enregistrement du BufferedImage image
     *
     * @throws IOException
     */
    public void saveImage() throws IOException {

        if(this.image != null)
            ImageIO.write(this.image, IMAGE_FORMAT, new File(IMAGE_FILENAME));
    }

    /**
     * Hashage d'une image enregistrée
     *
     * @return
     */
    public String getHashFromImage() throws NoSuchAlgorithmException {

        //Initialisation de la valeur de retour
        String hash = null;
        try {

            //Transformation de l'image en memoire en bytes
            byte[] imageBytes = ((DataBufferByte) this.image.getData().getDataBuffer()).getData();

            //creation d'un tableau de bytes du hash
            byte[] toHash = new byte[(imageBytes.length / 7) + 1];

            //Remplissage du tableau de bytes toHash
            System.arraycopy(imageBytes, 7, toHash, 0, imageBytes.length / 7);


            //Instanciation du hasheur SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            //Instanciation du formateur
            Formatter formatter = new Formatter();


            //Creation du hash
            for (byte b : md.digest(toHash)) {
                formatter.format("%02x", b);
            }

            hash =  formatter.toString();


        } catch (Exception e) {
            // Nombre aléatoire non généré
            log.error("Error with hashing photo : " + e.getMessage());
            log.error("Using default hash: " + DEFAULT_HASH);
        }

        //Retour du hash généré ou du hash par defaut
        return (hash != null)? hash : DEFAULT_HASH;
    }



}
