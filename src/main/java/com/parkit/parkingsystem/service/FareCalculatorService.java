package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;


public class FareCalculatorService {


	public void calculateFare(Ticket ticket){
		if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
			throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
		}

		//get time in minutes
		double inHour = ticket.getInTime().getTime() / (1000 * 60);
		double outHour = ticket.getOutTime().getTime() / (1000 * 60);



		//TODO: Some tests are failing here. Need to check if this logic is correct
		// convert duration on hour
		double duration = (outHour - inHour) / 60;

		TicketDAO ticketDao = new TicketDAO();
		String vehicleRegNumber = ticket.getVehicleRegNumber();

		// set free fare if duration is less than 30 minutes
		if (duration < 0.5) {
			ticket.setPrice(duration * Fare.RATE_PER_THIRTY_MINUTES);
		} else {
			switch (ticket.getParkingSpot().getParkingType()){
			case CAR: {

				System.out.println("nombre :" + ticketDao.getReccurentUser(vehicleRegNumber));
				// if ticket's number is superior than 1 then the user is recurring
				if (ticketDao.getReccurentUser(vehicleRegNumber) > 1) {

					System.out.println("5% discount has been applied");
					ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR * Fare.DISCOUNT_FIVE_PERCENT);
				}
				ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
				break;

			}
			case BIKE: {
				ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
				break;
			}
			default: throw new IllegalArgumentException("Unkown Parking Type");
			}
		}
	}
}