## Implementation & Extensions (All working) ##

1. - [x] Implemented all the steps including the winning a level step.
2. - [x] Possibility to get extra lives, which also give a time bonus.
3. - [x] Procedural generation of Pixmap for next level.
	*  including rocks of random length (50% chance for spawning a rock).
	*  Gaps between the rocks (for a max of one pixel of a gap).
	*  The bunny spawning on the first rock (100% chance).
	*  The goal spawning on the last rock (100% chance).
	*  Spawning of gold coins, extra lives, and feathers randomly between the bunny spawn and the goal spawn (gold coins have 3/4 and 1/2 chance to spawn, extra lives have 10% chance, and feathers have 15% chance to spawn).
4. - [x] Have a time limit to complete level (90 seconds). The play is penalized for dying by the time to complete being reduce by half (45 seconds), if they die again they are then penalized a third time (the timer is reduce to 30 seconds).
5. - [x] The player must have a score over 1000 (ten gold coin) to complete the level.
