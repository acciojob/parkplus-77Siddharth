package com.driver.services.impl;

import ch.qos.logback.core.joran.conditional.ElseAction;
import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot newParkingLot = new ParkingLot(name,address);
        parkingLotRepository1.save(newParkingLot);
        return newParkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        SpotType spotType = null;
        ParkingLot foundParkingLot = parkingLotRepository1.findById(parkingLotId).get();

        if (numberOfWheels == 2)
            spotType = SpotType.TWO_WHEELER;
        else if (numberOfWheels == 4)
            spotType = SpotType.FOUR_WHEELER;
        else
            spotType = SpotType.OTHERS;
        Spot newSpot = new Spot(spotType,pricePerHour,foundParkingLot);
        newSpot.setOccupied(false);
        List<Spot> spotList = foundParkingLot.getSpotList();
        spotList.add(newSpot);
        foundParkingLot.setSpotList(spotList);
        parkingLotRepository1.save(foundParkingLot);
        return newSpot;
    }

    @Override
    public void deleteSpot(int spotId) {
        Spot foundspot = spotRepository1.findById(spotId).get();
        ParkingLot foundParkingLot = parkingLotRepository1.findById(foundspot.getParkingLot().getId()).get();
        foundParkingLot.getSpotList().remove(foundspot);
        spotRepository1.deleteById(spotId);
        parkingLotRepository1.save(foundParkingLot);

    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        Spot currentSpot = spotRepository1.getOne(spotId);
        ParkingLot currentParking = parkingLotRepository1.findById(parkingLotId).get();
        currentSpot.setParkingLot(currentParking);
        currentSpot.setPricePerHour(pricePerHour);
        spotRepository1.save(currentSpot);
        return currentSpot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        for (Spot s:parkingLot.getSpotList()){
            spotRepository1.deleteById(s.getId());
        }
        parkingLotRepository1.deleteById(parkingLotId);
    }
}
