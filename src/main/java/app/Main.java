package app;

import engine.Application;
import engine.ApplicationInterface;

public class Main {

	public static void main(String[] args) {
		try {
			boolean vSync = true;
			ApplicationInterface gameLogic = new DummyGame();
			Application app = new Application("app", 600, 480, vSync, gameLogic);
			app.run();
		} catch (Exception excp) {
			excp.printStackTrace();
			System.exit(-1);
		}
	}
}