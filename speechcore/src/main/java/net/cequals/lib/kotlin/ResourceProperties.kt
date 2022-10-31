package net.cequals.lib.kotlin

import java.io.IOException
import java.util.*

class ResourceProperties(propertiesName: String) {

  val properties: Properties = Properties()

  init {
    try {
      ResourceProperties::class.java.getClassLoader().getResourceAsStream(propertiesName).use { input ->
        if (input == null) {
          println("Sorry, unable to find nlpserver.properties")
          return@use
        }
        properties.load(input)
      }
    } catch (ex: IOException) {
      ex.printStackTrace()
    }
  }

}