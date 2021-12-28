package pt.up.fe.cosn.gateway.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pt.up.fe.cosn.gateway.advices.responses.SimpleResponse;
import pt.up.fe.cosn.gateway.exceptions.NoCredentialsMatchException;
import pt.up.fe.cosn.gateway.exceptions.UserNotFoundException;

@ControllerAdvice
class NoCredentialsMatchAdvice {

    @ResponseBody
    @ExceptionHandler(NoCredentialsMatchException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public SimpleResponse employeeNotFoundHandler(NoCredentialsMatchException ex) {
      return new SimpleResponse(false, ex.getMessage());
    }
}