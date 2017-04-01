package game;

import game.logging.Log;
import game.vo.Hero;
import game.vo.Minion;
import game.vo.classes.Warrior;

public class Test {

	private static final String TAG = Test.class.getSimpleName();

	public static void main(String[] args) {

		Log.i(TAG, "Send animation");
		Thread thread = new Thread(){
			public void run(){
				try {
					sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Log.i(TAG, "Deal damage");
			}
		};
		thread.start();
	}
}
