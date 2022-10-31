package net.cequals.greatdictator.speechcore.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class Sentence(
  val index: Int,
  val parse: String,
  val sentiment: String
)