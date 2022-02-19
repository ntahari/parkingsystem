package com.parkit.parkingsystem.TicketDAO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
class TicketDAOTest {
	
    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static DataBasePrepareService dataBasePrepareService;

	private static Ticket ticket;
	@Mock
	private static TicketDAO ticketDAO;
	@Mock
    private static InputReaderUtil inputReaderUtil;
	
	@BeforeAll
	static void setUp() throws Exception {
		dataBasePrepareService = new DataBasePrepareService();
	}

	@AfterAll
	static void tearDown() throws Exception {
		
	}

	@BeforeEach
	void setUpPerTest() throws Exception {
		ticket = new Ticket();
		lenient().when(inputReaderUtil.readSelection()).thenReturn(1);
        lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		dataBasePrepareService.clearDataBaseEntries();
		
	}
	
	

//	@Test
//	void saveTicketTest() {
//		Date inTime = new Date();
//        inTime.setTime( System.currentTimeMillis() - (  60 * 60 * 1000) );
//        Date outTime = new Date();
//        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);
//
//        ticket.setVehicleRegNumber("ABCDEF");
//        ticket.setInTime(inTime);
//        ticket.setOutTime(outTime);
//        ticket.setPrice(5);
//        ticket.setParkingSpot(parkingSpot);
//        
//        
//        when(ticketDAO.saveTicket(ticket)).thenReturn(true);
//        verify(ticketDAO, times(1)).saveTicket(ticket);
//
//        assertThat(ticket).isNotNull();
//        assertThat(ticket.getPrice()).isEqualTo(5);
//	}

//	@Test
//	void testGetTicket() {
//	}

}
