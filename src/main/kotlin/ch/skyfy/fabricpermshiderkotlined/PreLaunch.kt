package ch.skyfy.fabricpermshiderkotlined

import ch.skyfy.fabricpermshiderkotlined.config.Configs
import ch.skyfy.json5configlib.ConfigManager
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint
import net.minecraft.server.command.CommandManager
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.nio.file.Path

class PreLaunch : PreLaunchEntrypoint {

    companion object {
        const val MOD_ID: String = "fabricpermshiderkotlined"
        val CONFIG_DIRECTORY: Path = FabricLoader.getInstance().configDir.resolve(MOD_ID)
        val LOGGER: Logger = LogManager.getLogger(PreLaunch::class.java)

        init {
            ConfigManager.loadConfigs(arrayOf(Configs::class.java))
        }
    }

    override fun onPreLaunch() {

        println("onPreLaunch")
    }

    private fun registerCommand() {
//        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
//            Configs.COMMAND_ALIASES.serializableData.aliases.forEach { commandAlias ->
//                dispatcher.register(CommandManager.literal(commandAlias.alias).redirect(commandAlias.baseCommand))
//            }
//        }
    }

}