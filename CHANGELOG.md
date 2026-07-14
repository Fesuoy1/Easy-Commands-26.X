# Changelog History

## [2.4.0-*] - 2026-07-15

### Changes:
- Added gamemode alias commands
  - `/creative [players]`
  - `/survival [players]`
  - `/adventure [players]`
  - `/spectator [players]`

## [2.3.0-neoforge] - 2026-07-11

## 🎉 Mod has been ported to NeoForge!

There should be no functional differences between the Fabric and NeoForge versions.
<br>If you encounter any issues (specific to NeoForge), please report them by opening an issue.</br>

## [2.3.0] - 2026-07-01

### Changes:
- Added new tool/weapon enchant commands:
  - `/enchantaxe <efficiency> [fortune] [silkTouch] [sharpness] [mending] [unbreaking]`
  - `/enchantshovel <efficiency> [fortune] [silkTouch] [mending] [unbreaking]`
  - `/enchanthoe <efficiency> [fortune] [silkTouch] [mending] [unbreaking]`
  - `/enchantbow <power> [punch] [flame] [infinity] [mending] [unbreaking]`
  - `/enchantcrossbow <multishot> [piercing] [quickCharge] [mending] [unbreaking]`
  - `/enchantmace <density> [breach] [windBurst] [smite] [baneOfArthropods] [fireAspect] [mending] [unbreaking]`
  - `/enchantspear <sharpness> [smite] [baneOfArthropods] [knockback] [fireAspect] [looting] [lunge] [mending] [unbreaking]`
  - `/enchanttrident <loyalty> [impaling] [riptide] [channeling] [mending] [unbreaking]`
  - `/enchantfishingrod <luckOfTheSea> [lure] [mending] [unbreaking]`

### Internal/developer changes:
- Used `Map.of()` instead of `HashMap` in `EnchantSwordCommand` and `EnchantPickaxeCommand`
- **Major**: Added automatic game tests when building the mod
  - *Note: For making huge changes or adding new commands, You are still recommended to test them in-game manually just to be safe*
- Usual code cleanup, etc.

## [2.2.0] - 2026-07-01

### Changes:
- Made more commands' parameters optional
  - `/repairall [repairInventory]` (Default: false)
  - `/feed [players]` (Feeds yourself if no players specified)
  - `/knockback [amount of levels] [target]` (Default level: 10)
  - `/knockbackstick [amount of levels] [target]` (Default level: 10)
  - `/setExplosionPower [power]` (Shows current value if no power given)
  - `/modifyTreeHeight [height]` (Shows current height if none specified)
- Added optional `target` player argument to:
  - `/repair [target]`
  - `/repairinventory [target]`
  - `/knockback [level] [target]`
  - `/knockbackstick [level] [target]`
- `/toggleExplosiveProjectiles` can now be set explicitly with `[true/false]` instead of just toggling
- Some fixes

## [2.1.1] - 2026-06-28

### Changes:
- Added update checker for mod menu

## [2.1.0] - 2026-06-26

### Changes:
- Added `/enchantpickaxe` command:
  - `/enchantpickaxe <efficiency> [fortune] [mending] [unbreaking] [silkTouch]`
- Both `/enchantsword` and `/enchantpickaxe` now work with any sword/pickaxe, including modded ones

### Internal/developer changes:
- Cleaned up unused client initializations
- Removed empty client-side entrypoint and unused client mixins config
- Gamerules are now registered directly in the mod initializer instead of a static initializer
- Code cleanup, etc.

## [2.0.0] - 2026-06-25

After 2 years, I've decided to update the mod to the latest version (26.2 as of writing)!

### Changes:
- Made more commands' parameters optional (parameters surrounded by [square brackets] indicate optional)
  - `/heal <players> [alsoFeed]`
  - `/explode <position x y z> <explosionPower> [createFire]`
  - `/enchantsword [sharpness] [sweepingEdge] [mending] [unbreaking] [fireAspect] [knockback] [looting]`
- Added back `/modifyTreeHeight` (which internally just sets the gamerule)
- Added `/resetTreeHeight` (Resets the tree height modifier back to default)
- Added `/toggleExplosiveProjectiles` (Toggles explosive projectiles on/off)
- Fixed certain sounds not playing

### Internal/developer changes:
- Made the codebase more cleaner and organized
- Other more stuff

## [1.3.2] - 2024-02-14

- Replaced
  - /modifyTreeHeight
  - /enableExplosiveProjectiles
  - with their gamerules for easier access (and wont reset back to their default values when rejoining the world)
  
- Other stuff!

## [1.3.12] - 2024-02-13

Backport to 1.20.2. Also works on 1.20.3 and 1.20.4.

## [1.3.11] - 2024-02-05

- Update Fabric API to 0.95.1+1.20.4

## [1.3.1] - 2024-02-04

- Added Smoke particles for projectiles when explosive projectiles are enabled with `/enableExplosiveProjectiles`

## [1.3.0] - 2024-02-03

- Added new commands. You can find them in the description of this project below /midnight.
- Other stuff implemented/fixed but i forgot

## [1.2.2] - 2024-02-02

- Fixed `/feed` command

## [1.2.1] - 2024-02-02

First GitHub release. See older releases on Modrinth.

### Modrinth changelog:
```
- Added suggestions for /knockback and /knockbackstick commands
- Maybe fixed bugs

### Now open source!
```

## [1.2.0] - 2024-01-23

Second update!

- Maybe bug fixes
- Added command: /heal [allPlayers true/false]
- Added command: /feed
- ... and more! (/day, /night, etc)

## [1.1.0] - 2024-01-22

Update from 1.0

- Fixed bugs
- Changed /knockbackstick command so you'll get it directly to your inventory
- Added icon in-game

## [1.0] - 2024-01-21

Initial Release
