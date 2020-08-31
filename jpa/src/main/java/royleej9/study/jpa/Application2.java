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
		
		removeUser(entityManagerFactory);
		
		member = findUser(entityManagerFactory);
		if (member != null) {
			logger.info("findUser-after remove:: {}", member.toString());			
		}

		entityManagerFactory.close();
	}

	private static void removeUser(EntityManagerFactory entityManagerFactory) {
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		final EntityTransaction entityTransaction = entityManager.getTransaction();

		try {
			// 등록/수정/삭제시 트랜잭션 필요
			entityTransaction.begin();

			String id = "id1";
			// 삭제 대상이 영속성 컨텍스트에 저장되어 있어야함 - 영속 상태
			Member findMember = entityManager.find(Member.class, id);
			entityManager.remove(findMember);
			
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

	private static Member findUser(EntityManagerFactory entityManagerFactory) {
		final EntityManager entityManager = entityManagerFactory.createEntityManager();

		String id = "id1";
		// 1차 캐시에서 먼저 조회 -> 없을 경우 db에서 조회 
		// 조회된 엔티티는 영속 상태가 된다
		Member findMember = entityManager.find(Member.class, id);
		entityManager.close();

		return findMember;
	}

	private static void updateUser(EntityManagerFactory entityManagerFactory) {
		final EntityManager entityManager = entityManagerFactory.createEntityManager();
		final EntityTransaction entityTransaction = entityManager.getTransaction();

		try {
			// 등록/수정/삭제시 트랜잭션 필요
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
			// 등록/수정/삭제시 트랜잭션 필요
			entityTransaction.begin();

			String id = "id1";
			Member member = new Member();
			member.setId(id);
			member.setUserName("test");
			member.setAge(4);

			// 등록
			// 1차 캐시 entity 저장 / insert sql 생성 후 내부 sql 저장소에 보관
			entityManager.persist(member);
			
			// 1.엔티티메니저의 flush 호출
			// 2 동기화 작업 실행 ->내부 sql저장소에 보관된 sql을 db로 전달.실행 
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
