package com.servlet.cinema.application.controller;



import com.servlet.cinema.application.entities.Order;
import com.servlet.cinema.application.entities.Session;
import com.servlet.cinema.application.entities.Ticket;
import com.servlet.cinema.application.entities.User;
import com.servlet.cinema.application.model.repository.OrderRepository;
import com.servlet.cinema.application.model.repository.SessionRepository;
import com.servlet.cinema.application.model.repository.TicketRepository;
import com.servlet.cinema.application.model.service.Hall.Place;
import com.servlet.cinema.application.model.service.OrderManager;
import com.servlet.cinema.application.model.service.Validator;
import com.servlet.cinema.framework.annotation.*;
import com.servlet.cinema.framework.web.Model;
import com.servlet.cinema.framework.security.SecurityManager;
import com.servlet.cinema.framework.web.RedirectAttributes;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller

public class OrderController {


    OrderManager orderManager = OrderManager.getInstance();


    @PreAuthorize("USER")
    @GetMapping(path = "/cinema/place")
    public String place(Model model, HttpServletRequest request
            , @RequestParam(name = "id") Long id) {
        SessionRepository sessionRepository = new SessionRepository();
        Optional<Session> o = sessionRepository.findById(id);
        sessionRepository.close();
        Session session;
        if (o.isPresent())
            session = o.get();
        else return "redirect:/cinema";
        ArrayList<ArrayList<Place>> topology = orderManager.getHall(session);
        model.addAttribute("mySession", session);
        model.addAttribute("topology", topology);
        model.response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
        return "place.jsp";
    }

    @PreAuthorize("USER")
    @PostMapping(path = "/cinema/order")
    public String order(@RequestParam(name = "data") String data,
                        @RequestParam(name = "session_id") Long id,
                        HttpServletRequest request,
                        HttpServletResponse response){
        User user = SecurityManager.getUserFromSession(request);
        Long orderId = null;
        if (user!=null)
                orderId=orderManager.book(List.of(data.split(",")),id,user);
        if (orderId!=null){
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            try {
                response.getWriter().write(orderId.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return "HttpStatus.bad";
            }
            return "HttpStatus.ok";
        } else return "HttpStatus.bad";
    }
    @PreAuthorize("USER")
    @GetMapping(path = "/cinema/order")
    public String payment(@RequestParam(name = "id") Long id,
                          HttpServletRequest request,
                          Model model){
        OrderRepository orderRepository = new OrderRepository();
        Optional<Order> o=orderRepository.findById(id);
        orderRepository.close();
        Order order;
        if (o.isPresent())
            order = o.get();
        else return "redirect:/cinema";
        User user = SecurityManager.getUserFromSession(request);
        if (!order.getUser().getUsername().equals(user.getUsername()))
            return "redirect:/cinema";
        if (order.isActive())
            return "orderOld.jsp";
        TicketRepository ticketRepository= new TicketRepository();
        order.setTickets(ticketRepository.findTicketsByOrder(order));
        ticketRepository.close();

        model.addAttribute("order", order);
        model.response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
        return "order.jsp";
    }

    @PreAuthorize("USER")
    @PostMapping(path = "/cinema/order/pay")
    public String pay(@RequestParam(name = "cvv") String cvv,
                      @RequestParam(name = "cardName") String holder,
                      @RequestParam(name = "cardNumber") String cardNumber,
                      @RequestParam(name = "dateM", required = false) Integer dateM,
                      @RequestParam(name = "dateY", required = false) Integer dateY,
                      @RequestParam(name = "order_id") Long order_id,
                      RedirectAttributes redirectAttributes) {
        if (!Validator.validCard(cardNumber, cvv, dateM, dateY, holder)) {
            ArrayList<String> messages = new ArrayList<>();
            messages.add("Card_data_error");
            redirectAttributes.addFlashAttribute("message", messages);
            redirectAttributes.addAttributes("id", order_id.toString());
            return "redirect:/cinema/order";
        }
        Order order;
        try {
            order = orderManager.submit(order_id);
        } catch (Exception e) {
            return "orderOld.jsp";
        }
        redirectAttributes.addAttributes("order", order.getOrder_id().toString());
        redirectAttributes.addAttributes("user", order.getUser().getId().toString());
        return "redirect:/cinema/download";
    }

    @PreAuthorize("USER")
    @GetMapping(path = "/cinema/download")
    public String download(@RequestParam(name = "order") Long order_id,
                           @RequestParam(name = "user") Integer user_id,
                           Model model) {
        User user = SecurityManager.getUserFromSession(model.request);
        if (!(user_id.equals(user.getId())))
            return "redirect:/cinema";
        model.addAttribute("id", order_id);
        return "download.jsp";
    }

    @PreAuthorize("USER")
    @GetMapping(path = "/cinema/downloadOrder")
    public String getFile(@RequestParam(name = "id") Long id,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        OrderRepository orderRepository = new OrderRepository();
        Optional<Order> o = orderRepository.findById(id);
        orderRepository.close();
        Order order;
        User user = SecurityManager.getUserFromSession(request);
        if (o.isPresent())
            order = o.get();
        else return "HttpStatus.bad";
        if (!order.isActive() || !order.getUser().getUsername().equals(user.getUsername()))
            return "HttpStatus.bad";

        response.setHeader("Content-disposition", "attachment;filename=" + "tickets_" + order.getOrder_id() + ".pdf");
        response.setContentType("application/pdf");
        try {
            orderManager.getPdf(order, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (Exception e) {
            throw new RuntimeException("IOError writing file to output stream",e);
        }
        return "HttpStatus.ok";
    }

    @PreAuthorize("ADMIN")
    @GetMapping(path = "/cinema/ticket")
    public String getFile(@RequestParam(name = "id") Long id,
                          @RequestParam(name = "salt") Long salt, Model model) {
        TicketRepository ticketRepository = new TicketRepository();
        Optional<Ticket> ticket = ticketRepository.findById(id);
        ticketRepository.close();
        if (ticket.isPresent() && ticket.get().getSalt().equals(salt)) {
            model.addAttribute("ticket", ticket.get());
            model.addAttribute("exist", true);
        } else model.addAttribute("exist", false);
        return "exist.jsp";
    }

}
