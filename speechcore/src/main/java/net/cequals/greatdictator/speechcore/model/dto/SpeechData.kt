package net.cequals.greatdictator.speechcore.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class SpeechData(
  val sentences: List<Sentence>?
)

