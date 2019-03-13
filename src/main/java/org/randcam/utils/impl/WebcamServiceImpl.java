package org.randcam.utils.impl;

import com.github.sarxos.webcam.Webcam;
import lombok.extern.slf4j.Slf4j;
import org.randcam.utils.WebcamService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Formatter;


/**
 * Service de gestion de la webcam
 */

@Slf4j
@Service("webcamService")
public class WebcamServiceImpl implements WebcamService {


    private final static String ROOT_LOCATION = "upload-dir/";
    private final static long FIXED_RATE = 10000;
    //Format de l'image sauvegardée
    private final static String IMAGE_FORMAT = "JPG";
    //Hash par defaut utilisé si le hashage de l'image échoue
    private final static String DEFAULT_HASH = "ab127d5f19adc3009dca86927ca2c4717d9a85db";
    private Webcam webcam;
    //Image en memoire
    private BufferedImage image;

    /**
     * Constructeur
     */
    public WebcamServiceImpl() {

        //Initialisation de webcam
        this.webcam = Webcam.getDefault();
        this.webcam.close();
        this.webcam.setViewSize(new Dimension(320, 240));
        this.webcam.open();

    }


    /**
     * Retourn True si la camera est active
     *
     * @return
     */
    @Override
    public boolean webcamActive() {
        return (this.webcam.isOpen() && this.webcam != null);
    }


    /**
     * Retourne le nom de la camera
     *
     * @return
     */
    @Override
    public String getName() {
        return this.webcam.getName();
    }


    /**
     * Acquiert une image de la camera et l'enregistre
     *
     * @return
     * @throws IOException
     */
    @Override
    public String requestImage() throws IOException {

        //Acquisition de l'image
        this.image = this.webcam.getImage();

        //Enregistrement du BufferedImage image
        return this.saveImage();

    }


    /**
     * Enregistrement du BufferedImage image
     *
     * @throws IOException
     */
    @Override
    public String saveImage() throws IOException {

        Date now = new Date();

        String fileName = now.getTime() + "." + IMAGE_FORMAT;
        String url = ROOT_LOCATION + fileName;

        if (this.image != null)
            ImageIO.write(this.image, IMAGE_FORMAT, new File(url));

        return fileName;
    }


    @Override
    public byte[] getImage(String file) throws IOException {

        File fi = new File(ROOT_LOCATION + file);
        return Files.readAllBytes(fi.toPath());

    }


    /**
     * Hashage d'une image enregistrée
     *
     * @return
     */
    @Override
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

            hash = formatter.toString();


        } catch (Exception e) {
            // Nombre aléatoire non généré
            log.error("Error with hashing photo : " + e.getMessage());
            log.error("Using default hash: " + DEFAULT_HASH);
        }

        //Retour du hash généré ou du hash par defaut
        return (hash != null) ? hash : DEFAULT_HASH;
    }


    /**
     * Tache cron. suppression des
     *
     * @throws IOException
     */
    @Scheduled(fixedRate = FIXED_RATE)
    public void clear() throws IOException {


        long now = new Date().getTime() - FIXED_RATE;

        Files.list(new File(ROOT_LOCATION).toPath())
                .forEach(path -> {
                            String filename = path.getFileName().toString().replaceFirst("[.][^.]+$", "");
                            if (Long.valueOf(filename) <= now) {
                                path.toFile().delete();
                                log.info("*** CLEAR : " + path + " deleted");
                            }


                        }
                );


    }

}
