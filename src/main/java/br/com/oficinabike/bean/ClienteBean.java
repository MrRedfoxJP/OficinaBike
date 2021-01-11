package br.com.oficinabike.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
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

import br.com.oficinabike.dao.ClienteDAO;
import br.com.oficinabike.domain.Cliente;
import br.com.oficinabike.util.HibernateUtil;
import br.com.oficinabike.util.JSFUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

@SuppressWarnings("serial")
@ManagedBean(name = "MBCliente")
@ViewScoped
public class ClienteBean implements Serializable {

	private List<Cliente> listaCliente;
	private String nomeBusca;
	private Cliente cliente;

	public void salvar() {

		try {

			ClienteDAO clidao = new ClienteDAO();
			clidao.merge(cliente);

			cliente = new Cliente();

			listaCliente = clidao.listar();

			JSFUtil.addInfoMessage("Cliente salvo com Sucesso");

		} catch (Exception e) {
			JSFUtil.addInfoMessage("Erro ao Salvar Cliente");
			e.printStackTrace();
		}
	}

	@PostConstruct
	public void listar() {
		try {
			ClienteDAO cliDao = new ClienteDAO();
			listaCliente = cliDao.listar();
		} catch (Exception e) {
			Messages.addGlobalError("Ocorreu um erro durante a listagem");
			e.printStackTrace();
		}
	}

	public void buscar() {
		try {
			listaCliente = new ClienteDAO().listaCliente(nomeBusca);
			if (listaCliente.isEmpty()) {
				JSFUtil.addWarnMessage("Lista Vazia");
			}
		} catch (Exception e) {
			JSFUtil.addErrorMessage("ERRO ao buscar: " + e.getMessage());
			e.printStackTrace();
		}

	}

	public void excluir(ActionEvent event) {

		Cliente clienteSelecionado = (Cliente) event.getComponent().getAttributes().get("clienteSelecionado");

		try {
			new ClienteDAO().excluir(clienteSelecionado);
			listaCliente = new ArrayList<Cliente>();
			JSFUtil.addInfoMessage("Excluido com sucesso!!");
			listaCliente = new ClienteDAO().listar();
		} catch (Exception e) {
			JSFUtil.addErrorMessage("ERRO ao Excluir: " + e.getMessage());
			System.out.println("ERROOOO...: " + e.getMessage());
		}

	}

	public void editar(ActionEvent event) {
		cliente = (Cliente) event.getComponent().getAttributes().get("clienteSelecionado");
	}

	public void novo() {
		cliente = new Cliente();
	}

	// public void imprimir() {
	// try {
	// String caminho = Faces.getRealPath("/reports/cliente.jasper");
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

		InputStream reportStream = context.getExternalContext().getResourceAsStream("/reports/cliente.jasper");

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

	public List<Cliente> getListaCliente() {
		return listaCliente;
	}

	public void setListaCliente(List<Cliente> listaCliente) {
		this.listaCliente = listaCliente;
	}

	public String getNomeBusca() {
		return nomeBusca;
	}

	public void setNomeBusca(String nomeBusca) {
		this.nomeBusca = nomeBusca;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}
