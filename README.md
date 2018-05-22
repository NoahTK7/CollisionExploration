Finds and records statistics about collisions in the CRC32 hashing algorithm.
Created by Noah Kurrack, May 19, 2018.

*Requires json-simple as a dependency

To compile and run:

javac -d out -cp "src/com/noahkurrack/:dependencies/json-simple-1.1.1.jar:dependencies/jcommander-1.73.jar" src/com/noahkurrack/*.java

java -server -cp "out/:dependencies/json-simple-1.1.1.jar:dependencies/jcommander-1.73.jar" com.noahkurrack.CollisionFinder
java -server -cp "out/:dependencies/json-simple-1.1.1.jar:dependencies/jcommander-1.73.jar" com.noahkurrack.Runner


Runner class arguments:

    --amount (-a): integer - number of times to run CollisionFinder.findCollision() [default: 2]
    --verbose (-v): boolean - output stats to command line [default: false]
    --file (-f): boolean - output stats to file (collisions.json) [default: true]
    

TODO:

-threading
-background process
-build system