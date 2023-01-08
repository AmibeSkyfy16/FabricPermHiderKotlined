package ch.skyfy.fabricpermhiderkotlined.utils;

import ch.skyfy.fabricpermhiderkotlined.ducks.CommandNodeDuck
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.tree.CommandNode
import com.mojang.brigadier.tree.RootCommandNode
import me.lucko.fabric.api.permissions.v0.Permissions
import net.fabricmc.fabric.api.util.TriState
import net.fabricmc.loader.impl.FabricLoaderImpl
import net.minecraft.command.CommandSource
import net.minecraft.server.MinecraftServer
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ExecuteCommand
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.command.TellRawCommand
import java.util.*

object CommandHider2 {

    @JvmStatic
    fun <S : CommandSource> getPrefix(source: S, root: RootCommandNode<S>, node: CommandNode<S>): String? {
        val prefix = findPackageName(source, root, node)
//        println("PACKAGE NAME: ${findPackageName(source, root, node)}")
        return if (source is ServerCommandSource && Optional.ofNullable(source.server)
                .map(MinecraftServer::getCommandManager)
                .map(CommandManager::getDispatcher)
                .map { obj: CommandDispatcher<ServerCommandSource> -> obj.root }.equals(Optional.of(root))
        ) if(prefix.isEmpty()) "command" else "$prefix:command"
        else null
    }

    private fun <S : CommandSource> findPackageName(source: S, root: RootCommandNode<S>, node: CommandNode<S>): String {
        fun <SS> findPackageNameImpl(ancestor: CommandNode<out SS>): String {
            if (ancestor.command != null) {
                val targetSplits = ancestor.command.javaClass.packageName.split(".")
                FabricLoaderImpl.INSTANCE.modsInternal.forEach foreach1@{ modContainer ->
                    modContainer.metadata.entrypointKeys.forEach { key ->
                        val entrypoints = modContainer.metadata.getEntrypoints(key)
                        entrypoints.forEach { entrypointMetadata ->
                            val splits = entrypointMetadata.value.split(".")
                            if (targetSplits[0] == splits[0] && targetSplits[1] == splits[1] && targetSplits[2] == splits[2]) {
                                return modContainer.metadata.id
                            }
                        }
                    }
                }

                if (targetSplits[0] == "net" && targetSplits[1] == "minecraft") return "minecraft"
            } else {
                ancestor.children.forEach {
                    return findPackageNameImpl(it)
                }
            }
            return ""
        }

        var modId = ""
        val ancestries = (node as CommandNodeDuck<*>).ancestries[root] ?: return ""
        ancestries.forEach { ancestry ->
            val ancestryIt = ancestry.listIterator(ancestry.size)

            if (!ancestryIt.hasPrevious() && ancestryIt.hasNext()) {
                val ancestor = ancestryIt.next()

                if (ancestor.command != null) {
                    val targetSplits = ancestor.command.javaClass.packageName.split(".")

                    FabricLoaderImpl.INSTANCE.modsInternal.forEach foreach1@{ modContainer ->
                        modContainer.metadata.entrypointKeys.forEach { key ->
                            val entrypoints = modContainer.metadata.getEntrypoints(key)
                            entrypoints.forEach { entrypointMetadata ->
                                val splits = entrypointMetadata.value.split(".")
                                if (targetSplits[0] == splits[0] && targetSplits[1] == splits[1] && targetSplits[2] == splits[2]) {
                                    modId = modContainer.metadata.id
                                    return@foreach1
                                }
                            }
                        }
                    }
                }
            }

            while (ancestryIt.hasPrevious()) {
                val ancestor = ancestryIt.previous()
                modId = findPackageNameImpl(ancestor)
            }
        }
        return modId
    }

    @JvmStatic
    fun <S : CommandSource> canUse(source: S, root: RootCommandNode<S>, node: CommandNode<S>, prefix: String): TriState {

        val ancestries = (node as CommandNodeDuck<*>).ancestries[root] ?: return TriState.DEFAULT

        var anyTrue = false

        ancestries.forEach { ancestry ->
            val permissions = mutableListOf<String>()
            if (prefix.isNotEmpty()) permissions.add(prefix)

            val sb = StringBuilder(prefix)
            val ancestryIt = ancestry.listIterator(ancestry.size)
            while (ancestryIt.hasPrevious()) {
                val ancestor = ancestryIt.previous()
                if (sb.isNotEmpty()) sb.append('.')
                sb.append(ancestor.name)
                permissions.add(sb.toString())
            }

            var result = TriState.DEFAULT
            val permissionIter = permissions.listIterator(permissions.size)

            while (permissionIter.hasPrevious()) {
                result = Permissions.getPermissionValue(source, permissionIter.previous())
                if (result != TriState.DEFAULT) break
            }

            when (result) {
                TriState.FALSE -> return TriState.FALSE
                TriState.TRUE -> anyTrue = true
                else -> {}
            }

        }

        return if (anyTrue) TriState.TRUE else TriState.DEFAULT

    }

}