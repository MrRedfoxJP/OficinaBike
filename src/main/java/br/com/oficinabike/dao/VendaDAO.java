package br.com.oficinabike.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.oficinabike.domain.ItemVenda;
import br.com.oficinabike.domain.Venda;
import br.com.oficinabike.util.HibernateUtil;

@SuppressWarnings("serial")
public class VendaDAO extends GenericDAO<Venda> {
	@SuppressWarnings("unchecked")
	public List<Venda> listaVenda(String nome) throws Exception {

		Session sessao = HibernateUtil.getSessionFactory().openSession();
		List<Venda> listaVenda = null;

		try {
			Criteria criteria = sessao.createCriteria(Venda.class);

			if (!"".equals(nome)) {
				criteria.add(Restrictions.ilike("nome", "%" + nome + "%"));
			}

			criteria.addOrder(Order.asc("nome"));
			listaVenda = criteria.list();
		} catch (Exception e) {
			System.out.println("ERRO...." + e.getMessage());
			throw new Exception(e.getMessage());
		} finally {
			sessao.close();
		}

		return listaVenda;
	}

	public void salvar(Venda venda, List<ItemVenda> itensVenda) {
		Session sessao = HibernateUtil.getSessionFactory().openSession();
		Transaction transacao = null;

		try {
			transacao = sessao.beginTransaction();

			sessao.save(venda);

			for (int posicao = 0; posicao < itensVenda.size(); posicao++) {
				ItemVenda itemVenda = itensVenda.get(posicao);
				itemVenda.setVenda(venda);

				sessao.save(itemVenda);
			}

			transacao.commit();
		} catch (RuntimeException ex) {
			if (transacao != null) {
				transacao.rollback();

			}
			throw ex;
		} finally {
			sessao.close();
		}
	}
}
