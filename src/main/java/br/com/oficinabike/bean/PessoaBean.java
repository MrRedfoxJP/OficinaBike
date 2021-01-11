package br.com.oficinabike.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.omnifaces.util.Messages;

import br.com.oficinabike.dao.PessoaDAO;
import br.com.oficinabike.domain.Pessoa;
import br.com.oficinabike.util.HibernateUtil;
import br.com.oficinabike.util.JSFUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class PessoaBean implements Serializable {
	private List<Pessoa> listaPessoa;
	private Pessoa pessoa;
	private String nomeBusca;

	public void salvar() {
		try {
			PessoaDAO pesDao = new PessoaDAO();
			pesDao.merge(pessoa);

			pessoa = new Pessoa();

			listaPessoa = pesDao.listar();

			Messages.addGlobalInfo("Pessoa salva com sucesso!");

		} catch (RuntimeException ex) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar pessoa");
			ex.printStackTrace();
		}

	}

	@PostConstruct
	public void listar() {
		try {
			PessoaDAO pesDao = new PessoaDAO();
			listaPessoa = pesDao.listar();
		} catch (RuntimeException ex) {
			Messages.addGlobalError("Ocorreu um erro no metodo de listar");
			ex.printStackTrace();
		}

	}

	public void excluir(ActionEvent event) throws Exception {
		try {
			pessoa = (Pessoa) event.getComponent().getAttributes().get("pessoaSelecionado");

			PessoaDAO pessoaDao = new PessoaDAO();

			pessoaDao.excluir(pessoa);

			listaPessoa = pessoaDao.listar();
			Messages.addGlobalInfo("Pessoa excluido com sucesso!");
			listaPessoa = pessoaDao.listar();
		} catch (RuntimeException e) {
			Messages.addGlobalError("Ocorreu um erro ao tentar excluir uma pessoa");
			e.printStackTrace();
		}

	}

	public void novo() {
		try {
			pessoa = new Pessoa();

		} catch (RuntimeException ex) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar uma nova pessoa");
			ex.printStackTrace();
		}
	}

	public void editar(ActionEvent event) {
		try {
			pessoa = (Pessoa) event.getComponent().getAttributes().get("pessoaSelecionado");

		} catch (RuntimeException ex) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar uma pessoa");
			ex.printStackTrace();
		}

	}

	public void buscar() {
		try {
			listaPessoa = new PessoaDAO().listaPessoa(nomeBusca);
			if (listaPessoa == null) {
				JSFUtil.addWarnMessage("Nenhum item encontrado");
			}
		} catch (Exception e) {
			JSFUtil.addErrorMessage("ERRO ao buscar: " + e.getMessage());
			e.printStackTrace();
		}
	}
	// public void imprimir() {
	// try {
	// String caminho = Faces.getRealPath("/reports/pessoas.jasper");
	//
	// Map<String, Object> parametros = new HashMap<>();
	//
	// Connection conexao = HibernateUtil.getConexao();
	//
	// JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros,
	// conexao);
	//
	// JasperViewer.viewReport(relatorio, false);
	// } catch (JRException e) {
	// Messages.addGlobalError("Ocorreu um erro ao tentar gerar um relatório");
	// e.printStackTrace();
	// }
	// }

	public void executarRelatorio() throws Exception {
		Connection conexao = HibernateUtil.getConexao();
		Map<String, Object> parametros = new HashMap<>();

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

		InputStream reportStream = context.getExternalContext().getResourceAsStream("/reports/pessoas.jasper");

		response.setContentType("application/pdf");

		try {
			ServletOutputStream servletOutputStream = response.getOutputStream();

			JasperRunManager.runReportToPdfStream(reportStream, servletOutputStream,

					parametros, conexao);
			servletOutputStream.flush();
			servletOutputStream.close();

		} catch (JRException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			context.responseComplete();
		}
	}
	
	public List<Pessoa> getListaPessoa() {
		return listaPessoa;
	}

	public void setListaPessoa(List<Pessoa> listaPessoa) {
		this.listaPessoa = listaPessoa;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public String getNomeBusca() {
		return nomeBusca;
	}

	public void setNomeBusca(String nomeBusca) {
		this.nomeBusca = nomeBusca;
	}
}
