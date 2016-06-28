package io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import vo.Message;
import game.GameServer;

import java.io.Serializable;

public class JsonRequest implements Serializable {
    @SerializedName("request_type")
	public String requestType;

	public static JsonRequest parse(GameServer gameServer, Message aMessage) {
        Gson gson = new GsonBuilder().create();

        JsonRequest request = null;
        if (aMessage != null && aMessage.getMessage() != null) {
	        request = gson.fromJson(aMessage.getMessage(), JsonRequest.class);
        }

        if (request != null && request.getRequestType() != null) {
            System.out.println("Got this request: " + request.toString());
        }

		if(request.isType("JOIN_SERVER")){
			JoinServerRequest parsedRequest = gson.fromJson(aMessage.getMessage(), JoinServerRequest.class);
			System.out.println("parsedRequest : " + parsedRequest.toString());
			gameServer.getGameCommunicationUtil().handleJoinServerRequest(parsedRequest);
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
}
