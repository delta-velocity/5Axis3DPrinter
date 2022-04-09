package app;

import engine.Application;
import engine.ApplicationInterface;

public class Main {

	public static void main(String[] args) {
		try {
			boolean vSync = true;
			ApplicationInterface logic = new AntiLatheApp();
			Application app = new Application("app", 600, 480, vSync, logic);
			app.run();
		} catch (Exception excp) {
			excp.printStackTrace();
			System.exit(-1);
		}
	}
}