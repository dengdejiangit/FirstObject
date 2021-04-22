package com.example.com_gift.bean;

public class TypeSelect {
    private String name;

    public TypeSelect(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TypeSelect{" +
                "name='" + name + '\'' +
                '}';
    }
}
