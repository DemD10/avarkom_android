package ru.arkell.avarkom.presentation.main;

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.activity_main.bottomBar
import kotlinx.android.synthetic.main.activity_main.bottom_sheet
import kotlinx.android.synthetic.main.activity_main.tabsViewPager
import kotlinx.android.synthetic.main.bottom_sheet_other.profile_item
import ru.arkell.avarkom.R
import ru.arkell.avarkom.extensions.getAvarkomApplication
import ru.arkell.avarkom.presentation.base.BaseActivity
import ru.arkell.avarkom.presentation.base.EmptyFragment
import ru.arkell.avarkom.presentation.base.ViewPagerAdapter
import ru.arkell.avarkom.presentation.main.di.AvarkomScreenComponent
import ru.arkell.avarkom.presentation.main.di.AvarkomScreenModule
import ru.arkell.avarkom.presentation.main.di.DaggerAvarkomScreenComponent
import ru.arkell.avarkom.presentation.main.tabs.car_accident.CarAccidentFragment
import ru.arkell.avarkom.presentation.main.tabs.navigator.NavigatorFragment
import ru.arkell.avarkom.presentation.main.tabs.news.NewsFragment
import ru.arkell.avarkom.presentation.main.tabs.profile.ProfileFragment
import javax.inject.Inject

class AvarkomActivity : BaseActivity(), AvarkomView {
  @Inject
  lateinit var avarkomPresenter: AvarkomPresenter

  private lateinit var component: AvarkomScreenComponent
  private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>

  public override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initComponent()
    avarkomPresenter.onAttach(this)
    avarkomPresenter.checkIfAuthorized()
  }

  override fun onDestroy() {
    super.onDestroy()
    avarkomPresenter.onDetach()
  }

  override fun initViewElements() {
    bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet)
    profile_item.setOnClickListener { showTab(4) }
    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
      override fun onStateChanged(bottomSheet: View, newState: Int) {
        if (newState == BottomSheetBehavior.STATE_COLLAPSED)
          bottomSheet.post({ bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN) })
      }

      override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    })

    val adapter = ViewPagerAdapter(supportFragmentManager)
    adapter.addFragment(NavigatorFragment(), "navigator")
    adapter.addFragment(EmptyFragment(), "empty_tag")
    adapter.addFragment(CarAccidentFragment(), "empty_tag")
    adapter.addFragment(NewsFragment(), "news")
    adapter.addFragment(ProfileFragment(), "profile")

    tabsViewPager.offscreenPageLimit = 5
    tabsViewPager.adapter = adapter
    bottomBar.selectTabAtPosition(0)
    bottomBar.setOnTabReselectListener { position ->
      when (position) {
        R.id.action_menu ->
          bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
      }
    }
    bottomBar.setOnTabSelectListener { position ->
      when (position) {
        R.id.action_map -> showTab(0)
        R.id.action_search -> showTab(2)
        R.id.action_news -> showTab(3)
        R.id.action_menu -> bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        else -> showTab(1)
      }
    }
  }

  private fun showTab(position: Int) {
    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

    tabsViewPager.currentItem = position
  }

  override fun showAvarkomMainView() {
    setContentView(R.layout.activity_main)
    initViewElements()
  }

  override fun initComponent() {
    component = DaggerAvarkomScreenComponent.builder()
        .persistenceComponent(this.getAvarkomApplication().persistenceComponent())
        .avarkomScreenModule(AvarkomScreenModule(this))
        .build()

    component.inject(this)
  }
}
