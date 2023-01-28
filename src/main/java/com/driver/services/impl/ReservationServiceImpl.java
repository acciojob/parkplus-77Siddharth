package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {

        User foundUser = userRepository3.findById(userId).get();
        if (foundUser == null) throw new Exception("Cannot make reservation");
        ParkingLot foundParking = parkingLotRepository3.findById(parkingLotId).get();
        if (foundParking == null) throw new Exception("Cannot make reservation");

        Spot foundSpot = null;
        int minPrice = Integer.MAX_VALUE;
        for (Spot sp:foundParking.getSpotList()){
            if (!sp.getOccupied()){
                int price = sp.getPricePerHour();
                if (price < minPrice ){
                    minPrice = price;
                    foundSpot = sp;
                }
            }
        }
        if (foundSpot == null) throw new Exception("Cannot make reservation");

        //Reserve a spot in the given parkingLot such that the total price is minimum.
        //Note that the price per hour for each spot is different
        //Note that the vehicle can only be parked in a spot having a type equal to or larger than given vehicle
        //If parkingLot is not found, user is not found, or no spot is available, throw "Cannot make reservation" exception.


        Reservation newReserveSpot = new Reservation(timeInHours, foundUser, foundSpot);
        foundSpot.setOccupied(true);
        List<Reservation> reservationList = foundUser.getReservationList();
        reservationList.add(newReserveSpot);
        foundUser.setReservationList(reservationList);
        userRepository3.save(foundUser);
        spotRepository3.save(foundSpot);
        reservationRepository3.save(newReserveSpot);

        return newReserveSpot;

    }
}
