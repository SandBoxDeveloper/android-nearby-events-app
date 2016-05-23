package com.hulldiscover.zeus.locationdistancecalulation.Model;

import android.graphics.PointF;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Zeus on 12/05/16.
 */
public class Event implements Serializable {

    int id;
    BigDecimal priceCatA;
    BigDecimal priceCatB;
    BigDecimal lowestPrice;
    String title;
    String event_image;
    int noOfTickets;
    PointF location;
    Integer distance;


    public Event() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getPriceCatA() {
        return priceCatA;
    }

    public void setPriceCatA(BigDecimal price) {
        this.priceCatA = price;
    }

    public BigDecimal getPriceCatB() {
        return priceCatB;
    }

    public void setPriceCatB(BigDecimal price) {
        this.priceCatB = price;
    }

    public BigDecimal getCheapestPrice() {
        return lowestPrice;
    }

    public void setCheapestPrice(BigDecimal price) {
        this.lowestPrice = price;
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

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

}
