package com.trendy.social

import com.trendy.domain.model.social.SocialServiceAuthentication
import com.trendy.domain.model.user.UserProfile
import com.trendy.services.exceptions.EntityPersistException
import com.trendy.services.exceptions.social.facebook.FacebookParamMissingException
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
     * Facebook generates an access token by exchanging a code retrieve earlier
     * through the authentication process.
     *
     * @return <code>Boolean.TRUE</code>
     */
    Boolean requiresExchangeForToken() {
        return Boolean.TRUE
    }

    /**
     * Process the authenticate procedure response for facebook. Retrieve the code
     * parameter and apply it in the social service authentication record to be
     * used in retrieving the access token.
     *
     * @param socialServiceAuthentication the social service authentication record
     * used in the facebook oauth procedure.
     * @param params list of parameters retrieved from facebook authentication procedure.
     */
    def processSocialServiceAuthenticationResponse(SocialServiceAuthentication socialServiceAuthentication, params) {
        if (!params.code) {
            log.error 'Code parameters not found in the params list.'
            log.error 'For facebook authentication the code parameter is required to receive an access token.'
            throw new FacebookParamMissingException("code")
        }

        socialServiceAuthentication.tokenExchangeable = params.code
        socialServiceAuthentication.urlForTokenExchange = getSocialServiceTokenExchangeUrl()
        if (!socialServiceAuthentication.save()) {
            log.error 'Failed to persist the social service authentication entry during configuration.'
            socialServiceAuthentication.errors.each {
                log.error it
            }
            throw new EntityPersistException(socialServiceAuthentication)
        }
    }

    /**
     * Generate social service authentication url for facebook authentication.
     * Client is redirected to this url to login on facebook social service.
     *
     * @return the social service authentication url for facebook authentication.
     */
    String getSocialServiceAuthenticationUrl(SocialServiceAuthentication socialServiceAuthentication) {
        String socialServiceUrl = getSocialServiceUrl()

        String socialServiceName = getSocialServiceName()
        String path = grailsApplication.config.social[socialServiceName].path

        String clientId = grailsApplication.config.social[socialServiceName].app_id
        String redirectUrlFromSocial = getRedirectUrlFromSocialService(socialServiceAuthentication)

        StringBuilder stringBuilder = new StringBuilder(socialServiceUrl)
        stringBuilder.append("/").append(path)
        stringBuilder.append("?")
        stringBuilder.append("client_id").append("=").append(clientId)
        stringBuilder.append("&").append("redirect_uri").append("=").append(redirectUrlFromSocial)

        return stringBuilder.toString()
    }

    /**
     * Generate social service validation url. This url is used to validate the
     * token or use an exchangeable to receive a token.
     *
     * @return the social service validation url.
     */
    String getSocialServiceTokenExchangeUrl() {
        String socialServiceName = getSocialServiceName()
        String scheme = grailsApplication.config.social[socialServiceName].scheme
        String graphHost = grailsApplication.config.social[socialServiceName].graph_host

        StringBuilder stringBuilder = new StringBuilder()
        stringBuilder.append(scheme).append("://")
        stringBuilder.append(graphHost)

        if (grailsApplication.hasProperty('social.'+socialServiceName+'.port')) {
            String port = grailsApplication.config.social[socialServiceName].port
            stringBuilder.append(":").append(port)
        }

        return stringBuilder.toString()
    }

    def receiveAccessTokenFromSocialService(SocialServiceAuthentication socialServiceAuthentication) {
        String accessTokenPath = grailsApplication.config.social[socialServiceName].access_token_path
        String clientId = grailsApplication.config.social[socialServiceName].app_id
        String appSecret = grailsApplication.config.social[socialServiceName].app_secret

        log.debug 'Token exchangeable is ' + socialServiceAuthentication.tokenExchangeable
        log.debug 'Redirection url handler is ' + socialServiceAuthentication.authenticationHandlerUrl

        def httpGetRequest = new HTTPBuilder()
        httpGetRequest.request('https://graph.facebook.com', Method.GET, ContentType.TEXT) { req ->
            uri.path = '/'+accessTokenPath
            uri.query = [client_id: clientId, redirect_uri: socialServiceAuthentication.authenticationHandlerUrl, client_secret: appSecret, 'code': socialServiceAuthentication.tokenExchangeable]

            response.success = { resp, reader ->
                log.debug 'Successful response retrieved from facebook...'

                BufferedReader br = new BufferedReader(reader)
                String dataReader = br.readLine()

                int indexOfAccessToke = dataReader.indexOf("access_token")
                int indexOfExpire = dataReader.indexOf("expires")

                String accessToken = dataReader.substring(indexOfAccessToke+"access_token".size()+1, indexOfExpire-1)
                String expiresIn = dataReader.substring(indexOfExpire+"expires".size()+1)

                log.debug 'Access token received is: ' + accessToken
                log.debug 'Access token expires in: ' + expiresIn

                socialServiceAuthentication.accessToken = accessToken
                socialServiceAuthentication.accessTokenLifeDuration = Long.parseLong(expiresIn)

                if (!socialServiceAuthentication.save()) {
                    log.error 'Failed to update social service authentication with access token and expire duration information.'
                    socialServiceAuthentication.errors.each {
                        log.error it
                    }
                    throw new EntityPersistException(socialServiceAuthentication)
                }
            }

            response.'400' = { resp, reader ->
                println 'Bad request retrieved...'
                log.debug 'Printing something from response: ' + resp
            }

            response.'404' = {
                log.debug 'Service not found...'
            }

            response.failure = { resp, reader ->
                log.debug resp.statusLine
            }
        }
    }

    def getUserId(SocialServiceAuthentication socialServiceAuthentication) {
        String apiVersion = grailsApplication.config.social[socialServiceAuthentication.serviceName].graph_api_version
        log.debug 'Api version is: ' + apiVersion

        def httpGetRequest = new HTTPBuilder()
        httpGetRequest.request('https://graph.facebook.com', Method.GET, ContentType.JSON) { req ->
            uri.path = "/${apiVersion}/me"
            uri.query = [fields: 'id', access_token: socialServiceAuthentication.accessToken]

            response.success = { resp, reader ->
                log.debug 'Successful response retrieved from facebook...'
                log.debug 'User id is: ' + reader.id
                return reader.id
            }

            response.'400' = { resp, reader ->
                println 'Bad request retrieved...'
                log.debug 'Printing something from response: ' + resp
            }

            response.'404' = {
                log.debug 'Service not found...'
            }

            response.failure = { resp, reader ->
                log.debug resp.statusLine
            }
        }
    }

    def retrieveOrCreateUser(String userid, SocialServiceAuthentication socialServiceAuthentication) {
        UserProfile userProfile = UserProfile.findByFacebookId(userid)
        if (userProfile) {
            log.debug "Found user with id ${userid} by use of facebook account."
        } else {
            log.debug "User not found with id ${userid} and will be created from scratch."
            String apiVersion = grailsApplication.config.social[socialServiceAuthentication.serviceName].graph_api_version
            log.debug 'Api version is: ' + apiVersion

            def httpGetRequest = new HTTPBuilder()
            httpGetRequest.request('https://graph.facebook.com', Method.GET, ContentType.JSON) { req ->
                uri.path = "/${apiVersion}/me"
                uri.query = [fields: 'id,first_name,last_name,email', access_token: socialServiceAuthentication.accessToken]

                response.success = { resp, reader ->
                    log.debug 'Successful response retrieved from facebook...'
                    log.debug 'User information retrieved are: ' + reader
                    log.debug 'User id is: ' + reader.id
                    log.debug 'User firstname is: ' + reader.first_name
                    log.debug 'User lastname is: ' + reader.last_name
                    log.debug 'User email is: ' + reader.email

                    userProfile = new UserProfile(facebookId: reader.id, firstname: reader.first_name, lastname: reader.last_name, email: reader.email)
                    if (!userProfile.save()) {
                        log.error 'Failed to persist user profile.'
                        userProfile.errors.each {
                            log.error it
                        }
                        throw new EntityPersistException(userProfile)
                    }

                    return userProfile
                }

                response.'400' = { resp, reader ->
                    println 'Bad request retrieved...'
                    log.debug 'Printing something from response: ' + resp
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
}
