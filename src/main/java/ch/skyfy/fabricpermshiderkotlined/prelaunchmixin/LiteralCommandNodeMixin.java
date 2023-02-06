package ch.skyfy.fabricpermshiderkotlined.prelaunchmixin;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

import static ch.skyfy.fabricpermshiderkotlined.utils.Test.COMMANDS;
import static ch.skyfy.fabricpermshiderkotlined.utils.Test.CREATED_ALIASES;

@SuppressWarnings("UnusedMixin")
@Mixin(value = LiteralCommandNode.class, remap = false)
public class LiteralCommandNodeMixin {
    @Mutable
    @Shadow
    @Final
    private String literal;
    @Mutable
    @Shadow
    @Final
    private String literalLowerCase;

//    @Inject(
//            method = "<init>",
//            at = @At(value = "HEAD")
//    )
//    private static <S> void init1(String literal, Command<?> command, Predicate<?> requirement, CommandNode<?> redirect, RedirectModifier<?> modifier, boolean forks, CallbackInfo ci) {
//
//    }

    private Boolean find(CommandNode<?> cn) {
        if (cn.getChildren().isEmpty()) {
//            if (cn.getName().equalsIgnoreCase("mc:gamerule")) {
//                System.out.println("999 " + cn.getName());
//                return true;
//            }
//            if (cn.getName().equalsIgnoreCase("gamerule")) {
//                System.out.println("111 " + cn.getName());
//                return true;
//            }
            if (CREATED_ALIASES.containsKey(cn.getName())) {
                System.out.println("found");
                return true;
            }
        } else {
            for (CommandNode<?> child : cn.getChildren()) {

                find(child);
            }
        }
        return false;
    }

    @Inject(
            method = "<init>",
            at = @At(value = "TAIL")
    )
    private <S> void init(String literal, Command<?> command, Predicate<?> requirement, CommandNode<?> redirect, RedirectModifier<?> modifier, boolean forks, CallbackInfo ci) {
        if(0 == 0)return; // Disable

        this.literal = literal;
        this.literalLowerCase = literal.toLowerCase(Locale.ROOT);

//        if(literal.equalsIgnoreCase("gamerule")){
//            return;
//        }

//        if(shouldReturn.get()){
//            System.out.println("shouldReturn true");
//            return;
//        }

//        if (redirect != null) {
////            if (find(redirect)) {
////                System.out.println("true returned");
////                return;
////            }
////            shouldReturn.set(true);
//            System.out.println("redirect not null");
//            return;
//        }

        if (CREATED_ALIASES.containsKey(literal)) {
            System.out.println("GAMRULE !");
            return;
        }


//        for (CommandAlias alias : Configs.COMMAND_ALIASES.getSerializableData().getAliases()) {
//            if (alias.getAlias().equalsIgnoreCase(literal)) {
//                if (CREATED_ALIASES.containsKey(literal)) {
//                    System.out.println("GAMRULE !");
//                    return;
//                }
//            }
//        }

//        if (!literal.equalsIgnoreCase("luckperms")) return;

//        System.out.println("\n\n\n");

        var foundCommandDispatcher = new AtomicBoolean(false);
        var foundCommandRegistrationCallback = new AtomicBoolean(false);
        var doneForCommandDispatcher = new AtomicBoolean(false);
        var doneForCommandRegistrationCallback = new AtomicBoolean(false);
        var list = new LinkedList<Class>();
        StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).forEach(stackFrame -> {
//            if (doneForCommandDispatcher.get() && !foundCommandRegistrationCallback.get()) return;
            list.add(stackFrame.getDeclaringClass());
            if (doneForCommandDispatcher.get()) return;
            if (doneForCommandRegistrationCallback.get()) return;

//            System.out.println("literal: " + literal);
//            System.out.println("\tstackFrame.getDeclaringClass().getCanonicalName() " + stackFrame.getDeclaringClass().getCanonicalName());
//            System.out.println("\tstackFrame.getDeclaringClass().getSimpleName() " + stackFrame.getDeclaringClass().getSimpleName());
//            System.out.println("\tstackFrame.getDeclaringClass().getName() " + stackFrame.getDeclaringClass().getName());
//            System.out.println("\n");

            if (foundCommandDispatcher.get()) {
                var packageArgs = stackFrame.getDeclaringClass().getPackageName().split("\\.");
                if (packageArgs[0].equalsIgnoreCase("net") && packageArgs[1].equalsIgnoreCase("minecraft")) {
                    COMMANDS.put(literal, "mc:" + literal);
                    this.literal = "mc:" + literal;
                } else {
                    COMMANDS.put(literal, packageArgs[0] + ":" + literal);
                    this.literal = packageArgs[0] + ":" + literal;

                    FabricLoaderImpl.INSTANCE.getModsInternal().forEach(modContainer -> {
                        for (String entrypointKey : modContainer.getMetadata().getEntrypointKeys()) {
                            var entryPoint = modContainer.getMetadata().getEntrypoints(entrypointKey);
                            entryPoint.forEach(entrypointMetadata -> {
                                var splits = entrypointMetadata.getValue().split("\\.");
                                if (packageArgs[0].equalsIgnoreCase(splits[0]) && packageArgs[1].equalsIgnoreCase(splits[1])) {
                                    COMMANDS.put(literal, modContainer.getMetadata().getId() + ":" + literal);
                                    this.literal = modContainer.getMetadata().getId() + ":" + literal;
                                }
                            });
                        }
                    });

                }

                this.literalLowerCase = literal.toLowerCase(Locale.ROOT);
                doneForCommandDispatcher.set(true);
                return;
            } else {
                if (foundCommandRegistrationCallback.get()) {
//                    list.forEach(aClass -> System.out.println("\t"+aClass));
                    var packageArgs = list.get(list.size() - 3).getPackageName().split("\\.");
                    COMMANDS.put(literal, packageArgs[2] + ":" + literal);
                    this.literal = packageArgs[2] + ":" + literal;
                    this.literalLowerCase = literal.toLowerCase(Locale.ROOT);

                    FabricLoaderImpl.INSTANCE.getModsInternal().forEach(modContainer -> {
                        for (String entrypointKey : modContainer.getMetadata().getEntrypointKeys()) {
                            var entryPoint = modContainer.getMetadata().getEntrypoints(entrypointKey);
                            entryPoint.forEach(entrypointMetadata -> {
                                var splits = entrypointMetadata.getValue().split("\\.");
                                if (packageArgs[0].equalsIgnoreCase(splits[0]) && packageArgs[1].equalsIgnoreCase(splits[1])) {
                                    COMMANDS.put(literal, modContainer.getMetadata().getId() + ":" + literal);
                                    this.literal = modContainer.getMetadata().getId() + ":" + literal;
                                }
                            });
                        }
                    });

                    doneForCommandRegistrationCallback.set(true);
                    return;
                }
            }

            if (stackFrame.getDeclaringClass().getSimpleName().equalsIgnoreCase("CommandDispatcher")) foundCommandDispatcher.set(true);
            if (stackFrame.getDeclaringClass().getSimpleName().equalsIgnoreCase("CommandRegistrationCallback")) foundCommandRegistrationCallback.set(true);

        });
        this.literalLowerCase = literal.toLowerCase(Locale.ROOT);
    }


}
