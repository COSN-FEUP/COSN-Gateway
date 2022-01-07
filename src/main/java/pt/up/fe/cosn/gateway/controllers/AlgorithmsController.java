package pt.up.fe.cosn.gateway.controllers;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.up.fe.cosn.gateway.advices.responses.SimpleResponse;
import pt.up.fe.cosn.gateway.entities.Algorithm;
import pt.up.fe.cosn.gateway.entities.User;
import pt.up.fe.cosn.gateway.factories.ResponseFactory;
import pt.up.fe.cosn.gateway.services.RoleService;
import pt.up.fe.cosn.gateway.services.UserService;
import pt.up.fe.cosn.gateway.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class AlgorithmsController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    private final String[] mockAlgorithmsList = {"K-means", "DBSCAN", "Neural Network", "LightGBM", "Decision Tree"};
    private final List<Algorithm> Algorithms = new ArrayList<>();

    @GetMapping("/getAlgorithms")
    @ResponseBody
    public ResponseEntity<Object> getAlgorithms(@RequestHeader("AuthToken") String authorization) {
        Claims claim = Utils.decodeJWT(authorization);
        Optional<User> userOptional = userService.getUserByEmail(claim.getSubject());

        if(userOptional.isEmpty())
            return ResponseFactory.bad(new SimpleResponse(false, "The token is not valid."));

        if(userOptional.get().getRole().getValue() > roleService.getAdministratorRole().get().getValue())
            return ResponseFactory.unauthorized(new SimpleResponse(false, "User is not authorized to do this operation."));

        //TODO Temporary mock values
        for (String mockAlgorithm : mockAlgorithmsList) {
            Algorithms.add(new Algorithm(mockAlgorithm));
        }

        return ResponseFactory.ok(Algorithms);
    }
}
