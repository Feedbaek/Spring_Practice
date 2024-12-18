package com.example.test.DI;

public class D {

    private final A a;

    public D(A a) {
        this.a = a;
    }

    public void doSomething() {
        a.doSomething();
    }
}
