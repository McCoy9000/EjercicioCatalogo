package catalogo.dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import catalogo.tipos.Articulo;
import catalogo.tipos.Producto;

public class ProductoVendidoDAOMySQL extends IpartekDAOMySQL implements ProductoDAO {

	private final static String FIND_ALL = "SELECT * FROM productos_vendidos";
	private final static String FIND_BY_ID = "SELECT * FROM productos_vendidos WHERE id = ?";
	private final static String INSERT = "INSERT INTO productos_vendidos (id, groupId, nombre, descripcion, precio, imagen)" + " VALUES (?, ?, ?, ?, ?, ?)";
	private final static String FIND_BY_NAME = "SELECT * FROM productos_vendidos WHERE nombre = ?";
	private final static String UPDATE = "UPDATE productos_vendidos " + "SET groupId = ?, nombre = ?, descripcion = ?, precio = ?, imagen = ? " + "WHERE id = ?";
	private final static String DELETE = "DELETE FROM productos_vendidos WHERE id = ?";
	private final static String DELETE_TABLE_PRODUCTOS = "DELETE FROM productos_vendidos";
	private PreparedStatement psFindAll, psFindById, psFindByName, psInsert, psUpdate, psDelete;

	public ProductoVendidoDAOMySQL(String url, String mysqlUser, String mysqlPass) {
		super(url);
	}

	public ProductoVendidoDAOMySQL() {

	}

	public Producto[] findAll() {

		ArrayList<Producto> productos = new ArrayList<Producto>();
		ResultSet rs = null;

		try {
			psFindAll = con.prepareStatement(FIND_ALL);

			rs = psFindAll.executeQuery();

			Producto producto;

			while (rs.next()) {

				producto = new Producto();

				producto.setId(rs.getInt("id"));
				producto.setGroupId(rs.getInt("groupId"));
				producto.setNombre(rs.getString("nombre"));
				producto.setDescripcion(rs.getString("descripcion"));
				producto.setPrecio(rs.getBigDecimal("precio"));
				producto.setImagen(rs.getInt("imagen"));

				productos.add(producto);
			}

		} catch (SQLException e) {
			throw new DAOException("Error en findAll", e);
		} finally {
			cerrar(psFindAll, rs);
		}
		return productos.toArray(new Producto[productos.size()]);
	}

	private void cerrar(PreparedStatement ps) {
		cerrar(ps, null);
	}

