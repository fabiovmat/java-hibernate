package java_hibernate;

import java.util.List;



import org.junit.Test;

import dao.DaoGeneric;
import model.TelefoneUser;
import model.UsuarioPessoa;

public class TesteHibernate {

	@Test
	public void testeHibernateUitl() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		UsuarioPessoa pessoa = new UsuarioPessoa();

		pessoa.setIdade(45);
		pessoa.setLogin("ed");
		pessoa.setNome("Edgar");
		pessoa.setSenha("123");
		pessoa.setSobrenome("Matt");
		pessoa.setEmail("java@test.com.br");

		daoGeneric.salvar(pessoa);

	}

	@Test
	public void testeBuscar2() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		UsuarioPessoa pessoa = daoGeneric.pesquisar(2L, UsuarioPessoa.class);

		System.out.println(pessoa);

	}

	@Test
	public void testeUpdate() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		UsuarioPessoa pessoa = daoGeneric.pesquisar(51L, UsuarioPessoa.class);

		pessoa.setIdade(99);
		pessoa.setNome("Nome atualizado xxx");

		pessoa = daoGeneric.updateMerge(pessoa);

		System.out.println(pessoa);

	}

	@Test
	public void testeDelete() throws Exception {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();

		UsuarioPessoa pessoa = daoGeneric.pesquisar(301L, UsuarioPessoa.class);

		daoGeneric.deletarPorId(pessoa);

		System.out.println(pessoa);

	}

	@Test
	public void testeConsultar() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> list = daoGeneric.listar(UsuarioPessoa.class);

		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
			System.out.println("-----------------------------------------");

		}

	}

	@Test
	public void testeQuery() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		@SuppressWarnings("unchecked")
		List<UsuarioPessoa> list = daoGeneric.getEntityManager()
				.createQuery("from UsuarioPessoa r where r.nome = 'Matos'").getResultList();

		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}

	}

	@Test
	public void testeQuery2() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		@SuppressWarnings("unchecked")
		List<UsuarioPessoa> list = daoGeneric.getEntityManager()
				.createQuery("from UsuarioPessoa as r ORDER BY r.id desc").setMaxResults(5).getResultList();

		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}

	}

	@Test
	@SuppressWarnings("unchecked")
	public void testQueryListporparametro() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> list = daoGeneric.getEntityManager()
				.createQuery("from UsuarioPessoa r where r.nome = :nome").setParameter("nome", "Matos").getResultList();

		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}

	}

	@Test

	public void testQuerySomaIdade() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		Long somaIdade = (Long) daoGeneric.getEntityManager().createQuery("select sum(r.idade)from UsuarioPessoa r")
				.getSingleResult();

		System.out.println("Soma de todas idades é" + somaIdade);

	}

	@Test
	@SuppressWarnings("unchecked")
	public void testeNamedQuery1() {
		DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
		List<UsuarioPessoa> list = daoGeneric.getEntityManager().createNamedQuery("UsuarioPessoa.findAll")
				.getResultList();

		for (UsuarioPessoa usuarioPessoa : list) {
			System.out.println(usuarioPessoa);
		}

	}

	@Test
	@SuppressWarnings("unchecked")
	public void testeNamedQuery2() {
	DaoGeneric<UsuarioPessoa> daoGeneric = new DaoGeneric<UsuarioPessoa>();
	List<UsuarioPessoa> list = daoGeneric.getEntityManager().createNamedQuery("UsuarioPessoa.findbyName").setParameter("nome", "Matos").getResultList();
	
	for (UsuarioPessoa usuarioPessoa : list) {
		System.out.println(usuarioPessoa);
	}
	
	
	}
	
	
	@Test
	@SuppressWarnings("unchecked")
	public void testeGravaTelefone() {
	@SuppressWarnings("rawtypes")
	DaoGeneric daoGeneric = new DaoGeneric();
	UsuarioPessoa pessoa = (UsuarioPessoa) daoGeneric.pesquisar(2L, UsuarioPessoa.class);
	
	TelefoneUser telefoneUser = new TelefoneUser();
	telefoneUser.setTipo("Celular");
	telefoneUser.setNumero("45455656");
	telefoneUser.setUsuarioPessoa(pessoa);
	
	daoGeneric.salvar(telefoneUser);
	
	
	
		
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testeConsultaTelefone() {
	@SuppressWarnings("rawtypes")
	DaoGeneric daoGeneric = new DaoGeneric();
	UsuarioPessoa pessoa = (UsuarioPessoa) daoGeneric.pesquisar(2L, UsuarioPessoa.class);
	
	for(TelefoneUser fone: pessoa.getTelefoneUsers()) {
		
		System.out.println(fone.getUsuarioPessoa().getNome());
		System.out.println(fone.getNumero());
		System.out.println(fone.getTipo());
		
		System.out.println("---------------------------------------------");
		
		
	}
		
		
	}
	
	
	
	
}