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
	@SerializedName("user_id")
	public String user_id;


	public static JsonRequest parse(Message aMessage) {
        Gson gson = new GsonBuilder().create();

        JsonRequest request = null;
        if (aMessage != null && aMessage.getMessage() != null) {
	        request = gson.fromJson(aMessage.getMessage(), JsonRequest.class);
        }

		if (request != null) {
			Log.i(TAG, "Got this request: " + request.toString());
			if(request.isType("JOIN_SERVER")){
				JoinServerRequest parsedRequest = gson.fromJson(aMessage.getMessage(), JoinServerRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				return parsedRequest;
			}else if(request.isType("GET_STATUS")){
				JoinServerRequest parsedRequest = gson.fromJson(aMessage.getMessage(), JoinServerRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				return parsedRequest;
			}else if(request.getRequestType().equals("CREATE_HERO")){
				CreateHeroRequest parsedRequest = gson.fromJson(aMessage.getMessage(), CreateHeroRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				return parsedRequest;
			}else if(request.getRequestType().equals("CREATE_USER")){
				CreateUserRequest parsedRequest = gson.fromJson(aMessage.getMessage(), CreateUserRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				return parsedRequest;
			}else if(request.getRequestType().equals("ATTACK")){
				AttackRequest parsedRequest = gson.fromJson(aMessage.getMessage(), AttackRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				return parsedRequest;
			}else if(request.getRequestType().equals("MOVE")){
				MoveRequest parsedRequest = gson.fromJson(aMessage.getMessage(), MoveRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				return parsedRequest;
			}else if(request.getRequestType().equals("SPELL")){
				SpellRequest parsedRequest = gson.fromJson(aMessage.getMessage(), SpellRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				return parsedRequest;
			}else if(request.getRequestType().equals("MINION_AGGRO")){
				MinionAggroRequest parsedRequest = gson.fromJson(aMessage.getMessage(), MinionAggroRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				return parsedRequest;
			}else if(request.getRequestType().equals("MINION_TARGET_IN_RANGE")){
				MinionAggroRequest parsedRequest = gson.fromJson(aMessage.getMessage(), MinionAggroRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				return parsedRequest;
			}else if(request.getRequestType().equals("END_GAME")){
				JsonRequest parsedRequest = gson.fromJson(aMessage.getMessage(), JsonRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				return parsedRequest;
			}else if(request.getRequestType().equals("CLICKED_PORTAL")){
				ClickPortalRequest parsedRequest = gson.fromJson(aMessage.getMessage(), ClickPortalRequest.class);
				Log.i(TAG, "parsedRequest : " + parsedRequest.toString());
				return parsedRequest;
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
