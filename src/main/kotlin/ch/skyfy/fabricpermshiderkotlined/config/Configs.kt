package ch.skyfy.fabricpermshiderkotlined.config

import ch.skyfy.fabricpermshiderkotlined.PreLaunch
import ch.skyfy.json5configlib.ConfigData


object Configs {
    @JvmField
    val COMMAND_ALIASES = ConfigData.invoke<CommandAliasesConfig, DefaultCommandAliasesConfig>(PreLaunch.CONFIG_DIRECTORY.resolve("command-aliases.json5"), true)
}
