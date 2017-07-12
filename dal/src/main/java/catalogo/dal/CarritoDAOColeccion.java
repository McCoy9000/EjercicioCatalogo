package catalogo.dal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.log4j.Logger;

import catalogo.constantes.Constantes;
import catalogo.tipos.Articulo;
import catalogo.tipos.Carrito;
import catalogo.tipos.Producto;

public class CarritoDAOColeccion implements CarritoDAO, Serializable, HttpSessionBindingListener {

	private static final long serialVersionUID = 2184412432713235074L;

	private static Logger log = Logger.getLogger(CarritoDAOColeccion.class);

	Carrito carrito;

	public CarritoDAOColeccion() {
		this.carrito = new Carrito();
	}

	public CarritoDAOColeccion(HashMap<Integer, Producto> productosCarrito) {
		this.carrito = new Carrito(productosCarrito);
	}

	public void anadirAlCarrito(Producto producto) {
		carrito.getListaProductos().put(producto.getId(), producto);
	}

	public void quitarDelCarrito(Integer idmap) {
		carrito.getListaProductos().remove(idmap);
	}

	public Producto buscarPorId(Integer idmap) {
		return carrito.getListaProductos().get(idmap);
	}

	public Producto[] buscarTodosLosProductos() {
		return carrito.getListaProductos().values().toArray(new Producto[carrito.getListaProductos().size()]);
	}

	public BigDecimal precioTotal() {

		BigDecimal precioTotal = BigDecimal.ZERO;
		BigDecimal precioTotalMasIva = BigDecimal.ZERO;
		for (Producto p : this.buscarTodosLosProductos()) {

			precioTotal = precioTotal.add(p.getPrecio());
		}
		
		precioTotalMasIva = precioTotal.multiply(Constantes.IVA.add(BigDecimal.ONE)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
				
		return precioTotalMasIva;
	}

	public Map<Integer, List<Producto>> getAlmacen() {

		Map<Integer, List<Producto>> almacen = new HashMap<>();

		Producto[] productosArr = this.buscarTodosLosProductos();

		for (Producto p : productosArr) {
			if (!almacen.containsKey(p.getGroupId())) {
				List<Producto> grupo = new ArrayList<>();
				grupo.add(p);
				almacen.put(p.getGroupId(), grupo);
			} else {
				List<Producto> grupo = almacen.get(p.getGroupId());
				grupo.add(p);
				almacen.put(p.getGroupId(), grupo);
			}
		}

		return almacen;
	}

	public int getStock(Producto producto) {

		Producto[] productosArr = this.buscarTodosLosProductos();
		return Collections.frequency(Arrays.asList(productosArr), producto);
	}

	public Producto[] getCatalogo() {

		Producto[] catalogo = new Producto[this.getAlmacen().size()];
		int i = 0;

		for (List<Producto> grupoProductos : this.getAlmacen().values()) {

			Producto muestra = grupoProductos.get(0);
			catalogo[i] = muestra;
			i++;

		}

		return catalogo;

	}
	
	public Articulo[] getCatalogoArticulos() {

		Articulo[] catalogo = new Articulo[this.getAlmacen().size()];
		int i = 0;

		for (List<Producto> grupoProductos : this.getAlmacen().values()) {

			Articulo muestra = new Articulo(grupoProductos.get(0).getId(), grupoProductos.get(0).getGroupId(), grupoProductos.get(0).getNombre(), grupoProductos.get(0).getDescripcion(), grupoProductos.get(0).getPrecio(),
					this.getStock(grupoProductos.get(0)));
			catalogo[i] = muestra;
			i++;

		}
		return catalogo;
	}

	@Override
	public void valueBound(HttpSessionBindingEvent event) {
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		HttpSession session = event.getSession();
		ServletContext application = session.getServletContext();
		application.setAttribute("carritoAbandonado", this);
		log.info("Carrito abandonado almacenado en aplicación");
	}

}
