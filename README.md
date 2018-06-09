## About
Finds and records statistics about collisions in the CRC32 hashing algorithm.

#### Build System
This project uses the Gradle build system to manage dependencies and compiling. System must have Gradle installed to use these commands. See  [here](https://gradle.org/) for installation.

Note: If compiling and running manually, update classpath to location of library jars (e.g. lib/jcommander-1.72.jar).

#### Output
   Writes each collision to file serialized to json (or plain text).
   File: ./out/collisions.json (./out/collisions.txt)

## Compiling
#### Gradle
    gradle shadowjar
Uses Shadow plugin to create 'fat jar' that includes dependencies for distribution. Avoids trouble with java classpath mechanism.

#### Manual
    javac -d out -cp "src/com/noahkurrack/:lib/json-simple-1.1.1.jar:lib/jcommander-1.72.jar:lib/chalk-1.0.2.jar" src/com/noahkurrack/*.java

## Running
#### Gradle
    java -server -jar build/libs/CollisionExploration-<version>-all.jar
#### Manual
    java -server -cp "out/:lib/json-simple-1.1.1.jar:lib/jcommander-1.72.jar:lib/chalk-1.0.2.jar" com.noahkurrack.collision.Runner <args>


Runner class arguments:

    --amount (-a):         integer - number of times to run CollisionFinder [default: 2]
    --verbose (-v):        boolean - output current progress to command line
    --simplefile (-sf):    boolean - output stats to plain text instead of json

## Credit
Created by Noah Kurrack, May 19, 2018. See [license](./license) file.
#### Open Source Libraries
   The following sets forth attribution notices for third party software that are used in the project. See [license](./license) file for more info.
- JCommander version 1.72 (http://jcommander.org)
- json-simple version 1.1.1 (http://code.google.com/p/json-simple/)
- chalk version 1.0.2 (https://github.com/tomas-langer/chalk)
  ##### Gradle Plugin
- Shadow version 2.0.4 (http://imperceptiblethoughts.com/shadow/)

###### TODO

- further configurability