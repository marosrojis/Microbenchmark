package cz.rojik.controller.rest.util;

import cz.rojik.backend.exception.BenchmarkNotFoundException;
import cz.rojik.backend.exception.InvalidBearerTokenException;
import cz.rojik.backend.exception.UserException;
import cz.rojik.error.ErrorDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.ws.rs.NotFoundException;
import java.util.Date;

/**
 * General controller layer exception resolver.
 *
 * @author Marek Rojik [marek@rojik.cz]
 */
@ControllerAdvice
class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGeneralException(Exception exception, WebRequest request) {
        return handleExceptionInternal(exception, request, HttpStatus.INTERNAL_SERVER_ERROR, false);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ErrorDetails> handleGeneralRuntimeException(Exception exception, WebRequest request) {
        logger.warn("Exception occured.", exception);

        return handleExceptionInternal(exception, request, HttpStatus.INTERNAL_SERVER_ERROR, false);
    }

    @ExceptionHandler(value = { InvalidBearerTokenException.class, UserException.class })
    public ResponseEntity<ErrorDetails> handleBadRequestException(Exception exception, WebRequest request) {
        return handleExceptionInternal(exception, request, HttpStatus.BAD_REQUEST, false);
    }

    @ExceptionHandler(value = { BenchmarkNotFoundException.class, NotFoundException.class })
    public ResponseEntity<ErrorDetails> handleNotFoundException(Exception exception, WebRequest request) {
        return handleExceptionInternal(exception, request, HttpStatus.NOT_FOUND, false);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Validation Failed",
                ex.getBindingResult().toString());
        return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ErrorDetails> handleExceptionInternal(Exception exception, WebRequest request, HttpStatus status, boolean warnLevel) {
        if (warnLevel) {
            logger.warn("Exception occured.", exception);
        } else {
            logger.info("Exception occured.", exception);
        }

        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                request.getDescription(false));

        logger.info(new ResponseEntity<>(errorDetails, status).getStatusCode().toString());
        return new ResponseEntity<>(errorDetails, status);
    }

}
