# Minecraft Plus Plus (M++)

## Overview

This mod aims to adjust some seemingly arbitrary limits imposed by the vanilla experience.  
It also adds some new features.  

Included are the following changes:  

1. Enchantments
    + Unbreaking limit: `III -> V`
    + Protection limit: `IV -> V`
    + Infinity can now be combined with Mending
    + Standard Protection can now be combined with a single specialized Protection, but specialized Protection enchantments remain incompatible with one another
2. Trees
    + It is now possible to grow large oak trees (using bone meal only) which are similar in appearance to dark oak trees

## Incompatibilities

This mod relies rather heavily on mixins to achieve its intended results, so its features may not all be compatible with some mods.

This mod may be partially or entirely incompatible with other mods that:

+ Override `InfinityEnchantment#canAccept` or modify which enchantments are compatible with Infinity
+ Override `ProtectionEnchantment#canAccept`, `ProtectionEnchantment#getMaxLevel` or modify which enchantments are compatible with Protection or its maximum level
+ Override `UnbreakingEnchantment#getMaxLevel` or modify the maximum level of Unbreaking
+ Override `SaplingBlock#generate` or modify how saplings generate trees

## License

This template is available under the CC0 license.
