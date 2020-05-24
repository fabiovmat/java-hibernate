package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import posjavahibernate.HibernateUtil;

public class DaoGeneric<E> {

	private EntityManager entityManager = HibernateUtil.getEntityManager();

	public void salvar(E entidade) {

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		entityManager.persist(entidade);
		transaction.commit();

	}

	public E updateMerge(E entidade) { // salva ou atualiza

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		E entidadeSalva = entityManager.merge(entidade);
		transaction.commit();

		return entidadeSalva;
	}

	public E pesquisar(Long id, Class<E> entidade) {

		E e = (E) entityManager.find(entidade, id);
		return e;

	}

	public void deletarPorId(E entidade) throws Exception {
		Object id = HibernateUtil.getPrimaryKey(entidade);
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		entityManager
				.createNativeQuery(
						"delete from " + entidade.getClass().getSimpleName().toLowerCase() + " where id = " + id)
				.executeUpdate(); // faz o delete
		transaction.commit();// grava alteracao no banco por ID
	}

	public List<E> listar(Class<E> entidade) {
		EntityTransaction transation = entityManager.getTransaction();
		transation.begin();
		@SuppressWarnings("unchecked")
		List<E> lista = entityManager.createQuery("from " + entidade.getName() + " r").getResultList();
		transation.commit();
		return lista;

	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

}
