[![GitHub Release](https://img.shields.io/github/v/release/unforbidable/tfcplus-bids?include_prereleases)](https://github.com/unforbidable/tfcplus-bids/releases)
[![CurseForge Version](https://img.shields.io/curseforge/v/630843)](https://www.curseforge.com/minecraft/mc-mods/tfcplus-bids/files?showAlphaFiles=show)
[![CurseForge Game Versions](https://img.shields.io/curseforge/game-versions/630843)](https://www.curseforge.com/minecraft/mc-mods/tfcplus-bids/files?showAlphaFiles=show)
[![CurseForge Downloads](https://img.shields.io/curseforge/dt/630843)](https://www.curseforge.com/minecraft/mc-mods/tfcplus-bids/files?showAlphaFiles=show)
[![GitHub License](https://img.shields.io/github/license/unforbidable/tfcplus-bids)](https://github.com/unforbidable/tfcplus-bids/blob/main/LICENSE)

# TFC Plus - Bids and Pieces
An addon for TerrafirmacraftPlus that is a collection of features that try to come together to extend the world of TFC+ far and wide, but especially back.

See the [wiki](../../wiki) for complete list and detailed description of features current and upcoming.

Please find the latest release at [courseforge](https://www.curseforge.com/minecraft/mc-mods/tfcplus-bids).

### Contributions

* Saddle Quern and Stone Press related models by [talhaereny](https://github.com/talhaereny)

### Translations

* Chinese by Eternal130 (v0.57.x)
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
