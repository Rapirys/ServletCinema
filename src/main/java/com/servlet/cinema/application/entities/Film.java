package com.servlet.cinema.application.entities;


import java.time.Duration;
import java.util.Objects;


public class Film {
    private Long film_id;
    private String titleRu;
    private String titleEn;
    private Duration duration;
    private boolean boxOffice = true;


    public String getTitleLocale(String locale) {
        return (locale.equals("en")) ? getTitleEn() : getTitleRu();
    }

    public String getFormatDuration() {
        return String.format("%02d", duration.toHours()) + ':' + String.format("%02d", duration.toMinutesPart());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Film films)) return false;
        return film_id.equals(films.film_id) && titleRu.equals(films.titleRu) && titleEn.equals(films.titleEn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(film_id, titleRu, titleEn, duration);
    }

    public Film() {
    }

    public Film(String titleRu, String titleEn, Duration duration) {
        this.titleRu = titleRu;
        this.titleEn = titleEn;
        this.duration = duration;
    }

    public boolean isBoxOffice() {
        return boxOffice;
    }

    public void setBoxOffice(boolean box_office) {
        this.boxOffice = box_office;
    }

    public Long getFilm_id() {
        return film_id;
    }

    public void setFilm_id(Long film_id) {
        this.film_id = film_id;
    }

    public String getTitleRu() {
        return titleRu;
    }

    public void setTitleRu(String title_ru) {
        this.titleRu = title_ru;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String title_en) {
        this.titleEn = title_en;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
