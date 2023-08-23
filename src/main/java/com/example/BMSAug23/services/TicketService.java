package com.example.BMSAug23.services;

import com.example.BMSAug23.exceptions.InvalidUser;
import com.example.BMSAug23.exceptions.SeatsAlreadyBookedException;
import com.example.BMSAug23.models.Ticket;

import java.util.List;

public interface TicketService {

    public Ticket bookTicket(List<Integer> showSeatIds, int userId) throws SeatsAlreadyBookedException, InvalidUser;
}

