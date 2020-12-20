import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

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
    private HashSet<Territory> attackableTerritories;

    private ArrayList<Continent> continents;

    private boolean troopCountSelectorInView = false;
    RockPaperScissorsGame rpsGame;
    // Continent List
    enum GameMode {
        TerAllocationMode, SoldierAllocationInit, SoldierAllocationMode, AttackMode, FortifyMode, EndOfTurn
    }
    private GameMode mode = GameMode.TerAllocationMode;

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
        cards = new Cards();
        isGameOver = false;
        mode = GameMode.TerAllocationMode;

        rpsGame = new RockPaperScissorsGame();

        setContinents();
        //riskView.setTerritoryColor(players.get(0).getColor());
        riskView.addTroopsLeft(players.get(0));
        setButtons();
        setClickableTerritories();

        play();
    }

    public void setContinents() {
        continents = new ArrayList<>();

        ArrayList<Territory> northAmericaList = new ArrayList<>();
        ArrayList<Territory> southAmericaList = new ArrayList<>();
        ArrayList<Territory> europeList = new ArrayList<>();
        ArrayList<Territory> africaList = new ArrayList<>();
        ArrayList<Territory> asiaList = new ArrayList<>();
        ArrayList<Territory> australiaList = new ArrayList<>();

        for( Territory territory: territories) {
            switch (territory.getName()) {
                case "Alaska":
                case "Northwest Territory":
                case "Alberta":
                case "Ontario":
                case "Quebec":
                case "Greenland":
                case "West America":
                case "East America":
                case "Central America": {
                    northAmericaList.add(territory);
                    break;
                }
                case "Venezuela":
                case "Brazil":
                case "Peru":
                case "Argentina": {
                    southAmericaList.add(territory);
                    break;
                }
                case "North Africa":
                case "South Africa":
                case "East Africa":
                case "Congo":
                case "Egypt": {
                    africaList.add(territory);
                    break;
                }
                case "Middle East":
                case "Afghanistan":
                case "India":
                case "Siam":
                case "China":
                case "Mongolia":
                case "Irkutsk":
                case "Ural":
                case "Siberia":
                case "Yakutsk":
                case "Kamchatka": {
                    asiaList.add(territory);
                    break;
                }
                case "Ukraine":
                case "Scandinavia":
                case "NE":
                case "SE":
                case "WE":
                case "Britain":
                case "Iceland": {
                    europeList.add(territory);
                    break;
                }
                case "Indonesia":
                case "New Guinea":
                case "Western Australia":
                case "Eastern Australia": {
                    australiaList.add(territory);
                    break;
                }
            }
        }

        System.out.printf("Continent territory count: %d \n", (northAmericaList.size() + southAmericaList.size() + europeList.size() + africaList.size() + asiaList.size() + australiaList.size()));

        Continent northAmerica = new Continent("North America", 5, northAmericaList);
        Continent southAmerica = new Continent("South America", 2, southAmericaList);
        Continent europe = new Continent("Europe", 5, europeList);
        Continent africa = new Continent("Africa", 3, africaList);
        Continent asia = new Continent("Asia", 7, asiaList);
        Continent australia = new Continent("Australia", 2, australiaList);

        continents.add(northAmerica);
        continents.add(southAmerica);
        continents.add(africa);
        continents.add(asia);
        continents.add(australia);
        continents.add(europe);
    }

    public int getCurPlayerId() {
        return curPlayerId;
    }

    public void setTroopCountInView() {
        riskView.addTroopsLeft(curPlayer);
    }

    public void play() {
        curPlayer = players.get(curPlayerId);
        riskView.updateCurPhase();
        riskView.setOnMouseClicked(e -> {
            executeFunctions();
        });
    }

    public void executeFunctions() {
        riskView.updateCurPhase();
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
            case FortifyMode: {
                startFortify();
                break;
            }
            case EndOfTurn: {
                nextTurn();
                nextMode();
                break;
            }
        }
    }

    public void checkForContinent( Player player) {
        //checks for continents one by one

        for( int i = 0; i < continents.size(); i++) {
            //checks for which continent this territory belongs to and eliminates the continent if the player doesn't have it
            List<Territory> territoryList = continents.get(i).getTerritories();
            boolean playerOwnsContinent = true;

            for( int j = 0; j < territoryList.size(); j++) {
                if( territoryList.get(i).getOwnerId() != getCurPlayerId()) {
                    playerOwnsContinent = false;
                    break;
                }
            }

            if( playerOwnsContinent) {
                player.setTroopCount( player.getTroopCount() + continents.get(i).getBonusTroopCount());
                if( continents.get(i).getName().equals( player.getTargetCont())) {
                    player.setTroopCount(player.getTroopCount() + 10);
                }
            }
        }
    }

    public void setClickableTerritories() {
        for( ClickableTerritory clickableTerritory: clickableTerritories) {
            clickableTerritory.setOnMouseClicked(e -> {

                if(mode == GameMode.AttackMode) {

                    if (clickableTerritory.getAssociatedTerritory().getOwnerId() == curPlayerId &&
                            clickableTerritory.getAssociatedTerritory().getTroopCount() > 1) {
                        //Source territory is clicked.

                        if(attackableTerritories != null) {
                            for (Territory attackableTerritory: attackableTerritories) {
                                ClickableTerritory clickAbleAttackableTerritory =
                                        clickableTerritories.get(attackableTerritory.getId());
                                clickAbleAttackableTerritory.setColor(attackableTerritory.getOwner().getColor());
                            }
                        }

                        sourceTer = territories[clickableTerritory.getTerritoryId()];
                        attackableTerritories = sourceTer.searchForAttackable();

                        for (Territory attackableTerritory: attackableTerritories) {
                            ClickableTerritory clickAbleAttackableTerritory =
                                    clickableTerritories.get(attackableTerritory.getId());
                            clickAbleAttackableTerritory.setColor("x");
                        }

                    } else if (sourceTer != null && attackableTerritories.contains(territories[clickableTerritory.getTerritoryId()])) {
                        targetTerritory = clickableTerritory.getAssociatedTerritory();
                        for (Territory attackableTerritory: attackableTerritories) {
                            if(attackableTerritory == targetTerritory) {
                                continue;
                            }
                            ClickableTerritory clickAbleAttackableTerritory =
                                    clickableTerritories.get(attackableTerritory.getId());
                            clickAbleAttackableTerritory.setColor(attackableTerritory.getOwner().getColor());
                        }
                        startAttack();
                    }

                }
                else if( sourceTer == null) {
                    if( !(mode == GameMode.TerAllocationMode //so that when it is territory allocation phase, it will click on a territory only once and never again
                            && clickedTerritories.contains(clickableTerritory.getAssociatedTerritory())))
                        sourceTer = clickableTerritory.getAssociatedTerritory();
                }
                else if( targetTerritory == null) {
                    targetTerritory = clickableTerritory.getAssociatedTerritory();
                }
            });
        }
    }

    public void setButtons() {
        riskView.getTroopCountSelectorPane().getNumButton().setOnMouseClicked(e -> {
            if(mode == GameMode.SoldierAllocationInit
                    || mode == GameMode.SoldierAllocationMode) {
                //sets selected troop integer, and places it in the territory specified itself
                int selectedTroop = riskView.getSelectedTroop();
                if(sourceTer.getTroopCount() == 0) {
                    sourceTer.setOwner(curPlayer);
                    (clickableTerritories.get(sourceTer.getId())).setColor(curPlayer.getColor());
                }
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
                    sourceTer = null;
                    targetTerritory = null;

                    playerCounterForEachTurn++;

                    if (playerCounterForEachTurn == playerCount && !gameStarted) {
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
                        //riskView.addNextPhaseButton();
                    }
                }
                riskView.updateTroopsCount(curPlayer);
            }
            else if(mode == GameMode.AttackMode) {

                int selectedTroop = riskView.getSelectedTroop();
                sourceTer.setTroopCount(sourceTer.getTroopCount() - selectedTroop);
                riskView.setOnKeyPressed(new RPSGame(selectedTroop));
                riskView.displayRPSView();
            }
            else if( mode == GameMode.FortifyMode) {
                //troopToTransfer will also be got from the troop count selection screen
                int troopToTransfer = riskView.getSelectedTroop();
                if (troopToTransfer != 0) {
                    //new troop counts will be set to each territory
                    sourceTer.setTroopCount(sourceTer.getTroopCount() - troopToTransfer);
                    targetTerritory.setTroopCount(targetTerritory.getTroopCount() + troopToTransfer);
                    //update their texts in riskview
                    riskView.updateText(sourceTer, sourceTer.getTroopCount(), sourceTer.hasAirport());
                    riskView.updateText(targetTerritory, targetTerritory.getTroopCount(), targetTerritory.hasAirport());

                    targetTerritory = null;
                    sourceTer = null;
                }

                riskView.removeTroopCountSelector();
                troopCountSelectorInView = false;
            }
        });

        riskView.getTroopCountSelectorPane().getBackButton().setOnMouseClicked(event -> {
            if(mode == GameMode.SoldierAllocationInit
                    || mode == GameMode.SoldierAllocationMode) {
                //goes back to the original Soldier Allocation function to take a territory as input again
                riskView.removeTroopCountSelector();
                troopCountSelectorInView = false;
                soldierAllocBeforeClicking = false;
                sourceTer = null;
                //mode does not change here
            }
            else if( mode == GameMode.FortifyMode) {
                riskView.removeTroopCountSelector();
                troopCountSelectorInView = false;
                sourceTer = null;
                targetTerritory = null;
            }
            else if( mode == GameMode.AttackMode) {
                riskView.removeTroopCountSelector();
                //targetTerritory = null;
            }
        });

        riskView.getTroopCountSelectorPane().getBuildAirportButton().setOnMouseClicked(e -> {
            if (!sourceTer.hasAirport()) {
                if (sourceTer.getTroopCount() > AIRPORT_COST) {
                    sourceTer.setTroopCount(sourceTer.getTroopCount() - AIRPORT_COST);

                    riskView.updateText(sourceTer, sourceTer.getTroopCount(), true);
                    riskView.removeTroopCountSelector();

                    troopCountSelectorInView = false;

                    sourceTer.setHasAirport(true);
                    Territory sourceTerritory = new AirportDecorator(sourceTer);
                    territories[sourceTer.getId()] = sourceTerritory;
                    //clickableTerritories.get(sourceTer.getId()).setAssociatedTerritory(sourceTerritory);
                    //reset source territory for a new allocation
                    sourceTer = null;
                    soldierAllocBeforeClicking = false;

                    //riskView.getTroopCountSelectorPane().getBuildAirportButton().setText("Remove Airport");

                    riskView.removeTroopCountSelector();
                    troopCountSelectorInView = false;
                }
            }
            else {
                sourceTer.setTroopCount(sourceTer.getTroopCount() + 2);
                riskView.updateText(sourceTer, sourceTer.getTroopCount(), false);

                Territory sourceTerritory = sourceTer.getTerritory();
                sourceTerritory.setHasAirport(false);
                territories[sourceTer.getId()] = sourceTerritory;
                //clickableTerritories.get(sourceTer.getId()).setAssociatedTerritory(sourceTerritory);

                //reset source territory for a new allocation
                sourceTer = null;
                soldierAllocBeforeClicking = false;
                //riskView.getTroopCountSelectorPane().getBuildAirportButton().setText("Build Airport");
                riskView.removeTroopCountSelector();

                troopCountSelectorInView = false;
            }
        });
    }

    public void startInitialization() {
        //startTerAlloc();
        startAutoTerAlloc();
    }

    public void startAutoTerAlloc() {
        if (mode == GameMode.TerAllocationMode) {
            riskView.getCardsButton().setOnMouseClicked(e -> {
                riskView.addCardExchangePane();
                riskView.setCardExchangeInfo(players.get(curPlayerId));
            });

            for (Territory territory : territories) {
                clickedTerritories.add(territory);
                riskView.addTextForTerritory(territory);
                territory.setOwner(curPlayer);
                territory.addTroop(1);

                curPlayer.decreaseTroop(1);
                curPlayer.setTerCount(curPlayer.getTerCount() + 1);

                riskView.addTroopsLeft(players.get(curPlayerId));
                clickableTerritories.get(territory.getId()).setColor(curPlayer.getColor());
                nextTurn();

                tempTerCount--;

                if (tempTerCount <= 0) {
                    //Starts the initial soldier allocation
                    riskView.setTerritoryClicked(false);
                    riskView.setTerritoryMode(mode);
                    curPlayerId = 0;
                    riskView.updatePlayerBar(players.get(curPlayerId));
                    setTroopCountInView();
                    //riskView.addNextPhaseButton();
                    riskView.getNextPhaseButton().setOnMouseClicked(event -> {
                        if (mode == GameMode.AttackMode || mode == GameMode.FortifyMode) {
                            riskView.goToNextPhase();
                            if(mode == GameMode.FortifyMode) {
                                riskView.removeNextPhaseButton();
                            }
                        }
                        nextMode();
                        setTroopCountInView();
                        executeFunctions();
                    });
                    nextMode();
                    riskView.updateCurPhase();
                    break;
                }

            }
        }
    }

    public void startTerAlloc() {
        if( mode == GameMode.TerAllocationMode) {
            riskView.getCardsButton().setOnMouseClicked(e -> {
                riskView.addCardExchangePane();
                riskView.setCardExchangeInfo(players.get(curPlayerId));
            });

            //check which territory was clicked for
            if (sourceTer != null && sourceTer.getOwnerId() == -1) {
                clickedTerritories.add(sourceTer);
                riskView.addTextForTerritory(sourceTer);
                sourceTer.setOwner(curPlayer);
                sourceTer.addTroop(1);

                curPlayer.decreaseTroop(1);
                curPlayer.setTerCount(curPlayer.getTerCount() + 1);

                riskView.addTroopsLeft(players.get(curPlayerId));
                clickableTerritories.get(sourceTer.getId()).setColor(curPlayer.getColor());

                nextTurn();

                //riskView.setTerritoryColor(curPlayer.getColor());
                sourceTer = null;

                tempTerCount--;
            }
            if (tempTerCount <= 0) {
                //Starts the initial soldier allocation
                riskView.setTerritoryClicked(false);
                riskView.setTerritoryMode(mode);
                curPlayerId = 0;
                riskView.updatePlayerBar(players.get(curPlayerId));
                setTroopCountInView();
                //riskView.addNextPhaseButton();
                riskView.getNextPhaseButton().setOnMouseClicked( event -> {
                    if (mode == GameMode.AttackMode || mode == GameMode.FortifyMode) {
                        riskView.goToNextPhase();
                        if(mode == GameMode.FortifyMode) {
                            riskView.removeNextPhaseButton();
                        }
                    }
                    nextMode();
                    setTroopCountInView();
                    executeFunctions();
                });
                nextMode();
                riskView.updateCurPhase();
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
                if (sourceTer != null && (sourceTer.getOwnerId() == curPlayer.getId() || sourceTer.getTroopCount() == 0)
                        && !troopCountSelectorInView) { //So that duplicates do not occur
                    troopCountSelectorInView = true;
                    riskView.addTroopCountSelector(curPlayer.getTroopCount(), mode);
                    if(sourceTer.hasAirport())
                        riskView.getTroopCountSelectorPane().getBuildAirportButton().setText("Remove Airport");
                    else
                        riskView.getTroopCountSelectorPane().getBuildAirportButton().setText("Build Airport");
                    setButtons();
                    setTroopCountInView();
                }
                else
                    sourceTer = null;
                riskView.addTroopsLeft(curPlayer);
            }
        }
    }

    public void startAttack() {
        if(mode != GameMode.AttackMode) {
            return;
        }

        if( !troopCountSelectorInView)
        {
            troopCountSelectorInView = true;
            riskView.addTroopCountSelector(sourceTer.getTroopCount() - 1, mode);
            troopCountSelectorInView = false;
        }

        riskView.getTroopCountSelectorPane().getNumButton().setOnMouseClicked(e -> {

                int selectedTroop = riskView.getSelectedTroop();

                int sourceTroopCount = sourceTer.getTroopCount();

                if(sourceTroopCount <= selectedTroop || sourceTroopCount < 2) {
                    return;
                }
                sourceTer.setTroopCount(sourceTroopCount - selectedTroop);
                riskView.setOnKeyPressed(new RPSGame(selectedTroop));
                riskView.displayRPSView();
        });

    }

    public void startFortify() {
        if( mode == GameMode.FortifyMode) {
            // returned from FortifyMode2
            //territory will both have to be non-null and match with the current player id
            if( sourceTer != null && sourceTer.getTroopCount() <= 0) sourceTer = null;

            if( sourceTer != null && targetTerritory != null && sourceTer.getTroopCount() > 0 && sourceTer.getOwnerId() == curPlayer.getId()) {
                fortifyableFromSource = sourceTer.searchForFortifyable( new HashSet<>());
                //check for the second territory clicked
                //if it is the first one, territory will be unclicked
                //if it is another territory that is not null, troop count screen will show up
                //territory will both have to be non-null and match with the current player id
                //riskView.setTerritoryClicked(false);

                if (targetTerritory.equals(sourceTer)) {
                    //reset the effects to the territory
                    mode = GameMode.FortifyMode;
                    targetTerritory = null; //if it is equal to the sourceTerritory, this will be useful
                    sourceTer = null;
                    return;
                } else if (fortifyableFromSource.contains(targetTerritory)) {
                    if(!troopCountSelectorInView) {
                        riskView.addTroopCountSelector(sourceTer.getTroopCount(), mode);
                    }

                    troopCountSelectorInView = true;

                    setButtons();
                }
            }

            if( sourceTer != null && sourceTer.getOwnerId() != curPlayerId) {
                sourceTer = null;
                targetTerritory = null;
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
                mode = GameMode.AttackMode;
                sourceTer = null;
                targetTerritory = null;
                riskView.addNextPhaseButton();
                break;
            }
            case AttackMode: {
                if (attackableTerritories != null) {
                    for (Territory attackableTerritory : attackableTerritories) {
                        ClickableTerritory clickAbleAttackableTerritory =
                                clickableTerritories.get(attackableTerritory.getId());
                        clickAbleAttackableTerritory.setColor(attackableTerritory.getOwner().getColor());
                    }
                }
                mode = GameMode.FortifyMode;
                break;
            }
            case FortifyMode: {
                update(curPlayer); //do this at the end of each turn
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

    public void giveCardToCurPlayer() {
        if( curPlayer.isCardDeserved()) {
            Card cardToGive = cards.drawCard();
            curPlayer.addCard( cardToGive);
            curPlayer.setCardDeserved(false);
        }
    }

    public void update( Player player) {
        giveCardToCurPlayer();
        checkForContinent(curPlayer);
    }

    public ArrayList<Player> getPlayers()
    {
        return (ArrayList<Player>) players;
    }

    public void nextTurn() {
        curPlayerId = (curPlayerId + 1) % playerCount;
        curPlayer = players.get(curPlayerId);
        while( curPlayer.getId() == -1) {
            curPlayerId = (curPlayerId + 1) % playerCount;
            curPlayer = players.get(curPlayerId);
        }
        setTroopCountInView();
    }

    public GameMode getMode()
    {
        return mode;
    }

    public void handleNextPhaseButton()
    {
        riskView.getNextPhaseButton().setOnMouseClicked(e->{
            if(mode == GameMode.AttackMode)
            {
                mode = GameMode.FortifyMode;
            }
            else if(mode == GameMode.FortifyMode)
            {
                mode = GameMode.AttackMode;
            }
        });
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
        if(players.get(this.curPlayerId).isAlly(target)) //if the parameters are true
        {
            //the alliance is removed from the both players
            players.get(this.curPlayerId).removeAlly(target.getId());
            target.removeAlly(this.curPlayerId);
        }
    }

    class RPSGame implements EventHandler<KeyEvent> {

        private char p1Choice;
        private char p2Choice;
        private boolean p1Chose;
        private boolean p2Chose;
        private int noOfP1Soldiers;
        private int noOfP2Soldiers;
        private boolean gameOver;
        private RPSView rpsView;

        public RPSGame(int noOfP1Soldiers){
            this.noOfP1Soldiers = noOfP1Soldiers;
            this.noOfP2Soldiers = targetTerritory.getTroopCount();
            this.rpsView = riskView.getRpsView();
            rpsView.updateTroopCounts(noOfP1Soldiers, noOfP2Soldiers);
        }

        @Override
        public void handle(KeyEvent keyEvent) {
            if(gameOver || keyEvent.getText().length() < 1) {
                return;
            }
            char choice = keyEvent.getText().charAt(0);
            if(!p1Chose && (choice == 'a' || choice == 's' || choice == 'd')) {
                p1Choice = choice;
                p1Chose = true;
            }
            else if(!p2Chose && (choice == '1' || choice == '2' || choice == '3')) {
                p2Choice = choice;
                p2Chose = true;
            }

            if(p1Chose && p2Chose) {
                reset();
                playRPS();
            }
        }

        public void playRPS(){

            int winner = compare();

            //Align will be fixed.
            //rpsView.updateWinner(winner);

            if(winner == 0 && noOfP1Soldiers == 1 && noOfP2Soldiers == 1) {
                reset();
                return;
            }

            if (winner != 1) {
                noOfP1Soldiers--;
            }

            if (winner != 2) {
                noOfP2Soldiers--;
            }

            rpsView.updateTroopCounts(noOfP1Soldiers, noOfP2Soldiers);

            if(noOfP1Soldiers > 0 && noOfP2Soldiers > 0) {
                reset();
                return;
            }

            gameOver = true;
            applyGameResult();
        }

        public int compare()
        {
            // 'a' or '1' = Rock
            // 's' or '2' = Paper
            // 'd' or '3' = Scissors

            if( (p1Choice == 'a' && p2Choice == '3')
                    || (p1Choice == 's' && p2Choice == '1')
                    ||(p1Choice == 'd' && p2Choice == '2'))
                return 1; // Player 1 wins
            if((p1Choice == 'd' && p2Choice == '1')
                    || (p1Choice == 'a' && p2Choice == '2')
                    ||(p1Choice == 's' && p2Choice == '3'))
                return 2; // Player 2 wins
            return 0; // Draw
        }

        public void reset() {
            p1Chose = false;
            p2Chose = false;
        }

        public void applyGameResult() {
            ClickableTerritory clickableSource = clickableTerritories.get(sourceTer.getId());
            ClickableTerritory clickableTarget = clickableTerritories.get(targetTerritory.getId());

            Player otherPlayer = null;

            if(noOfP2Soldiers == 0) {
                //The target has been occupied.
                targetTerritory.setTroopCount(noOfP1Soldiers);
                otherPlayer = targetTerritory.getOwner();
                otherPlayer.setTerCount(targetTerritory.getOwner().getTerCount() - 1);
                targetTerritory.setOwner(players.get(curPlayerId));
                curPlayer.setTerCount( curPlayer.getTerCount() + 1);
                attackableTerritories.remove(targetTerritory);

                players.get(curPlayerId).setCardDeserved(true);
            }
            else {
                targetTerritory.setTroopCount(noOfP2Soldiers);
            }

            clickableSource.setColor(clickableSource.getAssociatedTerritory().getOwner().getColor());
            clickableTarget.setColor(clickableTarget.getAssociatedTerritory().getOwner().getColor());
            riskView.updateText(targetTerritory, targetTerritory.getTroopCount());
            riskView.updateText(sourceTer, sourceTer.getTroopCount());


            riskView.removeRPSView();

            //check for endgame and player removal
            if( curPlayer.getTerCount() == 42) {
                endGame();
            }
            else if( otherPlayer != null && otherPlayer.getTerCount() == 0) { //the other player will be removed from the game
                removePlayer(otherPlayer);
            }

            sourceTer = null;
            targetTerritory = null;
            riskView.removeEventHandler(KeyEvent.ANY, this);
        }
    }

    public void removePlayer( Player player) {
        riskView.changeNameButtonColor(player);
        player.makePlayerNonExistent();
    }

    public void endGame() {
        //reset everything
        players = null;
        clickableTerritories = null;
        clickedTerritories = null;
        territories = null;
        attackableTerritories = null;

        //add an endgame panel
        riskView.addEndGamePane( curPlayer);
    }
}