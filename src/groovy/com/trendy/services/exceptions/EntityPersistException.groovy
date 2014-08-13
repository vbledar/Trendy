package com.trendy.services.exceptions

/**
 * Created by nvasili on 13/8/2014.
 */
class EntityPersistException extends RuntimeException {

    Object entity // the entity that could not be persisted

    public EntityPersistException(Object entity) {
        this.entity = entity
    }

    Object getEntity() {
        return this.entity
    }

    public String getMessage() {
        return 'Failed to persist entity with class type [' + entity?.getClass()?.getName() + '].'
    }
}
