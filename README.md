# TFC Plus - Bids and Pieces
A plugin for TerrafirmacraftPlus that aims to demonstrate various fully featured enhancements for gameplay and testing.

Please find the latest release at [courseforge](https://www.curseforge.com/minecraft/mc-mods/tfcplus-bids).

### Features
* Clay and Fireclay [Crucible](../../wiki/Crucible) - new crucibles with improved mechanics
* [Ore Bits](../../wiki/Ore-Bits) - breaking ore chunks into smaller pieces
* [Metal Blowpipe](../../wiki/Metal-Blowpipe) - recipe based glassware crafting

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
