package com.example.BMSAug23.models;

import jakarta.persistence.Entity;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class Ticket extends BaseModel{

    private User user;

    private List<Seat> seats;

    private Show show;

    private Date timeOfBooking;
    private double price;

}
