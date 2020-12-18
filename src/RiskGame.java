import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class RiskGame //extends Pane
{
    private final int TER_COUNT = 42;
    private List<Player> players;
    private Territory[] territories;
    private Player curPlayer;
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
    private boolean nextPhaseClicked = false;
    private HashSet<Territory> fortifyableFromSource;
    // RPS
    // Continent List
    enum GameMode {
        TerAllocationMode, SoldierAllocationInit, SoldierAllocationMode, AttackMode, FortifyMode1, FortifyMode2
    }
    private GameMode mode;

    public RiskGame(ArrayList<Player> players, Territory[] territories, RiskView riskView) {
        //this.setOnMouseClicked(e -> {

        //});

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
            riskView.addTroopsLeft(players.get(0));

            riskView.setOnMouseClicked(e -> {
                //check which territory was clicked for
                Territory territoryClicked = riskView.getClickedTerritory(mode);
                if(territoryClicked != null && territoryClicked.getOwnerId() == -1) {
                    curPlayer = players.get(curPlayerId);

                    territoryClicked.setOwner(curPlayer);
                    territoryClicked.setOwnerId(curPlayerId);

                    curPlayer.decreaseTroop(1);
                    curPlayer.setTerCount(curPlayer.getTerCount() + 1);
                    nextTurn();

                    riskView.addTroopsLeft(players.get(curPlayerId));

                    riskView.setTerritoryColor(players.get(curPlayerId).getColor());
                    tempTerCount--;
                }

                if(tempTerCount <= 0) {
                    //Starts the initial soldier allocation
                    //mode = GameMode.SoldierAllocationInit;
                    mode = GameMode.FortifyMode1;
                    riskView.setTerritoryClicked(false);
                    riskView.setTerritoryMode(mode);
                    curPlayer = players.get(curPlayerId);
                    //startSoldierAlloc();
                    startFortify();
                }
            });
        }
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }

    public void startSoldierAlloc() {
        curPlayer = players.get(curPlayerId);
        System.out.println(curPlayer);
        if(mode == GameMode.SoldierAllocationMode || mode == GameMode.SoldierAllocationInit) {
            riskView.setTerritoryMode(GameMode.SoldierAllocationMode);
            Player curPlayer = players.get(curPlayerId);
            int noOfTroops = curPlayer.getTroopCount();

            //below takes the selected territory from the original map source
            //if the selected territory is not null and is one of the current player's territories, RiskView adds
            //troop selection screen and from here on we will be taken to another GameMode method of the game
            riskView.addTroopsLeft(curPlayer);
            riskView.setOnMouseClicked(e -> {
                sourceTer = riskView.getClickedTerritory(mode);
                if( sourceTer != null && sourceTer.getOwnerId() == curPlayer.getId()) {
                    riskView.addTroopCountSelector(noOfTroops);
                    riskView.setMaxCountSelection(curPlayer.getTroopCount());
                    continueSoldierAllocation();
                }
                riskView.addTroopsLeft(curPlayer);
            });
        }
    }

    //resumes the main soldier allocation method, basically this phase goes back and forth between these two
    public void continueSoldierAllocation() {
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
            sourceTer.addTroop(selectedTroop);
            curPlayer.decreaseTroop(selectedTroop);
            riskView.removeTroopCountSelector();
            //sets the number to appear between more and less buttons
            //riskView.setMaxCountSelection(curPlayer.getTroopCount());
            if (curPlayer.getTroopCount() <= 0) {
                playerCounterForEachTurn++;

                if( playerCounterForEachTurn >= playerCount) {
                    System.out.println("Here");
                    setMode(GameMode.SoldierAllocationMode);
                }

                if( mode == GameMode.SoldierAllocationInit) {
                    nextTurn();

                    setMode(GameMode.SoldierAllocationInit);
                    startSoldierAlloc();
                }
                else if( mode == GameMode.SoldierAllocationMode) {
                    setMode(GameMode.AttackMode);
                    startAttack();
                }
            }
        });

    }

    public void startAttack() {
         //////////////////////////////////
         ///         TO DO              ///
         //////////////////////////////////
        startFortify();
    }

    public void startFortify() {
        if( mode == GameMode.FortifyMode1) {
            riskView.setOnMouseClicked(e -> {
                Territory sourceTerritory = null; //first set it as null so that it returns to null when the mode is
                                                 // returned from FortifyMode2
                sourceTerritory = riskView.getClickedTerritory( mode);
                //territory will both have to be non-null and match with the current player id
                if (sourceTerritory != null && sourceTerritory.getOwnerId() == curPlayer.getId()) {
                    mode = GameMode.FortifyMode2;
                    fortifyContinued(curPlayer, sourceTerritory);
                }

                if( nextPhaseClicked) {
                    nextTurn();
                    mode = GameMode.SoldierAllocationMode;
                    startSoldierAlloc();
                }
            });
        }
    }

    private void fortifyContinued( Player player, Territory sourceTerritory) {
        Button placeButton = riskView.getPlaceButton();
        Button backButton  = riskView.getBackButton();

        fortifyableFromSource = sourceTerritory.searchForFortifyable( new HashSet<>());

        riskView.setOnMouseClicked(e -> {
            //check for the second territory clicked
            //if it is the first one, territory will be unclicked
            //if it is another territory that is not null, troop count screen will show up
            //territory will both have to be non-null and match with the current player id
            Territory targetTerritory = riskView.getClickedTerritory(mode);
            System.out.println("Target: " + targetTerritory);
            if (targetTerritory == null || targetTerritory.equals(sourceTerritory)) {
                //call function to reset the effects to the territory
                startFortify();
                return;
            } else if (targetTerritory != null &&  fortifyableFromSource.contains(targetTerritory)) {
                //troop count selection screen will show up here
                riskView.addTroopCountSelector(sourceTerritory.getTroopCount());

                placeButton.setOnMouseClicked(event -> {
                    //troopToTransfer will also be got from the troop count selection screen
                    int troopToTransfer = riskView.getSelectedTroop(); // for now, a temp value
                    if( troopToTransfer != 0) {
                        //new troop counts will be set to each territory
                        sourceTerritory.setTroopCount(sourceTerritory.getTroopCount() - troopToTransfer);
                        targetTerritory.setTroopCount(targetTerritory.getTroopCount() - troopToTransfer);
                    }

                    riskView.removeTroopCountSelector();
                });

                backButton.setOnMouseClicked(event -> {
                    riskView.removeTroopCountSelector();
                    startFortify();
                });
            }
        });
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