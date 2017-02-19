package game.vo;

/**
 * Created by Jamnoran on 30-Jun-16.
 */
public class User {

	private Integer id;
	private String username;
	private String email;
	private String password;

	public User() {
	}

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", username='" + username + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				'}';
	}

	public String getSqlInsertQuery() {
		return "INSERT INTO `warlords`.`users` (`id`, `username`, `password`, `email`) VALUES (NULL, '" + getUsername() + "', '" + getPassword() + "', '" + getEmail() + "')";
	}
}
