package pt.up.fe.cosn.gateway.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.up.fe.cosn.gateway.advices.responses.VisualizationResponse;
import pt.up.fe.cosn.gateway.factories.ResponseFactory;
import pt.up.fe.cosn.gateway.requests.VisualizationRequest;

@RestController
public class VisualizationController {
    @PostMapping("/runVisualization")
    public ResponseEntity<Object> runVisualization(@RequestBody VisualizationRequest request) {
        return ResponseFactory.ok(new VisualizationResponse("Under Construction"));
    }
}
