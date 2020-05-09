package posjavahibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/*classe para ler e deixar a instancia do banco conectada
singleton*/

public class HibernateUtil {

	public static EntityManagerFactory factory = null;

	static {
		init();
	}

	public static void init() {

		try {

			if (factory == null) {
				factory = Persistence.createEntityManagerFactory("java-hibernate");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static EntityManager getEntityManager() {
		return factory.createEntityManager(); /* prova a parte de persistencia */

	}

	public static Object getPrimaryKey(Object entity) {// retorna a primary key
		return factory.getPersistenceUnitUtil().getIdentifier(entity);

	}

}
