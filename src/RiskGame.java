import java.awt.*;
import java.util.ArrayList;
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
    // RPS
    // Continent List
    enum GameMode {
        TroopAllocationMode, SoldierAllocationMode, AttackMode, FortifyMode
    }
    private GameMode mode = GameMode.TroopAllocationMode;

    public RiskGame(ArrayList<Player> players, Territory[] territories, RiskView riskView) {
         this.players = players;
         this.territories = territories;
         this.riskView = riskView;

         curPlayerId = 0;
         gamePhase = 0;
         playerCount = players.size();
         cards = null;   // for now
         isGameOver = false;
         // Continents
         // RPS
    }

    public Player play() {
        startTerAlloc();
        /**
        while (!isGameOver) {
            Player curPlayer = players.get(curPlayerId);
            startSoldierAlloc(curPlayer);
            startAttack(curPlayer);
            startFortify(curPlayer);
            update();
            curPlayerId = (curPlayerId + 1) % playerCount;
        }
         **/
        return null;
    }

    public void startTerAlloc() {
        if( mode == GameMode.TroopAllocationMode) {
            riskView.setTerritoryColor(players.get(0).getColor());
            riskView.addTroopsLeft(players.get(0));

            riskView.setOnMouseClicked(e -> {
                //check which territory was clicked for
                Territory territoryClicked = riskView.getClickedTerritory();
                if(territoryClicked != null && territoryClicked.getOwnerId() == -1) {
                    Player curPlayer = players.get(curPlayerId);

                    territoryClicked.setOwnerId(curPlayerId);

                    curPlayer.decreaseTroop(1);
                    curPlayer.setTerCount(curPlayer.getTerCount() + 1);
                    curPlayerId = (curPlayerId + 1) % playerCount;

                    riskView.addTroopsLeft(players.get(curPlayerId));

                    riskView.setTerritoryColor(players.get(curPlayerId).getColor());
                    tempTerCount--;
                }

                if(tempTerCount <= 0) {
                    System.out.println("aa");
                    mode = GameMode.AttackMode;
                    riskView.setTerritoryClicked(false);
                    riskView.setTerritoryMode(mode);
                }
            });
        }
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }

    public void startSoldierAlloc(Player player) {
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

                    if( territoryTemp.getOwnerId() == player.getId()) {
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
    }

    public void startAttack(Player player) {
         //////////////////////////////////
         ///         TO DO              ///
         //////////////////////////////////
    }

    public void startFortify(Player player) {
         Territory tempSource = null;
         Territory tempTarget = null;
         int troopToTransfer = 5;
         if(tempSource.getOwnerId() == curPlayerId && tempTarget.getOwnerId() == curPlayerId) {
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