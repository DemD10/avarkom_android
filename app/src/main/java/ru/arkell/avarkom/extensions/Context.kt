package ru.arkell.avarkom.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import ru.arkell.avarkom.di.AvarkomAppImpl
import ru.arkell.avarkom.presentation.main.tabs.navigator.select_new_address.SPEECH_REQUEST_CODE

inline fun <reified Activity : AppCompatActivity> Context.startScreen(
    init: Intent.() -> Unit = {}
) {
  val intent = Intent(this, Activity::class.java).apply(init)
  startActivity(intent)
}

fun Context.getAvarkomApplication() = applicationContext as AvarkomAppImpl

fun Activity.displaySpeechRecognizer(context: Activity) {
  val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
  intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
      RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
  // Start the activity, the intent will be populated with the speech text
  context.startActivityForResult(intent, SPEECH_REQUEST_CODE)
}

fun Fragment.displaySpeechRecognizer(context: Fragment) {
  val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
  intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
      RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
  // Start the activity, the intent will be populated with the speech text
  context.startActivityForResult(intent, SPEECH_REQUEST_CODE)
}
