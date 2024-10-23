package com.example.accommodate.global;

import java.io.Serializable;

public class Message implements Serializable
{
    private final long id;
    private final Action action;
    private final Object parameters;

    public Message(long id, Action action, Object parameters)
    {
        this.id = id;
        this.action = action;
        this.parameters = parameters;
    }

    public long id()
    {
        return id;
    }

    public Action action()
    {
        return action;
    }

    public Object parameters()
    {
        return parameters;
    }
}