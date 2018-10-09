package ru.arkell.avarkom.data.request.activation

import com.google.gson.annotations.SerializedName

data class ActivationBody(
    @SerializedName("sms_code") var smsCode: Int)
