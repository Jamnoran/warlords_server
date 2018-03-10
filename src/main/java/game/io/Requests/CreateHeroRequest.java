package game.io.Requests;

import com.google.gson.annotations.SerializedName;
import game.io.JsonRequest;

/**
 * Created by Jamnoran on 29-Jun-16.
 */
public class CreateHeroRequest extends JsonRequest {
	@SerializedName("class_type")
	public String class_type;

	public String getClass_type() {
		return class_type;
	}

	public void setClass_type(String class_type) {
		this.class_type = class_type;
	}

	@Override
	public String toString() {
		return "CreateHeroRequest{" +
				"class_type='" + class_type + '\'' +
				'}';
	}
}
