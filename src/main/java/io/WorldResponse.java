package io;

import vo.World;

/**
 * Created by Jamnoran on 27-Jun-16.
 */
public class WorldResponse {
	private String response_type = "WORLD";

	private World world;

	public WorldResponse() {
	}

	public WorldResponse(World world) {
		this.world = world;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	@Override
	public String toString() {
		return "WorldResponse{" +
				"world=" + world +
				'}';
	}
}
