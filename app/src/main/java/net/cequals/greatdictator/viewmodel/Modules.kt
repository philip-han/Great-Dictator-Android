package net.cequals.greatdictator.viewmodel

import net.cequals.greatdictator.service.GreatDictatorSpeechRecognizer
import net.cequals.greatdictator.speechcore.service.Speech
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

var modules = module {
  single<Speech> { p -> GreatDictatorSpeechRecognizer(p.get()) }
  viewModel { p -> GreatDictatorViewModel(p.get()) }
}