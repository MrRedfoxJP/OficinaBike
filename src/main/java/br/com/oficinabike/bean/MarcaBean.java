package br.com.oficinabike.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import lombok.Data;

import org.omnifaces.util.Messages;

import br.com.oficinabike.dao.MarcaDAO;
import br.com.oficinabike.domain.Marca;
import br.com.oficinabike.domain.Modelo;
import br.com.oficinabike.util.JSFUtil;

@Data
@SuppressWarnings("serial")
@ManagedBean(name = "MBMarca")
@ViewScoped
public class MarcaBean implements Serializable {

	private List<Marca> listaMarca;
	private String nomeBusca;
	private Marca marca;
	private Modelo modelo;

	public void salvar() {
		try {
			MarcaDAO marcaDao = new MarcaDAO();
			marcaDao.merge(marca);

			marca = new Marca();
			listaMarca = marcaDao.listar();

			JSFUtil.addInfoMessage("Marca 	Salva com Sucesso");

		} catch (Exception e) {
			JSFUtil.addErrorMessage("Erro ao salvar Marca: " + e.getMessage());
		}

	}

	@PostConstruct
	public void listar() {
		try {
			MarcaDAO marcaDao = new MarcaDAO();
			listaMarca = marcaDao.listar();
		} catch (Exception e) {
			Messages.addGlobalError("Ocorreu um erro durante a listagem");
			e.printStackTrace();
		}
	}

	public void buscar() {

		try {
			listaMarca = new MarcaDAO().listaMarca(nomeBusca);
			if (listaMarca.isEmpty()) {
				JSFUtil.addWarnMessage("Nenhum item encontrado");
			}
		} catch (Exception e) {
			JSFUtil.addErrorMessage("ERRO ao buscar: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void excluir(ActionEvent event) {

		Marca marcaSelecionada = (Marca) event.getComponent().getAttributes()
				.get("marcaSelecionada");

		try {
			new MarcaDAO().excluir(marcaSelecionada);
			listaMarca = new ArrayList<Marca>();
			JSFUtil.addInfoMessage("Excluido com sucesso!!");
			listaMarca = new MarcaDAO().listar();
		} catch (Exception e) {
			JSFUtil.addErrorMessage("ERRO ao Excluir: " + e.getMessage());
			System.out.println("ERROOOO...: " + e.getMessage());
		}

	}

	public void editar(ActionEvent event) {
		marca = (Marca) event.getComponent().getAttributes()
				.get("marcaSelecionada");
		
	}

	public void novo() {
		marca = new Marca();
		
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

	public Marca getMarca() {
		return marca;
	}

	public void setMarca(Marca marca) {
		this.marca = marca;
	}

	public Modelo getModelo() {
		return modelo;
	}

	public void setModelo(Modelo modelo) {
		this.modelo = modelo;
	}
	

}
