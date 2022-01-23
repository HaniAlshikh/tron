package de.alshikh.haw.tron.middleware.helloworld.service;

import de.alshikh.haw.tron.middleware.helloworld.service.data.datatypes.HelloWorldMessage;

public interface IHelloWorld {
    HelloWorldMessage helloWorld();

    HelloWorldMessage helloWorldBestEffort();

    HelloWorldMessage helloWorld(HelloWorldMessage message);
}
