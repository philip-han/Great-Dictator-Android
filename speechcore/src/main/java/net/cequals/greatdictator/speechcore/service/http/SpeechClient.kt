package net.cequals.greatdictator.speechcore.service.http

import net.cequals.greatdictator.speechcore.model.dto.SpeechData
import net.cequals.lib.kotlin.ResourceProperties
import net.cequals.lib.nullsafe.Op

interface SpeechClient {

  companion object SpeechClient {
    private val resourceProperties = ResourceProperties("nlpserver.properties")
    private val SCHEME = "http"
    private val HOST = resourceProperties.properties.getProperty("nlp.host") ?: throw RuntimeException("nlp server host address is undefined")
    private val PATH = "/?properties={\"outputFormat\":\"json\",\"annotators\":\"parse,sentiment,ssplit\"}"
    private val PORT = resourceProperties.properties.getProperty("nlp.port") ?: throw RuntimeException("nlp server port is undefined")
  }

  suspend fun parseSpeech(speech: String): Op<SpeechData>

  fun uri() = "${SCHEME}://$HOST:$PORT$PATH"

}