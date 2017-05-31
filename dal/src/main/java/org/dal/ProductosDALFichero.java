package org.dal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import org.tipos.Producto;

public class ProductosDALFichero implements ProductosDAL {

	File fichero = new File("productos.dat");

	private TreeMap<Integer, Producto> productos = new TreeMap<Integer, Producto>();
	
	public ProductosDALFichero() {

		super();

		if (!fichero.exists()) {

			escribirDatabase();
			
		} else {

			leerDatabase();
			try {
				Producto.siguienteId = (productos.lastKey() + 1);
			} catch (NoSuchElementException e) {
				Producto.siguienteId = 0;
			}
		}
	}

	public void alta(Producto producto) {
		if (productos.containsKey(producto.getId()))
			throw new ProductoYaExistenteDALException("Ya existe el producto " + producto.getNombre());

		productos.put(producto.getId(), producto);
		try {
			Producto.siguienteId = (productos.lastKey() + 1);
		} catch (NoSuchElementException e) {
			Producto.siguienteId = 0;
		}
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
		try {
			Producto.siguienteId = (productos.lastKey() + 1);
		} catch (NoSuchElementException e) {
			Producto.siguienteId = 0;
		}
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
			productos = (TreeMap<Integer, Producto>) ois.readObject();
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
}
