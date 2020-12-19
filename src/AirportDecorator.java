import java.util.HashSet;

public class AirportDecorator extends Territory {
    private final boolean HAS_AIRPORT = true;
    private Territory territory;

    //constructor
    public AirportDecorator(Territory territory) {
        super();
    }

    //method

    public Territory getTerritory() {
        return territory;
    }

    //Returns all the territories that can be attacked from this territory.
    @Override
    public HashSet<Territory> searchForAttackable() {
        HashSet<Territory> attackableTerritories = territory.searchForAttackable();

        for (Territory neighbor : territory.neighbors) {
            if (owner.getId() == neighbor.owner.getId()) {
                continue;
            }
            attackableTerritories.addAll(neighbor.searchForAttackable());
            for (Territory neighborOfNeighbor : neighbor.neighbors) {
                if (owner.getId() == neighbor.owner.getId()) {
                    continue;
                }
                attackableTerritories.addAll(neighborOfNeighbor.searchForAttackable());
            }
        }

        return attackableTerritories;
    }
}
