package com.github.solutetaire;

import java.util.ArrayList;
import java.util.Collections;

public class CardCollection {
    private ArrayList<Card> cards = new ArrayList<>();

    public CardCollection() {
    }

    public CardCollection(boolean full) {
        // If requested to be full, adds every single card in
        if (full) {
            for (char suit : new char[] {'h', 'd', 's', 'c'}) {
                for (int rank = 1; rank <= 13; rank++) {
                    cards.add(new Card(suit, rank, false));
                }
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card getCard(int cardIndex) {
        return cards.get(cardIndex);
    }

    public int getSize() {
        return cards.size();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void removeCard(int cardIndex) {
        cards.remove(cardIndex);
    }

    public Card getLastCard() {
        return getCard(cards.size() - 1);
    }

    public Card popLastCard() {
        Card card = getCard(cards.size() - 1);
        removeCard(cards.size() - 1);
        return card;
    }
}
