package io;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jamnoran on 29-Jun-16.
 */
public class CreateCharacterRequest extends JsonRequest{
	@SerializedName("classType")
	public String classType;

}
