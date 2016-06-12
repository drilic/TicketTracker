package model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * League class for all supported leagues.
 */
@Table(name = "League", id = "_id")
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

    @Override
    public String toString() {
        return leagueName;
    }
}
