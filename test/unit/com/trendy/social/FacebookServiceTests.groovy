package com.trendy.social



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(FacebookService)
class FacebookServiceTests {

    @Before
    void setUp() {
        service.grailsApplication = grailsApplication
    }

    void testSocialServiceName() {
        assertEquals "facebook", service.getSocialServiceName()
    }

    void testSocialServiceUrl() {
        assertEquals "https://www.facebook.com", service.getSocialServiceUrl()
    }

    void testSocialServiceRedirectUrl() {
        assertEquals "https://www.facebook.com/dialog/oauth", service.getFacebookFirstTimeRedirectURL()
    }

    void testSocialServiceRedirectUrlWithParameters() {

    }
}
