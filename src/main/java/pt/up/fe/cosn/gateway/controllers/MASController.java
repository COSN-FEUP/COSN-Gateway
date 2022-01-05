package pt.up.fe.cosn.gateway.controllers;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.up.fe.cosn.gateway.advices.responses.ExecuteOrderResponse;
import pt.up.fe.cosn.gateway.advices.responses.GenericResponse;
import pt.up.fe.cosn.gateway.advices.responses.OrdersResponse;
import pt.up.fe.cosn.gateway.advices.responses.SimpleResponse;
import pt.up.fe.cosn.gateway.entities.Order;
import pt.up.fe.cosn.gateway.entities.User;
import pt.up.fe.cosn.gateway.factories.ResponseFactory;
import pt.up.fe.cosn.gateway.requests.OrderRequest;
import pt.up.fe.cosn.gateway.services.RoleService;
import pt.up.fe.cosn.gateway.services.UserService;
import pt.up.fe.cosn.gateway.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
public class MASController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    private final String mockCity = "Porto";
    private final List<Order> orders = new ArrayList<>();

    @PostMapping("/executeOrder")
    public ResponseEntity<Object> executeOrder(@RequestHeader("Authorization") String authorization, @RequestBody OrderRequest request) {
        Claims claim = Utils.decodeJWT(authorization);
        Optional<User> userOptional = userService.getUserByEmail(claim.getSubject());

        if(userOptional.isEmpty())
            return ResponseFactory.bad(new SimpleResponse(false, "The token is not valid."));

        if(userOptional.get().getRole().getValue() > roleService.getAdministratorRole().get().getValue())
            return ResponseFactory.unauthorized(new SimpleResponse(false, "User is not authorized to do this operation."));

        return ResponseFactory.ok(new ExecuteOrderResponse(new Order(request.getLatitude(), request.getLongitude(), mockCity)));
    }

    @GetMapping("/getAllOrders")
    @ResponseBody
    public ResponseEntity<Object> getAllOrders(@RequestHeader("Authorization") String authorization) {
        Claims claim = Utils.decodeJWT(authorization);
        Optional<User> userOptional = userService.getUserByEmail(claim.getSubject());

        if(userOptional.isEmpty())
            return ResponseFactory.bad(new SimpleResponse(false, "The token is not valid."));

        if(userOptional.get().getRole().getValue() > roleService.getAdministratorRole().get().getValue())
            return ResponseFactory.unauthorized(new SimpleResponse(false, "User is not authorized to do this operation."));

        //TODO Temporary mock values
        for (int i = 0; i < 5; i++) {
            Random r = new Random();
            orders.add(new Order (r.nextDouble(), r.nextDouble() + i, mockCity + i));
        }
        return ResponseFactory.ok(new OrdersResponse(orders));
    }
}
