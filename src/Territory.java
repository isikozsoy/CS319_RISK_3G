import javafx.scene.paint.Color;
import java.util.HashSet;

public class Territory {
    //properties
    private String name;
    private int ownerId;
    private Player owner;
    private HashSet<Territory> neighbors;
    private int troopCount;
    private boolean hasAirport;

    //constructor
    Territory(String name, HashSet<Territory> neighbours) {
        this.name = name;
        ownerId = -1;
        owner = null;
        hasAirport = false;
        troopCount = 0;
        this.neighbors = neighbours;
    }

    //getter setters
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

    public Player getOwner() { return owner; }

    public void setOwner(Player owner) { this.owner = owner; }

    public int getTroopCount() {
        return troopCount;
    }

    public void setTroopCount(int troopCount) {
        this.troopCount = troopCount;
    }

    public boolean hasAirport() { return hasAirport; }

    public void setHasAirport(boolean hasAirport) { this.hasAirport = hasAirport; }

    public HashSet<Territory> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(HashSet<Territory> neighbors) {
        this.neighbors = neighbors;
    }
    //end getter setters

    public void addNeighbor(Territory neighbor) //adds given Territory parameter to the hashset using
    // .add(Object object) method
    {
        neighbors.add(neighbor);
    }

    public boolean isNeighbor(Territory territory) //checks if a given Territory object
    // is contained in the hashset: neighbors
    {
        return neighbors.contains(territory);
    }
}