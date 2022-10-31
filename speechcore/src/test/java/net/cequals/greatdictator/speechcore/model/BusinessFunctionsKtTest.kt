package net.cequals.greatdictator.speechcore.model

import net.cequals.greatdictator.speechcore.model.SpeechBusinessLogic.SpeechBusinessData

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class BusinessFunctionsKtTest {

  @Test
  fun puntuateIntoerrogative() {
    val positive = puntuateInterrogative(positiveInterrogativeData())
    assert(positive.speech.endsWith("?"))
    val negative = puntuateInterrogative(negativeInterrogativeData())
    assert(!negative.speech.endsWith("?"))

  }

  @Test
  fun isInterrogative() {
    assert(!isInterrogative(speechParsedString()))
    assert(isInterrogative(speechParsedStringInterrogative()))
  }

  @Test
  fun mapConfidenceRatingColor() {

    assertEquals("cyan",  mapConfidenceRatingColor(.89999f))
    assertEquals("green",  mapConfidenceRatingColor(.9f))
    assertEquals("green",  mapConfidenceRatingColor(.91f))

    assertEquals("yellow",  mapConfidenceRatingColor(.49999f))
    assertEquals("cyan",  mapConfidenceRatingColor(.5f))
    assertEquals("cyan",  mapConfidenceRatingColor(.51f))

    assertEquals("red",  mapConfidenceRatingColor(-.1f))
    assertEquals("red",  mapConfidenceRatingColor(.149999f))
    assertEquals("yellow",  mapConfidenceRatingColor(.15f))
    assertEquals("yellow",  mapConfidenceRatingColor(.151f))

  }

  fun positiveInterrogativeData() = SpeechBusinessData(
    "Is this a question",
    "green",
    "neutral",
    true
  )

  fun negativeInterrogativeData() = positiveInterrogativeData().copy(speech = "this a not question", interrogative = false)

  fun speechParsedStringInterrogative() = """
  (ROOT
  (SBARQ
    (WHNP (WP What))
    (SQ (VBZ does)
      (NP (PRP it))
      (VP (VB look)
        (PP (IN like)
          (ADVP (RB now)))))))  
  """.trimIndent()

  fun speechParsedString() = """
  (ROOT
  (S
    (NP (PRP i))
    (VP (VBD was)
      (VP (VBN caught)
        (PP (IN in)
          (NP (DT a) (NN landslide)))))))
  """.trimIndent()
}