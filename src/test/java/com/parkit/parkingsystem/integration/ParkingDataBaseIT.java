
package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;

import java.time.Duration;
import java.time.LocalDateTime;


@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        lenient().when(inputReaderUtil.readSelection()).thenReturn(1);
        lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }

    @Test 
    public void testParkingACar(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
        
        Ticket ticket = ticketDAO.getTicket("ABCDEF"); 
        assertThat(ticket).isNotNull();
        assertThat(ticket.getVehicleRegNumber()).isEqualTo("ABCDEF");
        assertThat(ticket.getParkingSpot().isAvailable()).isEqualTo(false);
        
    }

    @Test
    public void testParkingLotExit(){
    	//testParkingACar();
    	//ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    	//parkingService.processIncomingVehicle();
        
        //TODO: check that the fare generated and out time are populated correctly in the database;
            
        LocalDateTime inTime = LocalDateTime.now().minusHours(1);
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(new ParkingSpot(1,ParkingType.CAR, true));
        ticket.setVehicleRegNumber("ABCDEF");
        ticket.setPrice(0);
        ticket.setInTime(inTime);
        //ticket.setOutTime(null);
        ticketDAO.saveTicket(ticket); 
        
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();
        
    	Ticket ticketDb = ticketDAO.getTicket("ABCDEF");
        
        Duration dur = Duration.between(ticketDb.getInTime(),ticketDb.getOutTime());
		long durationSec = dur.getSeconds();
		// convert duration on hour
		double durationInHour = (double) durationSec / 3600;

    	assertThat(ticketDb.getOutTime()).isNotNull();
        //ABCDEF is a regular user we apply the 5% discount
        assertThat(ticketDb.getPrice()).isEqualTo(durationInHour * Fare.CAR_RATE_PER_HOUR * Fare.DISCOUNT_FIVE_PERCENT);
       
    } 

}
