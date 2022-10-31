package net.cequals.greatdictator.speechcore.service.http

import net.cequals.greatdictator.speechcore.model.dto.SpeechData
import net.cequals.lib.nullsafe.Op

abstract class AbstractSpeechClient: SpeechClient {

  protected abstract suspend fun platformParseSpeech(speech: String): SpeechData

  override suspend fun parseSpeech(speech: String): Op<SpeechData> {
    return runCatching {
      platformParseSpeech(speech)
    }.onSuccess {
    }.map { m: SpeechData ->
      Op.valueOp(m)
    }.recover { err ->
      Op.errorOp(err.cause ?: err)
    }.getOrThrow()
  }

}