package ru.arkell.avarkom.presentation.main.tabs.profile

import ru.arkell.avarkom.presentation.base.BaseLCEView

interface ProfileView : BaseLCEView {

    fun showUserPhoto()

    fun showUserName(name: String)

    fun showUserNumber(number: String)

    fun showFeedbackCount(count: String)

    fun showSuccessfulCount(count: String)

    fun showProfit(profit: String)

    fun showDeals(deals: List<String>)
}