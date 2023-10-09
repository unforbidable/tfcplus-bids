# TFC Plus - Bids and Pieces
A plugin for TerrafirmacraftPlus that aims to demonstrate various fully featured enhancements for gameplay and testing.

Please find the latest release at [courseforge](https://www.curseforge.com/minecraft/mc-mods/tfcplus-bids).

### Features
* [Clay and Fire Clay Crucible](../../wiki/Crucible) - new crucibles with improved mechanics
* [Ore Bits](../../wiki/Ore-Bits) - breaking ore chunks into smaller pieces
* [Metal Blowpipe](../../wiki/Metal-Blowpipe) - recipe based glassware crafting
* [Drinking Glass](../../wiki/Drinking-Glass) - various new drinking containers made out of glass
* [Furnace](../../wiki/Furnace) - for making glass in a crucible
* [Mud Brick Chimney](../../wiki/Mud-brick-chimney) - allows furnace construction before acquiring metal tools
* [Ceramic Pipe](../../wiki/Ceramic-Pipe) - used in making a mud brick chimney
* [Ceramic Mug](../../wiki/Clay-Mug) - a drinking container made out of clay
* [Drill](../../wiki/Drill) - a tool for drilling raw stone, tool heads made from stone, copper or any bronze
* [Quarry](../../wiki/Quarry) - for harvesting Rough Stone block using the drill and wedge method, sedimentary rocks only
* [Rough Stone and Rough Stone Bricks](../../wiki/Rough-Stone) - new building blocks for sedimentary rocks
* [Adze](../../wiki/Adze) - a tool for carving blocks, making rough stone bricks, peeling logs and so on, tool head made from stone, copper and any bronze
* [Carving](../../wiki/Carving) - for shaping blocks with an adze, in like manner to TFC detailing with a chisel, and carving recipes, using 3 carving modes
* [Peeled Log](../../wiki/Peeled-Log) - logs that had their bark stripped used to craft log walls
* [Log Walls](../../wiki/Log-Wall) - new building block with 6 variations for log cabin construction, and 2 variations of vertical log walls
* [Wood Pile](../../wiki/Wood-Pile) - for wood storage and seasoning peeled logs, stick bundles, and making charcoal
* [New Firepit](../../wiki/Firepit) - new firepit that can consume custom fuels, requires kindling to light
* [Kindling](../../wiki/Kindling) - kindling for lighting new firepit
* [More Stick Bundles](../../wiki/Stick-Bundle) - small and tied bundles of sticks as fuel in new firepit
* [Bark](../../wiki/Bark) - comes from peeling logs, some can be used to extract tannin, or bark fibers
* [Bark Fiber and Cordage](../../wiki/Bark-Fiber) - tying material and cordage from bark fibers
* [Drying Rack](../../wiki/Drying-Rack) - for curing bark fiber strips and drying meat, cheese, and seaweed
* [Firewood](../../wiki/Firewood) - fuel for new firepit, needs to be seasoned
* [Chopping Block](../../wiki/Chopping-Block) - for peeling and splitting logs
* [Birch Bark Equipment](../../wiki/Birch-Bark-Sheet) - kindling, bags, cups and shoes from birch bark
* [Wattle Trapdoor](../../wiki/Wattle-Trapdoor) - trapdoor made from wattle
* [Trapping Pit](../../wiki/Trapping-Pit) - for trapping mobs
* [Saddle Quern](../../wiki/Saddle-Quern) - carved from rough stone, used for crushing and grinding grain
* [Coarse Flour](../../wiki/Coarse-Flour) - made with Saddle Quern
* [Porridge](../../wiki/Porridge) - early game food from grain
* [Plug and Feather](../../wiki/Plug-And-Feather) - special wedges used for quarrying
* [Stone Press](../../wiki/Stone-Press) - lever based pressing device, used for extracting juices and oils from berries and fruits
* [~~Fluid Mixing~~](../../wiki/Fluid-Mixing) - ~~fluid mixing recipes for making Honey Water, Brine and Milk + Vinegar~~
* [Clay Lamp](../../wiki/Clay-Lamp) - stationary light source that consumes olive oil
* [Wall Hook](../../wiki/Wall-Hook) - for hanging some tools, clothes and some other items on a wall
* [Aquifer](../../wiki/Aquifer) - generates below the surface and spawns water blocks above when exposed
* [Bucket and Rope](../../wiki/Bucket-and-Rope) - for drawing water from deep wells
* [Large Bowl](../../wiki/Large-Bowl) - enables mixing barrel recipes that might require adding 500mB of liquid
* [Unfinished Anvil](../../wiki/Unfinished-Anvil) - enables anvil creation by welding seven double ingots together
* [Cooking Pot](../../wiki/Cooking-Pot) - for boiling and steaming foodstuffs and much more

### Contributions

* Saddle Quern and Stone Press related models by [talhaereny](https://github.com/talhaereny)

### Translations

* Chinese by Eternal130 (v0.31.x)
* Japanese by dymanic.day (v0.28.x)

### Other credits

* [TFCraft-NEIplugin](https://github.com/tfc-plus-addons/TFCraft-NEIplugin)

### Compatibility

* Waila - block information for crucible, quarrying, glassmaking, wood pile items, drying rack, saddle quern, stone press, etc
* Not Enough Items - recipes for seasoning, quarrying, drying, carving, chopping, saddle quern, stone press and new firepit fuels

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
