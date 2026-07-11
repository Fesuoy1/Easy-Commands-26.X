Modrinth page: https://modrinth.com/mod/fesuoy-easy-commands

## Have you wanted to access minecraft commands easily?

Suppose you wanted a OP Netherite sword right now without using /enchant command over and over... now with this mod you can do /enchantsword while holding any sword!

### Current Commands Implemented:

- /repair [target] (Repair a tool in your hand such as a pickaxe that is nearly broken, or a target player's held item)
- /repairinventory [target] (Repairs everything that is damaged in your inventory, or a target player's inventory)
- /repairall [repairInventory] (Repairs a damaged tool in every player's hands, use true for all players inventory. Default: false.)
- /killall [shouldAlsoKillPlayers true/false] (This explains itself)
- /knockback [amount of levels] [target] (put knockback on anything! can be 100 or whatever. Default level: 10. Optionally target a player's held item.)
- /knockbackstick [amount of levels] [target] (similar to the command above, give yourself or a target player a stick with knockback. Default level: 10.)
- /enchantsword <sharpness> [sweepingEdge] [mending] [unbreaking] [fireAspect] [knockback] [looting] (Enchants any sword you are holding with custom enchantment levels.)
- /enchantpickaxe <efficiency> [fortune] [mending] [unbreaking] [silkTouch] (Enchants any pickaxe you are holding with custom enchantment levels.)
- /enchantaxe <efficiency> [fortune] [silkTouch] [sharpness] [mending] [unbreaking] (Enchants any axe you are holding with custom enchantment levels.)
- /enchantshovel <efficiency> [fortune] [silkTouch] [mending] [unbreaking] (Enchants any shovel you are holding with custom enchantment levels.)
- /enchanthoe <efficiency> [fortune] [silkTouch] [mending] [unbreaking] (Enchants any hoe you are holding with custom enchantment levels.)
- /enchantbow <power> [punch] [flame] [infinity] [mending] [unbreaking] (Enchants any bow you are holding with custom enchantment levels.)
- /enchantcrossbow <multishot> [piercing] [quickCharge] [mending] [unbreaking] (Enchants any crossbow you are holding with custom enchantment levels.)
- /enchantmace <density> [breach] [windBurst] [smite] [baneOfArthropods] [fireAspect] [mending] [unbreaking] (Enchants any mace you are holding with custom enchantment levels.)
- /enchantspear <sharpness> [smite] [baneOfArthropods] [knockback] [fireAspect] [looting] [lunge] [mending] [unbreaking] (Enchants any spear you are holding with custom enchantment levels.)
- /enchanttrident <loyalty> [impaling] [riptide] [channeling] [mending] [unbreaking] (Enchants any trident you are holding with custom enchantment levels.)
- /enchantfishingrod <luckOfTheSea> [lure] [mending] [unbreaking] (Enchants any fishing rod you are holding with custom enchantment levels.)
- /heal <players> [alsoFeed true/false] (Heal specified players to max health! Also feeds them if true.)
- /feed [players] (Feeds specified players, or yourself if none specified! Use e.g. @a, @s, or a player name.)
- /day (basically /time set 1000)
- /noon (basically /time set 6000)
- /night (basically /time set 13000)
- /midnight (basically /time set 18000)
- /toggleExplosiveProjectiles [value] (Toggles explosive projectiles on/off, or set explicitly with true/false. All projectiles (e.g. snowball, arrow) explode on collision.)
- /setExplosionPower [power] (Sets the explosion power for explosive projectiles, or shows the current value if no power specified. Default is 2.5.)
- /modifyTreeHeight [height] (Changes how tall newly generated trees can be based on height, or shows the current height if none specified.)
- /resetTreeHeight (Resets the tree height modifier back to default.)
- /explode <position x y z> <explosionPower> [createFire true/false] (Explodes on a set position with specified explosion power and creates fire on explosion!)

### Note
Some or most commands ran through a command block might probably not work. so I recommend running it normally or via /execute. I didn't test it myself of course.

### Bugs?
To be honest, I don't debug much since I originally made this when I was bored, so expect some bugs that can occur.

If you do find any, Please report them on my [GitHub repository](https://github.com/Fesuoy1/Easy-Commands-26.X/issues).

## Dependencies
### Fabric
- [Fabric API](https://modrinth.com/mod/fabric-api)
### NeoForge
- None

## This Is Open Source.
I probably will port this to future versions of minecraft available, but definitely no plans for more backporting. If you want to backport to older versions then please [fork this repository](https://github.com/Fesuoy1/Easy-Commands-26.X).