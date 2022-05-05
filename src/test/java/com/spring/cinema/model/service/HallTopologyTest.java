package com.spring.cinema.model.service;


import com.servlet.cinema.application.entities.Ticket;
import com.servlet.cinema.application.model.service.Hall.HallTopology;

import com.servlet.cinema.application.model.service.Hall.Place;
import com.servlet.cinema.framework.Util.AppContext;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HallTopologyTest {

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
    void getCopyTopology() {
        ArrayList<ArrayList<Place>>  topology= HallTopology.getInstance().getCopyTopology();
        for (int i=0; i<topology.size(); i++)
            for (int j=0; j<topology.get(i).size(); j++)
                assertThat(topology.get(i).get(j)).isEqualTo(HallTopology.getInstance().topology.get(i).get(j));
    }

    @Test
    void GetCopyTopologyByTickets() {
        List<Ticket> tickets =new ArrayList<>();

        for (int i=0; i<HallTopology.getInstance().topology.size(); i+=2){
            for (int j=0; j<HallTopology.getInstance().topology.get(i).size(); j+=2) {
                if (HallTopology.getInstance().topology.get(i).get(j).type.equals('#')) {
                    Ticket ticket = new Ticket();
                    Place p=HallTopology.getInstance().topology.get(i).get(j);
                    ticket.setRow(p.row);
                    ticket.setPlace(p.place);
                    tickets.add(ticket);
                }
            }
        }
        ArrayList<ArrayList<Place>>  topology= HallTopology.getInstance().getCopyTopology(tickets);
        for (int i=0; i<HallTopology.getInstance().topology.size(); i++){
            for (int j=0; j<HallTopology.getInstance().topology.get(i).size(); j++) {
                if (i%2==0 && j%2==0 && HallTopology.getInstance().topology.get(i).get(j).type.equals('#')) {
                    assertThat(topology.get(i).get(j).type).isEqualTo('X');
                }
                else  assertThat(topology.get(i).get(j)).isEqualTo(HallTopology.getInstance().topology.get(i).get(j));
            }
        }
    }
}