import java.util.HashSet;

public class AirportDecorator extends Territory {

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
            attackableTerritories.addAll(neighbor.getNeighbors());

            for (Territory neighborOfNeighbor : neighbor.neighbors) {
                attackableTerritories.addAll(neighborOfNeighbor.getNeighbors());
            }
        }

        //Remove the territories of the owner and their allies from the attackable ones.
        attackableTerritories.removeIf(attackableTerritory -> attackableTerritory.getOwnerId() == getOwnerId()
                || attackableTerritory.getOwner().isAlly(owner));

        return attackableTerritories;
    }
}
