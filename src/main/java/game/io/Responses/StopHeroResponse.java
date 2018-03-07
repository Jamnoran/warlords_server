package game.io.Responses;

/**
 * Created by Jamnoran on 28-Oct-16.
 */
public class StopHeroResponse {

	private String response_type = "STOP_HERO";
	public Integer hero;

	public StopHeroResponse(Integer hero) {
		this.hero = hero;
	}

	public Integer getHero() {
		return hero;
	}

	public void setHero(Integer hero) {
		this.hero = hero;
	}

	@Override
	public String toString() {
		return "StopHeroResponse{" +
				"hero=" + hero +
				'}';
	}
}
