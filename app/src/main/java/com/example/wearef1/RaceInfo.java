package com.example.wearef1;

public class RaceInfo {
    private int raceNumber;
    private String raceName;
    private String raceDate;

    public RaceInfo(){
        this.raceNumber = 0;
        this.raceName = "";
    }

    public RaceInfo(int raceNumber, String raceName, String raceDate){
        this.raceNumber = raceNumber;
        this.raceName = raceName;
        this.raceDate = raceDate;
    }

    public int getRaceNumber() {
        return raceNumber;
    }

    public void setRaceNumber(int raceNumber) {
        this.raceNumber = raceNumber;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public String getRaceDate() {
        return raceDate;
    }

    public void setRaceDate(String raceDate) {
        this.raceDate = raceDate;
    }
}
