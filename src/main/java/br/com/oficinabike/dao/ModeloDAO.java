package br.com.oficinabike.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.oficinabike.domain.Modelo;
import br.com.oficinabike.util.HibernateUtil;

@SuppressWarnings("serial")
public class ModeloDAO extends GenericDAO<Modelo>{

	@SuppressWarnings("unchecked")
	public List<Modelo> listaModelo(String nome) throws Exception {

		Session sessao = HibernateUtil.getSessionFactory().openSession();
		List<Modelo> listaModelo = null;

		try {
			Criteria criteria = sessao.createCriteria(Modelo.class);

			if (!"".equals(nome)) {
				criteria.add(Restrictions.ilike("nome", "%" + nome + "%"));
			}

			criteria.addOrder(Order.asc("nome"));
			listaModelo = criteria.list();
		} catch (Exception e) {
			System.out.println("ERRO.fas" + e.getMessage());
			throw new Exception(e.getMessage());
		} finally {
			sessao.close();
		}

		return listaModelo;
	}
	
	@SuppressWarnings("unchecked")
	public List<Modelo> buscarPorMarca(Long modeloId){
		Session sessao = HibernateUtil.getSessionFactory().openSession();

		try {
			Criteria consulta = sessao.createCriteria(Modelo.class);
			consulta.add(Restrictions.eq("marca.id", modeloId));
			consulta.addOrder(Order.asc("nome"));
			List<Modelo> resultado = consulta.list();
			return resultado;

		} catch (RuntimeException ex) {
			throw ex;
		} finally {
			sessao.close();
		}
	}


}
