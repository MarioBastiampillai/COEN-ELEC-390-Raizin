package com.example.mario.raizin;

public class StateSingleton {
    private static StateSingleton instance;
    private String uv;

    public static StateSingleton instance() {
        if (instance == null) {
            instance = new StateSingleton();
        }

        return instance;
    }

    private StateSingleton() { }

    public String getUV() {
        return uv;
    }

    public void setUV(String uv) {
        this.uv = uv;
    }
}
