package br.com.oficinabike.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import lombok.Data;

@Data	
@Entity
@SequenceGenerator(name = "produto_sequence", sequenceName = "produto_sequence", allocationSize = 1)
public class Produto {
	
	
	@Id
	@GeneratedValue(generator = "produto_sequence", strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(length = 80)
	private String nome;
	
	
	private Double valorCompra;
	
	@Column(length = 10)
	private Double porcentagemLucroSugerido;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private Marca marca;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = true)
	private Modelo modelo;
	
	@Transient
	private Double precoTotal;
	
	
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

	public Double getValorCompra() {
		return valorCompra;
	}

	public void setValorCompra(Double valorCompra) {
		this.valorCompra = valorCompra;
	}

	public Double getPorcentagemLucroSugerido() {
		return porcentagemLucroSugerido;
	}

	public void setPorcentagemLucroSugerido(Double porcentagemLucroSugerido) {
		this.porcentagemLucroSugerido = porcentagemLucroSugerido;
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
	public Double getPrecoTotal() {
		
		precoTotal = valorCompra + valorCompra * porcentagemLucroSugerido / 100;
		return precoTotal;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((marca == null) ? 0 : marca.hashCode());
		result = prime * result + ((modelo == null) ? 0 : modelo.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime
				* result
				+ ((porcentagemLucroSugerido == null) ? 0
						: porcentagemLucroSugerido.hashCode());
		result = prime * result
				+ ((valorCompra == null) ? 0 : valorCompra.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Produto other = (Produto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (marca == null) {
			if (other.marca != null)
				return false;
		} else if (!marca.equals(other.marca))
			return false;
		if (modelo == null) {
			if (other.modelo != null)
				return false;
		} else if (!modelo.equals(other.modelo))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (porcentagemLucroSugerido == null) {
			if (other.porcentagemLucroSugerido != null)
				return false;
		} else if (!porcentagemLucroSugerido
				.equals(other.porcentagemLucroSugerido))
			return false;
		if (valorCompra == null) {
			if (other.valorCompra != null)
				return false;
		} else if (!valorCompra.equals(other.valorCompra))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return String.format("%s[id=%d]", getClass().getSimpleName(), getId());
	}
	
	
	//public void calcular(){
		
		//precoTotal = (valorCompra*porcentagemLucroSugerido)/100;
	//}
	
	

	
	
}
