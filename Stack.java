import java.util.ArrayList;
import java.util.Random;

public class Stack {
	public ArrayList<Card> stack = new ArrayList<Card>();
	private ArrayList<Card> backup = new ArrayList<Card>();
	
	public ArrayList<Card> setStock(ArrayList<Card> allCards, boolean isNew) {
		stack.clear();
		if (isNew) {
			Random rand = new Random();
			backup.clear();
			for (int i = 0; i < 24; i++) {
				addToStack(allCards.remove(rand.nextInt(allCards.size())), false, true);
			}
		} else {
			for (int i = 0; i < 24; i++) {
				addToStack(backup.get(i), false, false);
			}
		}
		return allCards;
	}
	
	public ArrayList<Card> setTableau(ArrayList<Card> allCards, int length, boolean isNew) {
		stack.clear();
		if (isNew) {
			Random rand = new Random();
			backup.clear();
			for (int i = 0; i < length-1; i++) {
				addToStack(allCards.remove(rand.nextInt(allCards.size())), false, true);
			}
			addToStack(allCards.remove(rand.nextInt(allCards.size())), true, true);
		} else {
			for (int i = 0; i < length-1; i++) {
				addToStack(backup.get(i), false, false);
			}
			addToStack(backup.get(length-1), true, false);
		}
		return allCards;
	}
	
	public void addToStack(Card card, boolean visibility, boolean addBackup) {
		card.isVisible = visibility;
		stack.add(card);
		if (addBackup) {
			backup.add(card);
		}
	}
	
	public void add(Card card) {
		stack.add(card);
	}
	
	public void add(int index, Card card) {
		stack.add(index, card);
	}
	
	public void clear() {
		stack.clear();
	}
	
	public Card get(int index) {
		return stack.get(index);
	}
	
	public Card getLast() {
		return stack.get(stack.size()-1);
	}
	
	public Card remove(int index) {
		return stack.remove(index);
	}
	
	public Card removeLast() {
		return stack.remove(stack.size()-1);
	}
	
	public void set(int index, Card card) {
		stack.set(index, card);
	}
	
	public int size() {
		return stack.size();
	}
}
