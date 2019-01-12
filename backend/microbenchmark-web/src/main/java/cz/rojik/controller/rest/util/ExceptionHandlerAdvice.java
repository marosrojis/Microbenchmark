package cz.rojik.controller.rest.util;

import cz.rojik.backend.exception.EntityNotFoundException;
import cz.rojik.backend.exception.InvalidBearerTokenException;
import cz.rojik.backend.exception.UserException;
import cz.rojik.backend.dto.ErrorDetailsDTO;
import cz.rojik.service.exception.DockerException;
import cz.rojik.service.exception.KillContainerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.ws.rs.NotFoundException;
import java.time.LocalDateTime;

/**
 * General controller layer exception resolver.
 *
 * @author Marek Rojik [marek@rojik.cz] on 05. 01. 2019
 */
@ControllerAdvice
class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = { Exception.class, RuntimeException.class })
    public ResponseEntity<ErrorDetailsDTO> handleGeneralException(Exception exception, WebRequest request) {
        return handleExceptionInternal(exception, request, HttpStatus.INTERNAL_SERVER_ERROR, false);
    }

    @ExceptionHandler(value = { InvalidBearerTokenException.class, UserException.class, DockerException.class, KillContainerException.class})
    public ResponseEntity<ErrorDetailsDTO> handleBadRequestException(Exception exception, WebRequest request) {
        return handleExceptionInternal(exception, request, HttpStatus.BAD_REQUEST, false);
    }

    @ExceptionHandler(value = { EntityNotFoundException.class, NotFoundException.class })
    public ResponseEntity<ErrorDetailsDTO> handleNotFoundException(Exception exception, WebRequest request) {
        return handleExceptionInternal(exception, request, HttpStatus.NOT_FOUND, false);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorDetailsDTO errorDetails = new ErrorDetailsDTO(LocalDateTime.now(), "Validation Failed",
                ex.getBindingResult().toString());
        return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorDetailsDTO> handleExceptionInternal(Exception exception, WebRequest request, HttpStatus status, boolean warnLevel) {
        if (warnLevel) {
            logger.warn("Exception occured.", exception);
        } else {
            logger.info("Exception occured.", exception);
        }

        ErrorDetailsDTO errorDetails = new ErrorDetailsDTO(LocalDateTime.now(), exception.getMessage(),
                request.getDescription(false));

        logger.info(new ResponseEntity<>(errorDetails, status).getStatusCode().toString());
        return new ResponseEntity<>(errorDetails, status);
    }

}
