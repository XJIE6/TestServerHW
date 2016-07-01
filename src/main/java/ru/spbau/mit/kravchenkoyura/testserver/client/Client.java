package ru.spbau.mit.kravchenkoyura.testserver.client;

import java.util.Random;

public abstract class Client {
    protected int elementNumber, requestNumber;
    protected long delay;
    protected Random random = new Random();
    Client(int elementNumber, int requestNumber, long delay) {
        this.elementNumber = elementNumber;
        this.requestNumber = requestNumber;
        this.delay = delay;
    }
    public abstract void start(String host, int port);
}
