/*
 * Copyright 2018 INVENTI Consulting, s.r.o.
 * http://www.inventi.cz
 */
package cz.rojik.exception;

/**
 *
 * @author Lukáš Hamrle
 */
public class ObjectNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ObjectNotFoundException(String message) {
        super(message);
    }

    public ObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
