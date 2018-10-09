package ru.arkell.avarkom.data.request.signIn

import ru.arkell.avarkom.extensions.default

data class SignInBody(var username: String = String.default(),
    var password: String = String.default()) {
}