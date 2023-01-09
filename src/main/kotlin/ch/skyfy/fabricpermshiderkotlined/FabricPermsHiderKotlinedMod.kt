package ch.skyfy.fabricpermshiderkotlined

import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.loader.api.FabricLoader
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.nio.file.Path

@Suppress("MemberVisibilityCanBePrivate")
class FabricPermsHiderKotlinedMod : DedicatedServerModInitializer {

    companion object {
        const val MOD_ID: String = "fabricpermhiderkotlined"
        val CONFIG_DIRECTORY: Path = FabricLoader.getInstance().configDir.resolve(MOD_ID)
        val LOGGER: Logger = LogManager.getLogger(FabricPermsHiderKotlinedMod::class.java)
    }

    init {

    }

    override fun onInitializeServer() {

    }

}