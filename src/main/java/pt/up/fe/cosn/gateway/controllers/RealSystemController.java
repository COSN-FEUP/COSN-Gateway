package pt.up.fe.cosn.gateway.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pt.up.fe.cosn.gateway.factories.ResponseFactory;

import java.util.Random;

@RestController
public class RealSystemController {
    @GetMapping("/getSensorValues")
    @ResponseBody
    public ResponseEntity<Object> getSensorValues() {
        //TODO Temporary mock values
        Random r = new Random();
        return ResponseFactory.ok("Sensor Values " + r.nextDouble());
    }
}
