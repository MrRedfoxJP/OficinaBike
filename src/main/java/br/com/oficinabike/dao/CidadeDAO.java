package br.com.oficinabike.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.oficinabike.domain.Cidade;
import br.com.oficinabike.util.HibernateUtil;

@SuppressWarnings("serial")
public class CidadeDAO extends GenericDAO<Cidade> {
	@SuppressWarnings("unchecked")
	public List<Cidade> listaCidade(String nome) throws Exception {

		Session sessao = HibernateUtil.getSessionFactory().openSession();
		List<Cidade> listaCidade = null;

		try {
			Criteria criteria = sessao.createCriteria(Cidade.class);

			if (!"".equals(nome)) {
				criteria.add(Restrictions.ilike("nome", "%" + nome + "%"));
			}

			criteria.addOrder(Order.asc("nome"));
			listaCidade = criteria.list();
		} catch (Exception e) {
			System.out.println("ERRO...." + e.getMessage());
			throw new Exception(e.getMessage());
		} finally {
			sessao.close();
		}

		return listaCidade;
	}
}
