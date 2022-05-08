package com.servlet.cinema.application.model.service.OrderManager;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfWriter;
import com.servlet.cinema.application.entities.Order;
import com.servlet.cinema.application.entities.Session;
import com.servlet.cinema.application.entities.Ticket;
import com.servlet.cinema.application.entities.User;
import com.servlet.cinema.application.model.repository.OrderRepository;
import com.servlet.cinema.application.model.repository.SessionRepository;
import com.servlet.cinema.application.model.repository.TicketRepository;
import com.servlet.cinema.application.model.service.Hall.HallTopology;
import com.servlet.cinema.application.model.service.Hall.Place;
import org.apache.log4j.Logger;


import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 *
 Handles all actions related to booking, orders, and payment.
 */

public class OrderManager {
    static int timeOfOrderMinute=15;
    private final static Logger logger = Logger.getLogger(OrderManager.class);

    private static final OrderManager instance = new OrderManager();
    public static OrderManager getInstance(){
        return instance;
    }


    public HallTopology hallTopology = HallTopology.getInstance();

    /**
     * Creates a new order, and books seats, the booking will be invalid after 15 minutes.
     * @throws SessionNotExist throws an error if order creation failed or DB error if the seats are already taken.
     * @param data List of places for tickets in the form :"row_place"
     * @param session_id id of session;
     * @param user user for which the order is registered
     * @return id of new order;
     */

    public Long book(List<String> data,Long session_id, User user){
        Long orderId= null;
        SessionRepository sessionRepository = new SessionRepository();
        OrderRepository orderRepository = OrderRepository.bound(sessionRepository);
        TicketRepository ticketRepository = TicketRepository.bound(sessionRepository);
        try{
            sessionRepository.beginTransaction();
            Session session= sessionRepository.findById(session_id).orElseThrow(SessionNotExist::new);
            Order order= new Order();
            order.setUser(user).setSession(session).setActive(false).setTime(LocalDateTime.now());
            orderRepository.deleteBySessionAndActiveFalseAndTimeBefore(session,LocalDateTime.now().minusMinutes(timeOfOrderMinute));
            order = orderRepository.save(order);
            List<Ticket> tickets=generateTickets(data, order);
            ticketRepository.saveAll(tickets);
            logger.debug("User with id: "+user.getId()+ " booked seats for session id: "+session_id+" order id: "+ order.getOrder_id());
            orderId=order.getOrder_id();
            sessionRepository.endTransaction();
        }catch (SessionNotExist e){
            logger.error("User with id: "+user.getId()+ " tried to book seats for session id: "+session_id+" bat session noe Exist.");
        }
        catch (Exception e){
            logger.error("User with id: "+user.getId()+ " tried to book seats for session id: "+session_id+" and failed.", e);
        } finally {
            sessionRepository.close();
        }
        return orderId;
    }


    /**
     * @param data List of places for tickets in the form :"row_place"
     * @param order Order entity to which tickets are added.
     * @return List of ticket which can be saved in DB.
     */
    public List<Ticket> generateTickets(List<String> data, Order order){
        List<Ticket> tickets = new ArrayList();
        for (String s:data){
            String[] t=s.split("_");
            Ticket ticket= new Ticket(Integer.parseInt(t[0]),Integer.parseInt(t[1]), order.getOrder_id(), order.getSession().getSession_id());
            tickets.add(ticket);
        }
        return tickets;
    }

    /**
     * @return hall topology with occupied seats
     */
    public ArrayList<ArrayList<Place>> getHall(Session session) {
        TicketRepository ticketRepository = new TicketRepository();
        List<Ticket> tickets=ticketRepository.getHallBySession(session.getSession_id());
        ticketRepository.close();
        return hallTopology.getCopyTopology(tickets);
    }

    public Order submit(Long order_id) throws Exception {
        OrderRepository orderRepository = new OrderRepository();
        SessionRepository sessionRepository = new SessionRepository();
        TicketRepository ticketRepository=TicketRepository.bound(orderRepository);
        orderRepository.beginTransaction();
        Order order= orderRepository.findById(order_id).orElseThrow(OrderNotExist::new);
        if (!order.isActive())
            order.setActive(true);
        else throw new OrderNotExist();
        order.getSession().incOccupancy(ticketRepository.countTicketByOrder(order));
        orderRepository.updateActive(order_id, order.isActive());
        sessionRepository.updateOccupancy(order.getSession().getSession_id(),order.getSession().getOccupancy());
        orderRepository.endTransaction();
        orderRepository.close();
        logger.debug("Order id: "+order_id+"  has been paid.");
        return order;
    }
    /**
     * Used to get a list of order tickets in PDF.
     * @param order Entity of order for which pdf will be generated
     * @param outputStream Stream with pdf written to it
     */
    public void getPdf(Order order, OutputStream outputStream) throws DocumentException {
        TicketRepository ticketRepository = new TicketRepository();
        List<Ticket> tickets=ticketRepository.findTicketsByOrder(order);
        ticketRepository.close();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        for (Ticket ticket: tickets){
            Font font = FontFactory.getFont(FontFactory.COURIER, 20, BaseColor.BLACK);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Paragraph paragraph= new Paragraph(order.getSession().getLocalDateTime().format(formatter), font);
            document.add(paragraph);
            paragraph = new Paragraph(order.getSession().getFilm().getTitleEn()+" "+order.getSession().getFilm().getTitleRu(), font);
            document.add(paragraph);
            paragraph = new Paragraph("Row: "+ticket.getRow(), font);
            document.add(paragraph);
            paragraph = new Paragraph("Place: "+ticket.getPlace(), font);
            document.add(paragraph);
            BarcodeQRCode qrCode = new BarcodeQRCode("http://localhost:8084/cinema/ticket?id="+ticket.getTicket_id()+"&salt="+ticket.getSalt(), 400, 400, null);
            Image codeQrImage = qrCode.getImage();
            codeQrImage.scaleAbsolute(400, 400);
            document.add(codeQrImage);
            document.newPage();
        }
        document.close();
    }
}

