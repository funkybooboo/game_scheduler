package models;

import java.util.Map;

public class Team implements Comparable<Team> {
    public int id;
    public String name;
    public String league;

    public int numberOfScheduledGames;
    public int numberOfEarlyGames;
    public int numberOfLateGames;
    public int numberOfDaysSinceLastGame;

    // Team.id to number of games played against that team.
    public Map<Integer, Integer> gamesPlayedAgainst;

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

    public int compareTo(Team other) {
        if (!this.league.equals(other.league)) {
            throw new IllegalArgumentException("Teams must be in the same league to compare.");
        }
        // Max distance between playing the same team (5 games).
        // A team should have at least 3 days break before they play again.
        // A team isn't allowed to play the same opponent again until they've played all other teams in the league.
        // A team should have an equal number of early and late games.



    }

}