	private void cerrar(PreparedStatement ps, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		} catch (Exception e) {
			throw new DAOException("Error en el cierre de ps o rs", e);
		}
	}

	public Producto findById(int id) {
		Producto producto = null;
		ResultSet rs = null;

		try {
			psFindById = con.prepareStatement(FIND_BY_ID);

			psFindById.setInt(1, id);
			rs = psFindById.executeQuery();

			if (rs.next()) {
				producto = new Producto();

				producto.setId(rs.getInt("id"));
				producto.setGroupId(rs.getInt("groupId"));
				producto.setNombre(rs.getString("nombre"));
				producto.setDescripcion(rs.getString("descripcion"));
				producto.setPrecio(rs.getBigDecimal("precio"));
				producto.setImagen(rs.getInt("imagen"));
			}

		} catch (Exception e) {
			throw new DAOException("Error en findById", e);
		} finally {
			cerrar(psFindById, rs);
		}

		return producto;
	}

	public Producto findByName(String nombre) {
		Producto producto = null;
		ResultSet rs = null;

		try {
			psFindByName = con.prepareStatement(FIND_BY_NAME);

			psFindByName.setString(1, nombre);
			rs = psFindByName.executeQuery();

			if (rs.next()) {
				producto = new Producto();

				producto.setId(rs.getInt("id"));
				producto.setGroupId(rs.getInt("groupId"));
				producto.setNombre(rs.getString("nombre"));
				producto.setDescripcion(rs.getString("descripcion"));
				producto.setPrecio(rs.getBigDecimal("precio"));
				producto.setImagen(rs.getInt("imagen"));
			}

		} catch (Exception e) {
			throw new DAOException("Error en findByName", e);
		} finally {
			cerrar(psFindByName, rs);
		}

		return producto;
	}

	public int insert(Producto producto) {
		ResultSet generatedKeys = null;

		try {
			psInsert = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

			psInsert.setInt(1, producto.getId());
			psInsert.setInt(2, producto.getGroupId());
			psInsert.setString(3, producto.getNombre());
			psInsert.setString(4, producto.getDescripcion());
			psInsert.setBigDecimal(5, producto.getPrecio());
			psInsert.setInt(6, producto.getImagen());

			int res = psInsert.executeUpdate();

			if (res != 1)
				throw new DAOException("La inserción ha devuelto un valor " + res);

			generatedKeys = psInsert.getGeneratedKeys();

			if (generatedKeys.next())
				return generatedKeys.getInt(1);
			else
				return 1;

		} catch (Exception e) {
			throw new DAOException("Error en insert", e);
		} finally {
			cerrar(psInsert, generatedKeys);
		}
	}

	public void update(Producto producto) {
		try {
			psUpdate = con.prepareStatement(UPDATE);

			psUpdate.setInt(1, producto.getGroupId());
			psUpdate.setString(2, producto.getNombre());
			psUpdate.setString(3, producto.getDescripcion());
			psUpdate.setBigDecimal(4, producto.getPrecio());
			psUpdate.setInt(5, producto.getImagen());

			psUpdate.setInt(6, producto.getId());

			int res = psUpdate.executeUpdate();

			if (res != 1)
				throw new DAOException("La actualización ha devuelto un valor " + res);

		} catch (Exception e) {
			throw new DAOException("Error en update", e);
		} finally {
			cerrar(psUpdate);
		}
	}

	public void delete(Producto producto) {
		delete(producto.getId());
	}

	public void delete(int id) {
		try {
			psDelete = con.prepareStatement(DELETE);

			psDelete.setInt(1, id);

			int res = psDelete.executeUpdate();

			if (res != 1)
				throw new DAOException("La actualización ha devuelto un valor " + res);

		} catch (Exception e) {
			throw new DAOException("Error en delete", e);
		} finally {
			cerrar(psDelete);
		}

	}

	public void deleteProductos() {
		try {
			psDelete = con.prepareStatement(DELETE_TABLE_PRODUCTOS);

			psDelete.executeUpdate();

		} catch (Exception e) {
			throw new DAOException("Error en delete table", e);
		} finally {
			cerrar(psDelete);
		}

	}

	public boolean validar(Producto producto) {

		Producto[] ProductosArr = this.findAll();

		for (Producto p : ProductosArr) {
			if (p.getId() == producto.getId()) {
				return true;
			}
		}
		return false;
	}

	public boolean validarNombre(Producto producto) {

		Producto[] productosArr = this.findAll();

		if (producto.getNombre() != null) {

			for (Producto s : productosArr) {

				if (producto.getNombre().equals(s.getNombre())) {

					return true;
				}
			}
		}
		return false;
	}

	public Map<Integer, List<Producto>> getAlmacen() {

		Map<Integer, List<Producto>> almacen = new HashMap<>();

		Producto[] productosArr = this.findAll();

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

		Producto[] productosArr = this.findAll();
		return Collections.frequency(Arrays.asList(productosArr), producto);
	}

	public Articulo[] getCatalogo() {

		Articulo[] catalogo = new Articulo[this.getAlmacen().size()];
		int i = 0;

		for (List<Producto> grupoProductos : this.getAlmacen().values()) {

			Articulo muestra = new Articulo(grupoProductos.get(0).getGroupId(), grupoProductos.get(0).getNombre(), grupoProductos.get(0).getDescripcion(), grupoProductos.get(0).getPrecio(),
					this.getStock(grupoProductos.get(0)));
			catalogo[i] = muestra;
			i++;

		}
		return catalogo;
	}
}
