package io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import game.logging.Log;
import vo.Message;
import game.GameServer;

import java.io.Serializable;

public class JsonRequest implements Serializable {
	private static final String TAG = JsonRequest.class.getSimpleName();
	@SerializedName("request_type")
	public String requestType;

	public static JsonRequest parse(GameServer gameServer, Message aMessage) {
        Gson gson = new GsonBuilder().create();

        JsonRequest request = null;
        if (aMessage != null && aMessage.getMessage() != null) {
	        request = gson.fromJson(aMessage.getMessage(), JsonRequest.class);
        }

        if (request != null && request.getRequestType() != null) {
	        Log.i(TAG, "Got this request: " + request.toString());
        }

		if (request != null) {
			if(request.isType("CHARACTER_ACTION")){
				JoinServerRequest parsedRequest = gson.fromJson(aMessage.getMessage(), JoinServerRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				gameServer.getGameCommunicationUtil().handleJoinServerRequest(parsedRequest);
			}else if(request.isType("GET_STATUS")){
				JoinServerRequest parsedRequest = gson.fromJson(aMessage.getMessage(), JoinServerRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				gameServer.getGameCommunicationUtil().handleGetStatusRequest(parsedRequest);
			}
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

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

	@Override
	public String toString() {
		return "JsonRequest{" +
				"requestType='" + requestType + '\'' +
				'}';
	}

	public JsonRequest getRequest() {
		return null;
	}
}
