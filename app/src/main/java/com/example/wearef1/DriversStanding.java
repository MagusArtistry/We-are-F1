package com.example.wearef1;

public class DriversStanding {
    private String position;
    private String driver;
    private String points;

    public DriversStanding(String position, String team, String points) {
        this.position = position;
        this.driver = team;
        this.points = points;
    }

    public String getPosition() {
        return position;
    }

    public String getDriver() {
        return driver;
    }

    public String getPoints() {
        return points;
    }
}
