package net.cequals.greatdictator.speechcore.service.http

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.coroutineScope
import net.cequals.greatdictator.speechcore.model.dto.SpeechData
import net.cequals.lib.nullsafe.Op

class CSpeechClient(val retriever: SpeechDataRetriever): AbstractSpeechClient() {

  val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build().adapter(SpeechData::class.java)

  override suspend fun platformParseSpeech(speech: String): SpeechData = coroutineScope {
    val s = retriever.retrieve(uri(), speech)
    moshi.fromJson(s.foldOrThrow())!!
  }

  interface SpeechDataRetriever {
    fun retrieve(uri: String, speech: String): Op<String>
  }

}