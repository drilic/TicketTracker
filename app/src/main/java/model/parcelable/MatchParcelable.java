package model.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Parcelable class for saving matches on rotate screen.
 */
public class MatchParcelable implements Parcelable {

    public String homeTeam;

    public String awayTeam;

    public Date gameStart;

    public int homeScore;

    public int awayScore;

    public boolean isFinished;

    public long matchServisId;

    public long betId;

    public long leagueId;

    public long statusId;

    public long ticketId;

    public MatchParcelable() {
        super();
    }

    public MatchParcelable(String homeTeam, String awayTeam, Date gameStart, int homeScore, int awayScore, long status, long matchServisId, long league, long ticket, long bet, Boolean isFinished) {
        super();
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.gameStart = gameStart;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.statusId = status;
        this.matchServisId = matchServisId;
        this.leagueId = league;
        this.ticketId = ticket;
        this.betId = bet;
        this.isFinished = isFinished;
    }

    protected MatchParcelable(Parcel in) {
        homeTeam = in.readString();
        awayTeam = in.readString();
        long tmpGameStart = in.readLong();
        gameStart = tmpGameStart != -1 ? new Date(tmpGameStart) : null;
        homeScore = in.readInt();
        awayScore = in.readInt();
        isFinished = in.readByte() != 0x00;
        matchServisId = in.readLong();
        betId = in.readLong();
        leagueId = in.readLong();
        statusId = in.readLong();
        ticketId = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(homeTeam);
        dest.writeString(awayTeam);
        dest.writeLong(gameStart != null ? gameStart.getTime() : -1L);
        dest.writeInt(homeScore);
        dest.writeInt(awayScore);
        dest.writeByte((byte) (isFinished ? 0x01 : 0x00));
        dest.writeLong(matchServisId);
        dest.writeLong(betId);
        dest.writeLong(leagueId);
        dest.writeLong(statusId);
        dest.writeLong(ticketId);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MatchParcelable> CREATOR = new Parcelable.Creator<MatchParcelable>() {
        @Override
        public MatchParcelable createFromParcel(Parcel in) {
            return new MatchParcelable(in);
        }

        @Override
        public MatchParcelable[] newArray(int size) {
            return new MatchParcelable[size];
        }
    };
}