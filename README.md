# Conquering The Snake

## About
This is an advanced Snake game with the purpose of implementing good practices from the java specifications.
It can be easily extended to different game modes. The current modes are: `Single Player`, `Two Players`, `Hamilton Mode`, `Genetic AI`.

### Single Mode
This is the basic mode of the game. When a player dies his score is sent to an API made with `Spring Boot`
and added to a `Postgres` database and top scores are them returned to player with another api call. 

### Two Players Mode
Two players ca play on the same board, they cannot collide with each other, and they can eat their own food.
The same rule applies with the score as the one player mode

### Hamilton Mode
Here things start to get interesting... I hope. This is the first step in making and AI snake, but it's still dummy, it
has no brain. The snake will always follow a path that visits all the cells. This way the snake will never touch itself or
the walls. The idea behind is creating a MST with half of the board then try to surround the 'lines' to get the path.
More about this process in the resource section.

### Genetic AI

The idea of making this AI is combining a neural network with a genetic algorithm. So the snake will have a brain that can see the
environmnet in 8 directions. For every direction the snake sees the distace between: food, wall and tal. So in total 8 * 3 = 24 distances.
That is actually the input for the brain and as the output it get's what movement shoud do. 

To combine with a genetic algorithm the brain is used as the snake gene converting it in gray codification. As a first step, a population
of snakes is generated that explores the board. After the exploration is ending (all the snakes finished) the genetic algorihtm selectes them
in such a way that the snakes with most scores have a higher chance of surviving for the next generation. Each snake receives a reward in
the moment it get's the food and a snaller one for surviving.

Right now the snakes figure out a way only to survive longer with getting at most two foods. Furder reseach needs to be made.

Demo

[![Demo](https://img.youtube.com/vi/nT1UmezI1NA/0.jpg)](https://www.youtube.com/watch?v=nT1UmezI1NA)

 
### Libraries/Frameworks Used
 * [Processing](https://processing.org) : GUI
 * [Neural Network Library](https://github.com/StamateValentin/Artificial-Intelligence-Playground)
 * [Genetic Algorithm Library](https://github.com/StamateValentin/Genetic-Algorithms-Playground)

## Class Diagram
<img src="https://raw.githubusercontent.com/StamateValentin/Conquering-The-Snake/master/diagrams/diagram.jpg" alt="" style="border-radius: 8px">

## Resources
 * https://github.com/ProWhalen/AndrewNg-ML/blob/master/Make%20Your%20Own%20Neural%20Network.pdf
 * http://www.cs.umsl.edu/~janikow/publications/1991/GA4Opt/text.pdf
 * https://johnflux.com/2015/05/02/nokia-6110-part-3-algorithms
 * https://github.com/CheranMahalingam/Snake_Hamiltonian_Cycle_Solver
 * https://www.geeksforgeeks.org/prims-minimum-spanning-tree-mst-greedy-algo-5/
 * https://youtu.be/cplfcGZmX7I
