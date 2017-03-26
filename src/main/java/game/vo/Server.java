package game.vo;

/**
 * Created by Eric on 2017-03-03.
 */
public class Server {
	private String id;
	private String ip;
	private String port;
	private String version;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean canCreateNewGame() {
		return false;
	}

	@Override
	public String toString() {
		return "Server{" +
				"id='" + id + '\'' +
				", ip='" + ip + '\'' +
				", port='" + port + '\'' +
				", version='" + version + '\'' +
				'}';
	}
}
