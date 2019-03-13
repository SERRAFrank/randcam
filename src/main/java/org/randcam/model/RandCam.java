package org.randcam.model;

import lombok.Data;

@Data
public class RandCam {

    private Integer min;

    private Integer max;

    private String imageUrl;

    private String hash;

    private Integer generatedNumber;


}
