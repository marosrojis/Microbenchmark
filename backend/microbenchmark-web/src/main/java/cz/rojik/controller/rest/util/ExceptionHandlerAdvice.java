package cz.rojik.controller.rest.util;

import cz.rojik.backend.exception.BenchmarkNotFoundException;
import cz.rojik.backend.exception.InvalidBearerTokenException;
import cz.rojik.backend.exception.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * General controller layer exception resolver.
 *
 * @author Marek Rojik [marek@rojik.cz]
 */
@ControllerAdvice
class ExceptionHandlerAdvice {

    @Autowired
    private MessageSource messageSource;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception exception, WebRequest request) {
        logger.warn("Exception occured.", exception);

        return new ResponseEntity<>(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = { InvalidBearerTokenException.class, UserException.class })
    public ResponseEntity<String> handleBadRequestException(Exception exception, WebRequest request) {
        return handleExceptionInternal(exception, request, HttpStatus.BAD_REQUEST, false);
    }

    @ExceptionHandler(BenchmarkNotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(Exception exception, WebRequest request) {
        return handleExceptionInternal(exception, request, HttpStatus.NOT_FOUND, false);
    }

    private ResponseEntity<String> handleExceptionInternal(Exception exception, WebRequest request, HttpStatus status, boolean warnLevel) {
        if (warnLevel) {
            logger.warn("Exception occured.", exception);
        } else {
            logger.info("Exception occured.", exception);
        }

        return new ResponseEntity<>(exception.getMessage(), status);
    }

}
