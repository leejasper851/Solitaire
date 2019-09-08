import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SolitaireMouseAdapter extends MouseAdapter {
	public Surface surface = new Surface(this);
	private int x;
	private int y;
	
	@Override
	public void mouseClicked(MouseEvent e) {
		getMouseCoords(e);
		surface.solitaireData.stockClick(x, y);
		surface.solitaireData.menuButtonClick(x, y);
		surface.repaint();
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		getMouseCoords(e);
		surface.solitaireData.mousePress(x, y);
		surface.repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		getMouseCoords(e);
		surface.solitaireData.mouseDrag(x, y);
		surface.repaint();
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		getMouseCoords(e);
		surface.solitaireData.mouseRelease(x, y);
		surface.repaint();
	}
	
	private void getMouseCoords(MouseEvent e) {
		Point mouseCoords = e.getPoint();
		x = (int) mouseCoords.getX();
		y = (int) mouseCoords.getY();
	}
}
