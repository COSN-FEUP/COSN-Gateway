package pt.up.fe.cosn.gateway.controllers;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pt.up.fe.cosn.gateway.advices.responses.SimpleResponse;
import pt.up.fe.cosn.gateway.entities.User;
import pt.up.fe.cosn.gateway.factories.ResponseFactory;
import pt.up.fe.cosn.gateway.services.RoleService;
import pt.up.fe.cosn.gateway.services.UserService;
import pt.up.fe.cosn.gateway.utils.Utils;

import java.util.Optional;
import java.util.Random;

@RestController
public class DomainModelController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    private String mockDomainModel = "Domain Model";
    @GetMapping("/getDomainModel")
    @ResponseBody
    public ResponseEntity<Object> getDomainModel(@RequestHeader("Authorization") String authorization) {
        Claims claim = Utils.decodeJWT(authorization);
        Optional<User> userOptional = userService.getUserByEmail(claim.getSubject());

        if(userOptional.isEmpty())
            return ResponseFactory.bad(new SimpleResponse(false, "The token is not valid."));

        if(userOptional.get().getRole().getValue() > roleService.getAdministratorRole().get().getValue())
            return ResponseFactory.unauthorized(new SimpleResponse(false, "User is not authorized to do this operation."));

        //TODO Temporary mock values
        return ResponseFactory.ok(mockDomainModel);
    }
}
