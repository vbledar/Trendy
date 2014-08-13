package com.trendy.social

import com.trendy.domain.model.social.SocialServiceAuthentication
import com.trendy.domain.model.user.UserProfile
import com.trendy.services.exceptions.EntityNotFoundException
import com.trendy.services.exceptions.EntityPersistException

class RedirectController {

    def facebookService

    def facebook() {
        boolean successful = true

        try {
            SocialServiceAuthentication socialServiceAuthentication = facebookService.findSocialServiceAuthentication(session.getId())
            facebookService.processSocialServiceAuthenticationResponse(socialServiceAuthentication, params)
            if (facebookService.requiresExchangeForToken()) {
                log.debug 'Exchanging for access token...'
                facebookService.receiveAccessTokenFromSocialService(socialServiceAuthentication)
            }

            String userId = facebookService.getUserId(socialServiceAuthentication)
            UserProfile userProfile = facebookService.retrieveOrCreateUser(userId, socialServiceAuthentication)

            flash.success = 'Facebook login successful.'
        } catch(EntityNotFoundException enfex) {
            log.error message (code: 'entity.not.found.exception.for.social.service', args: [enfex.getClassName(), enfex.getId(), facebookService.getSocialServiceName()])
            flash.error = 'Failed to process facebook authentication.'
            successful = false
        } catch(EntityPersistException epex) {
            log.error message(code: 'entity.exception.occurred')
            log.error message(code: 'entity.persistence.failure', args: [epex.getMessage()])
            flash.error = message(code: 'social.service.authentication.failure.unknown')
            successful = false
        } catch(RuntimeException rex) {
            log.error 'Failed to get access token', rex
            flash.error = rex.getMessage()
            successful = false
        }

        if (!successful) {
            log.debug 'Redirecting to main page due to failure.'
        }

        redirect (controller: 'landing', action: 'index')
    }
}
