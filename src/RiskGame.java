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
    private boolean nextPhaseClicked = false;
    // RPS
    // Continent List
    enum GameMode {
        TroopAllocationMode, InitialSoldierAllocationMode, SoldierAllocationMode, AttackMode, FortifyMode
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
        for(Player p = players.get(curPlayerId); p.getTroopCount() > 0; p = players.get(curPlayerId)) {
            startSoldierAlloc(players.get(curPlayerId));
            System.out.println(players.get(curPlayerId));
            nextTurn();
        }
        mode = GameMode.TroopAllocationMode;
        /**
        while (!isGameOver) {
            Player curPlayer = players.get(curPlayerId);
            if(curPlayer.getTroopCount() > 0)
                startSoldierAlloc(curPlayer);
            startAttack(curPlayer);
            startFortify(curPlayer);
            nextTurn();
        }
         **/
        return null;
    }
/**
    public void executePhases() {
        switch (mode) {
            case TroopAllocationMode: {
                startTerAlloc();
                break;
            }
            case InitialSoldierAllocationMode: {
                for(Player p = players.get(curPlayerId); p.getTroopCount() > 0; p = players.get(curPlayerId)) {
                    startSoldierAlloc(players.get(curPlayerId));
                    nextTurn();
                }
                break;
            }
            case SoldierAllocationMode: {
                startSoldierAlloc( players.get(curPlayerId));
                break;
            }
            case AttackMode: {
                startAttack( players.get( curPlayerId));
                if( isGameOver) {
                    endGame();
                }
            }
            case FortifyMode: {
                startFortify( players.get(curPlayerId));
            }
        }
    }
**/
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
        //her saldırı sonrası isGameOver'ı kontrol etmeli
        //next phase tıklandıysa mode = GameMode.FortifyMode
        //sonra executePhases() çağırır tekrar
    }

    public void startFortify(Player player) {
        if( mode == GameMode.FortifyMode) {
            riskView.setOnMouseClicked(e -> {
                Territory sourceTerritory = riskView.getClickedTerritoryAfterAllocation();
                //territory will both have to be non-null and match with the current player id
                if (sourceTerritory != null && sourceTerritory.getOwnerId() == player.getId()) {
                    //check for the second territory clicked
                    //if it is the first one, territory will be unclicked
                    //if it is another territory that is not null, troop count screen will show up
                    //territory will both have to be non-null and match with the current player id
                    Territory targetTerritory = riskView.getClickedTerritoryAfterAllocation();
                    if (targetTerritory.equals(sourceTerritory)) {
                        //call function to reset the effects to the territory
                        return;
                    } else if (targetTerritory != null && targetTerritory.getOwnerId() == player.getId()) {
                        //troop count selection screen will show up here
                        //troopToTransfer will also be got from the troop count selection screen
                        int troopToTransfer = 5; // for now, a temp value
                        //now the backend
                        //new troop counts will be set to each territory
                        sourceTerritory.setTroopCount(sourceTerritory.getTroopCount() - troopToTransfer);
                        targetTerritory.setTroopCount(targetTerritory.getTroopCount() - troopToTransfer);
                    }
                }

                if( nextPhaseClicked) {
                    nextTurn();
                    mode = GameMode.SoldierAllocationMode;
                    startTerAlloc();
                }
            });
        }
    }

    public void setNextPhaseClicked( boolean clicked) {
        nextPhaseClicked = clicked;
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