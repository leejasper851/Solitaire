import java.util.ArrayList;
import java.util.Arrays;

public class SolitaireData {
	private static final String[] numbersArray = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
	private static final ArrayList<String> numbers = new ArrayList<String>(Arrays.asList(numbersArray));
	private static final String[] suits = {"clubs", "diamonds", "hearts", "spades"};
	public Stack stock = new Stack();
	public Stack waste = new Stack();
	public Stack[] foundations = new Stack[4];
	public Stack[] tableaus = new Stack[7];
	public Stack cursor = new Stack();
	public int cursorX;
	public int cursorY;
	private int cursorSpaceX;
	private int cursorSpaceY;
	private String cursorHome;
	public int[] tableauSpacing = new int[7];
	private ArrayList<String> fromMoves = new ArrayList<String>();
	private ArrayList<Double> numMoves = new ArrayList<Double>();
	private ArrayList<String> toMoves = new ArrayList<String>();
	private int movesIndex;
	public String state;
	
	public SolitaireData() {
		for (int i = 0; i < 4; i++) {
			foundations[i] = new Stack();
		}
		for (int i = 0; i < 7; i++) {
			tableaus[i] = new Stack();
		}
		init(true);
	}
	
	private void init(boolean isNew) {
		waste.clear();
		for (int i = 0; i < 4; i++) {
			foundations[i].clear();
		}
		fromMoves.clear();
		numMoves.clear();
		toMoves.clear();
		state = "play";
		movesIndex = -1;
		ArrayList<Card> allCards = new ArrayList<Card>();
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 4; j++) {
				allCards.add(new Card(numbers.get(i), suits[j]));
			}
		}
		allCards = stock.setStock(allCards, isNew);
		for (int i = 0; i < 7; i++) {
			allCards = tableaus[i].setTableau(allCards, i+1, isNew);
			tableauSpacing[i] = 30;
		}
	}
	
	public void stockClick(int x, int y) {
		if (x > 20 && x < 140 && y > 10 && y < 190) {
			if (stock.size() > 0) {
				waste.addToStack(stock.removeLast(), true, false);
				addMove("stock", 1, "waste");
			} else if (waste.size() > 0) {
				for (int i = waste.size()-1; i > -1; i--) {
					stock.addToStack(waste.get(i), false, false);
				}
				waste.clear();
				addMove("waste", stock.size(), "stock");
			}
		}
	}
	
	public void menuButtonClick(int x, int y) {/*
		g2d.fillRect(300, 12, 120, 40);
		g2d.fillRect(300, 57, 120, 40);
		g2d.fillRect(300, 102, 120, 40);
		g2d.fillRect(300, 147, 120, 40);*/
		if (x > 300 && x < 420 && y > 12 && y < 52) {
			init(true);
		}
		if (x > 300 && x < 420 && y > 57 && y < 97) {
			init(false);
		}
		if (x > 300 && x < 420 && y > 102 && y < 142 && movesIndex > -1 && state.equals("play")) {
			undo();
		}
		if (x > 300 && x < 420 && y > 147 && y < 187 && movesIndex < toMoves.size()-1) {
			redo();
		}
		if (x > 425 && x < 575 && y > 347 && y < 387 && state.equals("win")) {
			init(true);
		}
	}
	
	public void mousePress(int x, int y) {
		if (x > 160 && x < 280 && y > 10 && y < 190 && waste.size() > 0) {
			cursorAddOne(waste.removeLast(), x, y, 160, "waste");
		}
		for (int i = 0; i < 4; i++) {
			if (x > 440+140*i && x < 560+140*i && y > 10 && y < 190 && foundations[i].size() > 0) {
				cursorAddOne(foundations[i].removeLast(), x, y, 440+140*i, "foundation"+Integer.toString(i));
			}
		}
		for (int i = 0; i < 7; i++) {
			if (x > 20+140*i && x < 140+140*i && y > 200 && tableaus[i].size() > 0 && y < 380+(tableaus[i].size()-1)*tableauSpacing[i]) {
				cursorAddTableau(i, x, y, 20+140*i);
			}
		}
		cursorX = x - cursorSpaceX;
		cursorY = y - cursorSpaceY;
	}
	
	private void cursorAddOne(Card card, int x, int y, int startX, String stackString) {
		cursor.add(card);
		cursorSpaceX = x - startX;
		cursorSpaceY = y - 10;
		cursorHome = stackString;
	}
	
	private void cursorAddTableau(int tableauIndex, int x, int y, int startX) {
		int index = (y-200) / tableauSpacing[tableauIndex];
		if (index >= tableaus[tableauIndex].size()) {
			index = tableaus[tableauIndex].size()-1;
		}
		if (tableaus[tableauIndex].get(index).isVisible) {
			while (tableaus[tableauIndex].size() > index) {
				cursor.add(tableaus[tableauIndex].remove(index));
			}
			cursorSpaceX = x - startX;
			cursorSpaceY = y - (200+index*tableauSpacing[tableauIndex]);
			cursorHome = "tableau" + Integer.toString(tableauIndex);
			adjustTableauSpacing(tableauIndex);
		}
	}
	
	public void mouseDrag(int x, int y) {
		cursorX = x - cursorSpaceX;
		cursorY = y - cursorSpaceY;
	}
	
	public void mouseRelease(int x, int y) {
		if (cursor.size() > 0) {
			for (int i = 0; i < 4; i++) {
				if (x > 440+140*i && x < 560+140*i && y > 10 && y < 190 && cursor.size() == 1 && cursor.get(0).canFoundation(foundations[i])) {
					addMove(cursorHome, 1, "foundation"+Integer.toString(i));
					foundations[i].add(cursor.remove(0));
				}
			}
			for (int i = 0; i < 7; i++) {
				int maxY = 380 + (tableaus[i].size()-1)*tableauSpacing[i];
				if (tableaus[i].size() == 0) {
					maxY = 380;
				}
				if (x > 20+140*i && x < 140+140*i && y > 200 && y < maxY && cursor.get(0).canTableau(tableaus[i])) {
					addMove(cursorHome, cursor.size(), "tableau"+Integer.toString(i));
					cursorRemoveTableau(i);
				}
			}
			postMouseRelease();
		}
	}
	
	private void postMouseRelease() {
		if (cursor.size() > 0) {
			if (cursorHome.equals("waste")) {
				waste.add(cursor.remove(0));
			}
			for (int i = 0; i < 4; i++) {
				if (cursorHome.equals("foundation"+Integer.toString(i))) {
					foundations[i].add(cursor.remove(0));
				}
			}
			for (int i = 0; i < 7; i++) {
				if (cursorHome.equals("tableau"+Integer.toString(i))) {
					cursorRemoveTableau(i);
				}
			}
		} else {
			for (int i = 0; i < 7; i++) {
				if (cursorHome.equals("tableau"+Integer.toString(i))) {
					revealTopCard(i);
				}
			}
		}
		if (foundations[0].size() == 13 && foundations[1].size() == 13 && foundations[2].size() == 13 && foundations[3].size() == 13) {
			state = "win";
		}
	}
	
	private void cursorRemoveTableau(int tableauIndex) {
		while (cursor.size() > 0) {
			tableaus[tableauIndex].add(cursor.remove(0));
		}
		adjustTableauSpacing(tableauIndex);
	}
	
	private void revealTopCard(int tableauIndex) {
		if (tableaus[tableauIndex].size() > 0) {
			if (!tableaus[tableauIndex].getLast().isVisible) {
				numMoves.set(movesIndex, numMoves.get(movesIndex) + 0.5);
				tableaus[tableauIndex].getLast().isVisible = true;
			}
		}
	}
	
	private void addMove(String from, double num, String to) {
		while (fromMoves.size() > movesIndex+1) {
			fromMoves.remove(movesIndex+1);
			numMoves.remove(movesIndex+1);
			toMoves.remove(movesIndex+1);
		}
		fromMoves.add(from);
		numMoves.add(num);
		toMoves.add(to);
		movesIndex++;
	}
	
	private void undo() {
		ArrayList<Card> temp = new ArrayList<Card>();
		temp = getToMoves(temp);
		putFromMoves(temp);
		movesIndex--;
	}
	
	private ArrayList<Card> getToMoves(ArrayList<Card> temp) {
		if (toMoves.get(movesIndex).equals("stock")) {
			for (int i = 0; i < numMoves.get(movesIndex); i++) {
				temp.add(stock.removeLast());
			}
		}
		if (toMoves.get(movesIndex).equals("waste")) {
			temp.add(waste.removeLast());
		}
		for (int i = 0; i < 4; i++) {
			if (toMoves.get(movesIndex).equals("foundation"+Integer.toString(i))) {
				temp.add(foundations[i].removeLast());
			}
		}
		for (int i = 0; i < 7; i++) {
			if (toMoves.get(movesIndex).equals("tableau"+Integer.toString(i))) {
				for (int j = 0; j < numMoves.get(movesIndex)-0.6; j++) {
					temp.add(0, tableaus[i].removeLast());
				}
				adjustTableauSpacing(i);
			}
		}
		return temp;
	}
	
	private void putFromMoves(ArrayList<Card> temp) {
		if (fromMoves.get(movesIndex).equals("stock")) {
			stock.addToStack(temp.get(0), false, false);
		}
		if (fromMoves.get(movesIndex).equals("waste")) {
			for (int i = 0; i < temp.size(); i++) {
				waste.addToStack(temp.get(i), true, false);
			}
		}
		for (int i = 0; i < 4; i++) {
			if (fromMoves.get(movesIndex).equals("foundation"+Integer.toString(i))) {
				foundations[i].add(temp.get(0));
			}
		}
		for (int i = 0; i < 7; i++) {
			if (fromMoves.get(movesIndex).equals("tableau"+Integer.toString(i))) {
				if (numMoves.get(movesIndex) != Math.floor(numMoves.get(movesIndex))) {
					tableaus[i].getLast().isVisible = false;
				}
				for (int j = 0; j < temp.size(); j++) {
					tableaus[i].add(temp.get(j));
				}
				adjustTableauSpacing(i);
			}
		}
	}
	
	private void redo() {
		ArrayList<Card> temp = new ArrayList<Card>();
		movesIndex++;
		temp = getFromMoves(temp);
		putToMoves(temp);
	}
	
	private ArrayList<Card> getFromMoves(ArrayList<Card> temp) {
		if (fromMoves.get(movesIndex).equals("stock")) {
			temp.add(stock.removeLast());
		}
		if (fromMoves.get(movesIndex).equals("waste")) {
			for (int i = 0; i < numMoves.get(movesIndex); i++) {
				temp.add(waste.removeLast());
			}
		}
		for (int i = 0; i < 4; i++) {
			if (fromMoves.get(movesIndex).equals("foundation"+Integer.toString(i))) {
				temp.add(foundations[i].removeLast());
			}
		}
		for (int i = 0; i < 7; i++) {
			if (fromMoves.get(movesIndex).equals("tableau"+Integer.toString(i))) {
				for (int j = 0; j < numMoves.get(movesIndex)-0.6; j++) {
					temp.add(0, tableaus[i].removeLast());
				}
				if (numMoves.get(movesIndex) != Math.floor(numMoves.get(movesIndex))) {
					tableaus[i].getLast().isVisible = true;
				}
				adjustTableauSpacing(i);
			}
		}
		return temp;
	}
	
	private void putToMoves(ArrayList<Card> temp) {
		if (toMoves.get(movesIndex).equals("stock")) {
			for (int i = 0; i < temp.size(); i++) {
				stock.addToStack(temp.get(i), false, false);
			}
		}
		if (toMoves.get(movesIndex).equals("waste")) {
			waste.addToStack(temp.get(0), true, false);
		}
		for (int i = 0; i < 4; i++) {
			if (toMoves.get(movesIndex).equals("foundation"+Integer.toString(i))) {
				foundations[i].add(temp.get(0));
			}
		}
		for (int i = 0; i < 7; i++) {
			if (toMoves.get(movesIndex).equals("tableau"+Integer.toString(i))) {
				for (int j = 0; j < temp.size(); j++) {
					tableaus[i].add(temp.get(j));
				}
				adjustTableauSpacing(i);
			}
		}
	}
	
	private void adjustTableauSpacing(int tableauIndex) {
		if (tableaus[tableauIndex].size() > 11) {
			tableauSpacing[tableauIndex] = 310 / (tableaus[tableauIndex].size()-1);
		} else {
			tableauSpacing[tableauIndex] = 30;
		}
	}
}
