package catalogo.dal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import catalogo.tipos.Producto;

public class ProductosDALFichero implements ProductosDAL {

	File fichero = new File("productos.dat");

	private Map<Integer, Producto> productos = new HashMap<>();

	public ProductosDALFichero() {

		super();

		if (!fichero.exists()) {

			escribirDatabase();

		} else {

			leerDatabase();
		}

	}

	public void alta(Producto producto) {

		if (productos.containsKey(producto.getId()))
			throw new ProductoYaExistenteDALException("Ya existe el producto " + producto.getNombre());

		productos.put(producto.getId(), producto);

		escribirDatabase();

	}

	public void modificar(Producto producto) {
		if (!productos.containsKey(producto.getId()))
			throw new DALException("Intento de modificar producto no existente " + producto);

		productos.put(producto.getId(), producto);
		escribirDatabase();
	}

	public void borrar(Producto producto) {
		productos.remove(producto.getId());
		escribirDatabase();

	}

	public Producto buscarPorId(Integer idmap) {
		return productos.get(idmap);
	}

	public Producto[] buscarTodosLosProductos() {
		return productos.values().toArray(new Producto[productos.size()]);
	}

	public boolean validar(Producto producto) {
		return productos.containsValue(producto);
	}

	public void escribirDatabase() {
		try {
			FileOutputStream fos = new FileOutputStream(fichero);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(productos);
			oos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void leerDatabase() {
		try {
			FileInputStream fis = new FileInputStream(fichero);
			ObjectInputStream ois = new ObjectInputStream(fis);
			productos = (Map<Integer, Producto>) ois.readObject();
			ois.close();
			fis.close();
		} catch (IOException e) {
			System.out.println("IOException reading");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Class not found Exception reading");
			e.printStackTrace();
		}

	}

	@Override
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

	@Override
	public int getStock(Producto producto) {

		Producto[] productosArr = this.buscarTodosLosProductos();
		return Collections.frequency(Arrays.asList(productosArr), producto);
	}

	@Override
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
}
