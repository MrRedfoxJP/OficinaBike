package br.com.oficinabike.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;


@Entity
@SequenceGenerator(name = "usuario_seq", sequenceName = "usuario_cod_seq", allocationSize =1)
public class Usuario {
	
	@Id
	@GeneratedValue(generator = "usuario_seq", strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(length = 32, nullable = false)
	private String senha;

	@Column(nullable = false)
	private Character tipo;
	
	@OneToOne
	@JoinColumn(nullable = false)
	private Pessoa pessoa;
	
	@Transient
	private String senhaSemCriptografia;	
	

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Character getTipo() {
		return tipo;
	}

	@Transient
	public String getTipoFormatado() {
		String tipoFormatado = null;

		if (tipo == 'A') {
			tipoFormatado = "Administrador";
		} else if (tipo == 'F') {
			tipoFormatado = "Funcionario";
		}

		return tipoFormatado;
	}

	public void setTipo(Character tipo) {
		this.tipo = tipo;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public String getSenhaSemCriptografia() {
		return senhaSemCriptografia;
	}

	public void setSenhaSemCriptografia(String senhaSemCriptografia) {
		this.senhaSemCriptografia = senhaSemCriptografia;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	

}
