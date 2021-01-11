package br.com.oficinabike.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;


@Entity
@SequenceGenerator(name = "pessoa_seq", sequenceName = "pessoa_cod_seq", allocationSize =1)
public class Pessoa{
	
	@Id
	@GeneratedValue(generator = "pessoa_seq", strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(length = 50, nullable = false)
	private String nome;

	@Column(length = 20, nullable = false)
	private String cpf;
	
	@Column(length = 50, nullable = false)
	private String rua;

	@Column(nullable = false)
	private Short numero;

	@Column(length = 50, nullable = false)
	private String bairro;

	@Column(length = 15, nullable = false)
	private String cep;

	@Column(length = 30)
	private String complemento;

	@Column(length = 20, nullable = false)
	private String telefone;

	@Column(length = 20, nullable = false)
	private String celular;

	@Column(length = 30, nullable = false)
	private String email;

	@Column(length = 30, nullable = false)
	private String cidade;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getRua() {
		return rua;
	}
	public void setRua(String rua) {
		this.rua = rua;
	}
	public Short getNumero() {
		return numero;
	}
	public void setNumero(Short numero) {
		this.numero = numero;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getCelular() {
		return celular;
	}
	public void setCelular(String celular) {
		this.celular = celular;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	
}
