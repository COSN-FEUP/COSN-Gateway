package pt.up.fe.cosn.gateway.controllers;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
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

import java.util.*;

@RestController
public class MASController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    private final String mockCity = "Porto";
    private final List<Order> orders = new ArrayList<>();

    @PostMapping("/executeOrder")
    public ResponseEntity<Object> executeOrder(@RequestHeader("AuthToken") String authorization, @RequestBody OrderRequest request) {
        Claims claim = Utils.decodeJWT(authorization);
        Optional<User> userOptional = userService.getUserByEmail(claim.getSubject());

        if(userOptional.isEmpty())
            return ResponseFactory.bad(new SimpleResponse(false, "The token is not valid."));

        if(userOptional.get().getRole().getValue() > roleService.getAdministratorRole().get().getValue())
            return ResponseFactory.unauthorized(new SimpleResponse(false, "User is not authorized to do this operation."));

        Map<String, Double> order = new HashMap<String, Double>();
        order.put("latitude", request.getLatitude());
        order.put("longitude", request.getLongitude());

        String URI_VISUALIZATION = "https://61d8d203e6744d0017ba8cc5.mockapi.io/Orders";

        String result = restTemplate.postForObject(URI_VISUALIZATION, order, String.class);

        return ResponseFactory.ok(new ExecuteOrderResponse(new Order(request.getLatitude(), request.getLongitude(), mockCity)));
    }

    @GetMapping("/getAllOrders")
    @ResponseBody
    public ResponseEntity<Object> getAllOrders(@RequestHeader("AuthToken") String authorization) {
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
