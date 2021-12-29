package pt.up.fe.cosn.gateway.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.up.fe.cosn.gateway.advices.responses.ExecuteOrderResponse;
import pt.up.fe.cosn.gateway.advices.responses.OrdersResponse;
import pt.up.fe.cosn.gateway.entities.Algorithm;
import pt.up.fe.cosn.gateway.entities.Order;
import pt.up.fe.cosn.gateway.factories.ResponseFactory;
import pt.up.fe.cosn.gateway.requests.OrderRequest;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AlgorithmsController {
    private final String[] mockAlgorithmsList = {"K-means", "DBSCAN", "Neural Network", "LightGBM", "Decision Tree"};
    private final List<Algorithm> Algorithms = new ArrayList<>();

    @GetMapping("/getAlgorithms")
    @ResponseBody
    public ResponseEntity<Object> getAlgorithms() {
        //TODO Temporary mock values
        for (String mockAlgorithm : mockAlgorithmsList) {
            Algorithms.add(new Algorithm(mockAlgorithm));
        }
        return ResponseFactory.ok(Algorithms);
    }
}
