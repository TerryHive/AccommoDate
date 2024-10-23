package com.example.accommodate.global;

import java.io.Serializable;

public class Room  implements Serializable
{
    public final String NAME;
    public final int GUESTS;
    public final String AREA;
    public final int PRICE;
    public final int STARS;
    public final int REVIEWS;
    public final String IMAGE;

    public Room(String name, int guests, String area, int price, int stars, int reviews, String image)
    {
        NAME = name;
        GUESTS = guests;
        AREA = area;
        PRICE = price;
        STARS = stars;
        REVIEWS = reviews;
        IMAGE = image;
    }

    @Override
    public String toString()
    {
        return "Room{" + "NAME='" + NAME + '\'' + ", GUESTS=" + GUESTS +
                ", AREA='" + AREA + '\'' + ", PRICE=" + PRICE +
                ", STARS=" + STARS + ", REVIEWS=" + REVIEWS +
                ", IMAGE='" + IMAGE + '\'' + '}';
    }

}