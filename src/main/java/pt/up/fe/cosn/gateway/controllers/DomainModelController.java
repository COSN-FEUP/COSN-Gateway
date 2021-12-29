package pt.up.fe.cosn.gateway.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pt.up.fe.cosn.gateway.factories.ResponseFactory;

import java.util.Random;

@RestController
public class DomainModelController {
    private String mockDomainModel = "Domain Model";
    @GetMapping("/getDomainModel")
    @ResponseBody
    public ResponseEntity<Object> getDomainModel() {
        //TODO Temporary mock values
        return ResponseFactory.ok(mockDomainModel);
    }
}
