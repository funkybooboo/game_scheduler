package models;

import java.util.List;

public class League implements Comparable<League> {
    public String name;
    public List<Team> teams;

    public League(String name, List<Team> teams) {
        this.name = name;
        this.teams = teams;
    }

    public String toString() {
        return """
                Name: %s
                Teams: %s
                """.formatted(name, teams);
    }

    public int compareTo(League other) {
        // TODO: Implement the comparison logic
    }
}
