package royleej9.study.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

// 파마미터가 없는 public/protected 기본 생성자 필수
// 생성자가 없는 경우 기본 생성자 자동 생성됨 - 자바 기본 기능
// 파라미터가 있는 생성자를 만들 경우 기본 생성자를 직접 만들어야함
// 저장 필드 final 사용 금지

// 필수
@Entity

// 생략시 클래스 이름으로 테이블 매핑
//@Table(name = "Member")
@Table(name = "Member", uniqueConstraints = {
		@UniqueConstraint(name = "NAME_AGE_UNIQUE", 
					columnNames = { "NAME", "AGE" }) 
		})
public class Member {

	@Id
	@Column(name = "ID")
//	@GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성 
	private String id;

	// ddl 생성시 컬럼 제약 조건
	// name, null 허용 여부, 길이
	@Column(name = "NAME", nullable = false, length = 10)
	private String userName;
	private Integer age;

	@Enumerated(EnumType.STRING)
	private RoleType roleType;

	@Temporal(TemporalType.TIMESTAMP) // 날짜 타입 매핑
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDate;

	// db-blob, clob 대응
	@Lob
	private String description;
	
	// 테이블 매핑에서 제외 
	@Transient
	private String test;
	

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

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public RoleType getRoleType() {
		return roleType;
	}

	public void setRoleType(RoleType roleType) {
		this.roleType = roleType;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Member [id=" + id + ", userName=" + userName + ", age=" + age + "]";
	}

}
