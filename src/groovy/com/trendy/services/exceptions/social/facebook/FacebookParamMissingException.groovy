package com.trendy.services.exceptions.social.facebook

/**
 * Created by nvasili on 13/8/2014.
 */
class FacebookParamMissingException extends RuntimeException {

    String parameterName

    public FacebookParamMissingException(String parameterName) {
        this.parameterName = parameterName
    }

    public String getMessage() {
        return "Parameter ${parameterName} is missing."
    }
}
