package net.cequals.greatdictator.speechcore.model

import kotlinx.coroutines.CoroutineScope
import net.cequals.greatdictator.speechcore.SpeechCore
import net.cequals.greatdictator.speechcore.service.http.CSpeechClient
import net.cequals.greatdictator.speechcore.service.http.CSpeechClient.SpeechDataRetriever
import net.cequals.greatdictator.speechcore.service.http.KtorSpeechClient
import net.cequals.greatdictator.speechcore.service.http.SpeechClient
import org.koin.dsl.module

var modules = module {

  single<SpeechCore> { params ->
    if (params.getOrNull<CoroutineScope>(CoroutineScope::class) == null) SpeechCore(get {params})
    else SpeechCore(get {params}, params.get())
  }
  single<SpeechModel> { p -> SpeechModel(get {p}, get {p}) }
  single<SpeechClient> { params ->
    if (params.getOrNull<SpeechDataRetriever>(SpeechDataRetriever::class) == null) KtorSpeechClient()
    else CSpeechClient(params.get())
  }

}