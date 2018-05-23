## About
Finds and records statistics about collisions in the CRC32 hashing algorithm.

#### Build System
This project uses the Gradle build system to manage dependencies and compiling. System must have Gradle installed to use these commands. See  [here](https://gradle.org/) for installation.

If compiling and running manually, update classpath to location of library jars (e.g. lib/jcommander-1.72.jar).

#### Output
   Writes each collision to file serialized to json.
   File: ./collision.json

## Compiling
#### Gradle
    gradle shadowjar
Uses Shadow plugin to create 'fat jar' that includes dependencies for distribution. Avoids trouble with java classpath mechanism.

#### Manual
    javac -d out -cp "src/com/noahkurrack/:lib/json-simple-1.1.1.jar:lib/jcommander-1.72.jar" src/com/noahkurrack/*.java

## Running
#### Gradle
    java -server -jar build/libs/CollisionExploration-<version>-all.jar
#### Manual
    java -server -cp "out/:lib/json-simple-1.1.1.jar:lib/jcommander-1.72.jar" com.noahkurrack.Runner <args>


Runner class arguments:

    --amount (-a):  integer - number of times to run CollisionFinder [default: 2]
    --verbose (-v): boolean - output stats to command line [default: false]
    --file (-f):    boolean - output stats to file (collisions.json) [default: true]

## Credit
Created by Noah Kurrack, May 19, 2018. See [license](./license) file.
#### Open Source Libraries
   The following sets forth attribution notices for third party software that are used in the project. See [license](./license) file for more info.
- JCommander version 1.72 (http://jcommander.org)
- json-simple version 1.1.1 (http://code.google.com/p/json-simple/)
    ##### Gradle Plugin
- Shadow version 2.0.4 (http://imperceptiblethoughts.com/shadow/)

###### TODO

- make background process
- further configurability