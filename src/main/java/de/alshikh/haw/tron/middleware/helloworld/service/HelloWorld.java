package de.alshikh.haw.tron.middleware.helloworld.service;

import de.alshikh.haw.tron.middleware.helloworld.service.data.datatypes.HelloWorldMessage;

public class HelloWorld implements IHelloWorld {
    public HelloWorld() {}

    @Override
    public HelloWorldMessage helloWorld() {
        return new HelloWorldMessage("Hello World");
    }

    @Override
    public HelloWorldMessage helloWorld(HelloWorldMessage message) {
        return new HelloWorldMessage("Hello World " + message.getMessage());
    }
}
