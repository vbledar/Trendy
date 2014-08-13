package com.trendy

import com.trendy.domain.model.social.SocialServiceAuthentication
import com.trendy.services.exceptions.EntityPersistException

class LandingController {

    def facebookService

    def index() {
    }

    def facebookLogin() {
        boolean successful = true
        SocialServiceAuthentication socialServiceAuthentication
        try {
            log.debug 'Initializing social service creation and configuration'
            socialServiceAuthentication = facebookService.createSocialServiceAuthentication(session.getId())
            log.debug 'social service created'
        } catch(EntityPersistException epex) {
            log.error message(code: 'entity.exception.occurred')
            log.error message(code: 'entity.persistence.failure', args: [epex.getMessage()])
            flash.error = message(code: 'social.service.authentication.failure.unknown')
            successful = false
        } catch(RuntimeException rex) {
            log.error message(code: 'runtime.exception.occurred')
            log.error message(code: 'entity.persistence.failure', args: [rex.getMessage()])
            flash.error = message(code: 'social.service.authentication.failure.unknown')
            successful = false
        } catch(Exception ex) {
            log.error message(code: 'exception.occurred')
            log.error message(code: 'exception.failure', args: [ex.getMessage()])
            flash.error = message(code: 'social.service.authentication.failure.unknown')
            successful = false
        }

        if (!successful) {
            log.debug 'Redirecting to application initial page due to exception occurrence...'
            redirect(action: 'index')
            return
        }

        log.debug 'Redirecting to facebook social service for authentication...'
        log.debug 'with redirection url handler being ' + socialServiceAuthentication.authenticationHandlerUrl
        redirect(url: socialServiceAuthentication.urlForAuthentication)
    }
}
