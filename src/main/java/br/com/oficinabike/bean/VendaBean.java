package br.com.oficinabike.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
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
import br.com.oficinabike.dao.PessoaDAO;
import br.com.oficinabike.dao.ProdutoDAO;
import br.com.oficinabike.dao.VendaDAO;
import br.com.oficinabike.domain.Cliente;
import br.com.oficinabike.domain.ItemVenda;
import br.com.oficinabike.domain.Pessoa;
import br.com.oficinabike.domain.Produto;
import br.com.oficinabike.domain.Venda;
import br.com.oficinabike.util.HibernateUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class VendaBean implements Serializable {
	private Venda venda;

	private List<Produto> produtos;
	private List<ItemVenda> itensVenda;
	private List<Cliente> clientes;
	private List<Pessoa> pessoas;
	private Produto produto;

	@PostConstruct
	public void novo() {
		try {
			venda = new Venda();
			venda.setPrecoTotal(new BigDecimal("0.00"));

			ProdutoDAO produtoDAO = new ProdutoDAO();
			produtos = produtoDAO.listar();

			itensVenda = new ArrayList<>();
		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar carregar a tela de vendas");
			erro.printStackTrace();
		}
	}

	public void adicionar(ActionEvent evento) {
		Produto produto = (Produto) evento.getComponent().getAttributes().get("produtoSelecionado");

		int achou = -1;
		for (int posicao = 0; posicao < itensVenda.size(); posicao++) {
			if (itensVenda.get(posicao).getProduto().equals(produto)) {
				achou = posicao;
			}
		}

		if (achou < 0) {
			ItemVenda itemVenda = new ItemVenda();
			itemVenda.setPrecoParcial(new BigDecimal(produto.getPrecoTotal()));
			itemVenda.setProduto(produto);
			itemVenda.setQuantidade(new Short("1"));

			itensVenda.add(itemVenda);
		} else {
			ItemVenda itemVenda = itensVenda.get(achou);
			itemVenda.setQuantidade(new Short(itemVenda.getQuantidade() + 1 + ""));
			itemVenda.setPrecoParcial(
					new BigDecimal(produto.getPrecoTotal()).multiply(new BigDecimal(itemVenda.getQuantidade())));
		}

		calcular();
	}

	public void remover(ActionEvent evento) {
		ItemVenda itemVenda = (ItemVenda) evento.getComponent().getAttributes().get("itemSelecionado");

		int achou = -1;
		for (int posicao = 0; posicao < itensVenda.size(); posicao++) {
			if (itensVenda.get(posicao).getProduto().equals(itemVenda.getProduto())) {
				achou = posicao;
			}
		}

		if (achou > -1) {
			itemVenda = itensVenda.get(achou);

			itemVenda.setQuantidade(new Short(itemVenda.getQuantidade() - 1 + ""));

			itemVenda.setPrecoParcial(
					venda.getPrecoTotal().subtract(new BigDecimal(itemVenda.getProduto().getPrecoTotal())));

			if (itemVenda.getQuantidade() == 0) {
				itensVenda.remove(achou);
			}

		}
		calcularemove(evento);
	}

	public void calcular() {
		venda.setPrecoTotal(new BigDecimal("0.00"));

		for (int posicao = 0; posicao < itensVenda.size(); posicao++) {
			ItemVenda itemVenda = itensVenda.get(posicao);
			venda.setPrecoTotal(venda.getPrecoTotal().add(itemVenda.getPrecoParcial()));
		}
	}

	public void calcularemove(ActionEvent evento) {
		ItemVenda itemVenda = (ItemVenda) evento.getComponent().getAttributes().get("itemSelecionado");
		venda.setPrecoTotal(new BigDecimal("0.00"));
		venda.setPrecoTotal(venda.getPrecoTotal().add(itemVenda.getPrecoParcial()));

	}

	public void finalizar() {
		try {
			venda.setHorario(new Date());

			ClienteDAO cliDao = new ClienteDAO();
			clientes = cliDao.listar();

			PessoaDAO pesDao = new PessoaDAO();
			pessoas = pesDao.listar();

		} catch (RuntimeException e) {
			Messages.addGlobalError("Ocorreu um erro ao tentar finalizar venda");
			e.printStackTrace();
		}
	}

	public void salvar() {
		try {
			if (venda.getPrecoTotal().signum() == 0) {
				Messages.addGlobalError("Informe um item para venda");
				return;
			}
			VendaDAO vendaDao = new VendaDAO();
			vendaDao.salvar(venda, itensVenda);

			novo();

			Messages.addGlobalInfo("Venda salva com sucesso!");
		} catch (Exception e) {
			Messages.addGlobalError("Ocorreu um erro ao tentar finalizar venda");
			e.printStackTrace();
		}
	}

	// public void imprimir() {
	// try {
	// String caminho = Faces.getRealPath("/reports/venda.jasper");
	//
	// Map<String, Object> parametros = new HashMap<>();
	//
	// Connection conexao = HibernateUtil.getConexao();

	// JasperPrint relatorio = JasperFillManager.fillReport(caminho, parametros,
	// conexao);

	// JasperViewer.viewReport(relatorio, false);
	//
	// JasperRunManager.runReportToPdf(inputStream, parameters)
	// } catch (JRException e) {
	// Messages.addGlobalError("Ocorreu um erro ao tentar gerar um relatório");
	// e.printStackTrace();
	// }
	// }

	public void imprimirtela() throws JRException {
		if (itensVenda.size() == 0) {
			Messages.addGlobalError("O documento não contém páginas");
		} else {
			RelatorioBean bean = new RelatorioBean();
			bean.visualizarRelatorio(itensVenda);
		}

	}

	public void executarRelatorio() throws Exception {
		Connection conexao = HibernateUtil.getConexao();
		Map<String, Object> parametros = new HashMap<>();

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

		InputStream reportStream = context.getExternalContext().getResourceAsStream("/reports/venda.jasper");

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
	
	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public List<ItemVenda> getItensVenda() {
		return itensVenda;
	}

	public void setItensVenda(List<ItemVenda> itensVenda) {
		this.itensVenda = itensVenda;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public List<Pessoa> getPessoas() {
		return pessoas;
	}

	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}

}
