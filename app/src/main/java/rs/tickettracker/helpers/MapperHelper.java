package rs.tickettracker.helpers;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import model.Bet;
import model.League;
import model.Match;
import model.Status;
import model.Ticket;
import model.parcelable.MatchParcelable;

/**
 * Created by gisko on 17-May-16.
 */
public class MapperHelper {

    public static MatchParcelable matchMapper(Match m) {
        MatchParcelable parMatch = new MatchParcelable();
        parMatch.awayScore = m.awayScore;
        parMatch.awayTeam = m.awayTeam;
        parMatch.gameStart = m.gameStart;
        parMatch.homeScore = m.homeScore;
        parMatch.homeTeam = m.homeTeam;
        parMatch.isFinished = m.isFinished;
        parMatch.leagueId = m.league.getId();
        parMatch.betId = m.bet.getId();
        parMatch.matchServisId = m.matchServisId;
        if (m.status != null) {
            parMatch.statusId = m.status.getId();
        } else {
            parMatch.statusId = -1;
        }
        if (m.ticket != null) {
            parMatch.ticketId = m.ticket.getId();
        } else {
            parMatch.ticketId = -1;
        }
        return parMatch;
    }

    public static Match matchMapperReverse(MatchParcelable parMatch, Ticket t) {
        Match m = new Match();
        m.awayScore = parMatch.awayScore;
        m.awayTeam = parMatch.awayTeam;
        m.gameStart = parMatch.gameStart;
        m.homeScore = parMatch.homeScore;
        m.homeTeam = parMatch.homeTeam;
        m.isFinished = parMatch.isFinished;
        m.matchServisId = parMatch.matchServisId;
        m.bet = Bet.load(Bet.class, parMatch.betId);
        m.league = League.load(League.class, parMatch.leagueId);
        if (parMatch.statusId != -1) {
            m.status = Status.load(Status.class, parMatch.statusId);
        }
        if (t != null) {
            m.ticket = t;
        }
        return m;
    }


    public static ArrayList<MatchParcelable> getListOfMappedMatches(List<Match> matches) {
        ArrayList<MatchParcelable> newMatches = new ArrayList<MatchParcelable>();
        for (Match m : matches) {
            newMatches.add(matchMapper(m));
        }
        return newMatches;
    }

    public static List<Match> getListOfMappedMatchesReversed(ArrayList<MatchParcelable> parMatches) {
        ArrayList<Match> matches = new ArrayList<Match>();
        Ticket t = null;
        if (parMatches.size() > 0) {
            long ticketId = parMatches.get(0).ticketId;
            if (ticketId != -1) {
                t = Ticket.load(Ticket.class, ticketId);
            }
        }
        for (MatchParcelable parMatch : parMatches) {
            matches.add(matchMapperReverse(parMatch, t));
        }

        return matches;
    }
}
