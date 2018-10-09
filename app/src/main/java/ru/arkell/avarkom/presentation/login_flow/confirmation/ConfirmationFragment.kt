package ru.arkell.avarkom.presentation.login_flow.confirmation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_confirmation.activationCode
import kotlinx.android.synthetic.main.fragment_confirmation.continueConfirmationButton
import kotlinx.android.synthetic.main.fragment_confirmation.phoneNumber
import kotlinx.android.synthetic.main.fragment_confirmation.progressBar
import kotlinx.android.synthetic.main.fragment_confirmation.signInCoordinatorContainer
import ru.arkell.avarkom.R
import ru.arkell.avarkom.R.string
import ru.arkell.avarkom.extensions.RxHelper
import ru.arkell.avarkom.extensions.getAvarkomApplication
import ru.arkell.avarkom.extensions.hide
import ru.arkell.avarkom.extensions.show
import ru.arkell.avarkom.extensions.showSnackbar
import ru.arkell.avarkom.presentation.login_flow.BaseLoginFragment
import ru.arkell.avarkom.presentation.login_flow.confirmation.di.ConfirmationScreenComponent
import ru.arkell.avarkom.presentation.login_flow.confirmation.di.ConfirmationScreenModule
import ru.arkell.avarkom.presentation.login_flow.confirmation.di.DaggerConfirmationScreenComponent
import javax.inject.Inject


class ConfirmationFragment : BaseLoginFragment(), ConfirmationView {
  private lateinit var component: ConfirmationScreenComponent

  @Inject
  lateinit var presenter: ConfirmationPresenter

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_confirmation, container, false)
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
    val phone = arguments.getString("phone", "")
    phoneNumber.setText(phone)
    continueConfirmationButton.setOnClickListener {
      presenter.confirmActivation(arguments.getInt("user_id"),
          activationCode.text.toString().toInt())
    }

    textChangeObservable(activationCode)
        .compose(RxHelper.applyObservableScheduler())
        .subscribe { code ->
          continueConfirmationButton.isEnabled = code.text().length >= 4
        }
  }

  override fun initComponent() {
    component = DaggerConfirmationScreenComponent.builder()
        .persistenceComponent(context.getAvarkomApplication().persistenceComponent())
        .confirmationScreenModule(ConfirmationScreenModule())
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

  override fun endLoginFlow() {
    Toast.makeText(activity, "success login", Toast.LENGTH_SHORT).show()
  }

  override fun showActivationError(message: String) {
    signInCoordinatorContainer.showSnackbar(message)
  }

  companion object {
    fun newInstance(userId: Int, phoneNumber: String): ConfirmationFragment {
      val fragment = ConfirmationFragment()
      val bundle = Bundle()
      bundle.putString("phone", phoneNumber)
      bundle.putInt("user_id", userId)
      fragment.arguments = bundle
      return fragment
    }
  }

  
}
