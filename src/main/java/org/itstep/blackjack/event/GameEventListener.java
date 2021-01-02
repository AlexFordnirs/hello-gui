package org.itstep.blackjack.event;

import org.itstep.blackjack.card.Card;

public interface GameEventListener {
    void gameStart();
    void stand();
    void playerGetCard(Card card , int point);
    void dealerGetCard(Card card , int point);
    void playerSetBet(int amount);
    void gameOver(String winner,int playerPoints,int dealerPoints);


}
