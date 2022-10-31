package net.cequals.greatdictator.speechcore.model

import net.cequals.greatdictator.speechcore.model.SpeechBusinessLogic.SpeechBusinessData
import net.cequals.greatdictator.speechcore.model.dto.Sentence
import net.cequals.lib.kotlin.where

/*

pure business functions - unit testing required

*/

fun puntuateInterrogative(data: SpeechBusinessData): SpeechBusinessData =
  if (data.interrogative) data.copy(speech = data.speech + "?")
  else data

fun toSpeechBusinessData(
  speech: String,
  confidence: Float,
  speechData: Sentence?
) = SpeechBusinessData(
  speech,
  mapConfidenceRatingColor(confidence),
  speechData?.sentiment ?: "n/a",
  isInterrogative(speechData?.parse)
)

fun isInterrogative(parse: String?) =
  parse?.let {
    it.contains("(SQ ") || it.contains("(SBARQ ")
  } ?: false

fun mapConfidenceRatingColor(confidence: Float?) =
  when (confidence) {
    confidence where { this >= .9f } -> "green"
    confidence where { this >= .5f && this < .9f } -> "cyan"
    confidence where { this >= .15f && this < .5f } -> "yellow"
    else -> "red"
  }
