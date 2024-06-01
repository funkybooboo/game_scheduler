package models;

import java.util.Map;

public class Team implements Comparable<Team> {
    public int id;
    public String name;
    public String league;

    public int numberOfGames;
    public int numberOfEarlyGames;
    public Map<String, Integer> numberOfGamesPlayedPerTeam;
    public int numberOfDaysSinceLastGame;

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
        // TODO make sure the team plays all other teams before playing the same team again
        // Compare based on the number of games
        int compareByGames = Integer.compare(this.numberOfGames, other.numberOfGames);
        if (compareByGames != 0) {
            return compareByGames;
        }

        // If number of games is the same, compare based on the number of early games
        int compareByEarlyGames = Integer.compare(this.numberOfEarlyGames, other.numberOfEarlyGames);
        if (compareByEarlyGames != 0) {
            return compareByEarlyGames;
        }

        // If number of early games is also the same, compare based on the number of games playing each other
        int compareByGamesPlayingEachOther = Integer.compare(this.numberOfGamesPlayedPerTeam.get(other.name), other.numberOfGamesPlayedPerTeam.get(this.name));
        if (compareByGamesPlayingEachOther != 0) {
            return compareByGamesPlayingEachOther;
        }

        // If the number of games played per team is also the same, compare based on the number of days since last game
        return Integer.compare(this.numberOfDaysSinceLastGame, other.numberOfDaysSinceLastGame);
    }

}
