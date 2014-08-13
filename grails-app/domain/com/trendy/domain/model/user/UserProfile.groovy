package com.trendy.domain.model.user

class UserProfile {

    String username
    String password

    String firstname
    String lastname

    String email

    String facebookId
    String twitterId
    String googlePlusId

    static constraints = {
        username nullable: true, size: 5..15, unique: true
        password nullable: true, size: 5..15
        firstname nullable: true, size: 2..30
        lastname nullable: true, size: 2..30
        email nullable: true, email: true

        facebookId nullable: true
        twitterId nullable: true
        googlePlusId nullable: true
    }
}
