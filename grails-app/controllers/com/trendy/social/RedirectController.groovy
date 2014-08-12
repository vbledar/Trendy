package com.trendy.social

class RedirectController {

    def facebookService

    def facebook() {
        log.debug 'Retrieved parameters from facebook are...'
        log.debug params

        log.debug 'Controller: ' + params.controller
        log.debug 'Action: ' + params.action

        log.debug 'Code: ' + params.code

        facebookService.exchangeCodeForAccessToken(params)
    }

    def facebookTokenReceiver() {
        log.debug 'Retrieved parameters from facebook token exchange are...'
        log.debug params
    }
}
