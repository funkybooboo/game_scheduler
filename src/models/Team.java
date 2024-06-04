package models;

import java.util.HashMap;
import java.util.Map;

public class Team {
    public int id;
    public String name;
    public String league;

    public int numberOfEarlyGames;
    public int numberOfLateGames;
    public int numberOfScheduledGames;
    public int numberOfDaysSinceLastGame;

    // Team.id -> hasPlayedAgainst
    public Map<Integer, Boolean> playedAgainst = new HashMap<>();

    public Team(int id, String name, String league) {
        this.id = id;
        this.name = name;
        this.league = league;
    }

    public String toString() {
        return """
                ID: %d
                Name: %s
                League: %s
                """.formatted(id, name, league);
    }
}
