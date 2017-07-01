package catalogo.dal;

import java.util.List;

import catalogo.tipos.Factura;
import catalogo.tipos.Producto;

public class FacturaDAOHibernate extends IpartekDAOHibernate implements FacturaDAO {


	public FacturaDAOHibernate() {
	}

	public Factura[] findAll() {

		@SuppressWarnings("unchecked")
		List<Factura> facturasList = (List<Factura>) manager.createQuery("FROM facturas").getResultList();

		return facturasList.toArray(new Factura[facturasList.size()]);
	}

	public Factura findById(int id) {

		return manager.find(Factura.class, id);
	}

	public int insert(Factura factura) {
		manager.persist(factura);
		
		return 0;
	}

	public void update(Factura factura) {
		Factura f = manager.find(Factura.class, factura);
		f.setNumero_factura(factura.getNumero_factura());
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
