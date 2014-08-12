package com.trendy

class LandingController {

    def facebookService

    def index() {
        log.debug 'In landing controller...'
        log.debug 'Redirecting to facebook...'
        String url = facebookService.facebookFirstTimeRedirectURL

        redirect(url: url)
    }
}
