package com.example.wearef1;

public class ConstructorStanding {
    private String position;
    private String team;
    private String points;

    public ConstructorStanding(String position, String team, String points) {
        this.position = position;
        this.team = team;
        this.points = points;
    }

    public String getPosition() {
        return position;
    }

    public String getTeam() {
        return team;
    }

    public String getPoints() {
        return points;
    }
}
