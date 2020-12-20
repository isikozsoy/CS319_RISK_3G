import java.util.ArrayList;

public class Continent {
    private String name;
    private int ownerId;
    private ArrayList<Territory> territories;
    private int bonusTroopCount;

    //constructor
    Continent(String name, int bonusTroopCount, ArrayList<Territory> territories)
    {
        this.name = name;
        this.bonusTroopCount = bonusTroopCount;
        this.territories = territories;
    }

    public boolean contains( Territory territory) {
        return territories.contains(territory);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public ArrayList<Territory> getTerritories() {
        return territories;
    }

    public void setTerritories(ArrayList<Territory> territories) {
        this.territories = territories;
    }

    public int getBonusTroopCount() {
        return bonusTroopCount;
    }

    public void setBonusTroopCount(int bonusTroopCount) {
        this.bonusTroopCount = bonusTroopCount;
    }

    public boolean hasAnOwner() //0 for owner id means there is no such player
    {
        return ownerId != 6;
    }
}
