package br.com.oficinabike.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.oficinabike.domain.Produto;
import br.com.oficinabike.util.HibernateUtil;

@SuppressWarnings("serial")
public class ProdutoDAO extends GenericDAO<Produto> {
	
	
	@SuppressWarnings("unchecked")
	public List<Produto> listaProduto(String nome) throws Exception{
		
		Session sessao = HibernateUtil.getSessionFactory().openSession();
		List<Produto> listaProduto = null;
		
		try{
			Criteria criteria = sessao.createCriteria(Produto.class);
			
			if(!"".equals(nome)){
				criteria.add(Restrictions.ilike("nome", "%"+nome+"%"));
			}
			
			criteria.addOrder(Order.asc("nome"));
			listaProduto =  criteria.list();				
		}catch(Exception e){
			System.out.println("ERRO...."+e.getMessage());
			throw new Exception(e.getMessage());
		}finally{
			sessao.close();
		}
		
		return listaProduto;		
	}
	
}
