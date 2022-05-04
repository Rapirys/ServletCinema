package com.servlet.cinema.application.controller.Admin;



import com.servlet.cinema.application.entities.Film;
import com.servlet.cinema.application.model.repository.FilmRepository;
import com.servlet.cinema.application.model.service.SortManager;
import com.servlet.cinema.framework.annotation.*;
import com.servlet.cinema.framework.web.Model;
import org.apache.log4j.Logger;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.time.Duration;
import java.util.List;

@Controller
public class FilmController {
    private final static Logger logger = Logger.getLogger(FilmController.class);
    SortManager sortManager = SortManager.getInstance();
    String path="C:/ServletCinemaFolder/src/main/webapp/static/posters/";

    @PreAuthorize("ADMIN")
    @GetMapping(path = "/cinema/admin/film")
    public String film(@RequestParam(name = "search", defaultValue = "") String search,
                       @RequestParam(name ="sort", defaultValue = "title_en") String sort,
                       @RequestParam(name = "status", defaultValue = "at_box_office") String status,
                       @RequestParam(name = "direction", defaultValue = "false") Boolean direction,
                       @RequestParam (name="page", defaultValue = "1") Integer page,
                       @RequestParam (name="quantity", defaultValue = "10") Integer quantity, Model model) {
        List<Film> films = sortManager.findFilms(search, sort, status, page,quantity, direction);
        model.addAttribute("maxPage", films.size()/quantity+1);
        model.addAttribute("page",page);
        model.addAttribute("quantity",quantity);
        model.addAttribute("search",search);
        model.addAttribute("films", films);
        return "film_ad.jsp";
    }

    @PreAuthorize("ADMIN")
    @PostMapping(path = "/cinema/admin/film/add")
    public String film_add(@RequestParam(name = "title_en") String titleEn,
                           @RequestParam(name = "title_ru") String titleRu,
                           @RequestParam(name = "duration") Duration duration,
                           HttpServletRequest request)  {
        FilmRepository filmRepository = new FilmRepository();
        Film film = new Film(titleRu, titleEn, duration);
        film=filmRepository.save(film);
        filmRepository.close();
        try {
            String fileName =film.getFilm_id().toString() + ".jpeg";
            for (Part part : request.getParts()) {
                part.write(path + fileName);
            }
            logger.debug("Add new movie film_id:"+film.getFilm_id());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Problem with loading poster for film_id:"+film.getFilm_id(), e);
        }
        return "redirect:/cinema/admin/film";
    }

    @PreAuthorize("ADMIN")
    @GetMapping(path = "/cinema/admin/film/delete")
    public String delete(@RequestParam(name = "id") Long id) {
        FilmRepository filmRepository = new FilmRepository();
        filmRepository.deleteById(id);
        filmRepository.close();
        if (new File(path+id+".jpeg").delete())
            logger.debug("Delete movie film_id:"+id);
        else logger.warn("Problem with delete poster for film_id:+newFilm.getFilm_id()");
        return "HttpStatus.ok";
    }

    @PreAuthorize("ADMIN")
    @GetMapping(path = "/cinema/admin/film/swap_status")
    public String swap_status(@RequestParam(name = "id") Long id,
                              @RequestParam(name = "status") Boolean current) {
        FilmRepository filmRepository = new FilmRepository();
        filmRepository.update_status(id, !current);
        filmRepository.close();
        logger.debug("Delete film id:"+id);
        return "HttpStatus.ok";
    }

}
