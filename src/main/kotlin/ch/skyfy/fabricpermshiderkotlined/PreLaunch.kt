package ch.skyfy.fabricpermshiderkotlined

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint

class PreLaunch : PreLaunchEntrypoint {
    override fun onPreLaunch() {
        println("onPreLaunch")
    }
}