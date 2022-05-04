package com.servlet.cinema.application.model.service;

import com.servlet.cinema.application.entities.Film;
import com.servlet.cinema.application.entities.Session;
import com.servlet.cinema.application.model.repository.FilmRepository;
import com.servlet.cinema.application.model.repository.SessionRepository;
import com.servlet.cinema.application.model.service.Hall.HallTopology;
import com.servlet.cinema.framework.data.Pageable;
import com.servlet.cinema.framework.data.Sort;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * Service for performing sorting and pagination when interacting with the database
 */
public class SortManager {

    private static final SortManager instance = new SortManager();
    public static SortManager getInstance(){
        return instance;
    }


    HallTopology hallTopology=HallTopology.getInstance();



    /**
     * @param search Substring to search.
     * @param sort DB column name.
     * @param status  "Any" or "at_box_office"
     * @param desc Ascending or descending true=descending.
     * @return Sorted list of movies matching the parameters.
     */
    public List<Film> findFilms(String search, String sort, String status, int page, int quantity, boolean desc) {
        Sort sortPattern;
        sortPattern = getDirection(desc, Sort.by(sort));
        Pageable pageable = Pageable.of(page-1,quantity,sortPattern);

        FilmRepository filmRepository = new FilmRepository();
        List<Film> result;
        if (status.equals("Any"))
            result = filmRepository.findAllByTitleEnContainsOrTitleRuContains(search,search,pageable);
        else if (status.equals("at_box_office"))
            result = filmRepository.findAllByTitleEnContainsOrTitleRuContainsAndBoxOffice(search,search, true, pageable);
        else result =  filmRepository.findAllByTitleEnContainsOrTitleRuContainsAndBoxOffice(search,search, false, pageable);
        filmRepository.close();
        return result;
    }

    /**
     * @param search Substring to search.
     * @param sort DB column name.
     * @param status  Any or Movie_is_passed or Movie_will_be_shown.
     * @param direction Ascending or descending true=descending.
     * @return Sorted list of Session matching the parameters.
     */
    public List<Session> findSession(String search, String sort, String status, int page, int quantity, boolean direction) {
        Sort sortPattern;
        if (sort.equals("time")) {
            sortPattern = Sort.by("date");
            sortPattern = getDirection(direction, sortPattern);
            sortPattern = sortPattern.and(Sort.by("time"));
            sortPattern = getDirection(direction, sortPattern);
        }
        else {
            sortPattern = Sort.by(sort);
            sortPattern = getDirection(direction, sortPattern);
        }
        Pageable pageable= Pageable.of(page-1,quantity,sortPattern);

        SessionRepository sessionRepository = new SessionRepository();
        List<Session> result = switch (status) {
            case "Any" -> sessionRepository.findAllByFilmTitleEnContains(search, pageable);
            case "movie_is_passed" -> sessionRepository.findAllByFilmTitleEnContainsAndPast(LocalTime.now(), search, pageable);
            default -> sessionRepository.findAllByFilmTitleEnContainsAndWillBeShown(LocalTime.now(), search, pageable);
        };
        sessionRepository.close();
        return result;
    }

    private Sort getDirection(boolean direction, Sort sortPattern) {
        if (!direction)
            sortPattern = sortPattern.descending();
        else sortPattern = sortPattern.ascending();
        return sortPattern;
    }

    public List<Film> findFilmsAtBoxOffice() {
        FilmRepository filmRepository = new FilmRepository();
        List<Film> result = filmRepository.findByBoxOfficeTrueOrderByTitleEn();
        filmRepository.close();
        return result;
    }


    /**
     Use to add sessions, guarantees uniqueness.
     * @param prototype Session entity with film, price, and time fields initialized.
     * @return Empty if no collisions are found, or containing a list of collisions if any
     */
    public Optional<List<Session>> findSessionCollisionOrSave(LocalDate date1, LocalDate date2, Session prototype) {
        LocalTime time1=prototype.getTime();
        LocalTime time2=time1.plus(prototype.getFilm().getDuration());
        List<Session> collision = new ArrayList<>();
        SessionRepository sessionRepository = new SessionRepository();
        sessionRepository.beginTransaction();
        for (Session session: sessionRepository.findSessionCollision(date1, date2)) {
            if ((time1.minusSeconds(30).isBefore(session.getTime()) && time2.plusSeconds(30).isAfter(session.getTime())) ||
                (time1.minusSeconds(30).isBefore(session.getEndTime()) && time2.plusSeconds(30).isAfter(session.getEndTime())))
                collision.add(session);
        }
        if (collision.size()!=0)
            return Optional.of(collision);
        while (date2.compareTo(date1)>=0) {
            prototype.setDate(date1);
            sessionRepository.save(new Session(prototype));
            date1=date1.plusDays(1);
        }
        sessionRepository.endTransaction();
        sessionRepository.close();
        return Optional.empty();
    }


    public LinkedList<Film> findSimpleFilms(String search, String sort_film) {
        Sort sort=Sort.by(sort_film).descending();
        FilmRepository filmRepository = new FilmRepository();
        LinkedList<Film> result = filmRepository.findAllByTitleEnContainsOrTitleRuContains(search,search, sort);
        filmRepository.close();
        return result;
    }

    public List<Session> findSimpleSession(Film film, String sort_session, LocalDate date1, boolean availability) {
        Sort sort=Sort.by(sort_session);
        int occupancy=(availability)?hallTopology.size():Integer.MAX_VALUE;
        SessionRepository sessionRepository = new SessionRepository();
        List<Session> result =sessionRepository.findAllBetween(film,date1,LocalTime.now(),occupancy,sort);
        sessionRepository.close();
        return result;
    }

    /**
     Used to get a list of movies on the main page.
     Associates a movie title with a table of sessions.
     * @param sort_session  The name of the column in the database on which the sorting takes place.
     * @param sortFilm movie sorting option, time or capacity
     * @param availability   Are there any empty seats in the room? If true then full halls will be discarded.
     * @return associate movies with a table of screenings by date
     */
    public HashMap<Film, List<List<Session>>> tableSessionByFilm(List<Film> films,String sort_session, String sortFilm, LocalDate date1, LocalDate date2, boolean availability) {
        HashMap<Film, List<List<Session>>> resalt=new HashMap<>();
        for(Iterator<Film> iterator = films.iterator(); iterator.hasNext();){
            Film film = iterator.next();
            List<List<Session>> sessionsByFilm= new ArrayList<>();
            LocalDate dateT=date1;
            while (date2.compareTo(dateT)>=0) {
                List<Session> sessionsAtDey=findSimpleSession(film,sort_session, dateT, availability);
                if (sessionsAtDey.size()!=0) {
                    if (!sortFilm.equals("time"))
                        sessionsAtDey.sort(Comparator.comparingLong(Session::getOccupancy));
                    sessionsByFilm.add(sessionsAtDey);
                }
                dateT=dateT.plusDays(1);
            }

            if (sessionsByFilm.size()==0)
                iterator.remove();
            else resalt.put(film, sessionsByFilm);
        }
        return resalt;
    }
}
