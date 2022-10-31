package net.cequals.greatdictator.speechcore.model

import net.cequals.greatdictator.speechcore.SpeechCore
import net.cequals.greatdictator.speechcore.service.Speech
import net.cequals.greatdictator.speechcore.service.http.CSpeechClient.SpeechDataRetriever
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf

class ModulesForJava(val speechDataRetriever: SpeechDataRetriever, val speech: Speech): KoinComponent {
  val viewModelCore: SpeechCore by inject { parametersOf(speechDataRetriever, speech) }
}

fun start() {
  startKoin {
    modules(
      modules
    )
  }
}