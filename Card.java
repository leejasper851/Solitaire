import java.util.ArrayList;
import java.util.Arrays;

public class Card {
	private static final String[] numbersArray = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
	private static final ArrayList<String> numbers = new ArrayList<String>(Arrays.asList(numbersArray));
	public String number;
	public String suit;
	public boolean isRed;
	public boolean isVisible;
	public CardDraw cardDraw;
	
	public Card(String number, String suit) {
		this.number = number;
		this.suit = suit;
		isRed = (suit.equals("diamonds") || suit.equals("hearts"));
		cardDraw = new CardDraw(this);
	}
	
	public boolean canFoundation(Stack foundation) {
		if (foundation.size() == 0) {
			return (number.equals("A"));
		}
		Card topCard = foundation.getLast();
		return (numbers.indexOf(number)-1 == numbers.indexOf(topCard.number) && suit.equals(topCard.suit));
	}
	
	public boolean canTableau(Stack tableau) {
		if (tableau.size() == 0) {
			return (number.equals("K"));
		}
		Card topCard = tableau.getLast();
		return (numbers.indexOf(number)+1 == numbers.indexOf(topCard.number) && isRed != topCard.isRed);
	}
}
