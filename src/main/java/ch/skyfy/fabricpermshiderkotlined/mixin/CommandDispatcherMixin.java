package ch.skyfy.fabricpermshiderkotlined.mixin;

import ch.skyfy.fabricpermshiderkotlined.config.CommandAlias;
import ch.skyfy.fabricpermshiderkotlined.config.Configs;
import ch.skyfy.fabricpermshiderkotlined.utils.Test;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.HashMap;
import java.util.Map;

import static ch.skyfy.fabricpermshiderkotlined.utils.Test.COMMANDS;
import static ch.skyfy.fabricpermshiderkotlined.utils.Test.shouldReturn;

@SuppressWarnings("UnusedMixin")
@Mixin(value = CommandDispatcher.class, remap = false)
public abstract class CommandDispatcherMixin {

    private static final Map<String, Integer> registerOnce = new HashMap<>();



    @Inject(method = "register", at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void register2(LiteralArgumentBuilder<?> command, CallbackInfoReturnable<LiteralCommandNode<?>> cir, LiteralCommandNode build) {
        if(0 == 0)return;
        var dispatcher = ((CommandDispatcher<ServerCommandSource>) (Object) this);


        if (registerOnce.containsKey(command.getLiteral())) {
            System.out.println("return");
            return;
        }

        for (CommandAlias commandAlias : Configs.COMMAND_ALIASES.getSerializableData().getAliases()) {

            System.out.println("getLitral from CommandDispatcherMixin " + command.getLiteral());
            System.out.println("COMMANDS.get(command.getLiteral()) " + COMMANDS.get(command.getLiteral()));
            if (commandAlias.getBaseCommand().equalsIgnoreCase(COMMANDS.get(command.getLiteral()))) {
//            if(commandAlias.getBaseCommand().equalsIgnoreCase(command.getLiteral())){
//                var commandNode = command.build();
                System.out.println("commandNode " + build.getName() + " " + build.getChildren().size());
                shouldReturn.set(true);

//                var typeName = ((ParameterizedType) dispatcher.getClass().getGenericSuperclass())
//                        .getActualTypeArguments()[0].getTypeName();
//                System.out.println("typeName: " + typeName);

                System.out.println("registering...");

                Test.CREATED_ALIASES.put(commandAlias.getAlias(), commandAlias.getBaseCommand());
//                commandNode.getChildren().stream().findFirst().get().

//                var result = recurseRedirect(CommandManager.literal(commandAlias.getAlias()), commandNode);
//                dispatcher.register((LiteralArgumentBuilder<ServerCommandSource>) result);

                registerOnce.putIfAbsent(command.getLiteral(), 1);
                dispatcher.register(CommandManager.literal(commandAlias.getAlias())
                        .redirect((CommandNode<ServerCommandSource>) build));
//                        .redirect((CommandNode<ServerCommandSource>) commandNode.getChildren().stream().findFirst().get()))
                shouldReturn.set(false);
            }
        }

    }

//    @Inject(
//            method = "register",
//            at = @At("TAIL")
//    )
//    <S> void register(LiteralArgumentBuilder<S> command, CallbackInfoReturnable<LiteralCommandNode<S>> cir) {
//        var dispatcher = ((CommandDispatcher<ServerCommandSource>) (Object) this);
//
//        System.out.println("test: " + command.getLiteral());
//        System.out.println("\n\n");
//
////        COMMANDS.forEach((s, s2) -> {
////            System.out.println("base: " + s + " new: " + s2);
////        });
//
//
//        if (registerOnce.containsKey(command.getLiteral())) {
//            System.out.println("return");
//            return;
//        }
//
//        for (CommandAlias commandAlias : Configs.COMMAND_ALIASES.getSerializableData().getAliases()) {
//
//            System.out.println("getLitral from CommandDispatcherMixin " + command.getLiteral());
//            System.out.println("COMMANDS.get(command.getLiteral()) " + COMMANDS.get(command.getLiteral()));
//            if (commandAlias.getBaseCommand().equalsIgnoreCase(COMMANDS.get(command.getLiteral()))) {
////            if(commandAlias.getBaseCommand().equalsIgnoreCase(command.getLiteral())){
//                var commandNode = command.build();
//                System.out.println("commandNode " + commandNode.getName() + " " + commandNode.getChildren().size());
//                shouldReturn.set(true);
//
////                var typeName = ((ParameterizedType) dispatcher.getClass().getGenericSuperclass())
////                        .getActualTypeArguments()[0].getTypeName();
////                System.out.println("typeName: " + typeName);
//
//                System.out.println("registering...");
//
//                Test.CREATED_ALIASES.put(commandAlias.getAlias(), commandAlias.getBaseCommand());
////                commandNode.getChildren().stream().findFirst().get().
//
////                var result = recurseRedirect(CommandManager.literal(commandAlias.getAlias()), commandNode);
////                dispatcher.register((LiteralArgumentBuilder<ServerCommandSource>) result);
//
//                dispatcher.register(CommandManager.literal(commandAlias.getAlias())
//                        .redirect((CommandNode<ServerCommandSource>) commandNode));
////                        .redirect((CommandNode<ServerCommandSource>) commandNode.getChildren().stream().findFirst().get()))
//                shouldReturn.set(false);
//                registerOnce.putIfAbsent(command.getLiteral(), 1);
//            }
//        }
//
//    }

//    ArgumentBuilder recurseRedirect(ArgumentBuilder argumentBuilder, CommandNode commandNode) {
//        var a = argumentBuilder.redirect(commandNode);
//        if(commandNode.getChildren().isEmpty())return a;
//        for (Object child : commandNode.getChildren()) {
//            return recurseRedirect(a, (CommandNode) child);
//        }
//        return a;
//    }

}
