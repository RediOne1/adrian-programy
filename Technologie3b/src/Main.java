import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

public class Main extends Thread {
	private Medium medium;

	public Main(Medium c) {
		this.medium = c;
	}

	public static String wiadomosci[] = { "Ala", "Sebastian", "Kot", "Dom",
			"Technologie", "Sieciowe" };

	public long ID = this.getId() - 7;
	public static final int ILE = 10;
	public static final long DELAY = 500;
	public int time = 2000, x = 10;

	@Override
	public void run() {
		try {
			while (true) {
				Random r = new Random();
				Thread.sleep(r.nextInt(time));
				if (x != 0) {
					time *= 2;
					x--;
				}
				while (!medium.czyWolny()) {
					Thread.sleep(r.nextInt(5) * 100);
				}
				Thread.sleep(DELAY);
				medium.wolny = false;

				medium.add("[START " + ID + "]");
				Thread.sleep(DELAY);
				String message = "W: "
						+ wiadomosci[r.nextInt(wiadomosci.length)];
				medium.add(message);
				Thread.sleep(DELAY);
				medium.add("[STOP " + ID + "]");

				String l1, l3;
				l3 = medium.komunikaty.get(medium.komunikaty.size() - 1);
				l1 = medium.komunikaty.get(medium.komunikaty.size() - 3);

				Thread.sleep(DELAY);
				medium.wolny = true;

				if (l1.equals("[START " + ID + "]")
						&& l3.equals("[STOP " + ID + "]")) {
					System.out.println("Watek " + ID + " przeslal dane.  "+ (time/2)+"ms");
					medium.succ++;
					break;
				} else {
					medium.add("[ERROR " + ID + "] - kolizja");
				}
			}
		} catch (Exception e) {
		}
	}

	public static void main(String... args) throws FileNotFoundException,
			IOException {
		Medium m = new Medium();
		for (int i = 0; i < 10; i++)
			new Main(m).start();
	}
}