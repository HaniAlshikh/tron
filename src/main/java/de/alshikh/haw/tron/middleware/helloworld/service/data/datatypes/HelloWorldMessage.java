package de.alshikh.haw.tron.middleware.helloworld.service.data.datatypes;

public class HelloWorldMessage {

    private final String message;

    public HelloWorldMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "HelloWorldMessage{" +
                "message='" + message + '\'' +
                '}';
    }
}
