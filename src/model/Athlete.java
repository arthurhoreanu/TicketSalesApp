package model;

public class Athlete {
    private int athleteID;
    private String athleteName;
    private String athleteSport;

    public Athlete(int athleteID, String athleteName, String athleteSport) {
        this.athleteID = athleteID;
        this.athleteName = athleteName;
        this.athleteSport = athleteSport;
    }


    public int getAthleteID() {
        return athleteID;
    }

    public void setAthleteID(int athleteID) {
        this.athleteID = athleteID;
    }

    public String getAthleteName() {
        return athleteName;
    }

    public void setAthleteName(String athleteName) {
        this.athleteName = athleteName;
    }

    public String getAthleteSport() {
        return athleteSport;
    }

    public void setAthleteSport(String athleteSport) {
        this.athleteSport = athleteSport;
    }

    @Override
    public String toString() {
        return "Athlete{" + "athleteID=" + athleteID + ", athleteName='" + athleteName + '\'' + ", athleteSport='" + athleteSport + '\'' + '}';
    }
}
