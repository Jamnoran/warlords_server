package game.io;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Eric on 2017-03-10.
 */
public class JsonResponse implements Serializable {
	@SerializedName("response_type")
	public String responseType;
	@SerializedName("code")
	public String code;
	@SerializedName("message")
	public String message;

	public JsonResponse() {
	}

	public JsonResponse(String resp, Integer codeToUser) {
		responseType = resp;
		code = "" + codeToUser;
	}

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	@Override
	public String toString() {
		return "JsonResponse{" +
				"responseType='" + responseType + '\'' +
				", code='" + code + '\'' +
				", message='" + message + '\'' +
				'}';
	}
}
