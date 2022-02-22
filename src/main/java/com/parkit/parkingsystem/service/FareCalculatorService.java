package com.parkit.parkingsystem.service;

import java.time.Duration;
import java.time.LocalDateTime;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;


public class FareCalculatorService {


	public void calculateFare(Ticket ticket){
		if( (ticket.getOutTime() == null) || (ticket.getOutTime().isBefore(ticket.getInTime())) ){
			throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
		}

		//get time in minutes
		LocalDateTime inHour = ticket.getInTime();
		LocalDateTime outHour = ticket.getOutTime();

		//TODO: Some tests are failing here. Need to check if this logic is correct

		Duration duration = Duration.between(inHour, outHour);
		long durationSec = duration.getSeconds();
		// convert duration on hour
		double durationInHour = (double) durationSec / 3600;
		
		TicketDAO ticketDao = new TicketDAO();
		String vehicleRegNumber = ticket.getVehicleRegNumber();
		double priceForCar = durationInHour * Fare.CAR_RATE_PER_HOUR;
		double priceForBike = durationInHour * Fare.BIKE_RATE_PER_HOUR;

		switch (ticket.getParkingSpot().getParkingType()){
		case CAR: {
			// set free fare if duration is less than 30 minutes
			if (durationInHour < 0.5) {
				ticket.setPrice(durationInHour * Fare.RATE_PER_THIRTY_MINUTES);
			
				// if ticket's number is superior than 1 then the user is recurring
			} else if (ticketDao.getReccurentUser(vehicleRegNumber) > 1) {
				System.out.println("5% discount has been applied");
				
				ticket.setPrice(priceForCar * Fare.DISCOUNT_FIVE_PERCENT);
			} else {
				ticket.setPrice(priceForCar);
			}
			break;
		}
		case BIKE: {
			// set free fare if duration is less than 30 minutes
			if (durationInHour < 0.5) {
				ticket.setPrice(durationInHour * Fare.RATE_PER_THIRTY_MINUTES);
				
			// if ticket's number is superior than 1 then the user is recurring
			} else if (ticketDao.getReccurentUser(vehicleRegNumber) > 1) {
				System.out.println("5% discount has been applied");
				ticket.setPrice(priceForBike * Fare.DISCOUNT_FIVE_PERCENT);
			
			} else {
				ticket.setPrice(priceForBike);
			}
			break;
		}
		default: throw new IllegalArgumentException("Unkown Parking Type");
		}

	}
}