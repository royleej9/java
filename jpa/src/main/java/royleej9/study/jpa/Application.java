package royleej9.study.jpa;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.h2.tools.Server;

import royleej9.study.jpa.ch01.Member;

public class Application {

	public static void main(String[] args) throws SQLException {
		startDatabase();

		// 1 factory 생성
		// 프로그램 전체에서 한번만 생성하며 공유 해서 사용
		// 스레드간 공유 가능
		final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpastudy");

		// 2 manager 생성
		// entity 를 db에 등록/수정/조회/삭제 할수 있음
		// 스레드간에 공유 및 재 사용 할 수 없음
		final EntityManager entityManager = entityManagerFactory.createEntityManager();

		// 3 transaction 생성
		// 데어터를 수정/삭제/추가시 필요
		final EntityTransaction entityTransaction = entityManager.getTransaction();

		try {
			// 4 transaction 시작
			entityTransaction.begin();

			logic(entityManager);

			// 5. transaction 커밋
			entityTransaction.commit();
		} catch (Exception ex) {
			// 롤백 -
			entityTransaction.rollback();
			ex.printStackTrace();
		} finally {
			// 6 manager 종료
			entityManager.close();
		}

		// 7. factory 종료
		entityManagerFactory.close();
	}

	private static void logic(EntityManager entityManager) {
		String id = "id1";
		Member member = new Member();
		member.setId(id);
		member.setUserName("test");
		member.setAge(4);
		
		// 등록
		entityManager.persist(member);
		
		id = "id2";
		Member member2 = new Member();
		member2.setId(id);
		member2.setUserName("test2");
		member2.setAge(4);
		
		// 등록
		entityManager.persist(member2);
		
		// 수정
		member.setAge(22);
		
		Member findMember = entityManager.find(Member.class, id);
		System.out.println("findMember: " + findMember.toString());
		
		List<Member> members = entityManager.createQuery("select m from Member m", Member.class).getResultList();
		System.out.println("1. members.size: " + members.size());
		
		// 삭제
		entityManager.remove(member);

		members = entityManager.createQuery("select m from Member m", Member.class).getResultList();
		System.out.println("2. members.size: " + members.size());
	}

	private static void startDatabase() throws SQLException {
		new Server().runTool("-tcp", "-web", "-ifNotExists");
	}
}
