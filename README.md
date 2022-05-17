# TerraFirmaCraft Plus - Bids and Pieces
A plugin for TerrafirmacraftPlus that aims to demonstrate various enhancements

## New crucible mechanic
Offers to solve some of the shortcomings of the existing crucible, and offers and an alternative (and a replacement) for the stone age pottery kiln smelting.

- Several input slots for material (stacks) to be melted
- Input slot for pouring molten metal into the crucible from an unshaped mold
- Output slots for draining molten metal into a mold
- Needs to be placed on top of a forge
- A gauge shows the combined temperature of metals being heated up and molten metal
- All input materials are heated up at the same time
- The speed at which materials are heated up depends on their type, amount, and size
- A gauge shows the amount of molten metal
- Crucible has two modes, SETUP and COOKING
- Adding and removing materials into input slots, pouring molten metal into the crucible and draining molten metal out is only allowed in the SETUP mode
- Materials are smelted only in the COOKING mode
- Molten metal is heated up and kept heated up in both modes
- The player can freely switch between modes, although the temperature of the materials in the input slots will reset if they are manipulated
- When all input materials are smelted the crucible automatically switches to SETUP mode
- The player can start the COOKING mode only when:
  - There are valid materials in the input slots
  - There is enough room for the materials in the input slots to fit into the crucible
- Crucible can be broken and picked up to be placed elsewhere, but any unsmetled materials will be dropped

### Example target cooking times
| Output | No interaction | Blowpipe | Manual Bellows |
| --- | --- | --- | --- |
| 5x bronze | 10h | 7h | 4h |

## New crucible variations
A clay crucible is available in the stone age with limited capabilities. 
A proper file clay crucible is available with expanded capabilities.

### Feature comparison
| Type | Max Temp | Max Volume | Input Slots | Drainage Slots |
| --- | ---: | ---: | --- | ---: |
| Clay | 1100 | 2000 | 4 ore bits | 1 |
| Fire clay | 2500 | 6000 | 9 any size ore + 3 ingot | 3 |

Because a clay crucible accepts only ore bits, the maximum volume of a batch is 1280, so two runs will be needed when making an anvil.

## Ore bits
Allows the stacking of all ore sizes, with better storage efficiency (except rich), simpler alloying math and faster smelting.

- Ore chunks (small, poor, normal, rich) can be hammered into bits
- One ore bit yields 5 units of metal and stack up to 64
- Ore bits heat up faster in a crucible

### Yield vs size comparison 
| Size | Stack Yield | Small Vessel Yield |
| --- | ---: | ---: |
| Bit | 320 | 1280 |
| Small | 160 | 640 |
| Poor | 240 | 960 |
| Normal | 400 | 1200 |
| Rich | 560 | 2240 |
| Ingot | 3200 | n/a |

## Other changes
Other conceptual changes.

- Only ore bits and small pieces can fit into a small vessel
- When grinding ore into grain in the future, ore needs to be hammered into bits first


