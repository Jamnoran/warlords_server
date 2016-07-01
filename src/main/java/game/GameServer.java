package game;

import com.google.gson.Gson;
import game.logging.Log;
import io.GameStatusResponse;
import vo.GameAnimation;
import vo.Hero;
import vo.Message;
import vo.Minion;
import vo.classes.Warrior;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class GameServer {
	private static final String TAG = GameServer.class.getSimpleName();
	private boolean gameRunning = true;
	private ServerDispatcher server;

	private int minionCount = 0;
	private ArrayList<Minion> minions = new ArrayList<>();
	private ArrayList<Hero> heroes = new ArrayList<>();
	private ArrayList<GameAnimation> animations = new ArrayList<>();


	public GameServer(ServerDispatcher server) {
		this.server = server;
		// Start a thread that sends game status every second, this should be changed to when something happens in future
//		startStatusThread();
		spawnMinions();
	}


	public void addHero(Hero hero) {
		if (hero.isClass(Hero.WARRIOR)) {
			Warrior warrior = (Warrior) hero;
			warrior.generateHeroInformation();
			heroes.add(warrior);
		} else if (hero.isClass(Hero.PRIEST)){
			Log.i(TAG, "No support for priests yet");
		}
		Log.i(TAG, "Hero joined with this user id: " + hero.getUser_id() + " characters in game: " + heroes.size());
		sendGameStatus();
	}



















	public void sendStatusToAllClients() {
		sendGameStatus();
	}

	private void startStatusThread() {
		Thread thread = new Thread(){
			public void run(){
				while(gameRunning){
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Log.i(TAG, "Sending game status");
					sendGameStatus();
				}
			}
		};

		thread.start();
	}

	private void spawnMinions() {
		Thread thread = new Thread(){
			public void run(){
				while(gameRunning){
					try {
						sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Log.i(TAG, "Sending game status");
					spawnMinion();
				}
			}
		};

		thread.start();
	}

	private void spawnMinion() {
		minionCount++;
		Minion minion = new Minion();
		minion.setId(minionCount);
		minion.generateMinionInformation();
		minions.add(minion);
		sendGameStatus();
	}

	public void sendGameStatus(){
		Gson gson = new Gson();
		String jsonInString = gson.toJson(new GameStatusResponse(minions, heroes, animations));
		server.dispatchMessage(new Message(jsonInString));
		clearSentAnimations();
	}

	private void clearSentAnimations() {
		animations.clear();
		Log.i(TAG, "Animations is cleared: " + animations.size());
	}

	public void attack(String userId, Integer minionId) {
		Log.i(TAG, "User " + userId + " Hero attacked minion: " + minionId + " minons count : " + minions.size());
		Iterator<Hero> it = heroes.iterator();
		while (it.hasNext()) {
			Hero hero = it.next();
			if(hero.getUser_id() == Integer.parseInt(userId)){
				Log.i(TAG, "Found hero thats attacking : " + hero.getClass_type());
				Iterator<Minion> ut = minions.iterator();
				while (ut.hasNext()) {
					Minion minion = ut.next();
					if(minion.getId() == minionId){
						Log.i(TAG, "Found minion to attack : " + minion.getId());
						minionDied(userId, minion.getId());
						ut.remove();
					}
				}
			}
		}
		Log.i(TAG, "Minion size now: " + minions.size());
		sendGameStatus();
	}

	private void minionDied(String userId, Integer minionId) {
		animations.add(new GameAnimation("MINION_DIED", minionId, Integer.parseInt(userId)));
	}
}
