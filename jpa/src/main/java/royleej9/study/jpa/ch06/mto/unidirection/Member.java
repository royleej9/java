package royleej9.study.jpa.ch06.mto.unidirection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Member {

	@Id
	@GeneratedValue
	@Column(name = "MEMBER_ID")
	private Long id;

	private String username;

	@ManyToOne
	@JoinColumn(name = "TEAM_ID")
	private Team team;

	public Member() {
	}

	public Member(String username, Team team) {
		this.username = username;
		this.team = team;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	@Override
	public String toString() {
		return "Member [id=" + id + ", username=" + username + ", team=" + team + "]";
	}

}
