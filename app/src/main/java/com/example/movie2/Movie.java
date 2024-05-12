package com.example.movie2;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    String movieCd;
    String movieNm;
    String movieNmEn;
    String prdtYear;
    String openDt;
    String typeNm;
    String prdtStatNm;
    String nationAlt;
    String genreAlt;
    String repNationNm;
    String repGenreNm;
    ArrayList<Director> directors = new ArrayList<>(); // 감독 리스트
    ArrayList<Company> companys = new ArrayList<>(); // 제작사 리스트

    public Movie(String movieCd, String movieNm, String openDt, String repGenreNm, ArrayList<Director> directors, String nationAlt, String genreAlt){
        this.movieCd = movieCd;
        this.movieNm = movieNm;
        this.openDt = openDt;
        this.repGenreNm = repGenreNm;
        this.directors = directors;
        this.nationAlt = nationAlt;
        this.genreAlt = genreAlt;
    }
}
