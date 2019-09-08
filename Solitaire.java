import java.awt.EventQueue;
import javax.swing.JFrame;

public class Solitaire extends JFrame {
	
	public Solitaire() {
		initUI();
	}
	
	private void initUI() {
		SolitaireMouseAdapter mouseAdapter = new SolitaireMouseAdapter();
		add(mouseAdapter.surface);
		setTitle("Solitaire");
		setSize(1000, 730);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Solitaire solitaire = new Solitaire();
				solitaire.setVisible(true);
			}
		});
	}
}
