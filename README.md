SPACE FARM:


A farming-themed tower defense game. 


CONTROLS:
1-5: Select wall/crop to place
Left Mouse Button: Place wall/crop
Right Mouse Button: Remove wall/harvest crop


CHEAT CODES:
P: Toggle debug (see pathfinding values/path)
L: Give the player +$100
N/M: Go to previous/next levels


LOW-BAR GOALS: 
--Crop Planting: Completed. Crops have different growth times, going from sprout-phase to mature-phase, with different capabilities granted when entering maturity. 

--Wave-based Gameplay: Completed. Game is broken down into a "Build" phase where they spend resources to plant crops and fortify their defenses, and a "Wave" phase where enemies spawn and attempt to get past the player's defenses. Levels have distinct sets of enemy "waves". Each of the waves have variable enemy spawning patterns. 

--Intelligent Enemy Pathfinding: Completed. Enemies utilize Dijkstra's algorithm to find the most efficient path to the player's base (the game ends once enough enemies have reached the base). This includes intelligently navigating through obstacles if the pathfinding algorithm deems it more efficient than walking around the obstacles.

--Economics: Completed. Player can harvest crops for more than they cost to plant, in order to save cash and buy more crops. An attempt was made at balancing crop planting costs, crop harvest values, and crop utility (more powerful crops are more expensive, cheap crops don't give as much value once harvested).

--Crop Variety: Completed. Four crops have been implemented so far, in addition to "wall" tiles which have no utility other than blocking enemy movement. Moonflowers are the default starter crop. They are cheap, and rapid-fire weak projectiles that are consumed on hit. Bomb Melons do damage in an Area of Effect centered on themselves, with a short cooldown. Jewel plants are purely for generating cash (they harvest for double what they cost to plant, although they take multiple waves to mature), and have no defensive capabilites. Finally are the Piercer plants, which fire a single, powerful bullet that damages all enemies along its path. 


OTHER FEATURES:
--Fast Forward & Skip Buttons: Enable the player to control the flow of time, and skip remainder of build phase in an attempt to speed up gameplay. 

--UI Tooltips: Game produces floating text when placing/harvesting crops to indicate to the player how much cash is being consumed/generated.

--Realized Visuals: The UI is still not entirely up to my standards, but I am much happier with my world-graphics. The game assets are entirely my own, designed with a limited palette in 32x32 pixel tiles scaled to 200%. The black "untraversable" ocean tiles also have their placements randomly generated to reduce visual clutter. 
