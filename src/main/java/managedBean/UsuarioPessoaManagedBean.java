package managedBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.xml.bind.DatatypeConverter;

import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import com.google.gson.Gson;

import dao.DaoEmail;
import dao.DaoGeneric;
import dao.DaoUsuario;
import model.EmailUser;
import model.UsuarioPessoa;

@ManagedBean(name = "usuarioPessoaManagedBean")
@ViewScoped
public class UsuarioPessoaManagedBean {

	private UsuarioPessoa usuarioPessoa = new UsuarioPessoa();

	private List<UsuarioPessoa> list = new ArrayList<UsuarioPessoa>();
	private DaoUsuario<UsuarioPessoa> daoGeneric = new DaoUsuario<UsuarioPessoa>();
	private BarChartModel barCharModel = new BarChartModel();
	private EmailUser emailuser = new EmailUser();
	private DaoEmail<EmailUser> daoEmail = new DaoEmail<EmailUser>();
	private String campoPesquisa;

	@PostConstruct
	public void init() {

		list = daoGeneric.listar(UsuarioPessoa.class);

		montarGrafico();
	}

	private void montarGrafico() {
		barCharModel = new BarChartModel();
		ChartSeries userSalario = new ChartSeries();/* grupo de funcionarios */

		for (UsuarioPessoa usuarioPessoa : list) {

			userSalario.set(usuarioPessoa.getNome(), usuarioPessoa.getSalario()); // add salario
		}
		barCharModel.addSeries(userSalario); // adiciona o grupo no bar model
		barCharModel.setTitle("Grafico de Salarios");// nome do grafico
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
		usuarioPessoa = new UsuarioPessoa();
		init();
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
			init();
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

	public EmailUser getEmailuser() {
		return emailuser;
	}

	public void setEmailuser(EmailUser emailuser) {
		this.emailuser = emailuser;
	}

	public void addEmail() {
		emailuser.setUsuarioPessoa(usuarioPessoa); // adiciona objeto pessoa
		emailuser = daoEmail.updateMerge(emailuser); // update no banco
		usuarioPessoa.getEmails().add(emailuser);
		emailuser = new EmailUser();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Resultado:", "Salvo com sucesso!"));

	}

	public void removerEmail() throws Exception {

		String codigoemail = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("codigoemail");

		EmailUser remover = new EmailUser();
		remover.setId(Long.parseLong(codigoemail));
		daoEmail.deletarPorId(remover);
		usuarioPessoa.getEmails().remove(remover);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Resultado:", "Deletado com sucesso!"));

	}

	public void pesquisar() {
		list = daoGeneric.pesquisar(campoPesquisa);
		montarGrafico(); 
	}

	public void setCampoPesquisa(String campoPesquisa) {
		this.campoPesquisa = campoPesquisa;
	}

	public String getCampoPesquisa() {
		return campoPesquisa;
	}
	
	public void upLoad(FileUploadEvent image) {
		
		String imagem = "data:image/png;base64," + DatatypeConverter.printBase64Binary(image.getFile().getContents());
		usuarioPessoa.setImagem(imagem);
		
	}
	
	public void download() throws IOException {
	Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	String fileDownloadID = params.get("fileDownloadID");
	UsuarioPessoa pessoa = daoGeneric.pesquisar(Long.parseLong(fileDownloadID), UsuarioPessoa.class);
	byte[] imagem = new org.apache.tomcat.util.codec.binary.Base64().decodeBase64(pessoa.getImagem().split("\\,")[1]);
	
	HttpServletResponse response = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
	
	response.addHeader("Content-Disposition", "attachment;filename=download.png");
	response.setContentType("application/octet-stream");
	response.setContentLength(imagem.length);
	response.getOutputStream().write(imagem);
	response.getOutputStream().flush();
	FacesContext.getCurrentInstance().getResponseComplete();
	
		
	}
	
}
