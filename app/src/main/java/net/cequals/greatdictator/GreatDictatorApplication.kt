package net.cequals.greatdictator

import android.app.Application
import org.koin.core.context.startKoin

class GreatDictatorApplication: Application() {

  override fun onCreate() {
    super.onCreate()
    startKoin {
      modules(
        net.cequals.greatdictator.speechcore.model.modules,
        net.cequals.greatdictator.viewmodel.modules
      )
    }
  }

}