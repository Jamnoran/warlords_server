package game.io;

import game.vo.Vector3;

/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class RotateTargetResponse {
	private String response_type = "ROTATE_TARGET";
	private boolean friendly = true;
	private int idOfTarget = 0;
	private Vector3 targetPosition;

	public String getResponse_type() {
		return response_type;
	}

	public void setResponse_type(String response_type) {
		this.response_type = response_type;
	}

	public boolean isFriendly() {
		return friendly;
	}

	public void setFriendly(boolean friendly) {
		this.friendly = friendly;
	}

	public int getIdOfTarget() {
		return idOfTarget;
	}

	public void setIdOfTarget(int idOfTarget) {
		this.idOfTarget = idOfTarget;
	}

	public Vector3 getTargetPosition() {
		return targetPosition;
	}

	public void setTargetPosition(Vector3 targetPosition) {
		this.targetPosition = targetPosition;
	}

	@Override
	public String toString() {
		return "RotateTargetResponse{" +
				"response_type='" + response_type + '\'' +
				", friendly=" + friendly +
				", idOfTarget=" + idOfTarget +
				'}';
	}
}
