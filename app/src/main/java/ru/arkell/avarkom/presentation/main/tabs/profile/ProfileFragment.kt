package ru.arkell.avarkom.presentation.main.tabs.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_profile.appbar
import kotlinx.android.synthetic.main.fragment_profile.feedback_count
import kotlinx.android.synthetic.main.fragment_profile.profile_deals
import kotlinx.android.synthetic.main.fragment_profile.profile_name
import kotlinx.android.synthetic.main.fragment_profile.profile_number
import kotlinx.android.synthetic.main.fragment_profile.profit_sum
import kotlinx.android.synthetic.main.fragment_profile.progressBar2
import kotlinx.android.synthetic.main.fragment_profile.successful_count
import ru.arkell.avarkom.R
import ru.arkell.avarkom.extensions.getAvarkomApplication
import ru.arkell.avarkom.presentation.base.BaseFragment
import ru.arkell.avarkom.presentation.main.tabs.profile.di.DaggerProfileScreenComponent
import ru.arkell.avarkom.presentation.main.tabs.profile.di.ProfileScreenComponent
import ru.arkell.avarkom.presentation.main.tabs.profile.di.ProfileScreenModule
import javax.inject.Inject

class ProfileFragment : BaseFragment(), ProfileView {

    private lateinit var component: ProfileScreenComponent

    @Inject
    lateinit var presenter: ProfilePresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onAttach(this)
        initViewElements()
        presenter.onViewCreated()
    }

    override fun initViewElements() {
    }

    override fun showLoading() {
        profile_deals.visibility = GONE
        appbar.visibility = GONE

        progressBar2.visibility = VISIBLE
    }

    override fun hideLoading() {
        profile_deals.visibility = VISIBLE
        appbar.visibility = VISIBLE

        progressBar2.visibility = GONE
    }

    override fun showError() {
    }

    override fun showUserPhoto() {
    }

    override fun showUserName(name: String) {
        profile_name?.text = name
    }

    override fun showUserNumber(number: String) {
        profile_number?.text = number
    }

    override fun showFeedbackCount(count: String) {
        feedback_count?.text = count
    }

    override fun showSuccessfulCount(count: String) {
        successful_count?.text = count
    }

    override fun initComponent() {
        component = DaggerProfileScreenComponent.builder()
            .persistenceComponent(context.getAvarkomApplication().persistenceComponent())
            .profileScreenModule(ProfileScreenModule())
            .build()

        component.inject(this)
    }

    override fun showProfit(profit: String) {
        profit_sum?.text = profit
    }

    override fun showDeals(deals: List<String>) {
    }
}