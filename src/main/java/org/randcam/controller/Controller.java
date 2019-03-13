package org.randcam.controller;

import org.randcam.model.RandCam;
import org.randcam.service.RandCamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.io.IOException;

@RestController
@RequestMapping("/")
public class Controller {


    @Autowired
    private RandCamService randCamService;

    @Autowired
    private ServletContext servletContext;


    @GetMapping
    public ResponseEntity<?> getStatus() {
        return randCamService.getStatus();
    }

    @GetMapping("/generateNumber")
    public RandCam generateNumber(@RequestParam(required = false) Integer min, @RequestParam(required = false) Integer max) throws Exception {

        min = (min != null) ? min : 0;
        max = (max != null) ? max : 100;

        return randCamService.generateNumber(min, max);

    }

    @GetMapping(value = "/{img}",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(
            @PathVariable(value = "img") String img) throws IOException {

        return randCamService.getImage("./" + img);

    }


}
