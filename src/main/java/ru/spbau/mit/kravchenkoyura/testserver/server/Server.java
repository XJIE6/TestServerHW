package ru.spbau.mit.kravchenkoyura.testserver.server;

import java.io.IOException;

//Интерфейс для всех серверов

public interface Server {
    public abstract void start();
    public abstract int getPort();
    public abstract void close() throws IOException;
}
