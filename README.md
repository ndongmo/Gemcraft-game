# Gemcraft like

This project aims to implement the so-called Gemcraft game using libgdx framework. It is a strategy tower defense game where the goal is to protect your territory (the Nexus) against some enemies coming from all sides. For this purpose, you use magic gems by putting them into towers or traps. Each gem has different effet and different cost, also it can be easily identify by its color. The player can set traps on the path of enemies and their actions depend on which gems they received. Towers can receive gems and fire bullets. Moreover, the player can update towers and traps level, and the more level they have the more gems they can receive and the more powerfull they are. The tower firing scope also depends on the tower's level. The player can also modify the behavior of its towers. The player has a life bar and a mana bar. The mana is used to buy gems, traps or towers.

## Game features
 - Campaign mode
 - Possibility to load level
 - Free level mode
 - High score
 - Multi-language : English, French
 
## Gems effect
 - Orange : drain enemies mana
 - Green : poison damage over time
 - Blue : slow down enemies
 - Red : area damage
 - Yellow : damage multiplicator
 - Cyan : paralysis effect
 - Magenta : string damage
 - Purple : vulnerability multiplicator
 
 ## Towers strategy
 - Fastest target : tracks the fastest enemy in its scope
 - Least life target : tracks the enemy with the least life
 - Most life target : tracks the enemy with the most life
 - Nexus closest target : tracks the Nexus closest enemy

## Implementation
The code source is located in [Gemcraft](https://github.com/ndongmo/Gemcraft-game/blob/master/Gemcraft) folder. It is a Gradle project, so it is easy to import on Eclipse or IntelliJ. The implementation details are shown in [class diagram](https://github.com/ndongmo/Gemcraft-game/blob/master/images/Gemcraft_diaclass.jpg).
<img src="https://github.com/ndongmo/Gemcraft-game/blob/master/images/Gemcraft_diaclass.jpg" width="70%" />

## Testing
The executable folder contains the executable jar, the config folder and the level folder. All these 3 entities should be kept together.

<table style="width:100%">
  <tr>
    <td>
      <img src="https://github.com/ndongmo/Gemcraft-game/blob/master/images/app_1.PNG" />
    </td>
    <td>
      <img src="https://github.com/ndongmo/Gemcraft-game/blob/master/images/app_2.PNG" />
    </td>
    <td>
     <img src="https://github.com/ndongmo/Gemcraft-game/blob/master/images/app_3.PNG" />
    </td>
    <td>
      <img src="https://github.com/ndongmo/Gemcraft-game/blob/master/images/app_4.PNG" />
    </td>
  </tr>
  <tr>
    <td>
      <img src="https://github.com/ndongmo/Gemcraft-game/blob/master/images/app_5.PNG" />
    </td>
    <td>
      <img src="https://github.com/ndongmo/Gemcraft-game/blob/master/images/app_6.PNG" />
    </td>
    <td>
     <img src="https://github.com/ndongmo/Gemcraft-game/blob/master/images/app_7.PNG" />
    </td>
    <td>
      <img src="https://github.com/ndongmo/Gemcraft-game/blob/master/images/app_8.PNG" />
    </td>
  </tr>
 </table>

## Authors

* F. Ndongmo Silatsa

## Licence

This project is licensed under the MIT License - see the [LICENSE.md](https://github.com/ndongmo/Gemcraft-game/blob/master/LICENSE.md) file for details

## Acknowledgments

- Soundtrack : [Eric Skiff](https://soundcloud.com/eric-skiff)
- Images : Internet
