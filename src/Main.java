import models.Game;
import models.IceTime;
import models.League;
import models.Team;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
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
                    List<Team> teams = getTeams(leagueMap.get("teams"));
                    League league = new League(teams);
                    leagueList.add(league);
                }
            }
        }
        return leagueList;
    }

    public static List<Team> getTeams(Object teams) {
        List<Team> teamList = new ArrayList<>();
        if (teams instanceof List<?> teamObjects) {
            for (Object teamObj : teamObjects) {
                if (teamObj instanceof Map<?, ?> teamMap) {
                    int teamId = (int) teamMap.get("id");
                    String teamName = (String) teamMap.get("name");
                    Team team = new Team(teamId, teamName);
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

    public static void main(String[] args) throws Exception {
        //
        String [] filenames = {"data/fall_2023_config.yml", "data/spring_2024_config.yml", "data/summer_2024_config.yml"};
        for (String filename : filenames) {
            System.out.println("Priority "+ filename);
            Map<String, Object> yamlMap = readYamlFile(filename);
            List<League> leagues = getLeagues(yamlMap.get("leagues"));
            List<IceTime> iceTimes = getIceTimes(yamlMap.get("ice_times"));
            Scheduler scheduler = new Scheduler(leagues, iceTimes);
            List<Game> games = scheduler.schedule();
            System.out.println("Games: " + games.size());
            for (Game game : games) {
                System.out.println(game);
            }
            System.out.println();
        }
    }
}
