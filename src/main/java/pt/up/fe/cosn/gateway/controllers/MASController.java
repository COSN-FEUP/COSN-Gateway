package pt.up.fe.cosn.gateway.controllers;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pt.up.fe.cosn.gateway.advices.responses.ExecuteOrderResponse;
import pt.up.fe.cosn.gateway.advices.responses.OrdersResponse;
import pt.up.fe.cosn.gateway.advices.responses.SimpleResponse;
import pt.up.fe.cosn.gateway.advices.responses.algorithmsResponses.AlgorithmResponse;
import pt.up.fe.cosn.gateway.advices.responses.algorithmsResponses.WorkflowResponse;
import pt.up.fe.cosn.gateway.advices.responses.masResponses.MyWorkflowResponse;
import pt.up.fe.cosn.gateway.advices.responses.masResponses.RunWorkflowResponse;
import pt.up.fe.cosn.gateway.entities.Order;
import pt.up.fe.cosn.gateway.entities.User;
import pt.up.fe.cosn.gateway.factories.ResponseFactory;
import pt.up.fe.cosn.gateway.requests.OrderRequest;
import pt.up.fe.cosn.gateway.requests.masRequests.WorkflowRequest;
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

    private static String masServiceURL = "https://mascontroller.herokuapp.com";

    @Hidden
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

    @PostMapping("/runWorkflow")
    public ResponseEntity<Object> runWorkflow(@RequestHeader("AuthToken") String authorization, @RequestBody WorkflowRequest request) {
        String getAlgorithmsUrlPath = masServiceURL +"/workflow/run";

        Claims claim = Utils.decodeJWT(authorization);
        Optional<User> userOptional = userService.getUserByEmail(claim.getSubject());

        if(userOptional.isEmpty())
            return ResponseFactory.bad(new SimpleResponse(false, "The token is not valid."));

        if(userOptional.get().getRole().getValue() > roleService.getAdministratorRole().get().getValue())
            return ResponseFactory.unauthorized(new SimpleResponse(false, "User is not authorized to do this operation."));

        Map<String, String> order = new HashMap<String, String>();
        order.put("workflowId", request.getWorkflowId());
        order.put("domainModelId", request.getDomainModelId());
        order.put("userId", userOptional.get().getId().toString());
        order.put("description", request.getDescription());

        RunWorkflowResponse result = restTemplate.postForObject(getAlgorithmsUrlPath, order, RunWorkflowResponse.class);

        return ResponseFactory.ok(result);
    }

    @GetMapping("/getMyWorkflows")
    @ResponseBody
    public ResponseEntity<Object> getMyWorkflows(@RequestHeader("AuthToken") String authorization) {
        String getAlgorithmsUrlPath = masServiceURL +"/workflow/findAll";

        Claims claim = Utils.decodeJWT(authorization);
        Optional<User> userOptional = userService.getUserByEmail(claim.getSubject());

        if(userOptional.isEmpty())
            return ResponseFactory.bad(new SimpleResponse(false, "The token is not valid."));

        if(userOptional.get().getRole().getValue() > roleService.getAdministratorRole().get().getValue())
            return ResponseFactory.unauthorized(new SimpleResponse(false, "User is not authorized to do this operation."));

        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", userOptional.get().getId().toString());

        MyWorkflowResponse result = restTemplate.postForObject(getAlgorithmsUrlPath, params, MyWorkflowResponse.class);

        return ResponseFactory.ok(result);
    }

    @GetMapping("/getWorkflowById")
    @ResponseBody
    public ResponseEntity<Object> getAllWorkflows(@RequestHeader("AuthToken") String authorization, @RequestHeader("Id") String id) {
        String getAlgorithmsUrlPath = masServiceURL +"/workflow/findById";

        Claims claim = Utils.decodeJWT(authorization);
        Optional<User> userOptional = userService.getUserByEmail(claim.getSubject());

        if(userOptional.isEmpty())
            return ResponseFactory.bad(new SimpleResponse(false, "The token is not valid."));

        if(userOptional.get().getRole().getValue() > roleService.getAdministratorRole().get().getValue())
            return ResponseFactory.unauthorized(new SimpleResponse(false, "User is not authorized to do this operation."));

        Map<String, String> params = new HashMap<String, String>();
        params.put("workflowExecutionId", id);
        params.put("userId", userOptional.get().getId().toString());

        RunWorkflowResponse result = restTemplate.postForObject(getAlgorithmsUrlPath, params, RunWorkflowResponse.class);

        return ResponseFactory.ok(result);
    }
}
