package models;

public class Game {
    public League league;
    public Team team1;
    public Team team2;
    public IceTime iceTime;

    public Game(League league, Team team1, Team team2, IceTime iceTime) {
        this.league = league;
        this.team1 = team1;
        this.team2 = team2;
        this.iceTime = iceTime;
    }

    public String toString() {
        return """
                League: %s
                Team 1: %s
                Team 2: %s
                Ice Time: %s
                """.formatted(league.name, team1.name, team2.name, iceTime.dateTime);
    }
}
