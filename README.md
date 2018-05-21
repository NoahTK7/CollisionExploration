Finds and records statistics about collisions in the CRC32 hashing algorithm.
Created by Noah Kurrack, May 19, 2018.

*Requires json-simple as a dependency

To compile and run:

javac -d out -cp "src/com/noahkurrack/:dependencies/json-simple-1.1.1.jar" src/com/noahkurrack/*.java

java -server -cp "out/:dependencies/json-simple-1.1.1.jar" com.noahkurrack.CollisionFinder
java -server -cp "out/:dependencies/json-simple-1.1.1.jar" com.noahkurrack.Runner <amount> <verbose>


Runner class arguments:

    amount: integer - number of times to run CollisionFinder.findCollision() [default: 2]
    verbose: boolean - output stats to command line [default: false]
    

TODO:

-file output argument
-threading
-background process