package ru.spbau.mit.kravchenkoyura.testserver.client;

//Абстрактный класс для всех клиентов

public abstract class Client {
    protected int elementNumber, requestNumber;
    protected long delay;
    public Client(int elementNumber, int requestNumber, long delay) {
        this.elementNumber = elementNumber;
        this.requestNumber = requestNumber;
        this.delay = delay;
    }
    public abstract void start(String host, int port);
}
