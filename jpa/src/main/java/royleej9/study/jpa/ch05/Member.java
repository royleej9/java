package royleej9.study.jpa.ch05;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Member {

	@Id
	@Column(name = "MEMBER_ID")
	private String id;

	private String userName;

	public Member() {
	}

	public Member(String id, String userName) {
		this.id = id;
		this.userName = userName;
	}

	@ManyToOne
	@JoinColumn(name = "TEAM_ID")
	private Team team;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		if (this.team != null) {
			this.team.getMembers().remove(this);
		}
		this.team = team;
		team.getMembers().add(this);
	}

	@Override
	public String toString() {
		return "Member [id=" + id + ", userName=" + userName + ", team=" + team + "]";
	}

}
