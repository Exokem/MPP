package utility;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.Map;

public class SlotOverrides
{
    public static Slot input1(Inventory inventory, int index, int x, int y)
    {
        return new Slot(inventory, index, x, y)
        {
            @Override
            public boolean canInsert(ItemStack stack)
            {
                return stack.isDamageable() || stack.isOf(Items.ENCHANTED_BOOK) || stack.hasEnchantments();
            }
        };
    }

    public static Slot input2(Inventory inventory, int index, int x, int y)
    {
        return new Slot(inventory, index, x, y)
        {
            @Override
            public boolean canInsert(ItemStack stack)
            {
                return stack.isDamageable() || stack.isOf(Items.ENCHANTED_BOOK) || stack.hasEnchantments() || stack.isOf(Items.BOOK);
            }
        };
    }

    public static Slot result
    (
        ScreenHandlerContext context, Inventory input, Inventory slotInventory,
        int index, int x, int y
    )
    {
        return new Slot(slotInventory, index, x, y)
        {
            public boolean canInsert(ItemStack stack)
            {
                return false;
            }

            public void onTakeItem(PlayerEntity player, ItemStack stack)
            {
                context.run((world, pos) ->
                {
                    if (world instanceof ServerWorld)
                    {
                        ExperienceOrbEntity.spawn((ServerWorld)world, Vec3d.ofCenter(pos), this.getExperience(world));
                    }

                    world.syncWorldEvent(1042, pos, 0);
                });

                ItemStack book = input.getStack(1);

                if (book.isOf(Items.BOOK) && !book.hasEnchantments())
                {
                    ItemStack source = input.getStack(0);

                    ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
                    EnchantmentHelper.get(source).forEach((enchantment, level) -> EnchantedBookItem.addEnchantment(enchantedBook, new EnchantmentLevelEntry(enchantment, level)));
                    input.setStack(1, enchantedBook);
                }

                else input.setStack(1, ItemStack.EMPTY);

                input.setStack(0, ItemStack.EMPTY);
            }

            private int getExperience(World world)
            {
                int ix = 0;
                int i = ix + this.getExperience(input.getStack(0));
                i += this.getExperience(input.getStack(1));

                if (i > 0)
                {
                    int j = (int)Math.ceil((double)i / 2.0D);
                    return j + world.random.nextInt(j);
                }

                else
                {
                    return 0;
                }
            }

            private int getExperience(ItemStack stack)
            {
                int i = 0;
                Map<Enchantment, Integer> map = EnchantmentHelper.get(stack);
                Iterator var4 = map.entrySet().iterator();

                while(var4.hasNext())
                {
                    Map.Entry<Enchantment, Integer> entry = (Map.Entry)var4.next();
                    Enchantment enchantment = (Enchantment)entry.getKey();
                    Integer integer = (Integer)entry.getValue();

                    if (!enchantment.isCursed())
                    {
                        i += enchantment.getMinPower(integer);
                    }
                }

                return i;
            }
        };
    }
}
