package game.io.Responses;

import game.vo.Talent;

import java.util.ArrayList;

/**
 * Created by Eric on 2017-05-18.
 */
public class TalentResponse  {
	private String response_type = "TALENTS";
	private final ArrayList<Talent> talents;
	private int totalTalentPoints = 0;

	public TalentResponse(ArrayList<Talent> talents, int totalPoints) {
		this.talents = talents;
		this.totalTalentPoints = totalPoints;
	}

	public ArrayList<Talent> getTalents() {
		return talents;
	}

	public String getResponse_type() {
		return response_type;
	}

	public void setResponse_type(String response_type) {
		this.response_type = response_type;
	}

	public int getTotalTalentPoints() {
		return totalTalentPoints;
	}

	public void setTotalTalentPoints(int totalTalentPoints) {
		this.totalTalentPoints = totalTalentPoints;
	}

	@Override
	public String toString() {
		return "TalentResponse{" +
				"response_type='" + response_type + '\'' +
				", talents=" + talents +
				", totalTalentPoints=" + totalTalentPoints +
				'}';
	}
}
