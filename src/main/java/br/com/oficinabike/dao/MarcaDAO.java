package br.com.oficinabike.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.oficinabike.domain.Marca;
import br.com.oficinabike.util.HibernateUtil;

@SuppressWarnings("serial")
public class MarcaDAO extends GenericDAO<Marca> {

	@SuppressWarnings("unchecked")
	public List<Marca> listaMarca(String nome) throws Exception {

		Session sessao = HibernateUtil.getSessionFactory().openSession();
		List<Marca> listaMarca = null;

		try {
			Criteria criteria = sessao.createCriteria(Marca.class);

			if (!"".equals(nome)) {
				criteria.add(Restrictions.ilike("nome", "%" + nome + "%"));
			}

			criteria.addOrder(Order.asc("nome"));
			listaMarca = criteria.list();
		} catch (Exception e) {
			System.out.println("ERRO.fos" + e.getMessage());
			throw new Exception(e.getMessage());
		} finally {
			sessao.close();
		}

		return listaMarca;
	}
	
	
}
