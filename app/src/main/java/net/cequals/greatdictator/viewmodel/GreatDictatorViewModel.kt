package net.cequals.greatdictator.viewmodel

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import net.cequals.greatdictator.R
import net.cequals.greatdictator.speechcore.SpeechCore
import net.cequals.lib.kotlin.map
import net.cequals.lib.nullsafe.Scoped
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class GreatDictatorViewModel(application: Application): AndroidViewModel(application), KoinComponent {

  private val speechCore: SpeechCore by inject { parametersOf(application.applicationContext, viewModelScope) }
  val speechFlow = speechCore.speechFlow
  val errorFlow: Flow<Scoped<String>> = speechCore.errorChannel.receiveAsFlow()
  val speechRecognizerStateFlow = speechCore.speechRecognizerStateFlow.map(viewModelScope) {
    if (it) Pair(R.string.listening, Color.Red)
    else Pair(R.string.sleeping, Color.Black)
  }

  fun onEvent() {
    speechCore.toggle()
  }

}