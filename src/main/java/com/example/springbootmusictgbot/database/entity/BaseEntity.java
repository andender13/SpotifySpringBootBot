package com.example.springbootmusictgbot.database.entity;

import java.io.Serializable;

public interface BaseEntity<T extends Serializable>
{
    void  setID(T id);
    T getID();
}
