package ru.arkell.avarkom.presentation.login_flow.welcome

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_welcome.licenceText
import kotlinx.android.synthetic.main.fragment_welcome.signInButton
import kotlinx.android.synthetic.main.fragment_welcome.signUpButton
import ru.arkell.avarkom.R
import ru.arkell.avarkom.presentation.base.BaseActivity
import ru.arkell.avarkom.presentation.login_flow.router.LoginFlowRouter

class WelcomeFragment : Fragment() {
  private val loginFlowRouter: LoginFlowRouter by lazy { LoginFlowRouter(activity as BaseActivity) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater?.inflate(R.layout.fragment_welcome, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initViewElements()
  }

  fun initViewElements() {
    signInButton.setOnClickListener { loginFlowRouter.showSignInScreen(R.id.loginFlowContainer) }
    signUpButton.setOnClickListener { loginFlowRouter.showSignUpScreen(R.id.loginFlowContainer) }
    licenceText.setOnClickListener { }
  }
}
