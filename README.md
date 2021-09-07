# SB-application

Spring Boot log processing application for detecting events longer than 4ms.
#### Created with:
Java | Maven | Spring boot | HSQLDB
***
How to use
-----------
1. Clone this project
2. Run in console `mvnw package` [for WINDOWS] or `./mvnw package` [for MAC]
3. Go to target sub directory `cd target` and run the jar file with `filepath` `java -jar logsParsing-1.0-SNAPSHOT.jar filepath`
4. Check `eventdb` and `eventdb.log` to verify results

TODO
--------------------
1. Tests
2. Multithreading version
