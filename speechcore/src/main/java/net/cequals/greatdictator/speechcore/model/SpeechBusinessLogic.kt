package net.cequals.greatdictator.speechcore.model

import net.cequals.greatdictator.speechcore.model.dto.SpeechData
import net.cequals.lib.nullsafe.Op

class SpeechBusinessLogic {

  internal val speechList: MutableList<SpeechBusinessData> = mutableListOf()

  fun addSpeech(speech: String, confidence: Float, parsedSpeech: Op<SpeechData>) {
    val speechData = parsedSpeech.fold()?.sentences?.get(0)
    speechList.add(
      toSpeechBusinessData(speech, confidence, speechData)
    )
  }

  fun removeItemAt(idx: Int) {
    speechList.removeAt(idx)
  }

  fun clearList() {
    speechList.clear()
  }

  data class SpeechBusinessData(
    val speech: String,
    val confidenceRatingColor: String,
    val sentiment: String,
    val interrogative: Boolean
  )

}
