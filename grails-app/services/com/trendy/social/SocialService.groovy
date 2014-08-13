package com.trendy.social

import com.trendy.domain.model.social.SocialServiceAuthentication
import com.trendy.services.exceptions.EntityNotFoundException
import com.trendy.services.exceptions.EntityPersistException
import groovyx.net.http.URIBuilder

abstract class SocialService {

    def grailsApplication

    def grailsLinkGenerator

    def messageSource

    /**
     * Returns the social service name like twitter, facebook, google+ etc
     * based on the implementation.
     *
     * @return the full url path of the social service.
     */
    abstract String getSocialServiceName()

    /**
     * Defines if the social service requires that an exchangeable is traded
     * for an access token.
     *
     * @return <code>Boolean.TRUE</code> if the access token is received through
     * an exchange.
     */
    abstract Boolean requiresExchangeForToken()

    def findSocialServiceAuthentication(String sessionId) {
        SocialServiceAuthentication socialServiceAuthentication = SocialServiceAuthentication.findBySessionId(sessionId)
        if (!socialServiceAuthentication) {
            log.debug 'Failed to find social service authentication record.'
            throw new EntityNotFoundException(params.id, SocialServiceAuthentication.getClass().getName(), sessionId)
        }
        return socialServiceAuthentication
    }

    def findSocialServiceAuthentication(params) {
        SocialServiceAuthentication socialServiceAuthentication = SocialServiceAuthentication.findByIdAndServiceName(params.id, getSocialServiceName())
        if (!socialServiceAuthentication) {
            log.debug 'Failed to find social service authentication record.'
            throw new EntityNotFoundException(params.id, SocialServiceAuthentication.getClass().getName(), null)
        }
        return socialServiceAuthentication
    }

    /**
     * Create a <code>SocialServiceAuthentication</code> entry.
     *
     * @return the created instance of <code>SocialServiceAuthentication</code>.
     */
    SocialServiceAuthentication createSocialServiceAuthentication(String sessionId) {
        SocialServiceAuthentication socialServiceAuthentication = new SocialServiceAuthentication(sessionId: sessionId)
        socialServiceAuthentication.serviceName = getSocialServiceName()

        if (!socialServiceAuthentication.save()) {
            log.error 'Failed to persist the social service authentication entry.'
            socialServiceAuthentication.errors.each {
                log.error it
            }
            throw new EntityPersistException(socialServiceAuthentication)
        }

        setupSocialServiceAuthentication(socialServiceAuthentication)

        return socialServiceAuthentication
    }

    /**
     * Configure the social service authentication record accordingly.
     *
     * @param socialServiceAuthentication, the record to configure.
     *
     * @return the configured social service authentication record.
     */
    def setupSocialServiceAuthentication(SocialServiceAuthentication socialServiceAuthentication) {
        socialServiceAuthentication.urlForAuthentication = getSocialServiceAuthenticationUrl()
        socialServiceAuthentication.authenticationHandlerUrl = getRedirectUrlFromSocialService(socialServiceAuthentication)

        log.debug 'Redirection url is: ' + socialServiceAuthentication.authenticationHandlerUrl

        if (!socialServiceAuthentication.save()) {
            log.error 'Failed to persist the social service authentication entry during configuration.'
            socialServiceAuthentication.errors.each {
                log.error it
            }
            throw new EntityPersistException(socialServiceAuthentication)
        }
    }

    abstract def processSocialServiceAuthenticationResponse(SocialServiceAuthentication socialServiceAuthentication, params)

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

    abstract receiveAccessTokenFromSocialService(SocialServiceAuthentication socialServiceAuthentication)

    /**
     * Generate social service authentication url. This url is used to redirect
     * user on the social service login page.
     *
     * @return the social service authentication url.
     */
    abstract String getSocialServiceAuthenticationUrl(SocialServiceAuthentication socialServiceAuthentication)

    /**
     * Generate social service validation url. This url is used to validate the
     * token or use an exchangeable to receive a token.
     *
     * @return the social service validation url.
     */
    abstract String getSocialServiceTokenExchangeUrl()

    /**
     * Generate the redirect url to which the social server will redirect to
     * when user cancels or finishes the login process there.
     *
     * @return the string representation of the redirect url from the social
     * server.
     */
    String getRedirectUrlFromSocialService(SocialServiceAuthentication socialServiceAuthentication) {
        String socialServiceName = getSocialServiceName()
        return grailsLinkGenerator.link(controller: 'redirect', action: socialServiceName, absolute: true) + '/'
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
