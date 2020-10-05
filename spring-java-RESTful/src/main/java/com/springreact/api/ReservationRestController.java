package com.springreact.api;

import com.springreact.model.Flight;
import com.springreact.model.Passenger;
import com.springreact.model.Reservation;
import com.springreact.repo.FlightRepository;
import com.springreact.repo.PassengerRepository;
import com.springreact.repo.ReservationRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.springreact.services.CreateReservationRequest;
import com.springreact.services.UpdateReservationRequest;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
public class ReservationRestController {

    FlightRepository flightRepository;
    PassengerRepository passengerRepository;
    ReservationRepository reservationRepository;

    ReservationRestController(FlightRepository flightRepository, PassengerRepository passengerRepository, ReservationRepository reservationRepository){
        this.flightRepository = flightRepository;
        this.passengerRepository = passengerRepository;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/flights")
    public List<Flight> findFlights(@RequestParam("from") String from, @RequestParam("to") String to, @RequestParam("departureDate") @DateTimeFormat(pattern = "MM-dd-yyyy") Date departureDate) {
        return flightRepository.findFlights(from, to, departureDate);
    }

    @RequestMapping("/flights/{id}")
    public Flight findFlight(@PathVariable("id") int id) {
        return flightRepository.findById(id).orElse(null);
    }

    @PostMapping("/reservation")
    @Transactional
    public Reservation saveReservation(@RequestBody CreateReservationRequest request){
        Flight flight = flightRepository.findById(request.getFlightId()).get();
        Passenger passenger = new Passenger();
        passenger.setFirstName(request.getPassengerFirstName());
        passenger.setLastName(request.getPassengerLastName());
        passenger.setMiddleName(request.getPassengerMiddleName());
        passenger.setEmail(request.getPassengerEmail());
        passenger.setPhone(request.getPassengerPhone());

        //Passenger savedPassenger = passengerRepository.save(passenger);

        Reservation reservation = new Reservation();
        reservation.setFlight(flight);
        reservation.setPassenger(passenger);
        reservation.setCheckedIn(false);

        return reservationRepository.save(reservation);
    }

    @RequestMapping(value = "/reservation/{id}")
    public Reservation findReservation(@PathVariable("id") int id){
        return reservationRepository.findById(id).orElse(null);
    }

    @PutMapping("/reservation")
    public Reservation updateReservation(@RequestBody UpdateReservationRequest request){
        Reservation reservation = reservationRepository.findById(request.getId()).orElse(null);
        reservation.setNumberOfBags(request.getNumberOfBags());
        reservation.setCheckedIn(request.isCheckIn());
        System.out.println(reservation.getNumberOfBags());
        return reservationRepository.save(reservation);
    }

}
