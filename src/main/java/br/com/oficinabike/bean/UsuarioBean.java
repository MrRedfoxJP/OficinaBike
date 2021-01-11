package br.com.oficinabike.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.omnifaces.util.Messages;

import br.com.oficinabike.dao.PessoaDAO;
import br.com.oficinabike.dao.UsuarioDAO;
import br.com.oficinabike.domain.Pessoa;
import br.com.oficinabike.domain.Usuario;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class UsuarioBean implements Serializable {
	private List<Usuario> listaUsuario;
	private Usuario usuario;
	private List<Pessoa> listaPessoa;
	
	public void salvar(){
		try {
			UsuarioDAO userDao = new UsuarioDAO();
			
			SimpleHash hash = new SimpleHash("md5", usuario.getSenha());
			usuario.setSenha(hash.toHex());
			
			userDao.merge(usuario);

			usuario = new Usuario();

			PessoaDAO pesDao = new PessoaDAO();
			listaPessoa = pesDao.listar();

			listaUsuario = userDao.listar();

			Messages.addGlobalInfo("Salvo com sucesso!");

		} catch (Exception e) {
			Messages.addGlobalError("Ocorreu um erro ao salvar");
			e.printStackTrace();
		}
	}
	
	@PostConstruct
	public void listar(){
		try {
			UsuarioDAO userDao = new UsuarioDAO();
			listaUsuario = userDao.listar();
		} catch (RuntimeException ex) {
			Messages.addGlobalError("Ocorreu um erro no metodo de listar");
			ex.printStackTrace();

		}
	}
	
	public void novo() {
		try {
			usuario = new Usuario();

			PessoaDAO pesDao = new PessoaDAO();
			listaPessoa = pesDao.listar();
		} catch (RuntimeException ex) {
			Messages.addGlobalError("Ocorreu um erro ao tentar gerar um novo usuario");
			ex.printStackTrace();
		}

	}
	
	public void editar(ActionEvent event) {
		try {
			usuario = (Usuario) event.getComponent().getAttributes().get("usuarioSelecionado");

			PessoaDAO pesDao = new PessoaDAO();
			listaPessoa = pesDao.listar();
		} catch (RuntimeException ex) {
			Messages.addGlobalError("Ocorreu um erro ao tentar selecionar um usuario");
			ex.printStackTrace();
		}
	}

	public void excluir(ActionEvent event) throws Exception {
		try {
			usuario = (Usuario) event.getComponent().getAttributes().get("usuarioSelecionado");

			UsuarioDAO userDao = new UsuarioDAO();
			userDao.excluir(usuario);
			Messages.addGlobalInfo("Usuario excluido com sucesso!");
			listaUsuario = userDao.listar();
		} catch (RuntimeException e) {
			Messages.addGlobalError("Ocorreu um erro ao tentar excluir um usuario");
			e.printStackTrace();
		}
	}
	
	public List<Usuario> getListaUsuario() {
		return listaUsuario;
	}
	public void setListaUsuario(List<Usuario> listaUsuario) {
		this.listaUsuario = listaUsuario;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public List<Pessoa> getListaPessoa() {
		return listaPessoa;
	}
	public void setListaPessoa(List<Pessoa> listaPessoa) {
		this.listaPessoa = listaPessoa;
	}	
	
}
