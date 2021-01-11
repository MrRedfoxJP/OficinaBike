package br.com.oficinabike.dao;

import java.util.List;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.oficinabike.domain.Usuario;
import br.com.oficinabike.util.HibernateUtil;

@SuppressWarnings("serial")
public class UsuarioDAO extends GenericDAO<Usuario> {
	@SuppressWarnings("unchecked")
	public List<Usuario> listaUsuario(String nome) throws Exception {

		Session sessao = HibernateUtil.getSessionFactory().openSession();
		List<Usuario> listaUsuario = null;

		try {
			Criteria criteria = sessao.createCriteria(Usuario.class);

			if (!"".equals(nome)) {
				criteria.add(Restrictions.ilike("nome", "%" + nome + "%"));
			}

			criteria.addOrder(Order.asc("nome"));
			listaUsuario = criteria.list();
		} catch (Exception e) {
			System.out.println("ERRO...." + e.getMessage());
			throw new Exception(e.getMessage());
		} finally {
			sessao.close();
		}

		return listaUsuario;
	}

	public Usuario autenticar(String cpf, String senha) {
		Session sessao = HibernateUtil.getSessionFactory().openSession();
		try {
			Criteria consulta = sessao.createCriteria(Usuario.class);
			consulta.createAlias("pessoa", "p");
			consulta.add(Restrictions.eq("p.cpf", cpf));

			SimpleHash hash = new SimpleHash("md5", senha);
			consulta.add(Restrictions.eq("senha", hash.toHex()));

			Usuario resultado = (Usuario) consulta.uniqueResult();
			return resultado;

		} catch (RuntimeException ex) {
			throw ex;
		} finally {
			sessao.close();
		}

	}
}