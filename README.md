[![GitHub Release](https://img.shields.io/github/v/release/unforbidable/tfcplus-bids?include_prereleases)](https://github.com/unforbidable/tfcplus-bids/releases)
[![CurseForge Version](https://img.shields.io/curseforge/v/630843)](https://www.curseforge.com/minecraft/mc-mods/tfcplus-bids/files?showAlphaFiles=show)
[![CurseForge Game Versions](https://img.shields.io/curseforge/game-versions/630843)](https://www.curseforge.com/minecraft/mc-mods/tfcplus-bids/files?showAlphaFiles=show)
[![CurseForge Downloads](https://img.shields.io/curseforge/dt/630843)](https://www.curseforge.com/minecraft/mc-mods/tfcplus-bids/files?showAlphaFiles=show)
[![GitHub License](https://img.shields.io/github/license/unforbidable/tfcplus-bids)](https://github.com/unforbidable/tfcplus-bids/blob/main/LICENSE)

# TFC Plus - Bids and Pieces
A plugin for TerrafirmacraftPlus that aims to demonstrate various fully featured enhancements for gameplay and testing.

Please find the latest release at [courseforge](https://www.curseforge.com/minecraft/mc-mods/tfcplus-bids).

### Main Features
* [Clay and Fire Clay Crucible](../../wiki/Crucible) - new crucibles with improved mechanics
* [Metal Blowpipe](../../wiki/Metal-Blowpipe) - recipe based glassware crafting
* [Furnace](../../wiki/Furnace) - for making glass in a crucible
* [Mud Brick Chimney](../../wiki/Mud-brick-chimney) - allows furnace and basic kiln construction before acquiring metal tools
* [Quarry](../../wiki/Quarry) - for harvesting Rough Stone block using the drill and wedge method
* [Rough Stone and Rough Stone Bricks and Tiles](../../wiki/Rough-Stone) - new building blocks from quarried rocks
* [Block Carving](../../wiki/Carving) - for shaping blocks with an adze, in like manner to TFC detailing with a chisel, and carving recipes, using 3 carving modes
* [Log Walls](../../wiki/Log-Wall) - new building block with 6 variations for log cabin construction, and 2 variations of vertical log walls
* [Wood Pile](../../wiki/Wood-Pile) - for wood storage and seasoning peeled logs, stick bundles, and making charcoal, extracting pitch, building pyres and fire-setting
* [New Firepit](../../wiki/Firepit) - new firepit that can consume custom fuels, requires kindling to light
* [Drying Rack](../../wiki/Drying-Rack) - for curing bark fiber strips and drying meat, cheese, and seaweed
* [Firewood](../../wiki/Firewood) - fuel for new firepit, needs to be seasoned
* [Chopping Block](../../wiki/Chopping-Block) - for peeling and splitting logs
* [Wattle Trapdoor](../../wiki/Wattle-Trapdoor) - trapdoor made from wattle
* [Saddle Quern](../../wiki/Saddle-Quern) - carved from rough stone, used for crushing and grinding grain
* [Stone Press](../../wiki/Stone-Press) - lever based pressing device, used for extracting juices and oils from berries and fruits
* [Clay Lamp](../../wiki/Clay-Lamp) - stationary light source that consumes olive oil, fish oil or linseed oil
* [Wall Hook](../../wiki/Wall-Hook) - for hanging some tools, clothes and some other items on a wall
* [Aquifer](../../wiki/Aquifer) - generates below the surface and spawns water blocks above when exposed
* [Cooking Pot](../../wiki/Cooking-Pot) - for boiling and steaming foodstuffs and much more, notably making cooked meals form prepared mixtures
* [Fish Oil](../../wiki/Fish-Oil) - an alternative fuel for clay lamps
* [Cooking Prep](../../wiki/Cooking-Prep) - for making classic salads and sandwiches and new meals such as stuffed peppers and stuffed mushrooms, wraps, and cooking mixes
* [More Crops](../../wiki/More-Crops) - new crops such as sea beet, beetroot, sugar beet, wild beans and broad beans and some converted TFC crops
* [Tallow](../../wiki/Tallow) - made from Suet which drops from appropriate animal mobs, for consumption or making candles and Pemmican
* [Screw Press](../../wiki/Screw-Press) - screw operated pressing device powered by TFC+ mechanisms
* [Wattle Gate](../../wiki/Wattle-Gate) - gate from wattle
* [Palisade](../../wiki/Palisade) - wall made of logs placed upright
* [More Kilns](../../wiki/Kiln) - Tunnel Kiln, Square Kiln, Beehive Kiln adapted from TFC+ and Climbing Kiln, powered by a wood pile, for pottery and wood drying
* [Unshaped Dough](../../wiki/Unshaped-Dough) - unshaped flour and water mixture which can be formed into dough for Bread, Flatbread and Hardtack
* [Pail](../../wiki/Pail) - a large wooden container for transferring liquids and milking up to 4 animals in one go (including Ibex)
* [Butter](../../wiki/Butter) - a dairy foodstuff made by churning Cream which is separated from milk
* [Hand Axe](../../wiki/Hand-Axe) - primitive stone tool for cutting and scraping
* [Composite Tools](../../wiki/Composite-Tools) - stone tools made with a binding material for enhanced durability and mining speed
* [Textile](../../wiki/Textile) - new textile materials and extended processing
* [Item Carving](../../wiki/Woodworking) - woodworking recipes to produce wooden tools and various material (also from bone)
* [Soap](../../wiki/Soap) - for performing basic hygiene while earning XP periodically and more
* [Edible Seeds](../../wiki/Edible-Seeds) - consumable seeds and derived oils such as Linseed

See the [wiki](../../wiki) for complete list and detailed description of features current and upcoming.

### Contributions

* Saddle Quern and Stone Press related models by [talhaereny](https://github.com/talhaereny)

### Translations

* Chinese by Eternal130 (v0.52.x)
* Japanese by dymanic.day (v0.28.x)

### Other credits

* [TFCraft-NEIplugin](https://github.com/tfc-plus-addons/TFCraft-NEIplugin)

### Compatibility

* WAILA - block information for crucible, quarrying, glassmaking, wood pile items, drying rack, saddle quern, stone press, screw press, cooking pot, etc
* NEI & NEI G.T.N.H. Unofficial - recipes for seasoning, quarrying, drying, carving, chopping, saddle quern, stone press, screw press, cooking, new firepit fuels, processing surface, etc

### Compiling

Required libraries to be placed in `libs` folder (or the latest version as available):
```
[1.7.10]TerraFirmaCraftPlus-deobf-0.89.1.jar
Waila-1.5.10_1.7.10.jar
```

Set up your environment as follows:
```
./gradlew setupDevWorkspace
```

Build mod as follows:
```
gradlew build
```

You'll also need to specify `JAVA_HOME` to point your Java 8 JDK installation, for example:
```
set JAVA_HOME=c:\Program Files\Java\jdk1.8.0_202\
```
