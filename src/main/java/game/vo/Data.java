package game.vo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable {

	@SerializedName("id")
	public Integer id;
	@SerializedName("positionX")
	public Double positionX;
	@SerializedName("positionY")
	public Double positionY;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getPositionX() {
		return positionX;
	}

	public void setPositionX(Double positionX) {
		this.positionX = positionX;
	}

	public Double getPositionY() {
		return positionY;
	}

	public void setPositionY(Double positionY) {
		this.positionY = positionY;
	}

	@Override
	public String toString() {
		return "Data [id=" + id + ", positionX=" + positionX + ", positionY="
				+ positionY + "]";
	}

}
