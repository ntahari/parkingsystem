package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class FareCalculatorServiceTest {


	private static FareCalculatorService fareCalculatorService;
	private Ticket ticket;



	@Mock
	private static  TicketDAO ticketDAO ;

	@BeforeAll
	private static void setUp() {
		fareCalculatorService = new FareCalculatorService();
	}

	@BeforeEach
	private void setUpPerTest() {
		ticket = new Ticket();

	}

	@Test
	@DisplayName("calculate the default fare for a car")
	public void calculateFareCar(){
		LocalDateTime inTime = LocalDateTime.now();
		// add one hour to outTime
		LocalDateTime outTime = LocalDateTime.now().plusHours(1);
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals(ticket.getPrice(), Fare.CAR_RATE_PER_HOUR);
	}

	@Test
	@DisplayName("calculate the default fare for a bike")
	public void calculateFareBike(){
		LocalDateTime inTime = LocalDateTime.now();
		// add one hour to outTime
		LocalDateTime outTime = LocalDateTime.now().plusHours(1); 
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals(ticket.getPrice(), Fare.BIKE_RATE_PER_HOUR);
	}

	//    @Test
	//    @DisplayName("return NullPointerException if type is null")
	//    public void calculateFareNullType(){
	//    	LocalDateTime inTime = LocalDateTime.now();
	//    	// add one hour to outTime
	//        LocalDateTime outTime = LocalDateTime.now().plusHours(1); 
	//        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);
	//
	//        ticket.setInTime(inTime);
	//        ticket.setOutTime(outTime);
	//        ticket.setParkingSpot(parkingSpot);
	//        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
	//    }

	@Test
	@DisplayName("return IllegalArgumentException if InTime is superior than OutTime for a car")
	public void calculateFareCarWithFutureInTime(){
		LocalDateTime inTime = LocalDateTime.now();
		// add one hour to outTime
		LocalDateTime outTime = LocalDateTime.now().minusHours(1); 
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	@DisplayName("return IllegalArgumentException if InTime is superior than OutTime for a bike")
	public void calculateFareBikeWithFutureInTime(){
		LocalDateTime inTime = LocalDateTime.now();
		// add one hour to outTime
		LocalDateTime outTime = LocalDateTime.now().minusHours(1); 
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
	}


	@Test
	@DisplayName("calculate the fare's bike for less than a hour ")
	public void calculateFareBikeWithLessThanOneHourParkingTime(){
		LocalDateTime inTime = LocalDateTime.now();
		// add 45 minutes to outTime
		LocalDateTime outTime = LocalDateTime.now().plusMinutes(45); 
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);

		assertEquals((0.75 * Fare.BIKE_RATE_PER_HOUR), ticket.getPrice() );
	}

	@Test
	@DisplayName("calculate the fare's car for less than a hour ")
	public void calculateFareCarWithLessThanOneHourParkingTime(){
		LocalDateTime inTime = LocalDateTime.now();
		// add 45 minutes to outTime
		LocalDateTime outTime = LocalDateTime.now().plusMinutes(45); 
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals( (0.75 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
	}

	@Test
	@DisplayName("calculate the fare's car for more than a a day ")
	public void calculateFareCarWithMoreThanADayParkingTime(){
		LocalDateTime inTime = LocalDateTime.now();
		// add 25 hours to outTime
		LocalDateTime outTime = LocalDateTime.now().plusHours(25); 
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals( (25 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
	}
	@Test
	@DisplayName("calculate the fare's bike for less than a day ")
	public void calculateFareBikeWithMoreThanADayParkingTime(){
		LocalDateTime inTime = LocalDateTime.now();
		// add 25 hours to outTime
		LocalDateTime outTime = LocalDateTime.now().plusHours(25); 
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals( (25 * Fare.BIKE_RATE_PER_HOUR) , ticket.getPrice());
	}

	@Test
	@DisplayName("calculate the fare's car for less than 30 minutes ")
	public void calculateFareCarWithLessThanThirtyMinutes() {
		LocalDateTime inTime = LocalDateTime.now();
		// add 15 minutes to outTime
		LocalDateTime outTime = LocalDateTime.now().plusMinutes(15); 
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals( (0.25 * Fare.RATE_PER_THIRTY_MINUTES) , ticket.getPrice());
	}

	@Test
	@DisplayName("calculate the fare's bike for less than 30 minutes ")
	public void calculateFareBikeWithLessThanThirtyMinutes() {
		LocalDateTime inTime = LocalDateTime.now();
		// add 15 minutes to outTime
		LocalDateTime outTime = LocalDateTime.now().plusMinutes(15); 
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		fareCalculatorService.calculateFare(ticket);
		assertEquals( (0.25 * Fare.RATE_PER_THIRTY_MINUTES) , ticket.getPrice());
	}

	@Test
	@DisplayName("return IllegalArgumentException if type is unkown")
	public void calculateFareUnkownType(){
		LocalDateTime inTime = LocalDateTime.now();
		// add one hour to outTime
		LocalDateTime outTime = LocalDateTime.now().plusHours(1); 
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.UNKOWN,false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
	}


	@Test
	@DisplayName("calculate the fare's bike for a regular user")
	public void calculateFareForRecurrentBike() {
		LocalDateTime inTime = LocalDateTime.now();
		// add one hour to outTime
		LocalDateTime outTime = LocalDateTime.now().plusHours(1); 
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

		ticket.setVehicleRegNumber("ABCDEF");
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		fareCalculatorService.calculateFare(ticket);
		assertEquals( (1 * Fare.BIKE_RATE_PER_HOUR * Fare.DISCOUNT_FIVE_PERCENT) , ticket.getPrice());

	}
	@Test
	@DisplayName("calculate the fare's car for a regular user")
	public void calculateFareForRecurrentCar() {
		LocalDateTime inTime = LocalDateTime.now();
		// add one hour to outTime
		LocalDateTime outTime = LocalDateTime.now().plusHours(1); 
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

		ticket.setVehicleRegNumber("ABCDEF");
		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);

		fareCalculatorService.calculateFare(ticket);
		assertThat(ticket.getPrice()).isEqualTo(Fare.CAR_RATE_PER_HOUR * Fare.DISCOUNT_FIVE_PERCENT);
	}

}
