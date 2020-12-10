import javafx.scene.paint.Color;
import java.util.HashSet;

public class Territory {
    //properties
    private String name;
    private int ownerId;
<<<<<<< HEAD
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
=======
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
>>>>>>> origin/master
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

<<<<<<< HEAD
=======
    public Player getOwner() { return owner; }

    public void setOwner(Player owner) { this.owner = owner; }

>>>>>>> origin/master
    public int getTroopCount() {
        return troopCount;
    }

    public void setTroopCount(int troopCount) {
        this.troopCount = troopCount;
    }

<<<<<<< HEAD
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
=======
    public boolean hasAirport() { return hasAirport; }

    public void setHasAirport(boolean hasAirport) { this.hasAirport = hasAirport; }
>>>>>>> origin/master

    public HashSet<Territory> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(HashSet<Territory> neighbors) {
        this.neighbors = neighbors;
    }
    //end getter setters

    public void addNeighbor(Territory neighbor) //adds given Territory parameter to the hashset using
<<<<<<< HEAD
                                                // .add(Object object) method
=======
    // .add(Object object) method
>>>>>>> origin/master
    {
        neighbors.add(neighbor);
    }

    public boolean isNeighbor(Territory territory) //checks if a given Territory object
<<<<<<< HEAD
                                                   // is contained in the hashset: neighbors
    {
        return neighbors.contains(territory);
    }
}
=======
    // is contained in the hashset: neighbors
    {
        return neighbors.contains(territory);
    }
}
>>>>>>> origin/master
