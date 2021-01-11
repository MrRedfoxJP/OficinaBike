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
import br.com.oficinabike.dao.ProdutoDAO;
import br.com.oficinabike.domain.Marca;
import br.com.oficinabike.domain.Modelo;
import br.com.oficinabike.domain.Produto;
import br.com.oficinabike.util.HibernateUtil;
import br.com.oficinabike.util.JSFUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

@SuppressWarnings("serial")
@ManagedBean(name = "MBProduto")
@ViewScoped
public class ProdutoBean implements Serializable {
	private List<Produto> listaProduto;
	private List<Marca> listaMarca;
	private String nomeBusca;
	private Marca marca;
	private List<Modelo> listaModelo;
	private Modelo modeloSelecionado;
	private Produto produto;

	@PostConstruct
	public void listar() {

		try {
			ProdutoDAO proDao = new ProdutoDAO();
			listaProduto = proDao.listar();

		} catch (Exception e) {
			Messages.addGlobalError("Ocorreu um erro durante a listagem");
			e.printStackTrace();

		}

	}

	public void salvar() {

		try {
			produto.setMarca(marca);
			produto.setModelo(modeloSelecionado);
			ProdutoDAO prodDao = new ProdutoDAO();
			prodDao.merge(produto);
			JSFUtil.addInfoMessage("Salvo com sucesso");

			produto = new Produto();

			marca = new Marca();
			modeloSelecionado = new Modelo();

			listaProduto = new ProdutoDAO().listar();

		} catch (Exception e) {
			JSFUtil.addErrorMessage("Erro ao salvar Produto: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void buscar() {
		try {
			listaProduto = new ProdutoDAO().listaProduto(nomeBusca);
			if (listaProduto == null) {
				JSFUtil.addWarnMessage("Nenhum item encontrado");
			}
		} catch (Exception e) {
			JSFUtil.addErrorMessage("ERRO ao buscar: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void excluir(ActionEvent event) {
		ProdutoDAO prodDao = new ProdutoDAO();
		Produto produto = (Produto) event.getComponent().getAttributes().get("produto");
		try {
			prodDao.excluir(produto);
			JSFUtil.addInfoMessage("Excluido com sucesso!!");
			listaProduto = new ProdutoDAO().listar();
		} catch (Exception e) {
			JSFUtil.addErrorMessage("ERRO ao Excluir: " + e.getMessage());
			System.out.println("ERROOOO...: " + e.getMessage());
		}

	}

	public void editar(ActionEvent event) {

		try {
			produto = (Produto) event.getComponent().getAttributes().get("produto");

			MarcaDAO marDao = new MarcaDAO();
			listaMarca = marDao.listar();

			listaModelo = new ArrayList<Modelo>();

		} catch (RuntimeException ex) {
			JSFUtil.addErrorMessage("Ocorreu um erro ao gerar um novo produto");
			ex.printStackTrace();

		}
	}

	public void novo() {

		try {
			produto = new Produto();

			marca = new Marca();

			MarcaDAO marDao = new MarcaDAO();
			listaMarca = marDao.listar();

			listaModelo = new ArrayList<Modelo>();

		} catch (RuntimeException ex) {
			JSFUtil.addErrorMessage("Ocorreu um erro ao gerar um novo produto");
			ex.printStackTrace();

		}
	}

	public void onMarcaChange() {
		try {
			if (marca != null) {

				ModeloDAO marDao = new ModeloDAO();
				listaModelo = marDao.buscarPorMarca(marca.getId());

			} else {
				listaModelo = new ArrayList<>();
			}
		} catch (Exception e) {
			JSFUtil.addErrorMessage("ERRO ao Mudar: " + e.getMessage());
			System.out.println("ERROOOO...:fau " + e.getMessage());
		}

	}

	// public void imprimir() {
	// try {
	// String caminho = Faces.getRealPath("/reports/produtos.jasper");
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

		InputStream reportStream = context.getExternalContext().getResourceAsStream("/reports/produtos.jasper");

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

	/** ======================================Getters And Setters===============================================================**/

	public List<Produto> getListaProduto() {
		return listaProduto;
	}

	public void setListaProduto(List<Produto> listaProduto) {
		this.listaProduto = listaProduto;
	}

	public List<Marca> getListaMarca() {
		return listaMarca;
	}

	public void setListaMarca(List<Marca> listaMarca) {
		this.listaMarca = listaMarca;
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

	public Modelo getModeloSelecionado() {
		return modeloSelecionado;
	}

	public void setModeloSelecionado(Modelo modeloSelecionado) {
		this.modeloSelecionado = modeloSelecionado;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Marca getMarca() {
		return marca;
	}

	public void setMarca(Marca marca) {
		this.marca = marca;
	}

}
