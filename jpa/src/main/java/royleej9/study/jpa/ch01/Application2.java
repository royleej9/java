package royleej9.study.jpa.ch01;

import java.sql.SQLException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.h2.tools.Server;

public class Application2 {

	static final Logger logger = LogManager.getLogger(Application2.class.getName());

	public static void main(String[] args) throws SQLException {
		startDatabase();

		final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpastudy");

		UserService userService = new UserService();
		userService.test(entityManagerFactory);

		entityManagerFactory.close();
	}

	private static void startDatabase() throws SQLException {
		new Server().runTool("-tcp", "-web", "-ifNotExists");
	}
}
