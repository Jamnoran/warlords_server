package vo;

public class Minion {

	private Integer id = 1;
	private float positionX = 10.0f;
	private float positionY = 10.0f;
	
	
	public Minion() {
		
	}
	public Minion(int id) {
		this.id = id;
	}

	public void setPositionY(float positionY) {
		this.positionY = positionY;
	}

	public void setPositionX(float positionX) {
		this.positionX = positionX;
	}

	public float getPositionY() {
		return positionY;
	}

	public float getPositionX() {
		return positionX;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void move() {
		
		if (positionX <= 15f) {
			positionX = positionX + 1.0f;
			positionY = positionY + 1.0f;
		}else {
			positionX = 1.0f;
			positionY = 1.0f;
		}
	}

}
