package com.trendy.user

import com.trendy.domain.model.user.UserProfile

class RegistrationController {

    /**
     * Render user registration form.
     */
    def userRegistration() {}

    def registerUser() {
        log.debug 'User registration requested with parameters:'
        log.debug params

        UserProfile userProfile = new UserProfile()
        bindData(userProfile, params)

        String username = params.username
        String password = params.password
        String rPassword = params.passwordRepeat
        String email = params.email

        if (!username || !password || !email) {
            flash.warning = 'Please, provide all required fields.'
            render (view: 'userRegistration', model: [userProfile: userProfile])
            return
        }

        if (!password.equals(rPassword)) {
            flash.warning = 'Passwords provided do not match.'
            render (view: 'userRegistration', model: [userProfile: userProfile])
            return
        }

        if (!userProfile.validate()) {
            flash.warning = 'Please, correct all errors and resubmit.'
            render (view: 'userRegistration', model: [userProfile: userProfile])
            return
        }

        flash.success = 'Registration completed successfully. Thank you.'
        session["userProfile"] = userProfile

        redirect(controller: 'landing', action: 'index')
    }
}
