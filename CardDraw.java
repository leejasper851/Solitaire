import java.util.ArrayList;
import java.util.Arrays;

public class CardDraw {
	private static final String[] numbersArray = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
	private static final ArrayList<String> numbers = new ArrayList<String>(Arrays.asList(numbersArray));
	private String number;
	private String suit;
	private boolean isRed;
	public String[][] draw;
	
	public CardDraw(Card card) {
		number = card.number;
		suit = card.suit;
		isRed = card.isRed;
		setDraw();
	}
	
	private void setDraw() {
		if (numbers.indexOf(number) < 10) {
			draw = new String[7+numbers.indexOf(number)][4];
		} else {
			draw = new String[7][4];
		}
		String suitImage = Character.toUpperCase(suit.charAt(0)) + suit.substring(1) + ".PNG";
		setNumbersSuits(suitImage);
		int index = 6;
		index = setATo5(suitImage, index);
		set6ToK(suitImage, index);
	}
	
	private void setNumbersSuits(String suitImage) {
		if (isRed) {
			setDrawRow(0, "Red"+number+".PNG", "60", "15", "1.03");
			setDrawRow(1, "Red"+number+".PNG", "60", "165", "-1.03");
		} else {
			setDrawRow(0, "Black"+number+".PNG", "60", "15", "1.03");
			setDrawRow(1, "Black"+number+".PNG", "60", "165", "-1.03");
		}
		setDrawRow(2, suitImage, "12", "36", "0.55");
		setDrawRow(3, suitImage, "108", "36", "0.55");
		setDrawRow(4, suitImage, "12", "144", "-0.55");
		setDrawRow(5, suitImage, "108", "144", "-0.55");
	}
	
	private int setATo5(String suitImage, int index) {
		if (number.equals("A") || number.equals("3") || number.equals("5") || number.equals("9")) {
			setDrawRow(index++, suitImage, "60", "90", "1");
		}
		if (number.equals("2") || number.equals("3")) {
			setDrawRow(index++, suitImage, "60", "30", "1");
			setDrawRow(index, suitImage, "60", "150", "-1");
		}
		if (number.equals("4") || number.equals("5") || number.equals("6") || number.equals("7") || number.equals("8") || number.equals("9") || number.equals("10")) {
			setDrawRow(index++, suitImage, "38", "30", "1");
			setDrawRow(index++, suitImage, "82", "30", "1");
			setDrawRow(index++, suitImage, "38", "150", "-1");
			setDrawRow(index++, suitImage, "82", "150", "-1");
		}
		return index;
	}
	
	private void set6ToK(String suitImage, int index) {
		if (number.equals("6") || number.equals("7") || number.equals("8")) {
			setDrawRow(index++, suitImage, "38", "90", "1");
			setDrawRow(index++, suitImage, "82", "90", "1");
		}
		if (number.equals("7") || number.equals("8")) {
			setDrawRow(index++, suitImage, "60", "60", "1");
		}
		if (number.equals("8")) {
			setDrawRow(index, suitImage, "60", "120", "-1");
		}
		if (number.equals("9") || number.equals("10")) {
			setDrawRow(index++, suitImage, "38", "70", "1");
			setDrawRow(index++, suitImage, "82", "70", "1");
			setDrawRow(index++, suitImage, "38", "110", "-1");
			setDrawRow(index++, suitImage, "82", "110", "-1");
		}
		if (number.equals("10")) {
			setDrawRow(index++, suitImage, "60", "50", "1");
			setDrawRow(index, suitImage, "60", "130", "-1");
		}
		if (number.equals("J") || number.equals("Q") || number.equals("K")) {
			setDrawRow(index, number+suitImage, "60", "90", "1");
		}
	}
	
	private void setDrawRow(int index, String image, String x, String y, String widthHeight) {
		draw[index][0] = image;
		draw[index][1] = x;
		draw[index][2] = y;
		draw[index][3] = widthHeight;
	}
}
