package br.com.oficinabike.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.oficinabike.domain.Pessoa;
import br.com.oficinabike.util.HibernateUtil;

@SuppressWarnings("serial")
public class PessoaDAO extends GenericDAO<Pessoa> {
	@SuppressWarnings("unchecked")
	public List<Pessoa> listaPessoa(String nome) throws Exception{
		
		Session sessao = HibernateUtil.getSessionFactory().openSession();
		List<Pessoa> listaPessoa = null;
		
		try{
			Criteria criteria = sessao.createCriteria(Pessoa.class);
			
			if(!"".equals(nome)){
				criteria.add(Restrictions.ilike("nome", "%"+nome+"%"));
			}
			
			criteria.addOrder(Order.asc("nome"));
			listaPessoa =  criteria.list();				
		}catch(Exception e){
			System.out.println("ERRO...."+e.getMessage());
			throw new Exception(e.getMessage());
		}finally{
			sessao.close();
		}
		
		return listaPessoa;		
	}
}
