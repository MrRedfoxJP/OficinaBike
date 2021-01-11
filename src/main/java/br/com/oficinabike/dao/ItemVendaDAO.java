package br.com.oficinabike.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.oficinabike.domain.ItemVenda;
import br.com.oficinabike.util.HibernateUtil;

@SuppressWarnings("serial")
public class ItemVendaDAO extends GenericDAO<ItemVenda> {
	@SuppressWarnings("unchecked")
	public List<ItemVenda> listaItemVenda(String nome) throws Exception{
		
		Session sessao = HibernateUtil.getSessionFactory().openSession();
		List<ItemVenda> listaItemVenda = null;
		
		try{
			Criteria criteria = sessao.createCriteria(ItemVenda.class);
			
			if(!"".equals(nome)){
				criteria.add(Restrictions.ilike("nome", "%"+nome+"%"));
			}
			
			criteria.addOrder(Order.asc("nome"));
			listaItemVenda =  criteria.list();				
		}catch(Exception e){
			System.out.println("ERRO...."+e.getMessage());
			throw new Exception(e.getMessage());
		}finally{
			sessao.close();
		}
		
		return listaItemVenda;		
	}
}
