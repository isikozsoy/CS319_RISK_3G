import java.util.HashSet;

public class AirportDecorater extends TerritoryDecorater {


    //constructor
    public AirportDecorater(Territory territory) {
        super(territory);
    }

    //method

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
