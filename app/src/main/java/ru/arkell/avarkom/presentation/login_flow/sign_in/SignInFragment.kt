package ru.arkell.avarkom.presentation.login_flow.sign_in


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.fragment_sign_in.continueSignInButton
import kotlinx.android.synthetic.main.fragment_sign_in.phoneNumber
import kotlinx.android.synthetic.main.fragment_sign_in.progressBar
import kotlinx.android.synthetic.main.fragment_sign_in.signInCoordinatorContainer
import kotlinx.android.synthetic.main.fragment_sign_in.signInToolbar
import kotlinx.android.synthetic.main.fragment_sign_in.userPassword
import ru.arkell.avarkom.R
import ru.arkell.avarkom.R.string
import ru.arkell.avarkom.extensions.RxHelper
import ru.arkell.avarkom.extensions.getAvarkomApplication
import ru.arkell.avarkom.extensions.hide
import ru.arkell.avarkom.extensions.show
import ru.arkell.avarkom.extensions.showSnackbar
import ru.arkell.avarkom.presentation.login_flow.BaseLoginFragment
import ru.arkell.avarkom.presentation.login_flow.sign_in.di.DaggerSignInScreenComponent
import ru.arkell.avarkom.presentation.login_flow.sign_in.di.SignInScreenComponent
import ru.arkell.avarkom.presentation.login_flow.sign_in.di.SignInScreenModule
import ru.arkell.avarkom.presentation.main.AvarkomRouter
import javax.inject.Inject


class SignInFragment : BaseLoginFragment(), SignInView {
  private lateinit var component: SignInScreenComponent
  private val avarkomRouter: AvarkomRouter by lazy { AvarkomRouter(this.activity) }

  @Inject
  lateinit var presenter: SignInPresenter

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_sign_in, container, false)
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
    signInToolbar.setNavigationOnClickListener {
      presenter.clearUserData()
      activity.onBackPressed()
    }
    continueSignInButton.setOnClickListener { presenter.signIn() }
    continueSignInButton.isEnabled = false
    initTextListeners()
  }

  private fun initTextListeners() {
    setPhoneMask(phoneNumber)

    val phoneObservable = textChangeObservable(phoneNumber)
    phoneObservable
        .compose(RxHelper.applyObservableScheduler())
        .subscribe { name ->
          presenter.let {
            it.signInData().username =
                "${name.text().toString().replace("\\s".toRegex(), "")}"
          }
        }

    val passwordObservable = textChangeObservable((userPassword))
    passwordObservable
        .compose(RxHelper.applyObservableScheduler())
        .subscribe { name ->
          presenter.let {
            it.signInData().password = "${name.text()}"
          }
        }

    val isSignInEnabled: Observable<Boolean> = Observable.combineLatest(
        phoneObservable,
        passwordObservable,
        BiFunction { u, p
          ->
          u.text().isNotEmpty() && p.text().isNotEmpty()
        })

    isSignInEnabled
        .compose(RxHelper.applyObservableScheduler())
        .subscribe { continueSignInButton.isEnabled = it }
  }

  override fun initComponent() {
    component = DaggerSignInScreenComponent.builder()
        .persistenceComponent(context.getAvarkomApplication().persistenceComponent())
        .signInScreenModule(SignInScreenModule())
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
    signInCoordinatorContainer.showSnackbar(getString(string.sign_in_error))
  }

  override fun showMainScreen() {
    avarkomRouter.startMainScreen()
  }
}
