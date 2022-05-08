package com.servlet.cinema.application.controller.Admin;

import com.servlet.cinema.application.entities.Film;
import com.servlet.cinema.application.entities.Session;
import com.servlet.cinema.application.model.repository.FilmRepository;
import com.servlet.cinema.application.model.repository.SessionRepository;
import com.servlet.cinema.application.model.service.Hall.HallTopology;
import com.servlet.cinema.application.model.service.SortManager;
import com.servlet.cinema.framework.annotation.*;
import com.servlet.cinema.framework.web.Model;
import com.servlet.cinema.framework.web.RedirectAttributes;
import org.apache.log4j.Logger;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Controller
public class SessionController {
    private final static Logger logger = Logger.getLogger(SessionController.class);
    SortManager sortManager =  SortManager.getInstance();
    HallTopology hallTopology = HallTopology.getInstance();

    @PreAuthorize("ADMIN")
    @GetMapping(path = "/cinema/admin/session")
    public String session(@RequestParam(name = "search", defaultValue = "") String search,
                          @RequestParam(name ="sort", defaultValue = "time") String sort,
                          @RequestParam(name = "status", defaultValue = "Any") String status,
                          @RequestParam(name = "direction", defaultValue = "false") Boolean direction,
                          @RequestParam(name="page", defaultValue = "1") Integer page,
                          @RequestParam(name="quantity", defaultValue = "10") Integer quantity,
                          Model model) {
        FilmRepository filmRepository = new FilmRepository();
        List<Film> films = filmRepository.findByBoxOfficeTrueOrderByTitleEn();
        filmRepository.close();
        model.addAttribute("films", films);
        List<Session> sessions =sortManager.findSession(search, sort, status, page,quantity, direction);
        if (!model.containsAttribute("sessions")) {
            model.addAttribute("maxPage", 1);
            model.addAttribute("sessions", sessions);
        }
        model.addAttribute("maxPage", sessions.size()/quantity+1);
        model.addAttribute("page",page);
        model.addAttribute("quantity",quantity);
        model.addAttribute("search",search);
        model.addAttribute("hallCapacity", hallTopology.size());
        return "session.jsp";
    }

    @PreAuthorize("ADMIN")
    @PostMapping(path = "/cinema/admin/session/add")
    public String film_add(@RequestParam(name="date1") LocalDate date1,
                           @RequestParam(name="date2") LocalDate date2,
                           @RequestParam(name = "price") Integer price,
                           @RequestParam(name = "film_id") Long film_id,
                           @RequestParam(name = "time") LocalTime time,
                           RedirectAttributes redirectAttributes) {
        FilmRepository filmRepository = new FilmRepository();
        Optional<Film> filmO=filmRepository.findById(film_id);
        filmRepository.close();
         if (filmO.isEmpty()){
             redirectAttributes.addFlashAttribute("error","No_movie_found");
             return "redirect:/cinema/admin/session";
         }
         Film film=filmO.get();
         Session session = new Session();
         session.setFilm(film).setTime(time).setPrice(price);
         Optional<List<Session>> sessionCollisionO =sortManager.findSessionCollisionOrSave(date1, date2,session);
         if (sessionCollisionO.isPresent()){
             List<Session> sessionCollision=sessionCollisionO.get();
             redirectAttributes.addFlashAttribute("error","There_is_a_session_at_this_time");
             redirectAttributes.addFlashAttribute("sessions",sessionCollision);
         }

        return "redirect:/cinema/admin/session";
    }

    @PreAuthorize("ADMIN")
    @GetMapping(path = "/cinema/admin/session/delete")
    public String delete(@RequestParam(name = "id") Long id) {
        SessionRepository sessionRepository = new SessionRepository();
        sessionRepository.deleteById(id);
        sessionRepository.close();
        logger.debug("Delete session id:"+id);
        return "HttpStatus.ok";
    }


}
