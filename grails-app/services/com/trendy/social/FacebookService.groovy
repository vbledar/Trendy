package com.trendy.social

import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import groovyx.net.http.URIBuilder

class FacebookService extends SocialService {

    private transient final String serviceName = "facebook"

    /**
     * Return the social service name. In this implementation it
     * is facebook.
     *
     * @return the social service name.
     */
    String getSocialServiceName() {
        return serviceName
    }

    /**
     * Generate the facebook login url where the usr is redirected to login
     * in his/her facebook account. This url contains the social server url
     * accompanied with the service path and required parameters.
     *
     * @return the redirection url for the user to login to the facebook
     * application.
     */
    def getFacebookFirstTimeRedirectURL() {
        String socialServiceUrl = getSocialServiceUrl()

        String socialServiceName = getSocialServiceName()
        String path = grailsApplication.config.social[socialServiceName].path

        String clientId = grailsApplication.config.social[socialServiceName].app_id
        String redirectUrlFromSocial = getRedirectUrlFromSocialService()

        StringBuilder stringBuilder = new StringBuilder(socialServiceUrl)
        stringBuilder.append("/").append(path)
        stringBuilder.append("?")
        stringBuilder.append("client_id").append("=").append(clientId)
        stringBuilder.append("&").append("redirect_uri").append("=").append(redirectUrlFromSocial)

        return stringBuilder.toString()
    }

    /**
     * Validate login procedure on social service.
     *
     * @param params, list of parameters required to proceed with
     * validation.
     *
     * @return true, if the validation is successful.
     */
    Boolean validateSocialServiceLogin(params) {

        exchangeCodeForAccessToken(params)

        return Boolean.TRUE
    }

    def exchangeCodeForAccessToken(params) {
        String socialServiceName = getSocialServiceName()
        String scheme = grailsApplication.config.social[socialServiceName].scheme
        String graphHost = grailsApplication.config.social[socialServiceName].graph_host
        String accessTokenPath = grailsApplication.config.social[socialServiceName].access_token_path

        String clientId = grailsApplication.config.social[socialServiceName].app_id
        String appSecret = grailsApplication.config.social[socialServiceName].app_secret

        String redirectUrlFromSocial = getRedirectUrlFromSocialService()

        StringBuilder stringBuilder = new StringBuilder()
        stringBuilder.append(scheme).append("://")
        stringBuilder.append(graphHost)

        if (grailsApplication.hasProperty('social.'+socialServiceName+'.port')) {
            String port = grailsApplication.config.social[socialServiceName].port
            stringBuilder.append(":").append(port)
        }

        log.debug 'Url request is: ' + stringBuilder.toString()
        log.debug 'Path is: ' + accessTokenPath

        def httpGetRequest = new HTTPBuilder()
        httpGetRequest.request('https://graph.facebook.com', Method.GET, ContentType.TEXT) { req ->
            uri.path = '/'+accessTokenPath
            uri.query = [client_id: clientId, redirect_uri: redirectUrlFromSocial, client_secret: appSecret, 'code': params.code]

            log.debug 'Url is: ' + uri.toString()

            response.success = { resp, reader ->
                log.debug 'Response status: ${resp.statusLine}'
                log.debug 'Headers: -----------'
                resp.headers.each { h ->
                    log.debug '${h.name}: ${h.value}'
                }
                log.debug 'Response data: -------'
                log.debug reader
            }

            response.'400' = {
                println 'Bad request retrieved...'
            }

            response.'404' = {
                log.debug 'Service not found...'
            }

            response.failure = { resp, reader ->
                log.debug resp.statusLine
            }
        }
    }

}
