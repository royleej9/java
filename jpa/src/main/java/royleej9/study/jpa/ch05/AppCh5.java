package royleej9.study.jpa.ch05;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.tools.Server;

import royleej9.study.jpa.ch01.Application2;

public class AppCh5 {

	static final Logger logger = LogManager.getLogger(Application2.class.getName());

	public static void main(String[] args) throws SQLException {
		startDatabase();

		final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpastudy");
		test(entityManagerFactory);
		select(entityManagerFactory);
		update(entityManagerFactory);
		select(entityManagerFactory);
		
		selectTeam(entityManagerFactory);
		entityManagerFactory.close();
	}

	public static void test(EntityManagerFactory entityManagerFactory) {
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		final EntityTransaction entityTransaction = entityManager.getTransaction();

		entityTransaction.begin();

		Team team1 = new Team();
		team1.setId("teamID1");
		team1.setName("teamName1");
		entityManager.persist(team1);

		Member member1 = new Member("member1", "회원1");
		member1.setTeam(team1);
		entityManager.persist(member1);

		Member member2 = new Member("member2", "회원2");
		member2.setTeam(team1);
		entityManager.persist(member2);

		entityTransaction.commit();
		entityManager.close();
	}

	public static void select(EntityManagerFactory entityManagerFactory) {
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		Member findMember = entityManager.find(Member.class, "member1");
		logger.info(findMember.toString());
		entityManager.close();
	}
	
	public static void selectTeam(EntityManagerFactory entityManagerFactory) {
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		Team team = entityManager.find(Team.class, "teamID1");
		logger.info("select Team:: " + team.getMembers());
		entityManager.close();
	}

	public static void update(EntityManagerFactory entityManagerFactory) {
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		final EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		Team team = new Team();
		team.setId("team3");
		team.setName("team3");
		entityManager.persist(team);

		Member member = entityManager.find(Member.class, "member1");
		member.setTeam(team);

		entityTransaction.commit();
		entityManager.close();
	}

	private static void startDatabase() throws SQLException {
		new Server().runTool("-tcp", "-web", "-ifNotExists");
	}

}
