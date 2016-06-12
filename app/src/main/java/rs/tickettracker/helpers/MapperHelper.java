package rs.tickettracker.helpers;

import java.util.ArrayList;
import java.util.List;

import model.Bet;
import model.League;
import model.Match;
import model.Status;
import model.Ticket;
import model.parcelable.MatchParcelable;

/**
 * This helper class is used for mapping objects.
 */
public class MapperHelper {

    /**
     * This method match model.Match to parcelable match object.
     * @param m - Match object than need to converted.
     * @return Parcelable match object.
     */
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

    /**
     * This method match parcelable match to model.Match object.
     * @param parMatch - Parcelable match object that need to be converted.
     * @param t - Ticket that contains all that matches
     * @return converted model.Match object
     */
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

    /**
     * Get list of all parcelable matches from one ticket that need to be converted.
     * @param matches List of matches from ticket
     * @return list of converted parcelable matches.
     */
    public static ArrayList<MatchParcelable> getListOfMappedMatches(List<Match> matches) {
        ArrayList<MatchParcelable> newMatches = new ArrayList<MatchParcelable>();
        for (Match m : matches) {
            newMatches.add(matchMapper(m));
        }
        return newMatches;
    }

    /**
     * Get list of all model.Matches converted from parcelable matches.
     * @param parMatches - list of parcelable matches that need to be converted.
     * @return List of converted model.Matches
     */
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
