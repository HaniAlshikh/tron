package de.alshikh.haw.tron.middleware.helloworld;

public class HelloWorld implements IHelloWorld {
    public HelloWorld() {}

    @Override
    public String sayHello() {
        return "Hello World";
    }
}
