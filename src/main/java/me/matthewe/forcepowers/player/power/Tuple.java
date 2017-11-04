package me.matthewe.forcepowers.player.power;

/**
 * Created by Matthew E on 11/4/2017.
 */
public class Tuple<A, B> {
    private A a;
    private B b;

    public Tuple(A a, B b) {
        this.a = a;
        this.b = b;
    }

    public A getA() {
        return a;
    }

    public Tuple<A, B> setA(A a) {
        this.a = a;
        return this;
    }

    public B getB() {
        return b;
    }

    public Tuple<A, B> setB(B b) {
        this.b = b;
        return this;
    }
}
