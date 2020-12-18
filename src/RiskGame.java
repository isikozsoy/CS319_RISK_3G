import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RiskGame {
    private final int TER_COUNT = 42;
    private final int AIRPORT_COST = 5;
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
    private int playerCounterForEachTurn = 0;
    // RPS
    // Continent List
    enum GameMode {
        TerAllocationMode, SoldierAllocationMode, SoldierAllocationModeContinued, AttackMode, FortifyMode
    }
    private GameMode mode;

    public RiskGame(ArrayList<Player> players, Territory[] territories, RiskView riskView) {
        for (Player p : players)
            p.setPlayerCount(players.size());
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
            riskView.getCardsButton().setOnMouseClicked(e -> {
                riskView.addCardExchangePane();
                riskView.setCardExchangeInfo(players.get(curPlayerId));
            });
            riskView.setTerritoryColor(players.get(0).getColor());
            riskView.addTroopsLeft(players.get(0));

            riskView.setOnMouseClicked(e -> {
                //check which territory was clicked for
                ClickableTerritory clickableTerritory = riskView.getClickableTerritory();
                if(clickableTerritory != null && clickableTerritory.getAssociatedTerritory() != null && clickableTerritory.getAssociatedTerritory().getOwnerId() == -1) {
                    Player curPlayer = players.get(curPlayerId);

                    clickableTerritory.getAssociatedTerritory().setOwnerId(curPlayerId);

                    curPlayer.decreaseTroop(1);
                    curPlayer.setTerCount(curPlayer.getTerCount() + 1);
                    nextTurn();

                    riskView.addTroopsLeft(players.get(curPlayerId));

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
            riskView.addAllianceRequestPane(curPlayer);
            riskView.setAllianceRequestInfo(curPlayer);

            //below takes the selected territory from the original map source
            //if the selected territory is not null and is one of the current player's territories, RiskView adds
            //troop selection screen and from here on we will be taken to another GameMode method of the game
            riskView.setOnMouseClicked(e -> {
                ClickableTerritory clickableTerritory = riskView.getClickableTerritory();
                if( clickableTerritory != null && clickableTerritory.getAssociatedTerritory() != null && clickableTerritory.getAssociatedTerritory().getOwnerId() == curPlayer.getId()) {
                    riskView.addTroopCountSelector(noOfTroops);
                    riskView.setMaxCountSelection(curPlayer.getTroopCount());
                    mode = GameMode.SoldierAllocationModeContinued;
                    continueSoldierAllocation(clickableTerritory);
                }
            });
        }
    }

    //resumes the main soldier allocation method, basically this phase goes back and forth between these two
    public void continueSoldierAllocation(ClickableTerritory clickableTerritory) {
        if( mode == GameMode.SoldierAllocationModeContinued) {
            Player curPlayer = players.get(curPlayerId);
            //the methods below each call specific buttons of RiskView instead of assigning mouse listeners to
            // riskView itself, like we have been doing. This is because RiskView and each button listener take inputs
            // separately, so if I were to click on "place" then I would need to click on another space in the window
            // for functions defined below to take place, in a scenario where riskView is assigned listeners.
            // That is why, here we are assigning mouse listeners to each button
            riskView.getBackButton().setOnMouseClicked(e -> {
                //goes back to the original Soldier Allocation function to take a territory as input again
                riskView.removeTroopCountSelector();
                mode = GameMode.SoldierAllocationMode;
                startSoldierAlloc();
            });
            riskView.getPlaceButton().setOnMouseClicked(e -> {
                //sets selected troop integer, and places it in the territory specified itself
                int selectedTroop = riskView.getSelectedTroop();
                clickableTerritory.getAssociatedTerritory().addTroop(selectedTroop);
                curPlayer.decreaseTroop(selectedTroop);
                riskView.removeTroopCountSelector();
                riskView.updateTroopsCount(curPlayer);
                riskView.updateTerTroopCount(clickableTerritory, selectedTroop);

                //sets the number to appear between more and less buttons
                //riskView.setMaxCountSelection(curPlayer.getTroopCount());
                if (curPlayer.getTroopCount() <= 0) {
                    playerCounterForEachTurn++;
                    nextTurn();
                    riskView.updatePlayerBar(players.get(curPlayerId));
                    riskView.updateTroopsCount(players.get(curPlayerId));

                    setMode(GameMode.SoldierAllocationMode);
                    startSoldierAlloc();

                    if( playerCounterForEachTurn >= playerCount) {
                        setMode(GameMode.AttackMode);
                    }
                }
            });
            riskView.getBuildAirportButton().setOnMouseClicked(e -> {
                curPlayer.decreaseTroop(AIRPORT_COST);
                /*
                * Territory should be modified.
                **/
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

    public ArrayList<Player> getPlayers()
    {
        return (ArrayList<Player>) players;
    }

    public void nextTurn() {
        curPlayerId = (curPlayerId + 1) % playerCount;
    }
}