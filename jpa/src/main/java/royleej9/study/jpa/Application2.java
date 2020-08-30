package royleej9.study.jpa;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.tools.Server;

public class Application2 {

	static final Logger logger = LogManager.getLogger(Application2.class.getName());

	public static void main(String[] args) throws SQLException {
		startDatabase();

		final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpastudy");

		addUser(entityManagerFactory);
		updateUser(entityManagerFactory);
		Member member = findUser(entityManagerFactory);
		
		logger.info("findUser:: {}", member.toString());

		entityManagerFactory.close();
	}

	private static Member findUser(EntityManagerFactory entityManagerFactory) {
		final EntityManager entityManager = entityManagerFactory.createEntityManager();

		String id = "id1";
		Member findMember = entityManager.find(Member.class, id);
		entityManager.close();

		return findMember;
	}

	private static void updateUser(EntityManagerFactory entityManagerFactory) {
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		final EntityTransaction entityTransaction = entityManager.getTransaction();

		try {
			entityTransaction.begin();
			
			// 수정 entity 검색 - 영속성 컨텍스트에 없는 경우 db 조회
			String id = "id2";
			// 영속성 컨텍스트에 저장 및 최초 조회 내용을 복사(스냅샷)을 1차 캐시에 보관
			Member findMember = entityManager.find(Member.class, id);
			if (findMember != null) {
				logger.info("findMember:: {}", findMember);

				// 스냅샷 내용과 변경 내용을 비교하여 update sql 생성
				// 변경 필드가 1개여도 전체 필드에 대한 update sql 생성
				// update member set age=?, name=? where id=?
				// hibernate.annotations.DynamicUpdate 어노테이션 사용시 변경 필드만 sql 생성
				findMember.setUserName("test2-changed");
			}

			entityTransaction.commit();
		} catch (Exception ex) {
			// 롤백 -
			entityTransaction.rollback();
			ex.printStackTrace();
		} finally {
			// 6 manager 종료
			entityManager.close();
		}
	}

	private static void addUser(EntityManagerFactory entityManagerFactory) {
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		final EntityTransaction entityTransaction = entityManager.getTransaction();

		try {
			entityTransaction.begin();

			String id = "id1";
			Member member = new Member();
			member.setId(id);
			member.setUserName("test");
			member.setAge(4);

			// 등록
			entityManager.persist(member);

			entityTransaction.commit();
		} catch (Exception ex) {
			// 롤백 -
			entityTransaction.rollback();
			ex.printStackTrace();
		} finally {
			// 6 manager 종료
			entityManager.close();
		}
	}

	private static void startDatabase() throws SQLException {
		new Server().runTool("-tcp", "-web", "-ifNotExists");
	}
}
