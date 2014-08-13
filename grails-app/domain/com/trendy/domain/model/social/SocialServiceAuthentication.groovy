package com.trendy.domain.model.social

class SocialServiceAuthentication {

    String sessionId

    Date dateCreated
    Date lastUpdated

    String tokenExchangeable
    String accessToken
    Long accessTokenLifeDuration

    String urlForAuthentication
    String urlForTokenExchange
    String authenticationHandlerUrl

    String serviceName

    static constraints = {
        sessionId nullable: false

        tokenExchangeable nullable: true
        accessToken nullable: true
        accessTokenLifeDuration nullable: true

        urlForAuthentication nullable: true
        urlForTokenExchange nullable: true
        authenticationHandlerUrl nullable: true

        serviceName nullable: false
    }

    static mapping = {
        tokenExchangeable type: 'text'
        accessToken type: 'text'
    }
}
