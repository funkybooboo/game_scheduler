package models;

import java.util.List;

public class League {
    public List<Team> teams;

    public League(List<Team> teams) {
        this.teams = teams;
    }

    public String toString() {
        return """
                Name: %s
                """.formatted(teams);
    }
}
