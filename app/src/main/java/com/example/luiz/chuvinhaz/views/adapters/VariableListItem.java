package com.example.luiz.chuvinhaz.views.adapters;

/**
 * Created by luiz on 6/1/15.
 */
public class VariableListItem {
    String name;
    String value;

    public VariableListItem(String name, String value) {
        super();
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString()
    {
        return name + ": " + value;
    }
}
