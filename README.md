# Tron VS Praktikum

> Hani Alshikh & Philip Gisella

We decided to split the Documentation into two parts, because the two parts are also split in the Code.  
The Application of course depends on the Middleware, but the Middleware could be used for other Applications / Games so it should have it's own Documentation.

## Play

To play the game please use the provided [system.properties](system.properties) as a base for the system configuration.

the system can be toggled to run locally or in a distributed fashion.

if the game should be started in a distributed fashion the [DirectoryServer](src/main/java/de/alshikh/haw/tron/middleware/directoryserver/DirectoryServer.java) musst be started. It can be found [here](src/main/java/de/alshikh/haw/tron/middleware/directoryserver/DirectoryServer.java).

to start a game the [GameManager](src/main/java/de/alshikh/haw/tron/manager/GameManager.java) musst be started. The [GameManager](src/main/java/de/alshikh/haw/tron/manager/GameManager.java) can be found [here](src/main/java/de/alshikh/haw/tron/manager/GameManager.java).

## Application Documentation

The Documentation for our Game Application as a JavaFX Application with the provided [view-libary](https://git.haw-hamburg.de/bai5-vsp-tron/view-library) can be found [here](docs/application/README.md) in our Repository.

## Middleware Documentation

The Documentation for the distributed Middleware can be found [here](docs/middleware/README.md) in our Repository.

## Template

As suggested we used the [arc42](docs/arc42-template-DE.md) Documentation Template and adjusted it to document our Design Decisions.
