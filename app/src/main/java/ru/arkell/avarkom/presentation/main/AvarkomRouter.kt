package ru.arkell.avarkom.presentation.main

import android.app.Activity
import android.content.Intent
import ru.arkell.avarkom.presentation.login_flow.LoginFlowActivity

class AvarkomRouter(var context: Activity) {
  fun startLoginScreen(){
    context.finishAffinity()
    context.startActivity(Intent(context, LoginFlowActivity::class.java))
  }


  fun startMainScreen(){
    context.finishAffinity()
    context.startActivity(Intent(context, AvarkomActivity::class.java))
  }
}