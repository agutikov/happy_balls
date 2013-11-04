happy_balls
===========


TODO
----

* Docs
	- javadocs, comments
	- model class hierarchy
	- engine internals (general), model internals, rendering internals


questions
---------

* Why suvitruf.Lesson2 works on emulator, but android application from
https://github.com/libgdx/libgdx/wiki/Manual-project-setup not?
Answer: add android-support-v4.jar to libs.
Why nothing about it in libgdx Wiki?

* correct activity lifecycle for android and ApplicationListener implementation
  in libgdx at all

* How to copy and rotate loaded texture and then use it rotated?

* How to resize window and save size of game screen but just change viewport?

* What about full screen on PC and android?

* Есть ли смысл заменить стандартные контейнеры на libgdx-овские?

architecture features, optimizations
------------------------------------

* world model
	- coordinates of objetcs related to there center,
	- start - left bottom
	- map objects - in linked 2-dim array - for simply checking collisions
	- map layers: flor, boxes and walls, player and enemies, effects (blows)
* rendering model:
	- layers - 2d array of blocks
	- coordinates of sprites in rendering format
	- relations with screen resolution and sprites resolution
	- "changed" flags for blocks, and whole changed flag for render on resize
	  and other
* output proxy: converts changes in world model to changes in rendering model
* input proxy: get coordinates in pixels and convert them to world coordinates
* game world model and whole game model (with vieport position, player settings,
  scores, etc...)

* check map correctness during loading

* git workflow - stable master, work in branches
* maven integration, remove libs from git - make them downloadable
* attach sourses to all jars

* atlas
* tiled map
* initialization in background




design features
---------------

