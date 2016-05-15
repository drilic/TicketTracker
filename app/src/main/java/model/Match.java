package model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Date;
import java.util.List;

/**
 * Created by gisko on 29-Apr-16.
 */

@Table(name = "Match", id = "_id")
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

    @Column(name = "isFinished")
    public boolean isFinished;

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

    public Match(String homeTeam, String awayTeam, Date gameStart, int homeScore, int awayScore, Status status, long matchServisId, League league, Ticket ticket, Bet bet, Boolean isFinished) {
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
        this.isFinished = isFinished;
    }

    public static List<Match> getAllMatchesFromTicket(Ticket t) {
        return new Select()
                .from(Match.class)
                .where("ticket= ?", t.getId())
                .execute();
    }

    @Override
    public String toString() {
        return fixTeamName(homeTeam) + "-" + fixTeamName(awayTeam);
    }

    public static String fixTeamName(String teamName) {
        if (teamName.length() > 11) {
            String trimmedTeamName = teamName.substring(0, 11);
            while (trimmedTeamName.charAt(trimmedTeamName.length() - 1) == ' ') {
                trimmedTeamName = trimmedTeamName.substring(0, trimmedTeamName.length() - 1);
            }
            return trimmedTeamName + "...";
        }
        return teamName;
    }
}
