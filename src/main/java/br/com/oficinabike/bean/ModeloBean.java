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

import br.com.oficinabike.dao.MarcaDAO;
import br.com.oficinabike.dao.ModeloDAO;
import br.com.oficinabike.domain.Marca;
import br.com.oficinabike.domain.Modelo;
import br.com.oficinabike.util.HibernateUtil;
import br.com.oficinabike.util.JSFUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

@SuppressWarnings("serial")
@ManagedBean(name = "MBModelo")
@ViewScoped
public class ModeloBean implements Serializable {

	private List<Marca> listaMarca;
	private Marca marcaSelecionado;
	private String nomeBusca;
	private List<Modelo> listaModelo;
	private Modelo modelo;

	@PostConstruct
	public void listar() {

		try {
			listaMarca = new MarcaDAO().listaMarca("");
			ModeloDAO modDao = new ModeloDAO();
			listaModelo = modDao.listar();
		} catch (Exception e) {
			Messages.addGlobalError("Ocorreu um erro durante a listagem");
			e.printStackTrace();

		}

	}

	public void Salvar() {
		try {
			modelo.setMarca(marcaSelecionado);
			ModeloDAO modDao = new ModeloDAO();
			modDao.merge(modelo);

			modelo = new Modelo();
			marcaSelecionado = new Marca();
			listaModelo = modDao.listar();

			JSFUtil.addInfoMessage("Modelo Salvo com Sucesso");
		} catch (Exception e) {
			JSFUtil.addErrorMessage("Erro ao salvar Modelo: " + e.getMessage());
			e.printStackTrace();
		}

	}

	public void buscar() {

		try {
			listaModelo = new ModeloDAO().listaModelo(nomeBusca);
			if (listaModelo.isEmpty()) {
				JSFUtil.addWarnMessage("Nenhum item encontrado");
			}
		} catch (Exception e) {
			JSFUtil.addErrorMessage("ERRO ao buscar: " + e.getMessage());
			e.printStackTrace();
		}

	}

	public void excluir(ActionEvent event) {
		Modelo modelo = (Modelo) event.getComponent().getAttributes().get("modelo");

		try {
			new ModeloDAO().excluir(modelo);
			listaModelo = new ArrayList<Modelo>();
			JSFUtil.addInfoMessage("Excluido com sucesso!!");
			listaModelo = new ModeloDAO().listar();
		} catch (Exception e) {
			JSFUtil.addErrorMessage("ERRO ao Excluir: " + e.getMessage());
			System.out.println("ERROOOO...: " + e.getMessage());
		}
	}

	public void editar(ActionEvent event) {
		try {
			modelo = (Modelo) event.getComponent().getAttributes().get("modelo");

			MarcaDAO marcaDao = new MarcaDAO();
			listaMarca = marcaDao.listar();
			marcaSelecionado = new Marca();

		} catch (RuntimeException ex) {
			JSFUtil.addErrorMessage("Ocorreu um erro ao gerar um novo modelo");
			ex.printStackTrace();

		}

	}

	public void novo() {
		try {
			modelo = new Modelo();

			MarcaDAO marcaDao = new MarcaDAO();
			listaMarca = marcaDao.listar();

		} catch (RuntimeException ex) {
			JSFUtil.addErrorMessage("Ocorreu um erro ao gerar um novo modelo");
			ex.printStackTrace();

		}
	}

	// public void imprimir() {
	// try {
	// String caminho = Faces.getRealPath("/reports/relatorio.jasper");
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

		InputStream reportStream = context.getExternalContext().getResourceAsStream("/reports/relatorio.jasper");

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

	public List<Marca> getListaMarca() {
		return listaMarca;
	}

	public void setListaMarca(List<Marca> listaMarca) {
		this.listaMarca = listaMarca;
	}

	public Marca getMarcaSelecionado() {
		return marcaSelecionado;
	}

	public void setMarcaSelecionado(Marca marcaSelecionado) {
		this.marcaSelecionado = marcaSelecionado;
	}

	public String getNomeBusca() {
		return nomeBusca;
	}

	public void setNomeBusca(String nomeBusca) {
		this.nomeBusca = nomeBusca;
	}

	public List<Modelo> getListaModelo() {
		return listaModelo;
	}

	public void setListaModelo(List<Modelo> listaModelo) {
		this.listaModelo = listaModelo;
	}

	public Modelo getModelo() {
		return modelo;
	}

	public void setModelo(Modelo modelo) {
		this.modelo = modelo;
	}

}
