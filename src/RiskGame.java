import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RiskGame {
    private final int TER_COUNT = 42;
    private final int AIRPORT_COST = 5;
    private List<Player> players;
    private List<ClickableTerritory> clickableTerritories;
    private List<Territory> clickedTerritories;
    private Territory[] territories;
    private Player curPlayer;
    private int curPlayerId;
    private int playerCount;
    private Cards cards;
    private boolean isGameOver;
    private RiskView riskView;
    private int tempTerCount = TER_COUNT;
    private Territory sourceTer;
    private int playerCounterForEachTurn = 0;
    private boolean nextPhaseClicked = false;
    private HashSet<Territory> fortifyableFromSource;
    private boolean gameStarted = false;
    private boolean soldierAllocBeforeClicking = false;
    private Territory targetTerritory;

    private char p1Choice, p2Choice;

    private boolean troopCountSelectorInView = false;
    RockPaperScissorsGame rpsGame;
    // Continent List
    enum GameMode {
        TerAllocationMode, SoldierAllocationInit, SoldierAllocationMode, AttackMode, FortifyMode, FortifyModeCont, EndOfTurn
    }
    private GameMode mode;

    public RiskGame(ArrayList<Player> players, Territory[] territories, List<ClickableTerritory> clickableTerritories, RiskView riskView) {
        for (Player p : players)
            p.setPlayerCount(players.size());
        this.players = players;
        this.territories = territories;
        this.clickableTerritories = clickableTerritories;
        this.riskView = riskView;

        clickedTerritories = new ArrayList<>();

        curPlayerId = 0;
        playerCount = players.size();
        cards = null;   // for now
        isGameOver = false;
        mode = GameMode.TerAllocationMode;
        // Continents
        rpsGame = new RockPaperScissorsGame();

        riskView.setTerritoryColor(players.get(0).getColor());
        riskView.addTroopsLeft(players.get(0));
        setButtons();
        setClickableTerritories();

        play();
    }

    public int getCurPlayerId() {
        return curPlayerId;
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
        System.out.println(mode);
        System.out.println(sourceTer);
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
                break;
            }
            case EndOfTurn: {
                nextTurn();
                nextMode();
                break;
            }
        }
    }

    public void setClickableTerritories() {
        for( ClickableTerritory clickableTerritory: clickableTerritories) {
            clickableTerritory.setOnMouseClicked(e -> {
                if( sourceTer == null) {
                    if( !(mode == GameMode.TerAllocationMode //so that when it is territory allocation phase, it will click on a territory only once and never again
                            && clickedTerritories.contains(clickableTerritory.getAssociatedTerritory())))
                        sourceTer = clickableTerritory.getAssociatedTerritory();
                }
                else if( targetTerritory == null)
                    targetTerritory = clickableTerritory.getAssociatedTerritory();
            });
        }
    }

    public void setButtons() {
        riskView.getTroopCountSelectorPane().getPlaceButton().setOnMouseClicked(e -> {
            if(mode == GameMode.SoldierAllocationInit
                    || mode == GameMode.SoldierAllocationMode) {
                //sets selected troop integer, and places it in the territory specified itself
                int selectedTroop = riskView.getSelectedTroop();
                sourceTer.addTroop(selectedTroop);
                curPlayer.decreaseTroop(selectedTroop);

                riskView.updateText(sourceTer, sourceTer.getTroopCount());

                riskView.removeTroopCountSelector();
                troopCountSelectorInView = false;
                //reset source territory for a new allocation
                sourceTer = null;
                soldierAllocBeforeClicking = false;
                //sets the number to appear between more and less buttons
                //riskView.setMaxCountSelection(curPlayer.getTroopCount());
                if (curPlayer.getTroopCount() <= 0) {
                    playerCounterForEachTurn++;

                    if (playerCounterForEachTurn >= playerCount) {
                        curPlayerId = 0;
                        curPlayer = players.get(curPlayerId); //the actual gameplay will start from the first player
                        gameStarted = true;
                    }

                    if (!gameStarted) {
                        nextTurn();
                        startSoldierAlloc();
                    }
                    else {
                        nextMode();
                        executeFunctions();
                    }
                }
                riskView.updateTroopsCount(curPlayer);
            }
            else if( mode == GameMode.FortifyModeCont) {
                //troopToTransfer will also be got from the troop count selection screen
                int troopToTransfer = riskView.getSelectedTroop();
                if (troopToTransfer != 0) {
                    //new troop counts will be set to each territory
                    sourceTer.setTroopCount(sourceTer.getTroopCount() - troopToTransfer);
                    targetTerritory.setTroopCount(targetTerritory.getTroopCount() + troopToTransfer);
                    //update their texts in riskview
                    riskView.updateText(sourceTer, sourceTer.getTroopCount());
                    riskView.updateText(targetTerritory, targetTerritory.getTroopCount());

                    System.out.println("Should be here");
                    targetTerritory = null;
                    sourceTer = null;
                }

                riskView.removeTroopCountSelector();
                troopCountSelectorInView = false;
            }
        });

        riskView.getTroopCountSelectorPane().getBackButton().setOnMouseClicked(event -> {
            if( mode == GameMode.FortifyModeCont) {
                riskView.removeTroopCountSelector();
                troopCountSelectorInView = false;
                sourceTer = null;
                targetTerritory = null;
                mode = GameMode.FortifyMode; //goes back to previous mode
            }
            if(mode == GameMode.SoldierAllocationInit
                    || mode == GameMode.SoldierAllocationMode) {
                //goes back to the original Soldier Allocation function to take a territory as input again
                riskView.removeTroopCountSelector();
                troopCountSelectorInView = false;
                soldierAllocBeforeClicking = false;
                //mode does not change here
            }
        });

        riskView.getTroopCountSelectorPane().getBuildAirportButton().setOnMouseClicked(e -> {
            if (!sourceTer.hasAirport()) {
                if (sourceTer.getTroopCount() > AIRPORT_COST) {
                    sourceTer.setTroopCount(sourceTer.getTroopCount() - AIRPORT_COST);

                    riskView.updateText(sourceTer, sourceTer.getTroopCount());
                    riskView.removeTroopCountSelector();

                    troopCountSelectorInView = false;

                    sourceTer.setHasAirport(true);
                    Territory sourceTerritory = new AirportDecorator(sourceTer);
                    territories[sourceTer.getId()] = sourceTerritory;
                    //reset source territory for a new allocation
                    sourceTer = null;
                    soldierAllocBeforeClicking = false;

                    riskView.getTroopCountSelectorPane().getBuildAirportButton().setText("Remove Airport");

                    riskView.removeTroopCountSelector();
                    troopCountSelectorInView = false;
                }
            }
            else {
                sourceTer.setTroopCount(sourceTer.getTroopCount() + 2);
                riskView.updateText(sourceTer, sourceTer.getTroopCount());

                Territory sourceTerritory = sourceTer.getTerritory();
                sourceTerritory.setHasAirport(false);
                territories[sourceTer.getId()] = sourceTerritory;

                //reset source territory for a new allocation
                sourceTer = null;
                soldierAllocBeforeClicking = false;
                riskView.getTroopCountSelectorPane().getBuildAirportButton().setText("Build Airport");
                riskView.removeTroopCountSelector();

                troopCountSelectorInView = false;
            }
        });

        riskView.getNextPhaseButton().setOnMouseClicked( event -> {
            if ( mode == GameMode.FortifyMode) mode = GameMode.EndOfTurn;
            else nextMode();
            setTroopCountInView();
            executeFunctions();
        });
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

            if( clickedTerritories.contains(sourceTer)) {
                System.out.println("aaaaaaaaaaaaaaaa");
                return;
            }

            //check which territory was clicked for
            if (sourceTer != null && sourceTer.getOwnerId() == -1) {
                clickedTerritories.add(sourceTer);
                riskView.addTextForTerritory(sourceTer);
                sourceTer.setOwner(curPlayer);
                sourceTer.setOwnerId(curPlayerId);
                sourceTer.addTroop(1);

                curPlayer.decreaseTroop(1);
                curPlayer.setTerCount(curPlayer.getTerCount() + 1);

                sourceTer = null;

                nextTurn();

                riskView.addTroopsLeft(players.get(curPlayerId));
                riskView.setTerritoryColor(curPlayer.getColor());

                tempTerCount--;
            }
            if (tempTerCount <= 0) {
                //Starts the initial soldier allocation
                riskView.setTerritoryClicked(false);
                riskView.setTerritoryMode(mode);
                curPlayerId = 0;
                setTroopCountInView();
                nextMode();
            }
        }
    }

    public int soldierAllocBeforeClicking() {
        curPlayer = players.get(curPlayerId);
        int noOfTroops = 0;
        if(mode == GameMode.SoldierAllocationMode || mode == GameMode.SoldierAllocationInit) {
            Player curPlayer = players.get(curPlayerId);
            noOfTroops = curPlayer.getTroopCount();
            if(!curPlayer.getAllianceReq().isEmpty()) {
                riskView.setAllianceRequestInfo(curPlayer);
                riskView.addAllianceRequestPane(curPlayer);
            }

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
                int noOfTroops = soldierAllocBeforeClicking(); //this method returns the number of troops of the current player
                if (noOfTroops == 0) {
                    nextMode();
                }
            }

            curPlayer = players.get(curPlayerId);
            if (mode == GameMode.SoldierAllocationMode || mode == GameMode.SoldierAllocationInit) {
                if (sourceTer != null && sourceTer.getOwnerId() == curPlayer.getId()
                        && !troopCountSelectorInView) { //So that duplicates do not occur
                    troopCountSelectorInView = true;
                    riskView.addTroopCountSelector(curPlayer.getTroopCount());
                    setButtons();
                    setTroopCountInView();
                }
                riskView.addTroopsLeft(curPlayer);
            }
        }
    }

    public void startAttack() {
        //select source and target territories
        if( mode == GameMode.AttackMode) {

            if( sourceTer != null && targetTerritory != null) {
                System.out.println(sourceTer.searchForAttackable());

                if( sourceTer.getOwnerId() == curPlayerId && sourceTer.getOwnerId() != targetTerritory.getOwnerId()) {
                    //we add a troop count selector
                    if( !troopCountSelectorInView) {
                        troopCountSelectorInView = true;
                        riskView.addTroopCountSelector(sourceTer.getTroopCount());
                    }
                    riskView.setMaxCountSelection(sourceTer.getTroopCount());

                    int troopsToAttack = riskView.getSelectedTroop();

                    riskView.setOnKeyPressed(e -> {
                        if( e.getCharacter().equalsIgnoreCase("A")
                                || e.getCharacter().equalsIgnoreCase("S")
                                || e.getCharacter().equalsIgnoreCase("D")) {
                            p1Choice = e.getCharacter().charAt(0);
                        }
                        if( e.getCharacter().equalsIgnoreCase("1")
                                || e.getCharacter().equalsIgnoreCase("2")
                                || e.getCharacter().equalsIgnoreCase("3")) {
                            p2Choice = e.getCharacter().charAt(0);
                        }
                    });

                    riskView.getAndSetAttackButton().setOnMouseClicked(e -> {
                        if( troopsToAttack > 0) {
                            int result = rpsGame.compare(p1Choice, p2Choice);

                            if( result == 1) { //player 1 wins
                                sourceTer.setTroopCount(sourceTer.getTroopCount() - 1);
                            }
                            else if( result == 2) {//player 2 wins
                                targetTerritory.setTroopCount(targetTerritory.getTroopCount() - 1);
                            }
                        }
                    });
                }

                if( sourceTer.getTroopCount() == 1) {
                    troopCountSelectorInView = false;
                    riskView.removeTroopCountSelector();

                    sourceTer = null;
                    targetTerritory = null;
                }
                else if( targetTerritory.getTroopCount() == 0) {

                }

                sourceTer = null;
                targetTerritory = null;
            }
        }
        //set key listeners here to then eliminate
        nextMode();
    }

    public void startFortify() {
        if( mode == GameMode.FortifyMode) {
            // returned from FortifyMode2
            //territory will both have to be non-null and match with the current player id
            if (sourceTer != null && sourceTer.getOwnerId() == curPlayer.getId()) {
                fortifyableFromSource = sourceTer.searchForFortifyable( new HashSet<>());
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
        System.out.println("Target: " + targetTerritory);
        if (targetTerritory == null || targetTerritory.equals(sourceTerritory)) {
            //reset the effects to the territory
            mode = GameMode.FortifyMode;
            targetTerritory = null; //if it is equal to the sourceTerritory, this will be useful
            sourceTer = null;
            return;
        } else if (targetTerritory != null &&  fortifyableFromSource.contains(targetTerritory)) {
            if(!troopCountSelectorInView)
                riskView.addTroopCountSelector(sourceTerritory.getTroopCount());
            troopCountSelectorInView = true;
            setButtons();

            int noOfTroops = riskView.getSelectedTroop();

            if( noOfTroops > 0) {
                sourceTer.setTroopCount(sourceTer.getTroopCount() - noOfTroops);
                targetTerritory.setTroopCount(targetTerritory.getTroopCount() + noOfTroops);
            }
        }
    }

    public void nextMode() {
        switch (mode) {
            case TerAllocationMode: {
                mode = GameMode.SoldierAllocationInit;
                break;
            }
            case SoldierAllocationInit:{
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
                System.out.println("To the end of turns");
                mode = GameMode.EndOfTurn;
                break;
            }
            case EndOfTurn: {
                System.out.println("End of turns");
                mode = GameMode.SoldierAllocationMode;
                break;
            }
        }
        riskView.setTerritoryMode(mode);
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
        curPlayer = players.get(curPlayerId);
        setTroopCountInView();
    }

    public void sendAllianceRequest(Player target) //to send an alliance req. with
    // source and target players
    {
        if(!players.get(curPlayerId).isAlly(target))
        //A player can send the request only in the SoldierAllocationMode, AttackMode, and FortifyMode
        {
            target.addAllianceReq(curPlayerId, players.get(curPlayerId).getName());
        }
    }

    public void cancelAlliance(Player target) //to cancel an existing alliance
    {
        if(mode == GameMode.SoldierAllocationMode) {
            if(players.get(this.curPlayerId).isAlly(target)) //if the parameters are true
            {
                //the alliance is removed from the both players
                players.get(this.curPlayerId).removeAlly(target.getId());
                target.removeAlly(this.curPlayerId);
            }
        }
    }
}