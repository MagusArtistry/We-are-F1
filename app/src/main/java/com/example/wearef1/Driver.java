package com.example.wearef1;

public class Driver {
    private String name;
    private String team_name;
    private String country;
    private int podiums;
    private float total_points;
    private int races_entered;
    private int world_championships_count;
    private String dob;
    private String nationality;
    private int year_info;
    private String ref_name;

    public Driver(String name, String team_name, String country, int podiums, float total_points, int races_entered, int world_championships_count, String dob, String nationality, String ref_name, int year_info)
    {
        this.name = name;
        this.team_name = team_name;
        this.country = country;
        this.podiums = podiums;
        this.total_points = total_points;
        this.races_entered = races_entered;
        this.world_championships_count = world_championships_count;
        this.dob = dob;
        this.nationality = nationality;
        this.year_info = year_info;
        this.ref_name = ref_name;
    }

    public Driver(){

    }

    public String getRef_name() {
        return ref_name;
    }

    public void setRef_name(String ref_name) {
        this.ref_name = ref_name;
    }

    public int getYear_info() {
        return year_info;
    }

    public void setYear_info(int year_info) {
        this.year_info = year_info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPodiums() {
        return podiums;
    }

    public void setPodiums(int podiums) {
        this.podiums = podiums;
    }

    public float getTotal_points() {
        return total_points;
    }

    public void setTotal_points(float total_points) {
        this.total_points = total_points;
    }

    public int getRaces_entered() {
        return races_entered;
    }

    public void setRaces_entered(int races_entered) {
        this.races_entered = races_entered;
    }

    public int getWorld_championships_count() {
        return world_championships_count;
    }

    public void setWorld_championships_count(int world_championships_count) {
        this.world_championships_count = world_championships_count;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
