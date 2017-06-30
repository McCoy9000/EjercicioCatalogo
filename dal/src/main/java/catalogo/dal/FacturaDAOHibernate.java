package catalogo.dal;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import catalogo.tipos.Factura;
import catalogo.tipos.Producto;

public class FacturaDAOHibernate {

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("Persistencia");
	private static EntityManager manager = emf.createEntityManager();

	public FacturaDAOHibernate() {
		// TODO Auto-generated constructor stub
	}

	public Factura[] findAll() {

		@SuppressWarnings("unchecked")
		List<Factura> facturasArr = (List<Factura>) manager.createQuery("FROM facturas").getResultList();

		return facturasArr.toArray(new Factura[facturasArr.size()]);
	}

	public Factura findById(int id) {

		Factura factura = manager.find(Factura.class, id); // createQuery("FROM facturas WHERE id=" + id);
		// Factura[] facturasArr = this.findAll();
		// for (Factura f: facturasArr) {
		// if (f.getId() == id)
		// return f;
		// }
		return factura;
	}

	public int insert(Factura factura) {
		manager.persist(factura);
		return 0;
	}

	public void update(Factura factura) {
		Factura f = manager.find(Factura.class, factura);
		f.setNumero_factura(f.getNumero_factura());
		f.setId_usuarios(factura.getId_usuarios());
		f.setFecha(factura.getFecha());
	}

	public void delete(Factura factura) {
		manager.remove(factura);
	}

	public void delete(int id) {
		Factura factura = this.findById(id);
		manager.remove(factura);
	}

	public void deleteFacturas() {

	}

	public Producto[] findProductoByFacturaId(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	public int insertFacturaProducto(int id_factura, int id_producto) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Double getPrecioTotal(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getMaxId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
