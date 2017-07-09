package catalogo.dal;

import java.math.BigDecimal;

import catalogo.tipos.Factura;
import catalogo.tipos.Producto;

public interface FacturaDAO extends IpartekDAO {
	public Factura[] findAll();

	public Factura findById(int id);

	public int insert(Factura factura);

	public void update(Factura factura);

	public void delete(Factura factura);

	public void delete(int id);

	public void deleteFacturas();

	public Producto[] findProductoByFacturaId(int id);

	public int insertFacturaProducto(int id_factura, int id_producto);

	public BigDecimal getIvaTotal(int id);
	
	public BigDecimal getPrecioTotal(int id);
	
	public int getMaxId();
}
