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

        List<LeftistHeap<Team>> leagueHeaps = new ArrayList<>();
        Map<LeftistHeap<Team>, League> heapLeagueMap = new HashMap<>();
        for (League league : leagues) {
            LeftistHeap<Team> heap = new MinLeftistHeap<>();
            heapLeagueMap.put(heap, league);
            for (Team team : league.teams) {
                heap.insert(team);
            }
            leagueHeaps.add(heap);
        }
        for (int i = 0; i < iceTimes.size(); i++) {
            makeGame(iceTimes.get(i), leagueHeaps.get(i % leagues.size()), games, heapLeagueMap);
        }

        return games;
    }

    private static void makeGame(IceTime iceTime, LeftistHeap<Team> heap, List<Game> games, Map<LeftistHeap<Team>, League> heapLeagueMap) {
        Team team1 = heap.delete();
        Team team2 = heap.delete();
        if (team1 != null && team2 != null) {
            Game game = new Game(team1, team2, iceTime);
            games.add(game);

            team1.numberOfScheduledGames++;
            team2.numberOfScheduledGames++;
            team1.numberOfDaysSinceLastGame = 0;
            team2.numberOfDaysSinceLastGame = 0;
            team1.gamesPlayedAgainst.put(team2.id, team1.gamesPlayedAgainst.getOrDefault(team2.id, 0) + 1);
            team2.gamesPlayedAgainst.put(team1.id, team2.gamesPlayedAgainst.getOrDefault(team1.id, 0) + 1);

            if (iceTime.isEarly) {
                team1.numberOfEarlyGames++;
                team2.numberOfEarlyGames++;
            }
            else {
                team1.numberOfLateGames++;
                team2.numberOfLateGames++;
            }

            League league = heapLeagueMap.get(heap);

            for (Team team : league.teams) {
                if (team != team1 && team != team2) {
                    team.numberOfDaysSinceLastGame++;
                }
            }
        }
        if (team1 != null && team1.numberOfScheduledGames < 10) {
            heap.insert(team1);
        }
        if (team2 != null && team2.numberOfScheduledGames < 10) {
            heap.insert(team2);
        }
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
