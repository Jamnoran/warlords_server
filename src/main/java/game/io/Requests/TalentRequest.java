package game.io.Requests;

import game.io.JsonRequest;
import game.vo.Talent;

import java.util.ArrayList;

/**
 * Created by Eric on 2017-06-05.
 */
public class TalentRequest extends JsonRequest {
	public int hero_id = 0;
	public ArrayList<Talent> talents;

	public int getHero_id() {
		return hero_id;
	}

	public void setHero_id(int hero_id) {
		this.hero_id = hero_id;
	}

	public ArrayList<Talent> getTalents() {
		return talents;
	}

	public void setTalents(ArrayList<Talent> talents) {
		this.talents = talents;
	}

	@Override
	public String toString() {
		return "TalentRequest{" +
				"hero_id=" + hero_id +
				", talents=" + talents +
				'}';
	}
}
