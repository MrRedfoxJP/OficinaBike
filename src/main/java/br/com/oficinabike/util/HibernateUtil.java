package br.com.oficinabike.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {

	private static Logger logger = Logger.getLogger(HibernateUtil.class.getName());
	private static final SessionFactory sessionFactory = buildSessionFactory();

	public static Connection getConexao() {
		Session sessao = sessionFactory.openSession();

		Connection conexao = sessao.doReturningWork(new ReturningWork<Connection>() {
			@Override
			public Connection execute(Connection conn) throws SQLException {

				return conn;
			}
		});

		return conexao;
	}

	private static SessionFactory buildSessionFactory() {
		try {

			Configuration configuration = new Configuration();
			configuration.configure("hibernate.cfg.xml");

			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties()).build();

			SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

			logger.info("Fabrica de Sessões Criada com sucesso");
			return sessionFactory;
		} catch (Throwable ex) {
			logger.warning("Falha ao tentar criar a fabrica de sessoes.");
			throw new ExceptionInInitializerError(ex);
			
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}