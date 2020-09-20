package royleej9.study.jpa.ch06.mto.unidirection;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.tools.Server;

import royleej9.study.jpa.ch01.Application2;

public class AppManyToOneUnidirection {

	static final Logger logger = LogManager.getLogger(AppManyToOneUnidirection.class.getName());

	
	// N : 1 단방향
	// Member -> Team
	public static void main(String[] args) throws Exception {
		startDatabase();
		final EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpastudy");

		addEntity(emf);
		addMembers(emf);
		selectMembers(emf);
		deleteRelation(emf);

//		emf.close();
	}
	
	private static void selectMembers(EntityManagerFactory emf) {
		final EntityManager em = emf.createEntityManager();
		
		List<Member> members = em.createQuery("select m from Member m", Member.class)
				   .getResultList();
		
		logger.info("select members-size: {}", members.size());
		members.forEach(member-> {
			logger.info("select members-member: {}", member.toString());			
			logger.info("select members-team: {}", member.getTeam().toString());
		});
		
		em.close();
	}

	public static void addEntity(EntityManagerFactory emf) {
		final EntityManager em = emf.createEntityManager();
		final EntityTransaction et = em.getTransaction();
		et.begin();
		Team team1 = new Team("Team-1");

		Member member1 = new Member("user1", team1);

		// 1. team insert sql
		// 2. member insert sql 실행
		em.persist(team1);
		em.persist(member1);

		// 1. member insert sql 실행
		// 2. team insert sql 실행
		// 3. member의 team_id 값 변경 update sql 실행
//		em.persist(member1);
//		em.persist(team1);

		et.commit();
		em.close();
	}

	private static void addMembers(EntityManagerFactory emf) {
		final EntityManager em = emf.createEntityManager();
		final EntityTransaction et = em.getTransaction();
		et.begin();
		List<Team> teams = em.createQuery("select t from Team t where name = :name", Team.class)
									   .setParameter("name", "Team-1")
									   .getResultList();
		if (teams.size() == 1) {
			Team team = teams.get(0);
			Member member2 = new Member("user2", team);
			Member member3 = new Member("user3", team);
			Member member4 = new Member("user4", team);						
			em.persist(member2);
			em.persist(member3);
			em.persist(member4);
		}
		
		Team team2 = new Team("Team-2");
		Member member5 = new Member("user5", team2);
		em.persist(team2);
		em.persist(member5);
		
		
		et.commit();
		em.close();
	}
	
	private static void deleteRelation(EntityManagerFactory emf) {
		final EntityManager em = emf.createEntityManager();
		final EntityTransaction et = em.getTransaction();
		et.begin();
		List<Member> members = em.createQuery("select m from Member m where username = :username", Member.class)
									   .setParameter("username", "user5")
									   .getResultList();
		if (members.size() == 1) {
			Member member = members.get(0);
			Team team = member.getTeam();
			member.setTeam(null);
			em.remove(team);
//			em.persist(member4);
		}
		
		et.commit();
		em.close();
	}

	private static void startDatabase() throws SQLException {
		new Server().runTool("-tcp", "-web", "-ifNotExists");
	}
}
