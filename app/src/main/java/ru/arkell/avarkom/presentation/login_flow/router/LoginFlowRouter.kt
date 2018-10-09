package ru.arkell.avarkom.presentation.login_flow.router

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import ru.arkell.avarkom.R
import ru.arkell.avarkom.presentation.base.BaseActivity
import ru.arkell.avarkom.presentation.login_flow.confirmation.ConfirmationFragment
import ru.arkell.avarkom.presentation.login_flow.sign_in.SignInFragment
import ru.arkell.avarkom.presentation.login_flow.sign_up.SignUpFragment
import ru.arkell.avarkom.presentation.login_flow.welcome.WelcomeFragment

class LoginFlowRouter(val activity: BaseActivity) {

  fun showSignInScreen(containerId: Int) {
    switchTo(SignInFragment(), containerId)
  }

  fun showSignUpScreen(containerId: Int) {
    switchTo(SignUpFragment(), containerId)
  }

  fun showConfirmationScreen(id: Int, userPhone: String, containerId: Int) {
    switchTo(ConfirmationFragment.newInstance(id, userPhone), containerId)
  }

  fun showWelcomeScreen(containerId: Int) {
    switchTo(WelcomeFragment(), containerId)
  }

  private fun switchTo(fragment: Fragment, containerId: Int) {
    val fragmentManager = getFragmentManager()
    val transaction = fragmentManager.beginTransaction()
    transaction.setCustomAnimations(R.anim.slide_left, R.anim.slide_right, R.anim.slide_left,
        R.anim.slide_right)
    transaction.replace(containerId, fragment, fragment.tag)
    if (getFragmentManager().fragments != null) {
      transaction.addToBackStack(fragment.tag)
    }
    transaction.commit()
  }

  private fun getFragmentManager(): FragmentManager {
    return activity.supportFragmentManager
  }
}