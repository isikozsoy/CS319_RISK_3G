import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RiskGame {

    private final int TER_COUNT = 42;
    private List<Player> players;
    private Territory[] territories;
    private int curPlayerId;
    private int gamePhase;
    private int playerCount;
    private Cards cards;
    private boolean isGameOver;
    private RiskView riskView;
    private int tempTerCount = TER_COUNT;
    private HashSet<Territory> attackableTer;
    RockPaperScissorsGame rpsGame;
    // RPS
    // Continent List
    enum GameMode {
        TroopAllocationMode, SoldierAllocationMode, AttackMode, FortifyMode
    }
    private GameMode mode = GameMode.TroopAllocationMode;

    public RiskGame(ArrayList<Player> players, RiskView riskView) {
         this.players = players;
         territories = new Territory[42];
         for (int i = 0; i < territories.length; i++) {
             territories[i] = new Territory(i);
         }
         this.riskView = riskView;

         curPlayerId = 0;
         gamePhase = 0;
         playerCount = players.size();
         cards = null;   // for now
         isGameOver = false;
         rpsGame = new RockPaperScissorsGame();
        addNeighbors();
        // Continents
         // RPS
    }

    final public void addNeighbors() {

        territories[0].addNeighbor(territories[1]);
        territories[0].addNeighbor(territories[3]);
        territories[0].addNeighbor(territories[37]);

        territories[1].addNeighbor(territories[0]);
        territories[1].addNeighbor(territories[3]);
        territories[1].addNeighbor(territories[4]);
        territories[1].addNeighbor(territories[2]);

        territories[2].addNeighbor(territories[5]);
        territories[2].addNeighbor(territories[1]);
        territories[2].addNeighbor(territories[4]);
        territories[2].addNeighbor(territories[19]);

        territories[3].addNeighbor(territories[0]);
        territories[3].addNeighbor(territories[1]);
        territories[3].addNeighbor(territories[4]);
        territories[3].addNeighbor(territories[6]);

        territories[4].addNeighbor(territories[3]);
        territories[4].addNeighbor(territories[1]);
        territories[4].addNeighbor(territories[7]);
        territories[4].addNeighbor(territories[6]);
        territories[4].addNeighbor(territories[5]);
        territories[4].addNeighbor(territories[2]);

        territories[5].addNeighbor(territories[4]);
        territories[5].addNeighbor(territories[7]);
        territories[5].addNeighbor(territories[2]);

        territories[6].addNeighbor(territories[7]);
        territories[6].addNeighbor(territories[4]);
        territories[6].addNeighbor(territories[8]);
        territories[6].addNeighbor(territories[3]);

        territories[7].addNeighbor(territories[6]);
        territories[7].addNeighbor(territories[4]);
        territories[7].addNeighbor(territories[5]);
        territories[7].addNeighbor(territories[8]);

        territories[8].addNeighbor(territories[6]);
        territories[8].addNeighbor(territories[7]);
        territories[8].addNeighbor(territories[9]);

        territories[9].addNeighbor(territories[11]);
        territories[9].addNeighbor(territories[4]);
        territories[9].addNeighbor(territories[10]);

        territories[10].addNeighbor(territories[9]);
        territories[10].addNeighbor(territories[12]);
        territories[10].addNeighbor(territories[11]);

        territories[11].addNeighbor(territories[9]);
        territories[11].addNeighbor(territories[12]);
        territories[11].addNeighbor(territories[10]);
        territories[11].addNeighbor(territories[13]);

        territories[12].addNeighbor(territories[11]);
        territories[12].addNeighbor(territories[10]);

        territories[13].addNeighbor(territories[16]);
        territories[13].addNeighbor(territories[11]);
        territories[13].addNeighbor(territories[24]);
        territories[13].addNeighbor(territories[14]);
        territories[13].addNeighbor(territories[15]);

        territories[14].addNeighbor(territories[24]);
        territories[14].addNeighbor(territories[41]);
        territories[14].addNeighbor(territories[13]);
        territories[14].addNeighbor(territories[15]);

        territories[15].addNeighbor(territories[16]);
        territories[15].addNeighbor(territories[41]);
        territories[15].addNeighbor(territories[13]);
        territories[15].addNeighbor(territories[15]);

        territories[16].addNeighbor(territories[17]);
        territories[16].addNeighbor(territories[13]);
        territories[16].addNeighbor(territories[15]);

        territories[17].addNeighbor(territories[18]);
        territories[17].addNeighbor(territories[16]);
        territories[17].addNeighbor(territories[15]);

        territories[18].addNeighbor(territories[17]);
        territories[18].addNeighbor(territories[15]);

        territories[19].addNeighbor(territories[2]);
        territories[19].addNeighbor(territories[22]);
        territories[19].addNeighbor(territories[20]);

        territories[20].addNeighbor(territories[19]);
        territories[20].addNeighbor(territories[23]);
        territories[20].addNeighbor(territories[21]);

        territories[21].addNeighbor(territories[23]);
        territories[21].addNeighbor(territories[24]);
        territories[21].addNeighbor(territories[20]);
        territories[21].addNeighbor(territories[41]);
        territories[21].addNeighbor(territories[39]);
        territories[21].addNeighbor(territories[40]);

        territories[22].addNeighbor(territories[23]);
        territories[22].addNeighbor(territories[25]);
        territories[22].addNeighbor(territories[20]);
        territories[22].addNeighbor(territories[19]);

        territories[23].addNeighbor(territories[22]);
        territories[23].addNeighbor(territories[25]);
        territories[23].addNeighbor(territories[20]);
        territories[23].addNeighbor(territories[24]);
        territories[23].addNeighbor(territories[21]);

        territories[24].addNeighbor(territories[13]);
        territories[24].addNeighbor(territories[14]);
        territories[24].addNeighbor(territories[21]);
        territories[24].addNeighbor(territories[23]);
        territories[24].addNeighbor(territories[25]);
        territories[24].addNeighbor(territories[41]);

        territories[25].addNeighbor(territories[22]);
        territories[25].addNeighbor(territories[23]);
        territories[25].addNeighbor(territories[24]);

        territories[26].addNeighbor(territories[30]);
        territories[26].addNeighbor(territories[27]);
        territories[26].addNeighbor(territories[28]);
        territories[26].addNeighbor(territories[29]);

        territories[27].addNeighbor(territories[26]);
        territories[27].addNeighbor(territories[27]);
        territories[27].addNeighbor(territories[28]);
        territories[27].addNeighbor(territories[29]);

        territories[28].addNeighbor(territories[26]);
        territories[28].addNeighbor(territories[27]);
        territories[28].addNeighbor(territories[29]);

        territories[29].addNeighbor(territories[26]);
        territories[29].addNeighbor(territories[27]);
        territories[29].addNeighbor(territories[28]);

        territories[30].addNeighbor(territories[31]);
        territories[30].addNeighbor(territories[32]);
        territories[30].addNeighbor(territories[26]);

        territories[31].addNeighbor(territories[30]);
        territories[31].addNeighbor(territories[32]);
        territories[31].addNeighbor(territories[41]);
        territories[31].addNeighbor(territories[39]);

        territories[32].addNeighbor(territories[31]);
        territories[32].addNeighbor(territories[30]);
        territories[32].addNeighbor(territories[39]);
        territories[32].addNeighbor(territories[40]);
        territories[32].addNeighbor(territories[38]);
        territories[32].addNeighbor(territories[33]);

        territories[33].addNeighbor(territories[32]);
        territories[33].addNeighbor(territories[30]);
        territories[33].addNeighbor(territories[35]);
        territories[33].addNeighbor(territories[37]);
        territories[33].addNeighbor(territories[34]);

        territories[34].addNeighbor(territories[37]);
        territories[34].addNeighbor(territories[33]);

        territories[35].addNeighbor(territories[36]);
        territories[35].addNeighbor(territories[38]);
        territories[35].addNeighbor(territories[33]);
        territories[35].addNeighbor(territories[37]);

        territories[36].addNeighbor(territories[38]);
        territories[36].addNeighbor(territories[35]);
        territories[36].addNeighbor(territories[37]);

        territories[37].addNeighbor(territories[0]);
        territories[37].addNeighbor(territories[36]);
        territories[37].addNeighbor(territories[33]);
        territories[37].addNeighbor(territories[34]);

        territories[38].addNeighbor(territories[40]);
        territories[38].addNeighbor(territories[32]);
        territories[38].addNeighbor(territories[33]);
        territories[38].addNeighbor(territories[35]);
        territories[38].addNeighbor(territories[36]);

        territories[39].addNeighbor(territories[40]);
        territories[39].addNeighbor(territories[41]);
        territories[39].addNeighbor(territories[31]);
        territories[39].addNeighbor(territories[32]);
        territories[39].addNeighbor(territories[24]);
        territories[39].addNeighbor(territories[21]);

        territories[40].addNeighbor(territories[38]);
        territories[40].addNeighbor(territories[32]);
        territories[40].addNeighbor(territories[39]);
        territories[40].addNeighbor(territories[21]);

        territories[41].addNeighbor(territories[24]);
        territories[41].addNeighbor(territories[21]);
        territories[41].addNeighbor(territories[14]);
        territories[41].addNeighbor(territories[31]);
        territories[41].addNeighbor(territories[39]);
        territories[41].addNeighbor(territories[15]);
    }

    public void loadAttackableTer(int territoryId) {
        attackableTer = territories[territoryId].searchForAttackable();
    }

    public boolean isAttackable(int territoryId) {
        return attackableTer.contains(territories[territoryId]);
    }

    public int getCurPlayerId() {
        return curPlayerId;
    }


    public int getTerritoryOwnerId(int territoryId) {
        return territories[territoryId].getOwner().getId();
    }

    public void startTerAlloc(int territoryId) {

/*        riskView.setTerritoryColor(players.get(0).getColor());
        riskView.addTroopsLeft(players.get(0));*/

        //check which territory was clicked for
        Territory territoryClicked = territories[territoryId];
        if (territoryClicked != null && territoryClicked.getOwner() == null) {

            territoryClicked.setTroopCount(5);
            Player curPlayer = players.get(curPlayerId);

            territoryClicked.setOwner(players.get(curPlayerId));

            curPlayer.decreaseTroop(1);
            curPlayer.setTerCount(curPlayer.getTerCount() + 1);


            riskView.setTerritoryColor(players.get(curPlayerId).getColor(), territoryId);
            curPlayerId = (curPlayerId + 1) % playerCount;
            riskView.addTroopsLeft(players.get(curPlayerId));
            tempTerCount--;
        }

        if (tempTerCount <= 0) {
            System.out.println("aa");
            mode = GameMode.AttackMode;
            riskView.setTerritoryClicked(false);
            riskView.setTerritoryMode(mode);
        }
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }

/*    public void startSoldierAlloc(Player player) {
        riskView.setTerritoryMode(GameMode.SoldierAllocationMode);
        if(mode == GameMode.SoldierAllocationMode) {
            riskView.setTerritoryMode(GameMode.SoldierAllocationMode);

            riskView.setOnMouseClicked(e -> {
                int noOfTroops = player.getTroopCount();
                Territory territoryClicked = riskView.getClickedTerritory();
                if( territoryClicked != null) {
                    Territory territoryTemp = new Territory("deneme");
                    int noOfTroopsToBeAllocatedTemp = 0;
                    //number of troops to be allocated is got from the player with a listener
                    //                                                                  //
                    ///////////////////////////////////////////////////////////////////////

                    if( territoryTemp.getOwner().getId() == player.getId()) {
                        territoryTemp.setTroopCount(noOfTroopsToBeAllocatedTemp);
                        player.decreaseTroop(noOfTroopsToBeAllocatedTemp);
                        noOfTroops = noOfTroops - noOfTroopsToBeAllocatedTemp;
                    }
                }

                if(noOfTroops == 0) {
                    setMode(GameMode.AttackMode);
                }
            });

            player.setTroopCount(0);
        }
    }*/

    public int startAttack(int sourceId, int targetId, int troopCount, int[] newTroopCount) {

        if (mode != GameMode.AttackMode || troopCount < 1) {
            return -1;
        }

        Territory source = territories[sourceId];
        int sourceTroopCount = source.getTroopCount();

        if(sourceTroopCount <= troopCount)
            return -1;

        int result;

        Territory target = territories[targetId];

        riskView.setTerritoryMode(GameMode.SoldierAllocationMode);

        source.setTroopCount(sourceTroopCount - troopCount);

        int targetTroopCount = territories[targetId].getTroopCount();

        int[] remainingTroops = rpsGame.play('A', '1',troopCount, targetTroopCount);

        if(remainingTroops[1] == 0) {
            target.setTroopCount(remainingTroops[0]);
            target.setOwner(players.get(curPlayerId));
            result = 0;
        }
        else {
            target.setTroopCount(remainingTroops[1]);
            result = 1;
        }

        newTroopCount[0] = source.getTroopCount();
        newTroopCount[1] = target.getTroopCount();

        return result;
    }

    public void startFortify(Player player) {
         Territory tempSource = null;
         Territory tempTarget = null;
         int troopToTransfer = 5;
         if(tempSource.getOwner().getId() == curPlayerId && tempTarget.getOwner().getId() == curPlayerId) {
             tempSource.setTroopCount(tempSource.getTroopCount() - troopToTransfer);
             tempTarget.setTroopCount(tempTarget.getTroopCount() + troopToTransfer);
         }
    }

    //Builds an airport to the territory whose id is given.
    public void buildAirport(int territoryId) {
        Territory territory = territories[territoryId];
        territory.setHasAirport(true);
        territory = new AirportDecorator(territory);
        territories[territoryId] = territory;
    }

    public void update() {
        //////////////////////////////////
        ///         TO DO              ///
        //////////////////////////////////
    }
}