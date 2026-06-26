Modrinth page: https://modrinth.com/mod/fesuoy-easy-commands

## Have you wanted to access minecraft commands easily?

Suppose you wanted a OP Netherite sword right now without using /enchant command over and over... now with this mod you can do /enchantsword while holding any sword!

### Current Commands Implemented:

- /repair (Repair a tool in your hand such as a pickaxe that is nearly broken)
- /repairinventory (Repairs everything that is damaged in your inventory)
- /repairall <repairInventory true/false> (Repairs a damaged tool in every player's hands, use true parameter for all players inventory)
- /killall [shouldAlsoKillPlayers true/false] (This explains itself)
- /knockback <amount of levels> (put knockback on anything! can be 100 or whatever)
- /knockbackstick <amount of levels> (similar to the command above, give yourself a stick with knockback)
- /enchantsword <sharpness> [sweepingEdge] [mending] [unbreaking] [fireAspect] [knockback] [looting] (Enchants any sword you are holding with custom enchantment levels.)
- /enchantpickaxe <efficiency> [fortune] [mending] [unbreaking] [silkTouch] (Enchants any pickaxe you are holding with custom enchantment levels.)
- /heal <players> [alsoFeed true/false] (Heal specified players to max health! Also feeds them if true.)
- /feed <players> (Feeds specified players! Use e.g. @a, @s, or a player name.)
- /day (basically /time set 1000)
- /noon (basically /time set 6000)
- /night (basically /time set 13000)
- /midnight (basically /time set 18000)
- /toggleExplosiveProjectiles (Toggles explosive projectiles on/off. All projectiles (e.g. snowball, arrow) explode on collision.)
- /setExplosionPower <power> (Sets the explosion power for explosive projectiles. Default is 2.5.)
- /modifyTreeHeight <height> (Changes how tall newly generated trees can be based on height, which can lead to some interesting results.)
- /resetTreeHeight (Resets the tree height modifier back to default.)
- /explode <position x y z> <explosionPower> [createFire true/false] (Explodes on a set position with specified explosion power and creates fire on explosion!)

### Note
Some or most commands ran through a command block might probably not work. so I recommend running it normally or via /execute. I didn't test it myself of course.

### Bugs?
To be honest, I don't debug much since I originally made this when I was bored, so expect some bugs that can occur.

If you do find any, Please report them on my [GitHub repository](https://github.com/Fesuoy1/Easy-Commands-26.X/issues).

## Dependencies
- [Fabric API](https://modrinth.com/mod/fabric-api)

## This Is Open Source.
I probably will port this to future versions of minecraft available, but definitely no plans for more backporting. If you want to backport to older versions then please [fork this repository](https://github.com/Fesuoy1/Easy-Commands-26.X).