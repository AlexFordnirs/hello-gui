package org.itstep.blackjack;

import lombok.extern.slf4j.Slf4j;
import org.itstep.blackjack.card.Card;
import org.itstep.blackjack.event.GameEventListener;
import org.itstep.ui.CardView;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

@Slf4j
public class Game {
    public static final int TWENTY_ONE = 21;
    public static final int THRESHOLD = 19;

    private final Player player;
    private final Player dealer;
    private final Deck deck;
    public  int money;
    public Game() {
        deck = new Deck();
        dealer = new Player(0);
        player = new Player(1000);
        eventListeners = new ArrayList<>();
    }


    public void hit() {
        Card card = deck.getOne();
        player.takeCard(card);
        log.info("Player take a card {}", card);
        // TODO: вызвать publishPlayerTakeCard
        publishPlayerTakeCard(card);
        if (player.getPoints() > TWENTY_ONE) {
            dealer.getCards().get(0).setHide(false);
            // TODO: вызвать publishGameOver
            log.info("Game over. Win {}", getWinner());
        publishGameOver(getWinner());
        }
    }

    public void stand() {
        dealer.getCards().get(0).setHide(false);
        // TODO: вызвать publishStand
        log.info("Stand");
        publishStand();
        while (dealer.getPoints() < THRESHOLD) {
            Card card = deck.getOne();
            dealer.takeCard(card);
            // TODO: вызвать publishDealerTakeCard
            log.info("Dealer take a card {}", card);
            publishDealerTakeCard(card);
        }
        if(getWinner().equals("Player")){ player.AppdateMoney(money*2); playerSetBet(player.getCash());}
        // TODO: вызвать publishGameOver(getWinner());
        log.info("Game over. Win {}", getWinner());
        publishGameOver(getWinner());
    }

    public void setBet(int amount) throws NoMoneyEnough {
        player.setBet(amount);
        // TODO: реализовать логику добавления ставки
        money=amount;
        playerSetBet(player.getCash());
    }

    public String getWinner() {
        if (player.getPoints() <= TWENTY_ONE) {
            if (dealer.getPoints() > TWENTY_ONE) {
                return "Player";
            } else if (player.getPoints() > dealer.getPoints()) {
                return "Player";
            } else {
                return "Dealer";
            }
        }
        return "Dealer";
    }

   private final List<GameEventListener> eventListeners;
    public void addGameEventListener(GameEventListener eventListener){
        eventListeners.add(eventListener);
    }

    private void publishStand(){
        eventListeners.forEach(GameEventListener::stand);
    }
    private void publishStart(){
        eventListeners.forEach(GameEventListener::gameStart);
    }
    private void publishGameOver(String winner){
        eventListeners.forEach(l->l.gameOver(winner,player.getPoints(),dealer.getPoints()));
    }
    private void publishPlayerTakeCard(Card card){
        eventListeners.forEach(l->l.playerGetCard(card,player.getPoints()));
    }
    private void publishDealerTakeCard(Card card){
        eventListeners.forEach(l->l.dealerGetCard(card,dealer.getPoints()));
    }
    private void playerSetBet(int amount){
        eventListeners.forEach(l -> l.playerSetBet(amount));
    }

    public void play() {
        deck.shuffle();
        player.clear();
        dealer.clear();
        // TODO: вызвать publishStart();
        log.info("Play game");
        publishStart();
        Card firstCard = deck.getOne();
        player.takeCard(firstCard);
        // TODO: вызвать publishPlayerTakeCard
        publishPlayerTakeCard(firstCard);
        log.info("Player take first card {}", firstCard);
        Card second = deck.getOne();
        player.takeCard(second);
        // TODO: вызвать publishPlayerTakeCard
        log.info("Player take second card {}", second);
        publishPlayerTakeCard(second);
        dealer.clear();
        Card hiddenCard = deck.getOne();
        hiddenCard.setHide(true);
        dealer.takeCard(hiddenCard);
        // TODO: вызвать publishDealerTakeCard
        log.info("Dealer take hidden card {}", hiddenCard);
        publishDealerTakeCard(hiddenCard);
        Card lastCard = deck.getOne();
        dealer.takeCard(lastCard);
        // TODO: вызвать publishDealerTakeCard
        log.info("Dealer take second card {}", lastCard);
        publishDealerTakeCard(lastCard);
    }

}
