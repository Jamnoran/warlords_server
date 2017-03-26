package game.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import game.logging.Log;
import game.vo.Message;

import java.io.Serializable;

public class JsonRequest implements Serializable {
	private static final String TAG = JsonRequest.class.getSimpleName();
	@SerializedName("request_type")
	public String requestType;
	@SerializedName("user_id")
	public String user_id;


	public static JsonRequest parse(Message aMessage) {
        Gson gson = new GsonBuilder().create();
        JsonRequest request = null;
        if (aMessage != null && aMessage.getMessage() != null) {
	        request = gson.fromJson(aMessage.getMessage(), JsonRequest.class);
        }
		return request;
	}


	public boolean isType(String requestType) {
        if (requestType != null) {
            if (this.requestType.equalsIgnoreCase(requestType)) {
                return true;
            } else {
                return false;
            }
        }else {
            return false;
        }
    }

	public String getRequestType() {
        return requestType;
    }

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	@Override
	public String toString() {
		return "JsonRequest{" +
				"requestType='" + requestType + '\'' +
				", user_id='" + user_id + '\'' +
				'}';
	}
}
