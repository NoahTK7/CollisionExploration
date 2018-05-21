Finds and records statistics about collisions in the CRC32 hashing algorithm.
Created by Noah Kurrack, May 19, 2018.

*Requires json-simple as a dependency

To compile and run:

javac -d out -cp "src/com/noahkurrack/:dependencies/json-simple-1.1.1.jar" src/com/noahkurrack/*.java

java -cp "out/:dependencies/json-simple-1.1.1.jar" com.noahkurrack.CollisionFinder
java -cp "out/:dependencies/json-simple-1.1.1.jar" com.noahkurrack.Runner


Runner class takes one argument: number of time to run CollisionFinder.findCollision().