package com.example.BMSAug23.services;

import com.example.BMSAug23.exceptions.InvalidUser;
import com.example.BMSAug23.exceptions.SeatsAlreadyBookedException;
import com.example.BMSAug23.models.*;
import com.example.BMSAug23.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService{

    private ShowSeatRepository showSeatRepository;
    private UserRepository userRepository;
    private SeatTypeShowRepository seatTypeShowRepository;
    private SeatsRepository seatsRepository;

    private TicketRepository ticketRepository;

    @Autowired
    public TicketServiceImpl(ShowSeatRepository showSeatRepository, UserRepository userRepository, SeatTypeShowRepository seatTypeShowRepository, SeatsRepository seatsRepository, TicketRepository ticketRepository) {
        this.showSeatRepository = showSeatRepository;
        this.userRepository = userRepository;
        this.seatTypeShowRepository = seatTypeShowRepository;
        this.seatsRepository = seatsRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket bookTicket(List<Integer> showSeatIds, int userId) throws SeatsAlreadyBookedException, InvalidUser {

        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()){

            throw new InvalidUser("Invalid login");
        }
        //Get User
        User user = userOptional.get();

        List<ShowSeat> showSeats = checkAndBlockSeats(showSeatIds, user);


        //Get show
        Show show = showSeats.get(0).getShow();

        //Get seatIds
        List<Integer> seatIds = showSeats.stream()
                .map( showSeat -> showSeat.getSeat().getId() )
                .toList();
        List<Seat> seats = seatsRepository.findByIdIn(seatIds);


        //Get price
        // Get pricing information from seat type show and for given tickets, determine the correct price
        List<SeatTypeShow> seatTypeShows = seatTypeShowRepository.findByShow(show.getId());

        Map<SeatType, Double> priceMap = new EnumMap<>(SeatType.class);
        for (SeatTypeShow sts: seatTypeShows){
            priceMap.put(sts.getSeatType(), sts.getPrice());
        }

        double totalAmount = 0.0d;
        for (ShowSeat showSeat : showSeats) {
            totalAmount += priceMap.get(showSeat.getSeat().getSeatType());
        }

        Ticket ticket = new Ticket();
        ticket.setPrice(totalAmount);
        ticket.setUser(user);
        ticket.setShow(show);
        ticket.setSeats(seats);
        ticket.setTimeOfBooking(new Date());
        return ticketRepository.save(ticket);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<ShowSeat> checkAndBlockSeats(List<Integer> showSeatIds, User user) throws SeatsAlreadyBookedException {
        // First lets make sure that the tickets we are trying to book are available
        List<ShowSeat> showSeats = showSeatRepository.findByIdInAndSeatStatus_Available(showSeatIds);

        if(showSeats ==null ||  showSeats.size() != showSeatIds.size()){
            throw new SeatsAlreadyBookedException("The seats you are trying to book is already booked");
        }

        for (ShowSeat showSeat : showSeats) {
            showSeat.setSeatStatus(SeatStatus.BLOCKED);
            showSeat.setUser(user);
            showSeatRepository.save(showSeat);
        }
        return showSeats;
    }
}
