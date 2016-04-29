package model;

/**
 * Created by gisko on 29-Apr-16.
 */
public class League {
    private long id;
    private String leagueName;
    private long leagueServisId;

    public League(){

    }

    public League(String leagueName, long leagueServisId){
        this.leagueName = leagueName;
        this.leagueServisId = leagueServisId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
                "id=" + id +
                ", leagueName='" + leagueName + '\'' +
                ", leagueServisId=" + leagueServisId +
                '}';
    }
}
