package ru.arkell.avarkom.presentation.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

  override fun onCreate(@Nullable savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
  }

  abstract fun initViewElements()
  abstract fun initComponent()
}
