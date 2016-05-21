package com.hulldiscover.zeus.locationdistancecalulation.Model;

import android.graphics.PointF;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Zeus on 12/05/16.
 */
public class Event implements Serializable {

    int id;
    BigDecimal price;
    String title;
    String event_image;
    int noOfTickets;
    PointF location;


    public Event() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEvent_image() {
        return event_image;
    }

    public void setEvent_image(String event_image) {
        this.event_image = event_image;
    }

    public int getNoOfTickets() {
        return noOfTickets;
    }

    public void setNoOfTickets(int noOfTickets) {
        this.noOfTickets = noOfTickets;
    }

    public PointF getLocation() {
        return location;
    }

    public void setLocation(float pointX, float pointY) {
        this.location = new PointF();
        this.location.x = pointX;
        this.location.y = pointY;
    }
}
