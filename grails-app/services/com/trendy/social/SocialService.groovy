package com.trendy.social

import groovyx.net.http.URIBuilder

abstract class SocialService {

    def grailsApplication

    def grailsLinkGenerator

    /**
     * Returns the social service name like twitter, facebook, google+ etc
     * based on the implementation.
     *
     * @return the full url path of the social service.
     */
    abstract String getSocialServiceName()

    /**
     * Validates the user login when a redirect has occured.
     *
     * @return true, if the validation is successful.
     */
    abstract Boolean validateSocialServiceLogin(params)

    /**
     * Build and return the social server url. This url does not include
     * the service to be called on the social server.
     *
     * @return the social server url.
     */
    def getSocialServiceUrl() {
        String socialServiceName = getSocialServiceName()
        String scheme = grailsApplication.config.social[socialServiceName].scheme
        String host = grailsApplication.config.social[socialServiceName].host

        StringBuilder stringBuilder = new StringBuilder()
        stringBuilder.append(scheme).append("://")
        stringBuilder.append(host)

        if (grailsApplication.hasProperty('social.'+socialServiceName+'.port')) {
            String port = grailsApplication.config.social[socialServiceName].port
            stringBuilder.append(":").append(port)
        }

        return stringBuilder.toString()
    }

    /**
     * Generate the redirect url to which the social server will redirect to
     * when user cancels or finishes the login process there.
     *
     * @return the string representation of the redirect url from the social
     * server.
     */
    String getRedirectUrlFromSocialService() {
        String socialServiceName = getSocialServiceName()
        return grailsLinkGenerator.link(controller: 'redirect', action: socialServiceName, absolute: true)
    }

    /**
     * For social services that require token exchange for a code (or something
     * else for that matter) this method provides the redirection url required
     * to process the token information.
     *
     * @return the string representation of the redirect url from social server
     * when the token exchange has completed.
     */
    String getRedirectUrlForTokenExchangeFromSocialService() {
        String socialServiceName = getSocialServiceName()
        return grailsLinkGenerator.link(controller: 'redirect', action: socialServiceName+'TokenReceiver', absolute: true)
    }
}
