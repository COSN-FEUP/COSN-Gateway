package pt.up.fe.cosn.gateway.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.up.fe.cosn.gateway.advices.responses.ExecuteOrderResponse;
import pt.up.fe.cosn.gateway.advices.responses.GenericResponse;
import pt.up.fe.cosn.gateway.advices.responses.OrdersResponse;
import pt.up.fe.cosn.gateway.entities.Order;
import pt.up.fe.cosn.gateway.factories.ResponseFactory;
import pt.up.fe.cosn.gateway.requests.OrderRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class MASController {
    private final String mockCity = "Porto";
    private final List<Order> orders = new ArrayList<>();

    @PostMapping("/executeOrder")
    public ResponseEntity<Object> executeOrder(@RequestBody OrderRequest request) {
        return ResponseFactory.ok(new ExecuteOrderResponse(new Order(request.getLatitude(), request.getLongitude(), mockCity)));
    }

    @GetMapping("/getAllOrders")
    @ResponseBody
    public ResponseEntity<Object> getAllOrders() {
        //TODO Temporary mock values
        for (int i = 0; i < 5; i++) {
            Random r = new Random();
            orders.add(new Order (r.nextDouble(), r.nextDouble() + i, mockCity + i));
        }
        return ResponseFactory.ok(new OrdersResponse(orders));
    }
}
