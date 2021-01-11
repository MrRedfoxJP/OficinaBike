package br.com.oficinabike.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.oficinabike.domain.Estado;
import br.com.oficinabike.util.HibernateUtil;

@SuppressWarnings("serial")
public class EstadoDAO extends GenericDAO<Estado> {
	@SuppressWarnings("unchecked")
	public List<Estado> listaEstado(String nome) throws Exception {

		Session sessao = HibernateUtil.getSessionFactory().openSession();
		List<Estado> listaEstado = null;

		try {
			Criteria criteria = sessao.createCriteria(Estado.class);

			if (!"".equals(nome)) {
				criteria.add(Restrictions.ilike("nome", "%" + nome + "%"));
			}

			criteria.addOrder(Order.asc("nome"));
			listaEstado = criteria.list();
		} catch (Exception e) {
			System.out.println("ERRO...." + e.getMessage());
			throw new Exception(e.getMessage());
		} finally {
			sessao.close();
		}

		return listaEstado;
	}
}
