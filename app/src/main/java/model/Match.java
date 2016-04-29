package model;

import java.util.Date;

/**
 * Created by gisko on 29-Apr-16.
 */
public class Match {
    private long id;
    private String homeTeam;
    private String awayTeam;
    private Date gameStart;
    private int homeScore;
    private int awayScore;
    private Status status;
    private long matchServisId;

    public Match(){

    }

    public Match(String homeTeam, String awayTeam, Date gameStart, int homeScore, int awayScore, Status status, long matchServisId) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.gameStart = gameStart;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.status = status;
        this.matchServisId = matchServisId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public Date getGameStart() {
        return gameStart;
    }

    public void setGameStart(Date gameStart) {
        this.gameStart = gameStart;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public long getMatchServisId() {
        return matchServisId;
    }

    public void setMatchServisId(long matchServisId) {
        this.matchServisId = matchServisId;
    }

    @Override
    public String toString() {
        return homeTeam+" "+homeScore+":"+awayScore+" "+awayTeam;
    }
}
