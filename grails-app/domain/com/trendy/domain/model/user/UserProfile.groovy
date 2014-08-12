package com.trendy.domain.model.user

class UserProfile {

    String username
    String password

    String firstname
    String lastname

    String email

    static constraints = {
        username nullable: true, size: 5..15, unique: true
        password nullable: true, size: 5..15
        firstname nullable: true, size: 2..30
        lastname nullable: true, size: 2..30
        email nullable: false, email: true
    }
}
