import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Surface extends JPanel {
	public SolitaireData solitaireData = new SolitaireData();
	
	public Surface(SolitaireMouseAdapter mouseAdapter) {
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		repaint();
	}
	
	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setPaint(new Color(0, 100, 0));
		g2d.fillRect(0, 0, 1000, 700);
		g2d.setPaint(new Color(0, 200, 0));
		for (int i = 0; i < 7; i++) {
			if (i != 2) {
				g2d.fillRoundRect(i*140+20, 10, 120, 180, 10, 10);
			}
			g2d.fillRoundRect(i*140+20, 200, 120, 180, 10, 10);
		}
		g2d.setStroke(new BasicStroke(2));
		drawStockWasteMenuFoundation(g2d);
		drawTableauCursorWin(g2d);
		g2d.dispose();
	}
	
	private void drawStockWasteMenuFoundation(Graphics2D g2d) {
		if (solitaireData.stock.size() > 0) {
			drawCard(g2d, 20, 10, false);
		}
		if (solitaireData.waste.size() > 0) {
			drawFullCard(g2d, solitaireData.waste.getLast(), 160, 10);
		}
		drawRectangle(g2d, 300, 12, 120, 40);
		g2d.setFont(new Font("Dialog", Font.PLAIN, 20));
		g2d.drawString("New Game", (720-g2d.getFontMetrics().stringWidth("New Game")) / 2, 39);
		drawRectangle(g2d, 300, 57, 120, 40);
		g2d.drawString("Restart", (720-g2d.getFontMetrics().stringWidth("Restart")) / 2, 84);
		drawRectangle(g2d, 300, 102, 120, 40);
		g2d.drawString("Undo", (720-g2d.getFontMetrics().stringWidth("Undo")) / 2, 129);
		drawRectangle(g2d, 300, 147, 120, 40);
		g2d.drawString("Redo", (720-g2d.getFontMetrics().stringWidth("Redo")) / 2, 174);
		for (int i = 0; i < 4; i++) {
			if (solitaireData.foundations[i].size() > 0) {
				drawFullCard(g2d, solitaireData.foundations[i].getLast(), 440+140*i, 10);
			}
		}
	}
	
	private void drawTableauCursorWin(Graphics2D g2d) {
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < solitaireData.tableaus[i].size(); j++) {
				Card tableauCard = solitaireData.tableaus[i].get(j);
				if (tableauCard.isVisible) {
					drawFullCard(g2d, tableauCard, 20+140*i, 200+j*solitaireData.tableauSpacing[i]);
				} else {
					drawCard(g2d, 20+140*i, 200+j*solitaireData.tableauSpacing[i], false);
				}
			}
		}
		for (int i = 0; i < solitaireData.cursor.size(); i++) {
			drawFullCard(g2d, solitaireData.cursor.get(i), solitaireData.cursorX, solitaireData.cursorY+i*30);
		}
		if (solitaireData.state.equals("win")) {
			drawRectangle(g2d, 400, 300, 200, 100);
			g2d.setFont(new Font("Dialog", Font.PLAIN, 30));
			g2d.drawString("You Win!", (1000-g2d.getFontMetrics().stringWidth("You Win!")) / 2, 335);
			g2d.drawRect(425, 347, 150, 40);
			g2d.setFont(new Font("Dialog", Font.PLAIN, 25));
			g2d.drawString("New Game", (1000-g2d.getFontMetrics().stringWidth("New Game")) / 2, 376);
		}
	}
	
	private void drawCard(Graphics2D g2d, int startX, int startY, boolean isVisible) {
		if (isVisible) {
			g2d.setPaint(new Color(255, 255, 255));
		} else {
			g2d.setPaint(new Color(50, 175, 255));
		}
		g2d.fillRoundRect(startX, startY, 120, 180, 10, 10);
		g2d.setPaint(new Color(0, 0, 0));
		g2d.drawRoundRect(startX, startY, 120, 180, 10, 10);
	}
	
	private void drawFullCard(Graphics2D g2d, Card card, int startX, int startY) {
		drawCard(g2d, startX, startY, true);
		String[][] draw = card.cardDraw.draw;
		for (int i = 0; i < draw.length; i++) {
			Image image = new ImageIcon("Images/" + draw[i][0]).getImage();
			int imageWidth = (int) (image.getWidth(null) * Double.parseDouble(draw[i][3]));
			int imageHeight = (int) (image.getHeight(null) * Double.parseDouble(draw[i][3]));
			g2d.drawImage(image, startX + Integer.parseInt(draw[i][1]) - imageWidth/2, startY + Integer.parseInt(draw[i][2]) - imageHeight/2, imageWidth, imageHeight, null);
		}
	}
	
	private void drawRectangle(Graphics2D g2d, int startX, int startY, int width, int height) {
		g2d.setPaint(new Color(0, 0, 0));
		g2d.fillRect(startX, startY, width, height);
		g2d.setPaint(new Color(255, 255, 255));
		g2d.drawRect(startX, startY, width, height);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}
}
