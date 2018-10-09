package ru.arkell.avarkom.presentation.login_flow

import android.os.Bundle
import ru.arkell.avarkom.R
import ru.arkell.avarkom.presentation.base.BaseActivity
import ru.arkell.avarkom.presentation.login_flow.router.LoginFlowRouter

class LoginFlowActivity : BaseActivity() {
  private val loginFlowRouter: LoginFlowRouter by lazy { LoginFlowRouter(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login_flow)
    showFirstWelcomeFragment()
  }

  override fun initViewElements() {
  }

  private fun showFirstWelcomeFragment() {
    loginFlowRouter.showWelcomeScreen(R.id.loginFlowContainer)
  }

  override fun initComponent() {
  }
}
