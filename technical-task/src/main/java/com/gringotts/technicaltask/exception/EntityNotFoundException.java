package com.gringotts.technicaltask.exception;

import static java.lang.String.format;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(Class<?> clazz, Object id) {
        super(format("Entity of type [%s] with id [%s] could not be retrieved.", clazz.getSimpleName(), id));
    }

    public EntityNotFoundException(Class<?> clazz) {
        super(format("Entity of type [%s] could not be retrieved.", clazz.getSimpleName()));
    }
}
