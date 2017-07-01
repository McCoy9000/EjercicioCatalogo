package catalogo.dal;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class IpartekDAOHibernate implements IpartekDAO {

	protected static EntityManagerFactory emf = Persistence.createEntityManagerFactory("Persistencia");
	protected static EntityManager manager;
	
	public IpartekDAOHibernate() {
	}

	@Override
	public void abrir() {
		manager = emf.createEntityManager();
		
	}

	@Override
	public void cerrar() {
		manager.close();
	}

	@Override
	public void iniciarTransaccion() {
		manager.getTransaction().begin();
	}

	@Override
	public void confirmarTransaccion() {
		manager.getTransaction().commit();

	}

	@Override
	public void deshacerTransaccion() {
		manager.getTransaction().rollback();

	}

	@Override
	public void reutilizarConexion(IpartekDAO dao) {
		// TODO Auto-generated method stub

	}

}
