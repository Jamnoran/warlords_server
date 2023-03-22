package game.io.Responses;

import game.models.vo.Vector3;

/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class RotateTargetResponse {
	private String response_type = "ROTATE_TARGET";
	private int sourceId = 0;
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

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int source) {
		this.sourceId = source;
	}

	@Override
	public String toString() {
		return "RotateTargetResponse{" +
				"response_type='" + response_type + '\'' +
				", sourceId=" + sourceId +
				", friendly=" + friendly +
				", idOfTarget=" + idOfTarget +
				", targetPosition=" + targetPosition +
				'}';
	}
}
