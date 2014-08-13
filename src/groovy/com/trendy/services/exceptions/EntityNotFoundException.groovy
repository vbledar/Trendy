package com.trendy.services.exceptions

/**
 * Created by nvasili on 13/8/2014.
 */
class EntityNotFoundException extends RuntimeException {

    String id
    String className

    String sessionId

    public EntityNotFoundException(String id, String className, String sessionId) {
        this.id = id
        this.className = className
        this.sessionId = sessionId
    }

    public String getId() {
        return this.id
    }

    public String getClassName() {
        return this.className
    }

    public String getMessage() {
        if (sessionId) {
            return "Entriy of ${className} with session id ${sessionId} not found in database."
        } else {
            return "Entity of type ${className} with id ${id} not found in database."
        }
    }
}
