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
    private Territory sourceTer;
    private int ctr = 0;
    // RPS
    // Continent List
    enum GameMode {
        TerAllocationMode, SoldierAllocationMode, AttackMode, FortifyMode
    }
    private GameMode mode;

    public RiskGame(ArrayList<Player> players, Territory[] territories, RiskView riskView) {
        for (Player p : players)
            p.setPlayerCount(players.size());
        System.out.println(players.size());
         this.players = players;
         this.territories = territories;
         this.riskView = riskView;

         curPlayerId = 0;
         gamePhase = 0;
         playerCount = players.size();
         cards = null;   // for now
         isGameOver = false;
         mode = GameMode.TerAllocationMode;
         // Continents
         // RPS
    }

    public Player play() {
        startInitialization();
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

    public void startInitialization() {
        startTerAlloc();
    }

    public void startTerAlloc() {
        if( mode == GameMode.TerAllocationMode) {
            riskView.setTerritoryColor(players.get(0).getColor());

            riskView.setOnMouseClicked(e -> {
                //check which territory was clicked for
                Territory territoryClicked = riskView.getClickedTerritory();
                if(territoryClicked != null && territoryClicked.getOwnerId() == -1) {
                    System.out.println(territoryClicked.getName());
                    Player curPlayer = players.get(curPlayerId);

                    System.out.println(curPlayerId);
                    territoryClicked.setOwnerId(curPlayerId);

                    curPlayer.decreaseTroop(1);
                    curPlayer.setTerCount(curPlayer.getTerCount() + 1);
                    nextTurn();

                    riskView.setTerritoryColor(players.get(curPlayerId).getColor());
                    tempTerCount--;
                }

                if(tempTerCount <= 0) {
                    System.out.println("aa");
                    mode = GameMode.SoldierAllocationMode;
                    riskView.setTerritoryClicked(false);
                    riskView.setTerritoryMode(mode);
                    startSoldierAlloc();
                }
            });
        }
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }

    public void startSoldierAlloc() {
        if(mode == GameMode.SoldierAllocationMode) {
            riskView.setTerritoryMode(GameMode.SoldierAllocationMode);
            Player curPlayer = players.get(curPlayerId);
            int noOfTroops = curPlayer.getTroopCount();
            riskView.setOnMouseClicked(e -> {
                if(ctr == 0) {
                    sourceTer = riskView.getClickedTerritory();
                    if (sourceTer != null && sourceTer.getOwnerId() == curPlayer.getId()) {
                        System.out.println("meekan bizim");
                        riskView.addTroopCountSelector(noOfTroops);
                        ctr = -1;
                    }
                }
                else if(riskView.getSelectedTroop() != 0) {
                    System.out.println("zero");
                    ctr = 1;
                }
                else if (ctr == 1){
                    int selectedTroop = riskView.getSelectedTroop();
                    sourceTer.addTroop(selectedTroop);
                    System.out.println(" selected troop " + selectedTroop);
                    curPlayer.decreaseTroop(selectedTroop);
                    System.out.println(" player troop " + curPlayer.getTroopCount());
                    System.out.println(" ter troop" + sourceTer.getTroopCount());
                    ctr = 0;
                    if (noOfTroops == 0) {
                        //setMode(GameMode.AttackMode);
                    }
                }
            });
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

    public void nextTurn() {
        curPlayerId = (curPlayerId + 1) % playerCount;
    }
}