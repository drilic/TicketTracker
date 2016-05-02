package model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * Created by gisko on 29-Apr-16.
 */

@Table(name = "League")
public class League extends Model {

    @Column(name = "leagueName", notNull = true)
    public String leagueName;

    @Column(name = "leagueServisId", notNull = true)
    public long leagueServisId;

    public List<Match> matches() {
        return getMany(Match.class, "league");
    }

    public League() {
        super();
    }

    public League(String leagueName, long leagueServisId) {
        super();
        this.leagueName = leagueName;
        this.leagueServisId = leagueServisId;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public long getLeagueServisId() {
        return leagueServisId;
    }


    public void setLeagueServisId(long leagueServisId) {
        this.leagueServisId = leagueServisId;
    }

    @Override
    public String toString() {
        return "League{" +
                ", leagueName='" + leagueName + '\'' +
                ", leagueServisId=" + leagueServisId +
                '}';
    }
}
