package catalogo.dal;

import catalogo.tipos.Factura;

public interface FacturaDAO extends IpartekDAO {
	public Factura[] findAll();

	public Factura findById(int id);

	public int insert(Factura factura);

	public void update(Factura factura);

	public void delete(Factura factura);

	public void delete(int id);

}
