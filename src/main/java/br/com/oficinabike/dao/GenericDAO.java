package br.com.oficinabike.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import br.com.oficinabike.util.HibernateUtil;

/**
 *
 * @author Thiago
 */
@SuppressWarnings("serial")
public class GenericDAO<Entidade> implements Serializable {
	private Class<Entidade> classe;
	
	@SuppressWarnings("unchecked")
	public GenericDAO() {
		this.classe = (Class<Entidade>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	public void salvar(Entidade obj) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transacao = null;
		try {
			
			transacao = session.beginTransaction();
			session.save(obj);
			transacao.commit();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			transactionRollback(transacao);
			throw new Exception(ex);
		} finally {
			sessionClose(session);
		}
	}

	public void excluir(Entidade obj) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transacao = null;
		try {
			transacao = session.beginTransaction();
			session.delete(obj);
			transacao.commit();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			transactionRollback(transacao);
			throw new Exception(ex);
		} finally {
			sessionClose(session);
		}
	}

	public void editar(Entidade obj) throws Exception {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transacao = null;
		try {
			transacao = session.beginTransaction();
			session.update(obj);
			transacao.commit();
		} catch (HibernateException ex) {
			ex.printStackTrace();
			transactionRollback(transacao);
			throw new Exception(ex);
		} finally {
			sessionClose(session);
		}

	}

	public void merge(Entidade entidade) {
		Session sessao = HibernateUtil.getSessionFactory().openSession();
		Transaction transacao = null;

		try {
			transacao = sessao.beginTransaction();
			sessao.merge(entidade);
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
	
	@SuppressWarnings("unchecked")
	public List<Entidade> listar() {
		Session sessao = HibernateUtil.getSessionFactory().openSession();

		try {
			Criteria consulta = sessao.createCriteria(classe);
			List<Entidade> resultado = consulta.list();
			return resultado;

		} catch (RuntimeException ex) {
			throw ex;
		} finally {
			sessao.close();
		}
	}

	public void sessionClose(Session session) {
		if (session != null && session.isOpen()) {
			session.close();
		}
	}

	public void transactionRollback(Transaction transaction) {
		if (transaction != null) {
			transaction.rollback();
		}
	}
}
