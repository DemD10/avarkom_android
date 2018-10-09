package ru.arkell.avarkom.data.response.signUp

import ru.arkell.avarkom.extensions.default

data class UserProfile(
    var id: Int = 0,
    var name: String = String.default(),
    var phone: String = String.default(),
    var email: String = String.default(),
    var username: String = String.default(),
    var token: String = String.default()
)
