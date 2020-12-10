import javafx.scene.paint.Color;
import java.util.HashSet;

public class Territory {
    //properties
    private String name;
    private int ownerId;
    private HashSet<Territory> neighbors;
    private int troopCount;
    private Color color;
    private boolean hasAnAirport;

    //constructor
    Territory(String name, Color color)
    {
        this.name = name;
        ownerId = -1;
        this.color = color;

        neighbors = new HashSet<>();
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

    public int getTroopCount() {
        return troopCount;
    }

    public void setTroopCount(int troopCount) {
        this.troopCount = troopCount;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean hasAirport() {
        return hasAnAirport;
    }

    public void buildAnAirport() //to build an airport hasAnAirport is set to true
    {
        hasAnAirport = true;
    }

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
