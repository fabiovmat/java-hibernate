package managedBean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import com.google.gson.Gson;

import dao.DaoUsuario;
import model.UsuarioPessoa;

@ManagedBean(name = "usuarioPessoaManagedBean")
@ViewScoped
public class UsuarioPessoaManagedBean {

	private UsuarioPessoa usuarioPessoa = new UsuarioPessoa();

	private List<UsuarioPessoa> list = new ArrayList<UsuarioPessoa>();
	private DaoUsuario<UsuarioPessoa> daoGeneric = new DaoUsuario<UsuarioPessoa>();
	private BarChartModel barCharModel = new BarChartModel();
	
	
	@PostConstruct
	public void init() {
		list = daoGeneric.listar(UsuarioPessoa.class);
		
		ChartSeries userSalario = new ChartSeries("Salario do Usuario");/*grupo de funcionarios*/
		userSalario.setLabel("Users");
		for (UsuarioPessoa usuarioPessoa : list) {
		userSalario.set(usuarioPessoa.getSalario(), usuarioPessoa.getSalario() ); //add salario
		}
		barCharModel.addSeries(userSalario); //adiciona o grupo no bar model
		barCharModel.setTitle("Grafico de Salarios");//nome do grafico
		}
	

	public BarChartModel getBarCharModel() {
		return barCharModel;
	}

	
	public void pesquisaCep(AjaxBehaviorEvent event) {
		try {

			// System.out.println("Cep digitado + " + usuarioPessoa.getCep());
			URL url = new URL("https://viacep.com.br/ws/" + usuarioPessoa.getCep() + "/json/"); // pega o cep que foi
																								// digitado
			URLConnection connection = url.openConnection(); // abre uma conexao com a url
			InputStream is = connection.getInputStream(); // abre uma conexao com fluxo de dados
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8")); // le o fluxo de dados vindo
																						// como retorno com objeto de
																						// inpoutstream e utiliza
																						// codificaçao UTF para tratar
																						// acentuacação

			String cep = "";
			StringBuilder jsonCep = new StringBuilder();

			while ((cep = br.readLine()) != null) { // le o retorno e enquanto for difrente de nulo
				jsonCep.append(cep);// adiciona

			}

			UsuarioPessoa userCepPessoa = new Gson().fromJson(jsonCep.toString(), UsuarioPessoa.class);			
			
			usuarioPessoa.setCep(userCepPessoa.getCep());
			usuarioPessoa.setLogradouro(userCepPessoa.getLogradouro());
			usuarioPessoa.setComplemento(userCepPessoa.getComplemento());
			usuarioPessoa.setBairro(userCepPessoa.getBairro());
			usuarioPessoa.setLocalidade(userCepPessoa.getLocalidade());
			usuarioPessoa.setUf(userCepPessoa.getUf());
			usuarioPessoa.setUnidade(userCepPessoa.getUnidade());
			usuarioPessoa.setIbge(userCepPessoa.getIbge());
			usuarioPessoa.setGia(userCepPessoa.getGia());
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public UsuarioPessoa getUsuarioPessoa() {
		return usuarioPessoa; // get e set somente do usuario pessoa nao precisa DaoGeneric
	}

	public void setUsuarioPessoa(UsuarioPessoa usuarioPessoa) {
		this.usuarioPessoa = usuarioPessoa;
	}

	public String salvar() {

		daoGeneric.salvar(usuarioPessoa);
		list.add(usuarioPessoa);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", "Salvo com sucesso!"));
		return "";
	}

	public String novo() {

		usuarioPessoa = new UsuarioPessoa();
		return "";
	}

	public List<UsuarioPessoa> getList() {

		return list;
	}

	public String remover() {

		try {
			daoGeneric.removerUsuario(usuarioPessoa);
			list.remove(usuarioPessoa);
			usuarioPessoa = new UsuarioPessoa();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação", "Deletado com sucesso!"));

		} catch (Exception e) {
			if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Informação: ", "Existem telefones para o usuário!"));
			} else {

				e.printStackTrace();

			}

		}
		return "";

	}

}
