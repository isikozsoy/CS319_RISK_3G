public class RockPaperScissorsGame {
    public int[] play(char p1Choice, char p2Choice, int noOfP1Soldiers, int noOfP2Soldiers) {

        do {

            int winner = compare(p1Choice, p2Choice);

            if(winner == 0 && noOfP1Soldiers == 1 && noOfP2Soldiers == 1) {
                continue;
            }

            if (winner != 1) {
                noOfP1Soldiers--;
            }

            if (winner != 2) {
                noOfP2Soldiers--;
            }

            // game is played until one of the players have no soldiers
        } while (noOfP1Soldiers > 0 && noOfP2Soldiers > 0);

        int[] remainingTroops = {noOfP1Soldiers, noOfP2Soldiers};

        return remainingTroops;
    }

    public int compare(char p1Choice, char p2Choice)
    {
        // 'A' or 'a' or '1' = Rock
        // 'S' or 's' or '2' = Paper
        // 'D' or 'd' or '3' = Scissors
        Character.toUpperCase(p1Choice);
        if( (p1Choice == 'A' && p2Choice == '3')
                || (p1Choice == 'S' && p2Choice == '1')
                ||(p1Choice == 'D' && p2Choice == '2'))
            return 1; // Player 1 wins
        if((p1Choice == 'D' && p2Choice == '1')
                || (p1Choice == 'A' && p2Choice == '2')
                ||(p1Choice == 'S' && p2Choice == '3'))
            return 2; // Player 2 wins
        return 0; // Draw
    }

}
