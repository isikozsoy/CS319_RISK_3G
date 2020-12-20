import java.util.HashSet;

public class AirportDecorator extends TerritoryDecorator {

    //constructor
    public AirportDecorator(Territory territory) {
        super(territory);
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

        }

        //Remove the territories of the owner and their allies from the attackable ones.
        attackableTerritories.removeIf(attackableTerritory -> attackableTerritory.getOwnerId() == territory.getOwnerId()
                || attackableTerritory.getOwner().isAlly(territory.owner));

        return attackableTerritories;
    }
}
