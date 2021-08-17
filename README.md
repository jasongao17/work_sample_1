Table of contents
=================

<!--ts-->
   * [Milestone One](#milestone-one)
      * [Project Description](#project-description)
      * [Feedback from TA
](#feedback-from-ta)
      * [Follow-up tasks](#follow-up-tasks)
   * [Milestone Two](#milestone-two)
      * [Main responsibilities](#main-responsibilities)
      * [Project Roadmap](#project-roadmap)
      * [Progress so far](#progress-so-far)
   * [Milestone Three](#milestone-three)
      * [Mockup Concrete Language Design](#mockup-concrete-language-design)
      * [User Study notes](#user-study-notes)
      * [Changes to the Original Language Design](#changes-to-the-original-language-design)
   * [Milestone Four](#milestone-four)
      * [Status of implementation](#status-of-implementation)
      * [Final User Study](#final-user-study)
      * [Final Plans](#final-plans)
   * [Citations](#citations)
      * [Image resource](#image-resource)
   * [Final Notes](#final-notes)
      * [Final User Study result](#final-user-study-result)
      * [Comments on the project](#comments-on-the-project)
   * [Documentation](#documentation)
      * [Introduction](#introduction)
      * [Usage](#usage)
         * [Player](#player)
         * [Monsters](#monsters)
         * [Dungeons](#dungeons)
         * [Element](#element)
            * [Booster](#booster)
            * [Item](#item)
            * [Miscellaneous](#miscellaneous)
            * [Loop](#loop)
<!--te-->

# Milestone One

## Project Description

We are planning to design a dungeon maker which will be similar to the [**Tower of the Sorcerer**](https://tig.fandom.com/wiki/Tower_of_the_Sorcerer) game. We discussed things about the features of the main character, including the moving commands, attack command, health points setting and special skills setting. Moreover, the game designers(clients) can design the structure of the missions; configure features such as monsters’ HP, ATK, DEF, AND movement(static or shift as loops); Also clients can generate easter eggs in the game.

## Feedback from TA

Our DSL needs to have programming capability, which means users of our DSL should have freedom to implement unique dungeon games as they want. This includes users being able to decide where to put the walls, where to put the enemies, and where to put items and so on. In order to achieve this goal, TA suggested that we should add not only enough features(like different walls, enemies, items etc.), but also some syntaxes like `if` and `loop` statements to enhance the functionality of our DSL.

Moreover, the DSL should also be innovative, useful. Some of our potential DSL options, like the web maker, have already had matured DSLs to deal with the problems in the industry. So we should not repeat the jobs. Our DSL is not just for final exhibition, but we really want to provide a dungeon game maker with a simpler procedure that they can only use several simple commands to implement the game.

## Follow-up tasks

1. Language Grammar Decision and Documentation(include the EBNF format)
2. Generating User stories(some, due to the time constraint)
3. Implementation of the DSL followed with the user stories
4. User Interface design and implementation(Not a fancy UI, due to time constraint)
5. Testing and deploying

# Milestone Two

## Main responsibilities

1. Grammar, Tokenizing, Parsing : Haolin Wu, Zhenhuan Gao, Kuanmin Huang
2. Class diagram and class generation : Yihao Zheng, Yuchen Yang

## Project Roadmap

- (Sep 28th) Finalize the grammar
- (Sep 28-30) First user study
- (Sep 30th) Class diagram and java class files 
- (Sep 30th) Finish the tokenizer

- (Oct 3rd) Finish parser/validator 
- (Oct 3rd) More user study (if necessary)

- (Otc 9th) M4: Should have done logics (general logic on program and the game logic) and implementation besides UI
- (Oct 9th) Final user study

- (Oct 16th) UI and documentation done

- (Oct 18th) Video done

## Progress so far

1. Language grammar decision done (EBNF format done as well)
2. Discussed on how the game logic and implementation will be
3. Writing Tokenizer
4. Writing java classes

# Milestone Three

## Mockup Concrete Language Design

Essentially the language will allow users to create a player, create multiple monsters, create multiple dungeon and add multiple items/elements in the dungeon.

### EBNF Grammar:
```
PROGRAM ::= PLAYER MONSTER* DUNGEON+ 

MONSTER ::= “DEFINE” MONSTER_TYPE REFERENCE_NAME “{” HP ATK DEF PATH “}”

PLAYER ::= “DEFINE PLAYER” “{” HP ATK DEF “}”

DUNGEON ::= “DEFINE DUNGEON” REFERENCE_NAME POSITION “{“ START GOAL DUNGEON_OBJECT* “}”

DUNGEON_OBJECT ::= REFERENCE_MONSTER | COIN | DOOR | KEY | WALL | ITEM | SET | DELETE

REFERENCE_MONSTER ::= “MONSTER” REFERENCE_NAME POSITION “;”

ITEM ::= HP_MAX | ATK_MAX | DEF_MAX | HEAL_POT

MONSTER_TYPE  ::= “SLIME” | “SKELETON” | “BOSS”

HP ::= “HEALTH” NUM “;”

ATK ::= “ATTACK” NUM “;”

DEF ::= “DEFENCE” NUM “;”

POSITION ::= “(” NUM “,” NUM “)”

START ::= "START" POSITION “;”

GOAL ::= “GOAL” POSITION “;”

WALL ::= “WALL” POSITION “;”

HP_MAX ::= “HP_MAX” POSITION NUM “;”

ATK_MAX ::= “ATK_MAX” POSITION NUM “;”

DEF_MAX ::= “DEF_MAX” POSITION NUM “;”

HEAL_POT ::= “HEAL_POT” POSITION NUM “;”

COIN ::= “COIN” POSITION “;”

DOOR ::= “DOOR” COLOR POSITION “;”

KEY ::= “KEY” COLOR POSITION “;”

DELETE ::= "DELETE" POSITION “;”

SET ::= WALL_SET | COIN_SET | DELETE_SET

WALL_SET ::= “WALL” DIRECTION POSITION NUM “;”

COIN_SET ::= “COIN” DIRECTION POSITION NUM “;”

DELETE_SET ::= “DELETE” DIRECTION POSITION NUM “;”

PATH ::= “PATH” “[“ (DIRECTION (“,” DIRECTION )*)* ”]” 

DIRECTION ::= (LEFT | RIGHT | UP | DOWN) 

NUM ::= [0-9]+

REFERENCE_NAME ::= [A-Za-z0-9_ ]+   // Name can be combination of letters, numbers, _ or spaces

COLOR ::= “GREEN” | “BLUE” | “RED”
```

Check input.tcts for sample input of the language

## User Study notes

The user has knowledge in programming and has played video games. The user was given a brief description on what the program is meant to do, we then showed him a sample of our input language and ask him if he can guess and explain to us what each statement is telling the program to do. Lastly we asked for his opinions and feedback on the language.

The user was able to guess almost all the grammar meaning correctly and gave some suggestion on the grammar names and positioning to make it more intuitive and consistent. 

One concern the user had was that for building a large dungeon, it would be difficult to keep track of objects in the dungeon and especially for walls in the dungeon since there will be a lot of walls created for a large dungeon. The user suggested that it might be better to have a new input that allows users to create small rooms and put these rooms into the dungeon, the rooms created will automatically be surrounded by walls so users will have a better time keeping track of walls and items by keeping track of rooms and the items in the rooms instead.

The input for room creation has been written but due to time constraint we have added this feature into our backlog. For now the solution to mitigate this problem is to have an input that allows users to add a set of walls in a direction.

## Changes to the Original Language Design 

Most changes are related to simplifying/changing the grammar inputs to make the grammar more intuitive and consistent with other inputs. We also added a statement to allow putting multiple walls to address user study feedback.

# Milestone Four

## Status of implementation

Implementation of game logic is closing to an end. User interface will be started right away. Plans and actions of adding list of coins or list of delete grammar into our language similar to adding list of walls. Plans and actions of adding room creation into our DSL.

## Final User Study

The plan for our final user study is similar to the previous one. User should have knowledge of basic programming and video game experience. 

### We will give the user:
1. Brief description of our DSL
2. Sample inputs
3. Grammar of our DSL (the EBNF)
4. The user interface of the sample inputs

### Feedback Expected from the User:
1. The difficulty of learning and writing the input in a correct grammar/syntax
2. How satisfactory the user is for our language and the features (such as generating list of walls, coins)
3. What should we improve and add to the language in the future

## Final Plans
Time is ticking!

- (Oct 16th) the whole DSL and documentation done
- (Oct 16th) Final user study done
- (Oct 18th) Video done

# Citations

## Image resource

- Player ::= https://www.pinterest.ca/pin/292171094577895155/
- Heart, Sword, Shield, Skeleton and Boss ::= https://www.deviantart.com/
- Pixel audience(koukou.jpg) ::= http://pixelartmaker.com/art/42895229ffa030d
- Flag ::= http://pixelartmaker.com/art/85a5e4769d3da43
- Other images originated from Zhenhuan Gao.

# Final Notes

## Final User Study result

The user had no knowledge of game/ui design and have never learned about DSL and EBNF grammar.

### Steps taken: 
1. We gave the user a background of our project and show a sample input of the grammar.
2. We ask the user to guess and tell us what each statement means
3. We asked for feedback and improvements on the grammar
4. We showed the user a picture of a dungeon and asked the user to write an input to generate this dungeon
5. We asked for feedback and improvements again after writing the input and show the ui of the input

### Result
- The user was able to understand and guess correctly what each statement means
- The picture produced using the user's input in the exercise was almost identical to the actual dungeon picture (Missing 1 wall)
- There was problems with validation/parser with the input but the user was able to spot the error and the reason through error handling messages
- The user had some problems visualizing where the walls are in the dungeon (Although they were still able to accurately produce the correct dungeon)
- Overall user study and feedback was positive as it was simple to understand, easy to use and the user would be able to produce a new dungeon from scratch if they are asked to

## Comments on the project

The overall quality of the project is good as there was good usability and error handling in a reasonable way based on the user studies.

### Problems (Fixed now) encountered while coding and building our project:

#### Tokenization stage
- Having fixed literals that is a sub-string of another (DEF and DEFINE).

#### Evaluation stage
- Problems with sizing of the pictures and dungeon not centered or square shaped. 
- Problems with Key press not responding and dungeon not updating on level change. 
- There was single responsibility problems. For example, the UI was the one in charged of switching dungeon level but the game controller should be the one to handle this.

### Negative points:
- Although its easier to keep track of walls now its still hard to visualize all the position of the walls
- Violation of principles (Regularity), the position values in the dungeon statement represents the dungeon weight and height but the dungeon object position means the x and y coordinate of the dungeon even though they are defined the same (Interestingly both user study the user was not confused about this)
- You cannot add the same monster twice in a dungeon (This is due to monster being passed by reference to the dungeon)

### Improvements:

#### Grammar
- Add the room statement suggested by the user to handle wall creation and visualization better
- Generate a new monster instead of reference it in a dungeon to handle same monster appearing in dungeon twice
- Add traps that can move or appear for a duration

#### UI
- Show animation when a fight is occurring and when user interact take an item.

#### Game
- Handling racing condition when both monster and player move to the same grid at the same time
- Allow going back to previous level (Save states of previous level and now there can be multiple goals each leading to different levels)
- Overhaul on battle to change to skill-based.

# Documentation

## Introduction

The Dungeon Game DSL is to help people with no programming or ui/game design experiences pereicens to develop a Tower of the Sorcerer type game efficiently and effectively. The DSL is external, which means it will be interpreted into Java code, and then run by JVM. In order to do a successful interpretation, the language has already been implemented with efficient tokenizer, parser, validator, and evaluator. The AST nodes produced by previous processes are then consumed by the game logic and UI to create the dungeon game.

The game passes when a player goes through all levels. During the game, the player cannot let his/her HP go to 0. Otherwise, the player fails to pass the game. Alongside with that, we add a small challenge that the player should collect as many coins as possible.

The simple documentation is here to help you with basic usage of this DSL. Throughout the documentation, you will not only learn how to use different functions in the DSL, including some shortcut methods, but also you will gain a better understanding of how this DSL works.

## Usage

In order to have a valid Game definition, you need to have 3 components: Player, Monsters and Dungeons. They need to appear in order of player, monsters then dungeons, otherwise, an error will be thrown.

### Player

To define a player, you can start with the following code.

```
DEFINE PLAYER {
	HEALTH 500;
	ATTACK 25;
	DEFENCE 20;
}
```

This will define the initial state of the player with 500 hp, 25 attack and 20 defence values. This definition block is wrapped by curly braces. It contains three attributes, HEALTH, ATTACK and DEFENCE. Each is separated by a semicolon.

#### Requirements: 
- Definition block must contain 3 fields: HEALTH, ATTACK, DEFENCE
- HEALTH, ATTACK and DEFENCE must be whole numbers 
- HEALTH must be greater than 0

### Monsters

There are three types of monsters which can be defined: SLIME, SKELETON and BOSS. All these three types of monsters can be customised in the same way and they are identical in functionality. The only difference is in UI representation. To define a monster start with the following code.

```
DEFINE SLIME a_cute_slime {
	HEALTH 85;
	ATTATCK 5;
	DEFENCE 20;
	PATH [DOWN, DOWN, UP, LEFT, RIGHT]
}
```

In this sample code, we define a monster slime with the name a_cute_slime. It has 85 hp, 5 attack and 20 defence values. It will go in a path of DOWN, DOWN, UP, LEFT, RIGHT where each direction means a movement of an x or y coordinate of the dungeon (UP = y - 1, DOWN = y + 1, LEFT = x - 1, RIGHT = x +1). 

The path will run in a loop but does not guarantee to go back to the initial position after a loop. Therefore, if you want to make a closed loop, you may need to write it in a ‘reversed palindrome’, such as [DOWN, LEFT, LEFT, RIGHT, RIGHT, UP]. If you want to have a monster which stays in a fixed position, you can have an empty PATH list []. If the monster tries to move to an impassable object, it will be blocked and stop moving. 

When the monster encounters the player, they will attack each other until one of them gets killed (ends with 0 HP). Each turn the lost HP of the attacked object is (ATK of opponent - player/monster's DEF). If the defence value is greater or equal to the attack then the resulting damage is 1.

##### Requirements:
- All defined monsters must have unique names
- Names can be alphabetical letters with spaces and a special character “_”
- Definition block must contain 4 fields: HEALTH, ATTACK, DEFENCE and PATH
- HEALTH, ATTACK, DEFENCE must be whole numbers 
- HEALTH must be greater than 0
- PATH must can be empty or contain 4 possible direction values: UP, DOWN, LEFT and RIGHT
- 

### Dungeons

The order of the defined dungeon will represent the levels of the dungeon where first dungeon defiend is the first level.
To define a dungeon, you must give the name and initial size of the dungeon.

```
DEFINE DUNGEON my_dungeon (11, 10) {
	Start (0, 0)
	Goal (5, 5)

	// … more elements
}
```

The coordinate of the dungeon represents the width (x-coordinate) and height (y-coordinate) of the dungeon. 
The dungeon will represented by a 2D grid where coordinate (0, 0) is at the top left corner and Y-axis goes in downward direction.
The definition block represents the objects to be added to the dungeon. You can add any number of different elements into the dungeon.

If you don’t want a rectangular/square dungeon, you can trim the blocks of dungeon by calling the delete function:

```
        DELETE (X, Y)
        DELETE DIRECTION (X, Y) NUM;  
```

Details can be found on the ELEMENT or LOOP section.

##### Requirements:
- Dungeon names must be unique 
- Dungeon names can be alphabetical letters with spaces and a special character “_”.
- Start and Goal element must be at the start of the dungeon definition block in order.
- Dungeon size limit is 20 width and 20 height
- Elements to be added to the dungeon must be inside the dungeon
- Elements can not be added to the same poistion
- Same monster name cannot be added to the same dungeon.

### Element

### Booster

##### HP_MAX
```
				HP_MAX (X, Y) AMOUNT;
```

- (X, Y): The coordinate to place the element in the dungeon.

- AMOUNT: The amount to add to the player’s attributes.

The HP_MAX increases the upper limit of the player’s hp by the given amount. It will also increase the current HP of the player by the given amount.

##### ATK_MAX
```
				ATK_MAX (X, Y) AMOUNT;
```

- (X, Y): The coordinate to place the element in the dungeon.

- AMOUNT: The amount to add to the player’s attributes.

The ATK_MAX increases the upper limit of the player’s attack by the given amount.

##### DEF_MAX
```
				DEF_MAX (X, Y) AMOUNT;
```

- (X, Y): The coordinate to place the element in the dungeon.

- AMOUNT: The amount to add to the player’s attributes.

The DEF_MAX increases the upper limit of the player’s defence by the given amount.

##### HEAL_POT
```
                HEAL_POT (X, Y) AMOUNT;
```

- (X, Y): The coordinate to place the element in the dungeon.

- AMOUNT: The amount to add to the player’s attributes.

The HEAL_POT increases the current player’s hp by the given amount. It will not exceed the player’s max hp.

#### Requirements:
- AMOUNT must be greater than 0
- (X, Y) must be a coordinate within dungeon

### Item

##### COIN
```
                COIN (X, Y);
```

- (X, Y): The coordinate to place the element in the dungeon.

COIN is an element that players can take that counts towards the game score. 

##### KEY
```
                KEY COLOR (X, Y);
```

- COLOR: The color of the key. Possible values - RED, BLUE, GREEN. 

- (X, Y): The coordinate to place the element in the dungeon.

KEY is an element that players can take. The color of the key can be used to open the door with the same color.

#### Requirements:
- (X, Y) must be a coordinate within dungeon

### Miscellaneous

##### WALL
```
                WALL (X, Y);
```

- (X, Y): The coordinate to place the element in the dungeon.

Places a wall in the dungeon.

##### DOOR
```
		        DOOR COLOR (X, Y);
```

- COLOR: The color of the key. Possible values - RED, BLUE, GREEN. 

- (X, Y): The coordinate to place the element in the dungeon.

DOOR is an element that is usually inaccessible to the player. The only way to pass the door is if the player has the key with the same color to the door. The key will then be consumed to unlock the door permanently.

##### MONSTER
```
	        	MONSTER NAME (X, Y);
```

- NAME: The name of a defined monster

- (X, Y): The coordinate to place the element in the dungeon.

This is how you place the monster you have defined previously into the dungeon.

##### START
```
                START (X, Y);
```

- (X, Y): The coordinate to place the element in the dungeon.

The coordinate of the starting point of the player, this will also place the player into the dungeon at that location.

##### GOAL
```
                GOAL (X, Y);
```

- (X, Y): The coordinate to place the element in the dungeon.

The coordinate of the goal. If the player reaches the goal, the level will be cleared. If there are multiple dungeons then the next dungeon defined will be the next level the player will be in.

##### DELETE
```
				DELETE (X, Y);
```

- (X, Y): The coordinate of the fundamental block that you want to delete. 

DELETE deletes one block of the game panel. The DELETE is not the meaning of removing the element inside a block. You will only want to use it when making a special shaped dungeon.

#### Requirements:
- (X, Y) must be a coordinate within dungeon
- The name of a monster to be added to the dungeon must correspond to the name of a monster defined previously
- There can only be one Start and Goal element placed in the dungeon 

### Loop

We consider having loop features in our language to give users a shortcut to do things like adding the walls, coins or trimming the dungeon efficiently.

##### Delete blocks
```
                DELETE DIRECTION (X, Y) NUM; 
```

- DIRECTION: The direction to delete the blocks. Possible values - UP, DOWN, LEFT, RIGHT.

- (X, Y): The coordinate of start point to delete the fundamental blocks
 
- NUM: number of blocks that you want to delete

This statement will delete a number of blocks in the direction stated. The first delete will occur on the starting point.

##### Add walls
```
				WALL DIRECTION (X, Y) NUM;
```

- DIRECTION: The direction to place the wall elements. Possible values - UP, DOWN, LEFT, RIGHT.

- (X, Y): The coordinate of the start point to add the walls.
 
- NUM: number of walls you want to add.

This statement adds a number of walls in the direction stated. The first wall added will be on the starting point.

##### Add coins
```
				COIN DIRECTION (X, Y) NUM;
```

- DIRECTION: The direction to place the coin elements. Possible values - UP, DOWN, LEFT, RIGHT.

- (X, Y): The coordinate of the start point to add the walls.
 
- NUM: number of walls you want to add.

This statement adds a number of coins in the direction stated. The first wall added will be on the starting point.

#### Requirements:
- (X, Y) must be a coordinate within dungeon
- The number to add must not cause the elements to be added outside of the dungeon
- NUM must be a whole number
