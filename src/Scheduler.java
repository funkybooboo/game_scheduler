import heap.leftist_heap.LeftistHeap;
import heap.leftist_heap.MinLeftistHeap;
import models.Game;
import models.IceTime;
import models.League;
import models.Team;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Scheduler {

    public static Map<String, Object> readYamlFile(String yamlFile) throws Exception {
        try {
            // Create a YAML object
            Yaml yaml = new Yaml();

            // Load YAML file into a Map
            FileInputStream inputStream = new FileInputStream(yamlFile);
            return yaml.load(inputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        throw new Exception("didnt get data");
    }

    public static List<League> getLeagues(Object leagues) {
        List<League> leagueList = new ArrayList<>();
        if (leagues instanceof List<?> leagueObjects) {
            for (Object leagueObj : leagueObjects) {
                if (leagueObj instanceof Map<?, ?> leagueMap) {
                    String leagueName = (String) leagueMap.get("name");
                    List<Team> teams = getTeams(leagueMap.get("teams"), leagueName);
                    League league = new League(leagueName, teams);
                    leagueList.add(league);
                }
            }
        }
        return leagueList;
    }

    public static List<Team> getTeams(Object teams, String leagueName) {
        List<Team> teamList = new ArrayList<>();
        if (teams instanceof List<?> teamObjects) {
            for (Object teamObj : teamObjects) {
                if (teamObj instanceof Map<?, ?> teamMap) {
                    int teamId = (int) teamMap.get("id");
                    String teamName = (String) teamMap.get("name");
                    Team team = new Team(teamId, teamName, leagueName);
                    teamList.add(team);
                }
            }
        }
        return teamList;
    }

    public static List<IceTime> getIceTimes(Object iceTimes) {
        List<IceTime> iceTimesList = new ArrayList<>();
        if (iceTimes instanceof List<?> iceTimeObjects) {
            for (Object iceTimeObj : iceTimeObjects) {
                if (iceTimeObj instanceof String iceTimeString) {
                    IceTime iceTime = new IceTime(iceTimeString);
                    iceTimesList.add(iceTime);
                }
            }
        }
        return iceTimesList;
    }

    public static List<Game> getGames(List<League> leagues, List<IceTime> iceTimes) {
        iceTimes.sort(Comparator.comparing(iceTime -> iceTime.dateTime));
        List<Game> games = new ArrayList<>();

        // TODO Implement the logic to generate games

        return games;
    }

    public static void main(String args[]) throws Exception {
        // "data/spring_2024_config.yml", "data/summer_2024_config.yml"
        String [] filenames = {"data/fall_2023_config.yml"};
        for (String filename : filenames) {
            System.out.println("Priority "+ filename);
            System.out.println();
            Map<String, Object> yamlMap = readYamlFile(filename);
            List<League> leagues = getLeagues(yamlMap.get("leagues"));
            List<IceTime> iceTimes = getIceTimes(yamlMap.get("ice_times"));
            List<Game> games = getGames(leagues, iceTimes);
            System.out.println("Games: " + games.size());
            for (Game game : games) {
                System.out.println(game);
            }
        }
    }
}
