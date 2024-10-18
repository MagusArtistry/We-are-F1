package com.example.wearef1;

public class Team {
    private String ref_name;
    private String full_team_name;
    private String base;
    private String team_chief;
    private String technical_chief;
    private String chassis;
    private String power_unit;
    private String world_championships;
    private int pole_position_count;
    private int fastest_lap_count;
    private String first_driver;
    private String second_driver;

    private int year_info;

    public Team(){

    }

    public Team(String ref_name, int year_info, String full_team_name, String base, String team_chief, String technical_chief, String chassis, String power_unit, String world_championships, int pole_position_count, int fastest_lap_count, String first_driver, String second_driver)
    {
        this.full_team_name = full_team_name;
        this.base = base;
        this.team_chief = team_chief;
        this.technical_chief = technical_chief;
        this.chassis = chassis;
        this.power_unit = power_unit;
        this.world_championships = world_championships;
        this.pole_position_count = pole_position_count;
        this.fastest_lap_count = fastest_lap_count;
        this.first_driver = first_driver;
        this.second_driver = second_driver;
        this.ref_name = ref_name;
        this.year_info = year_info;

    }

    public int getYear_info() {
        return year_info;
    }

    public void setYear_info(int year_info) {
        this.year_info = year_info;
    }

    public String getRef_name() {
        return ref_name;
    }

    public void setRef_name(String ref_name) {
        this.ref_name = ref_name;
    }

    public String getFull_team_name() {
        return full_team_name;
    }

    public void setFull_team_name(String full_team_name) {
        this.full_team_name = full_team_name;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getTeam_chief() {
        return team_chief;
    }

    public void setTeam_chief(String team_chief) {
        this.team_chief = team_chief;
    }

    public String getTechnical_chief() {
        return technical_chief;
    }

    public void setTechnical_chief(String technical_chief) {
        this.technical_chief = technical_chief;
    }

    public String getChassis() {
        return chassis;
    }

    public void setChassis(String chassis) {
        this.chassis = chassis;
    }

    public String getPower_unit() {
        return power_unit;
    }

    public void setPower_unit(String power_unit) {
        this.power_unit = power_unit;
    }

    public String getWorld_championships() {
        return world_championships;
    }

    public void setWorld_championships(String world_championships) {
        this.world_championships = world_championships;
    }

    public int getPole_position_count() {
        return pole_position_count;
    }

    public void setPole_position_count(int pole_position_count) {
        this.pole_position_count = pole_position_count;
    }

    public int getFastest_lap_count() {
        return fastest_lap_count;
    }

    public void setFastest_lap_count(int fastest_lap_count) {
        this.fastest_lap_count = fastest_lap_count;
    }

    public String getFirst_driver() {
        return first_driver;
    }

    public void setFirst_driver(String first_driver) {
        this.first_driver = first_driver;
    }

    public String getSecond_driver() {
        return second_driver;
    }

    public void setSecond_driver(String second_driver) {
        this.second_driver = second_driver;
    }
}
