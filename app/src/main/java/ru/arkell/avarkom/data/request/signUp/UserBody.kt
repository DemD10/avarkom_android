package ru.arkell.avarkom.data.request.signUp

import ru.arkell.avarkom.extensions.default

data class UserBody(
    var password: String = String.default(),
    var phone: String = String.default(),
    var name: String = String.default(),
    var email: String = String.default()
)