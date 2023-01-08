package ch.skyfy.fabricpermhiderkotlined.mixin;

import net.fabricmc.loader.api.LanguageAdapter;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.fabricmc.loader.impl.ModContainerImpl;
import net.fabricmc.loader.impl.discovery.ModCandidate;
import net.fabricmc.loader.impl.entrypoint.EntrypointStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(FabricLoaderImpl.class)
public class FabricLoaderImpMixin {

    @Shadow @Final protected Map<String, ModContainerImpl> modMap;
    @Shadow private List<ModCandidate> modCandidates;
    @Shadow protected List<ModContainerImpl> mods;
    @Shadow @Final private Map<String, LanguageAdapter> adapterMap;
    @Shadow @Final private EntrypointStorage entrypointStorage;

    @Inject(method = "finishModLoading", at = @At("TAIL"), remap = false)
    public void test(CallbackInfo ci){
        System.out.println("MOD LOADING FINISHED");
    }

}
