package org.randcam.view;

import org.randcam.service.RandGenerator;
import org.randcam.service.WebcamService;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class View extends JFrame {

    private WebcamService webcamService = new WebcamService();

    private RandGenerator randGenerator = new RandGenerator();


    private JLabel image = new JLabel();
    private JLabel rangeLabel =  new JLabel("Range :");
    private JLabel fromLabel =  new JLabel("De :");
    private JLabel toLabel =  new JLabel("A :");
    private JLabel hashLabel =  new JLabel("Hash : ");
    private JLabel genLabel =  new JLabel("Code généré : ");




    private JTextField minField = new JTextField("0", 10);
    private JTextField maxField = new JTextField("100", 10);


    private JButton pictureAction = new JButton("Prendre une photo");
    private JButton generateAction = new JButton("Generer un nombre");

    public View() throws IOException {

        this.setTitle("RandGenerator");
        this.setSize(750, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.pictureAction.addActionListener(event -> {
            try {
                this.getImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.generateAction.addActionListener(event -> {
            try {
                this.getHash();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        iniLayout();

        this.setVisible(true);
    }


    void iniLayout() throws IOException {
        this.getImage();

        JPanel rightPanel = new JPanel();
        JPanel generationParamPanel = new JPanel();
        generationParamPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        generationParamPanel.setLayout(new FlowLayout());
        generationParamPanel.add(fromLabel);
        generationParamPanel.add(minField);
        generationParamPanel.add(toLabel);
        generationParamPanel.add(maxField);



        JPanel componentPanel = new JPanel();
        componentPanel.setLayout(new BoxLayout(componentPanel, BoxLayout.PAGE_AXIS));
        componentPanel.add(rangeLabel);
        componentPanel.add(generationParamPanel);
        componentPanel.add(hashLabel);
        componentPanel.add(genLabel);

        rightPanel.setLayout(new FlowLayout());
        rightPanel.add(componentPanel);

        JPanel imagePanel = new JPanel();
        imagePanel.add(this.image);


        JPanel buttonPanel = new JPanel();
        buttonPanel.add(this.pictureAction);
        buttonPanel.add(this.generateAction);

        this.setLayout(new BorderLayout());
        this.getContentPane().add(imagePanel, BorderLayout.WEST);
        this.getContentPane().add(rightPanel, BorderLayout.EAST);
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

    }


    private void getImage() throws IOException {
        this.image.setIcon(this.webcamService.requestImage());
    }

    private void getHash() throws NoSuchAlgorithmException, IOException {
        this.getImage();

        String hash = this.webcamService.getHashFromImage();
        hashLabel.setText("Hash : " + hash);
        this.randGenerator.hexStringToByteArray(hash);

        int temp = this.randGenerator.generateNewNumber(Integer.parseInt(minField.getText()), Integer.parseInt(maxField.getText()));
        genLabel.setText("Code généré : " + String.valueOf(temp));

    }


}
