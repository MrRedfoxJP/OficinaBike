package br.com.oficinabike.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import br.com.oficinabike.dao.PessoaDAO;
import br.com.oficinabike.dao.UsuarioDAO;
import br.com.oficinabike.domain.Pessoa;
import br.com.oficinabike.domain.Usuario;

@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class AutenticacaoBean implements Serializable {
	private Usuario usuario;
	private Pessoa pessoa;

	@PostConstruct
	public void iniciar() {
		usuario = new Usuario();
		pessoa = new Pessoa();
		usuario.setPessoa(pessoa);
	}

	public void autenticar() {
		try {
			//teste para commit

			UsuarioDAO userDao = new UsuarioDAO();

			usuario = userDao.autenticar(usuario.getPessoa().getCpf(), usuario.getSenha());
			
			if (usuario == null) {
				System.out.println("Falha ao autenticar");
				Messages.addGlobalError("O usuario ou senha estão incorretos");
				Faces.redirect("./paginas/Autenticacao.xhtml");
				Faces.getSession().removeAttribute("autenticacaoBean");
				//Faces.getSession().invalidate();

				
			} else {
				Messages.addGlobalInfo("Usuario autenticado com sucesso!");
				Faces.redirect("./paginas/index.xhtml");
			}

		} catch (IOException e) {
			Messages.addGlobalError("Erro ao redirecionar a pagina principal");
			e.printStackTrace();
		}

	}
	
	public void salvar() throws Exception {
		try {
			PessoaDAO pesDao = new PessoaDAO();
			pesDao.merge(pessoa);
			
			List<Pessoa> listPessoa = pesDao.listaPessoa(pessoa.getNome());
			
			UsuarioDAO usuDao = new UsuarioDAO();			
			SimpleHash hash = new SimpleHash("md5", usuario.getSenha());
			usuario.setSenha(hash.toHex());
			usuario.getPessoa().setId(listPessoa.get(0).getId());			
			usuDao.merge(usuario);

			Messages.addGlobalInfo("Pessoa salva com sucesso!");

		} catch (RuntimeException ex) {
			Messages.addGlobalError("Ocorreu um erro ao tentar salvar pessoa");
			ex.printStackTrace();
		}

	}

	public String sair() {
		usuario = null;
		Faces.getSession().removeAttribute("autenticacaoBean");
		return "/paginas/Autenticacao.xhtml?faces-redirect=true";
	}
	
	public String primeiroAcesso() {
		return "/paginas/cadastro.xhtml?faces-redirect=true";
	}

	public Pessoa getPessoa() {
		return pessoa;
	}
	
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	
	public Usuario getUsuario() {
		
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
