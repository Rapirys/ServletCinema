package com.servlet.cinema.application.service;


import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.servlet.cinema.application.entities.Film;
import com.servlet.cinema.application.entities.Order;
import com.servlet.cinema.application.entities.Session;
import com.servlet.cinema.application.entities.Ticket;
import com.servlet.cinema.application.model.repository.OrderRepository;
import com.servlet.cinema.application.model.repository.SessionRepository;
import com.servlet.cinema.application.model.repository.TicketRepository;
import com.servlet.cinema.application.model.service.Hall.HallTopology;
import com.servlet.cinema.application.model.service.Hall.Place;
import com.servlet.cinema.application.model.service.OrderManager.OrderManager;
import com.servlet.cinema.application.model.service.OrderManager.OrderNotExist;
import com.servlet.cinema.framework.Util.AppContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.io.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static com.itextpdf.text.pdf.parser.PdfTextExtractor.getTextFromPage;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OrderManagerTest {

    OrderManager orderManager = OrderManager.getInstance();


    @BeforeAll
    static void init() throws FileNotFoundException {
        ServletContextEvent servletContextEvent = mock(ServletContextEvent.class);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getResourceAsStream("Hall_topology.txt"))
                .thenReturn(new FileInputStream("src/main/webapp/Hall_topology.txt"));
        when(servletContextEvent.getServletContext()).thenReturn(servletContext);
        AppContext.servletContextEvent = servletContextEvent;
    }

    @Test
    void generateTickets() {
        List<String> data = Arrays.asList("1_1", "2_2", "3_3", "4_4");
        Order order = new Order();
        order.setOrder_id(1L);
        order.setSession(new Session().setSession_id(2L));
        List<Ticket> tickets = orderManager.generateTickets(data, order);
        int i = 0;
        for (Ticket ticket : tickets) {
            i++;
            assertEquals(i, ticket.getPlace());
            assertEquals(i, ticket.getRow());
            assertEquals(ticket.getOrder_id(), 1L);
            assertEquals(ticket.getSession_id(), 2L);
        }
    }

    @Test
    public void getHall() {
        LinkedList<Ticket> tickets = new LinkedList<>();
        tickets.add(new Ticket());
        try (MockedConstruction<TicketRepository> mocked
                     = mockConstruction(TicketRepository.class, (mock, context) -> when(mock.getHallBySession(1L)).thenReturn(tickets))) {
            Session session = new Session();
            session.setSession_id(1L);

            ArrayList<ArrayList<Place>> test = new ArrayList<>();

            HallTopology hallTopology = mock(HallTopology.class);
            when(hallTopology.getCopyTopology(tickets)).thenReturn(test);
            orderManager.hallTopology = hallTopology;

            assertThat(orderManager.getHall(session)).isEqualTo(test);


            orderManager.hallTopology = HallTopology.getInstance();
        }
    }

    @Test
    public void getPdf() throws IOException, DocumentException {

        Order order = new Order();

        List<Ticket> tickets = new ArrayList<>();

        Session session = new Session();
        Film film = new Film();
        film.setTitleEn("Title1");
        film.setTitleRu("Title2");
        film.setDuration(Duration.ofHours(1));
        session.setDate(LocalDate.of(1984, 1, 1));
        session.setTime(LocalTime.of(12, 0));
        session.setFilm(film);
        order.setSession(session);

        Ticket ticket = new Ticket(1, 1, order.getOrder_id(), session.getSession_id());
        ticket.setTicket_id(1L);

        tickets.add(ticket);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (MockedConstruction<TicketRepository> mocked = mockConstruction(TicketRepository.class,
                (mock, context) -> when(mock.findTicketsByOrder(order)).thenReturn(tickets))) {
            orderManager.getPdf(order, outputStream);
        }
        InputStream inStream = new ByteArrayInputStream(outputStream.toByteArray());
        PdfReader pdfReader = new PdfReader(inStream);

        assertEquals(getTextFromPage(pdfReader, 1),
                """
                        1984-01-01 12:00:00
                        Title1 Title2
                        Row: 1
                        Place: 1""");

    }

    @Test
    public void submitOrderNotExist() {
        OrderRepository orderRepository = mock(OrderRepository.class);

        try (MockedConstruction<OrderRepository> mocked = mockConstruction(OrderRepository.class, (mock, context) -> {
                    when(orderRepository.findById(0L)).thenReturn(Optional.empty());
                    when(orderRepository.findById(1L)).thenReturn(Optional.of(new Order().setActive(true)));
                }
        ); MockedConstruction<SessionRepository> mocked2 = mockConstruction(SessionRepository.class)) {

            assertThrows(OrderNotExist.class, () -> orderManager.submit(0L));


            assertThrows(OrderNotExist.class, () -> orderManager.submit(1L));
        }

    }

}