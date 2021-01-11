package br.com.oficinabike.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.oficinabike.domain.Cliente;
import br.com.oficinabike.util.HibernateUtil;

@SuppressWarnings("serial")
public class ClienteDAO extends GenericDAO<Cliente>{
	@SuppressWarnings("unchecked")
	public List<Cliente> listaCliente(String nomeCliente) throws Exception {

		Session sessao = HibernateUtil.getSessionFactory().openSession();
		List<Cliente> listaCliente = null;

		try {
			Criteria criteria = sessao.createCriteria(Cliente.class);

			if (!"".equals(nomeCliente)) {
				criteria.add(Restrictions.ilike("nomeCliente", "%" + nomeCliente + "%"));
			}

			criteria.addOrder(Order.asc("nomeCliente"));
			listaCliente = criteria.list();
		} catch (Exception e) {
			System.out.println("ERRO...." + e.getMessage());
			throw new Exception(e.getMessage());
		} finally {
			sessao.close();
		}

		return listaCliente;
	}
}
