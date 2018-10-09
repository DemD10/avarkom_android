package ru.arkell.avarkom.presentation.login_flow.sign_up


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_sign_up.continueSignUpButton
import kotlinx.android.synthetic.main.fragment_sign_up.progressBar
import kotlinx.android.synthetic.main.fragment_sign_up.signUpCoordinatorContainer
import kotlinx.android.synthetic.main.fragment_sign_up.signUpToolbar
import kotlinx.android.synthetic.main.fragment_sign_up.userEmail
import kotlinx.android.synthetic.main.fragment_sign_up.userName
import kotlinx.android.synthetic.main.fragment_sign_up.userPassword
import kotlinx.android.synthetic.main.fragment_sign_up.userPhone
import ru.arkell.avarkom.R
import ru.arkell.avarkom.R.string
import ru.arkell.avarkom.extensions.getAvarkomApplication
import ru.arkell.avarkom.extensions.hide
import ru.arkell.avarkom.extensions.show
import ru.arkell.avarkom.extensions.showSnackbar
import ru.arkell.avarkom.presentation.base.BaseActivity
import ru.arkell.avarkom.presentation.login_flow.BaseLoginFragment
import ru.arkell.avarkom.presentation.login_flow.router.LoginFlowRouter
import ru.arkell.avarkom.presentation.login_flow.sign_up.di.DaggerSignUpScreenComponent
import ru.arkell.avarkom.presentation.login_flow.sign_up.di.SignUpScreenComponent
import ru.arkell.avarkom.presentation.login_flow.sign_up.di.SignUpScreenModule
import javax.inject.Inject


class SignUpFragment : BaseLoginFragment(), SignUpView {
  private lateinit var component: SignUpScreenComponent
  private val loginFlowRouter: LoginFlowRouter by lazy { LoginFlowRouter(activity as BaseActivity) }

  @Inject
  lateinit var presenter: SignUpPresenter

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_sign_up, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    presenter.onAttach(this)
    initViewElements()
  }

  override fun onDetach() {
    super.onDetach()
    presenter.onDetach()
  }

  override fun initViewElements() {
    signUpToolbar.setNavigationOnClickListener {
      presenter.clearUserData()
      activity.onBackPressed() }
    continueSignUpButton.setOnClickListener { presenter.signUp() }
    initTextListeners()
  }

  private fun initTextListeners() {
    setPhoneMask(userPhone)

    textChangeObservable(userName).subscribe { name ->
      presenter.let {
        it.user().name = "${name.text()}"
      }
    }
    textChangeObservable(userEmail).subscribe { name ->
      presenter.let {
        it.user().email = "${name.text()}"
      }
    }
    textChangeObservable((userPhone)).subscribe { name ->
      presenter.let {
        it.user().phone = "${name.text().toString().replace("\\s".toRegex(), "")}"
      }
    }
    textChangeObservable((userPassword)).subscribe { name ->
      presenter.let {
        it.user().password = "${name.text()}"
      }
    }
  }

  override fun initComponent() {
    component = DaggerSignUpScreenComponent.builder()
        .persistenceComponent(context.getAvarkomApplication().persistenceComponent())
        .signUpScreenModule(SignUpScreenModule())
        .build()

    component.inject(this)
  }

  override fun showLoading() {
    progressBar.show()
  }

  override fun hideLoading() {
    progressBar.hide(true)
  }

  override fun showError() {
    signUpCoordinatorContainer.showSnackbar(getString(string.sign_up_error))
  }

  override fun showConfirmationView(id: Int, phone: String) {
    loginFlowRouter.showConfirmationScreen(id , phone, R.id.loginFlowContainer)
  }
}
