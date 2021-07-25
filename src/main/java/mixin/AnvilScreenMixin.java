package mixin;

import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin extends ForgingScreen<AnvilScreenHandler>
{
    @ModifyArg(method = "drawForeground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;getWidth(Lnet/minecraft/text/StringVisitable;)I"))
    private StringVisitable adjustAnvilCostShadow(StringVisitable intercepted)
    {
        return getReplacedLevelText();
    }

    @ModifyArg
        (
            method = "drawForeground",
            at = @At
                     (
                         value = "INVOKE",
                         target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I"
                     )
        )
    private Text adjustAnvilCostDisplay(Text intercepted)
    {
        return getReplacedLevelText();
    }

    public AnvilScreenMixin(AnvilScreenHandler handler, PlayerInventory playerInventory, Text title, Identifier texture)
    {
        super(handler, playerInventory, title, texture);
    }

    private TranslatableText getReplacedLevelText()
    {
        return new TranslatableText("container.repair.cost", this.handler.getLevelCost());
    }
}
