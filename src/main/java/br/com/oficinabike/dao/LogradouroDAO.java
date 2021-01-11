package br.com.oficinabike.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.oficinabike.domain.Logradouro;
import br.com.oficinabike.util.HibernateUtil;

@SuppressWarnings("serial")
public class LogradouroDAO extends GenericDAO<Logradouro> {

	@SuppressWarnings("unchecked")
	public List<Logradouro> listaLogradouro(String nome) throws Exception {

		Session sessao = HibernateUtil.getSessionFactory().openSession();
		List<Logradouro> listaLogradouro = null;

		try {
			Criteria criteria = sessao.createCriteria(Logradouro.class);

			if (!"".equals(nome)) {
				criteria.add(Restrictions.ilike("nome", "%" + nome + "%"));
			}

			criteria.addOrder(Order.asc("nome"));
			listaLogradouro = criteria.list();
		} catch (Exception e) {
			System.out.println("ERRO...." + e.getMessage());
			throw new Exception(e.getMessage());
		} finally {
			sessao.close();
		}

		return listaLogradouro;
	}

}
