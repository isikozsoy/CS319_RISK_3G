import java.util.HashSet;

public abstract class TerritoryDecorator extends Territory {

    //property
    protected Territory territory;

    //constructor
    public TerritoryDecorator(Territory territory) {
        super(territory.id);
        this.territory = territory;
    }

    //methods

    public Player getOwner() { return territory.owner; }

    public void setOwner(Player owner) { territory.setOwner(owner); }

    public int getTroopCount() {
        return territory.getTroopCount();
    }

    public void setTroopCount(int troopCount) {
        territory.setTroopCount(troopCount);
    }

    public boolean hasAirport() { return territory.hasAirport(); }

    public void setHasAirport(boolean hasAirport) { territory.setHasAirport(hasAirport); }

    public HashSet<Territory> getNeighbors() {
        return territory.getNeighbors();
    }

    public void setNeighbors(HashSet<Territory> neighbors) {
        territory.setNeighbors(neighbors);
    }

    // Adds given Territory parameter to the neighbors hashset
    public void addNeighbor(Territory neighbor)
    {
        territory.addNeighbor(neighbor);
    }

    // Checks if a given Territory object
    // is contained in the neighbors hashset
    public boolean isNeighbor(Territory territory)
    {
        return neighbors.contains(territory);
    }
}
