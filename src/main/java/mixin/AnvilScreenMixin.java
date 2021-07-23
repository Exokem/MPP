package mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin extends ForgingScreen<AnvilScreenHandler>
{
    public AnvilScreenMixin(AnvilScreenHandler handler, PlayerInventory playerInventory, Text title, Identifier texture)
    {
        super(handler, playerInventory, title, texture);
    }

    @Shadow
    private PlayerEntity player;

    public void drawForeground(MatrixStack matrices, int mouseX, int mouseY)
    {
        RenderSystem.disableBlend();
        super.drawForeground(matrices, mouseX, mouseY);
        int i = ((AnvilScreenHandler)this.handler).getLevelCost();
        if (i > 0) {
            int j = 8453920;
            Object text3;
            if (!((AnvilScreenHandler)this.handler).getSlot(2).hasStack()) {
                text3 = null;
            } else {
                text3 = new TranslatableText("container.repair.cost", new Object[]{i});
                if (!((AnvilScreenHandler)this.handler).getSlot(2).canTakeItems(this.player)) {
                    j = 16736352;
                }
            }

            if (text3 != null) {
                int k = this.backgroundWidth - 8 - this.textRenderer.getWidth((StringVisitable)text3) - 2;
                fill(matrices, k - 2, 67, this.backgroundWidth - 8, 79, 1325400064);
                this.textRenderer.drawWithShadow(matrices, (Text)text3, (float)k, 69.0F, j);
            }
        }

    }
}
