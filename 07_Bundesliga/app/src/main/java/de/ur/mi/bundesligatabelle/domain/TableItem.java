package de.ur.mi.bundesligatabelle.domain;

public class TableItem {
        private int rank;
        private String name;
        private int goals;
        private int goalsAgainst;
        private int playedGames;
        private int points;

    public TableItem(int rank, String name, int goals, int goalsAgainst, int games, int points) {
        this.rank = rank;
        this.name = name;
        this.goals = goals;
        this.goalsAgainst = goalsAgainst;
        this.playedGames = games;
        this.points = points;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    public int getPlayedGames() {
        return playedGames;
    }

    public void setPlayedGames(int playedGames) {
        this.playedGames = playedGames;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
