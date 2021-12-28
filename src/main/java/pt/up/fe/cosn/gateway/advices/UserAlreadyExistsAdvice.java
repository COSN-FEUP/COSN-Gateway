package pt.up.fe.cosn.gateway.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pt.up.fe.cosn.gateway.advices.responses.SimpleResponse;
import pt.up.fe.cosn.gateway.exceptions.UserAlreadyExistsException;
import pt.up.fe.cosn.gateway.exceptions.UserNotFoundException;

@ControllerAdvice
class UserAlreadyExistsAdvice {

    @ResponseBody
    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.IM_USED)
    public SimpleResponse employeeNotFoundHandler(UserAlreadyExistsException ex) {
      return new SimpleResponse(false, ex.getMessage());
    }
}