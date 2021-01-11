package br.com.oficinabike.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.oficinabike.domain.Bairro;
import br.com.oficinabike.util.HibernateUtil;

@SuppressWarnings("serial")
public class BairroDAO extends GenericDAO<Bairro> {

	@SuppressWarnings("unchecked")
	
	public List<Bairro> listaBairro(String nome) throws Exception {

		Session sessao = HibernateUtil.getSessionFactory().openSession();
		List<Bairro> listaBairro = null;

		try {
			Criteria criteria = sessao.createCriteria(Bairro.class);

			if (!"".equals(nome)) {
				criteria.add(Restrictions.ilike("nome", "%" + nome + "%"));
			}

			criteria.addOrder(Order.asc("nome"));
			listaBairro = criteria.list();
		} catch (Exception e) {
			System.out.println("ERRO...." + e.getMessage());
			throw new Exception(e.getMessage());
		} finally {
			sessao.close();
		}

		return listaBairro;
	}

}
