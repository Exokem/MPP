# Minecraft Plus Plus (M++)

## Overview

This mod aims to adjust some seemingly arbitrary limits imposed by the vanilla experience.  
It also adds some new features.  
More than anything else though, __this mod is a vanilla expansion__ so you shouldn't complain when it doesn't work in your hundred mod pack.  

Included are the following changes:  

1. Enchantments
    + Unbreaking limit: `III -> V`
    + Protection limit: `IV -> V`
    + Infinity can now be combined with Mending
    + Standard Protection can now be combined with a single specialized Protection, but specialized Protection enchantments remain incompatible with one another
    + Anvils will no longer prevent combinations whose costs reach or exceed 40 levels
    + The grindstone now allows enchantments to be removed from enchanted items when a book is placed in the bottom input slot
2. Trees
    + It is now possible to grow large oak trees (using bone meal only) which are similar in appearance to dark oak trees
3. Mobs
    + The Elder Guardian mining fatigue effect is much less severe: Level `III -> II` and Duration `5:00 -> 1:30`
    + The Shulker levitation effect is much less severe: Duration `0:10 -> 0:03`

## Incompatibilities

This mod relies rather heavily on mixins to achieve its intended results, so its features may not all be compatible with some mods.

This mod may be partially or entirely incompatible with other mods that:

+ Override `InfinityEnchantment#canAccept` or modify which enchantments are compatible with Infinity
+ Override `ProtectionEnchantment#canAccept`, `ProtectionEnchantment#getMaxLevel` or modify which enchantments are compatible with Protection or its maximum level
+ Override `UnbreakingEnchantment#getMaxLevel` or modify the maximum level of Unbreaking
+ Override `SaplingBlock#generate` or modify how saplings generate trees
+ Override `AnvilScreen#drawForeground` or modify how the anvil gui is rendered
+ Override `AnvilScreenHandler#updateResult` or modify how the anvil decides combination results
+ Modify in any way `GrindstoneScreenHandler` or attempt to overhaul the functionality of the grindstone
+ Override `ElderGuardianEntity#mobTick` or modify the behavior of Elder Guardian entities
+ Override `ShulkerBulletEntity#onEntityHit` or modify the behavior of Shulker projectiles

As per the license, however, you may create a variant of this mod using its source code that excludes any particular conflicting feature.

## License

This template is available under the CC0 license.
