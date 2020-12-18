import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class RiskGame
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
    private boolean gameStarted = false;
    private boolean soldierAllocBeforeClicking = false;
    // RPS
    // Continent List
    enum GameMode {
        TerAllocationMode, SoldierAllocationInit, SoldierAllocationMode, AttackMode, FortifyMode, FortifyModeCont, EndOfTurn
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

        riskView.setTerritoryColor(players.get(0).getColor());
        riskView.addTroopsLeft(players.get(0));

        play();
    }

    public void setTroopCountInView() {
        riskView.addTroopsLeft(curPlayer);
    }

    public void play() {
        curPlayer = players.get(curPlayerId);
        riskView.setOnMouseClicked(e -> {
            executeFunctions();
        });
    }

    public void executeFunctions() {
        switch (mode) {
            case TerAllocationMode: {
                startInitialization();
                break;
            }
            case SoldierAllocationInit: {
                startSoldierAlloc();
                break;
            }
            case SoldierAllocationMode: {
                startSoldierAlloc();
                break;
            }
            case AttackMode: {
                startAttack();
                break;
            }
            case FortifyMode: {
                startFortify();
                break;
            }
            case FortifyModeCont: {
                fortifyContinued( sourceTer);
            }
            case EndOfTurn: {
                nextTurn();
                nextMode();
                break;
            }
        }
    }

    /**
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

        return null;
    }
    **/

    public void startInitialization() {
        startTerAlloc();
    }

    public void startTerAlloc() {
        if( mode == GameMode.TerAllocationMode) {
            //check which territory was clicked for
            Territory territoryClicked = riskView.getClickedTerritory(mode);
            if (territoryClicked != null && territoryClicked.getOwnerId() == -1) {
                territoryClicked.setOwner(curPlayer);
                territoryClicked.setOwnerId(curPlayerId);

                curPlayer.decreaseTroop(1);
                curPlayer.setTerCount(curPlayer.getTerCount() + 1);
                nextTurn();

                riskView.addTroopsLeft(players.get(curPlayerId));

                riskView.setTerritoryColor(curPlayer.getColor());
                tempTerCount--;
            }

            if (tempTerCount <= 0) {
                //Starts the initial soldier allocation
                riskView.setTerritoryClicked(false);
                riskView.setTerritoryMode(mode);
                nextMode();
            }
        }
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }

    public int soldierAllocBeforeClicking() {
        curPlayer = players.get(curPlayerId);
        int noOfTroops = 0;
        if(mode == GameMode.SoldierAllocationMode || mode == GameMode.SoldierAllocationInit) {
            Player curPlayer = players.get(curPlayerId);
            noOfTroops = curPlayer.getTroopCount();

            //below takes the selected territory from the original map source
            //if the selected territory is not null and is one of the current player's territories, RiskView adds
            //troop selection screen and from here on we will be taken to another GameMode method of the game
            riskView.addTroopsLeft(curPlayer);
        }
        return noOfTroops;
    }

    public void startSoldierAlloc() {
        if( mode == GameMode.SoldierAllocationMode || mode == GameMode.SoldierAllocationInit) {
            if (gameStarted) {
                curPlayer.updateTroopCount();
            }

            if (!soldierAllocBeforeClicking) {
                soldierAllocBeforeClicking = true;
                int noOfTroops = soldierAllocBeforeClicking();
                if (noOfTroops == 0) nextMode();
            }

            curPlayer = players.get(curPlayerId);
            if (mode == GameMode.SoldierAllocationMode || mode == GameMode.SoldierAllocationInit) {
                sourceTer = riskView.getClickedTerritory(mode);
                if (sourceTer != null && sourceTer.getOwnerId() == curPlayer.getId()) {
                    setTroopCountInView();
                    riskView.setMaxCountSelection(curPlayer.getTroopCount());
                    continueSoldierAllocation();
                }
                riskView.addTroopsLeft(curPlayer);
            }
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
            soldierAllocBeforeClicking = false;
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
                    curPlayerId = 0;
                    curPlayer = players.get(curPlayerId); //the actual gameplay will start from the first player
                    gameStarted = true;
                }

                if( !gameStarted) {
                    nextTurn();
                    startSoldierAlloc();
                }
                else {
                    nextMode();
                    executeFunctions();
                }
            }
        });

    }

    public void startAttack() {
         //////////////////////////////////
         ///         TO DO              ///
         //////////////////////////////////
        nextMode();
    }

    public void startFortify() {
        if( mode == GameMode.FortifyMode) {
            Territory sourceTerritory = null; //first set it as null so that it returns to null when the mode is
            // returned from FortifyMode2
            sourceTerritory = riskView.getClickedTerritory( mode);
            //territory will both have to be non-null and match with the current player id
            System.out.println("Source: " + sourceTerritory);
            if (sourceTerritory != null && sourceTerritory.getOwnerId() == curPlayer.getId()) {
                fortifyableFromSource = sourceTerritory.searchForFortifyable( new HashSet<>());
                System.out.println(fortifyableFromSource);
                nextMode();
            }
        }
    }

    private void fortifyContinued( Territory sourceTerritory) {
        //check for the second territory clicked
        //if it is the first one, territory will be unclicked
        //if it is another territory that is not null, troop count screen will show up
        //territory will both have to be non-null and match with the current player id
        //riskView.setTerritoryClicked(false);
        Territory targetTerritory = riskView.getClickedTerritory(mode);
        System.out.println("Target: " + targetTerritory);
        if (targetTerritory == null || targetTerritory.equals(sourceTerritory)) {
            //reset the effects to the territory
            mode = GameMode.FortifyMode;
            return;
        } else if (targetTerritory != null &&  fortifyableFromSource.contains(targetTerritory)) {
            //troop count selection screen will show up here
            setTroopCountInView();

            riskView.getPlaceButton().setOnMouseClicked(event -> {
                //troopToTransfer will also be got from the troop count selection screen
                int troopToTransfer = riskView.getSelectedTroop(); // for now, a temp value
                if( troopToTransfer != 0) {
                    //new troop counts will be set to each territory
                    sourceTerritory.setTroopCount(sourceTerritory.getTroopCount() - troopToTransfer);
                    targetTerritory.setTroopCount(targetTerritory.getTroopCount() - troopToTransfer);
                }

                riskView.removeTroopCountSelector();
            });

            riskView.getBackButton().setOnMouseClicked(event -> {
                riskView.removeTroopCountSelector();
                mode = GameMode.FortifyMode; //goes back to previous mode
            });
        }

        riskView.getNextPhaseButton().setOnMouseClicked( event -> {
            nextMode();
            executeFunctions();
        });
    }

    public void nextMode() {
        switch (mode) {
            case TerAllocationMode: {
                mode = GameMode.SoldierAllocationInit;
                break;
            }
            case SoldierAllocationInit: {
                mode = GameMode.SoldierAllocationMode;
                break;
            }
            case SoldierAllocationMode: {
                mode = GameMode.FortifyMode;
                break;
            }
            case AttackMode: {
                mode = GameMode.FortifyMode;
                break;
            }
            case FortifyMode: {
                mode = GameMode.FortifyModeCont;
                break;
            }
            case FortifyModeCont: {
                mode = GameMode.EndOfTurn;
                break;
            }
            case EndOfTurn: {
                mode = GameMode.SoldierAllocationMode;
                break;
            }
        }
        riskView.setTerritoryMode(mode);
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
        curPlayer = players.get(curPlayerId);
        setTroopCountInView();
    }
}