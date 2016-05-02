package model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by gisko on 29-Apr-16.
 */

@Table(name = "Match")
public class Match extends Model {

    @Column(name = "homeTeam", notNull = true)
    public String homeTeam;

    @Column(name = "awayTeam", notNull = true)
    public String awayTeam;

    @Column(name = "gameStart", notNull = true)
    public Date gameStart;

    @Column(name = "homeScore")
    public int homeScore;

    @Column(name = "awayScore")
    public int awayScore;

    @Column(name = "matchServisId", notNull = true)
    public long matchServisId;

    @Column(name = "bet", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public Bet bet;

    @Column(name = "league", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public League league;

    @Column(name = "status", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public Status status;

    @Column(name = "ticket", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public Ticket ticket;

    public Match() {
        super();
    }

    public Match(String homeTeam, String awayTeam, Date gameStart, int homeScore, int awayScore, Status status, long matchServisId, League league, Ticket ticket, Bet bet) {
        super();
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.gameStart = gameStart;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.status = status;
        this.matchServisId = matchServisId;
        this.league = league;
        this.ticket = ticket;
        this.bet = bet;
    }

    @Override
    public String toString() {
        return homeTeam + " " + homeScore + ":" + awayScore + " " + awayTeam;
    }
}
