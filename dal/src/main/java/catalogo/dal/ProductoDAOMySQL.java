package catalogo.dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import catalogo.tipos.Producto;

public class ProductoDAOMySQL extends IpartekDAOMySQL implements ProductoDAO {

	private final static String FIND_ALL = "SELECT * FROM productos";
	private final static String FIND_BY_ID = "SELECT * FROM productos WHERE id = ?";
	private final static String INSERT = "INSERT INTO productos (groupId, nombre, descripcion, precio)" + " VALUES (?, ?, ?, ?)";
	private final static String FIND_BY_NAME = "SELECT * FROM productos WHERE nombre = ?";
	private final static String UPDATE = "UPDATE productos " + "SET groupId = ?, nombre = ?, descripcion = ?, precio = ? " + "WHERE nombre = ?";
	private final static String DELETE = "DELETE FROM productos WHERE nombre = ?";

	private PreparedStatement psFindAll, psFindById, psFindByName, psInsert, psUpdate, psDelete;

	public ProductoDAOMySQL(String url, String mysqlUser, String mysqlPass) {
		super(url, mysqlUser, mysqlPass);
	}

	public ProductoDAOMySQL() {

	}

	public Producto[] findAll() {

		ArrayList<Producto> productos = new ArrayList<Producto>();
		ResultSet rs = null;

		try {
			psFindAll = con.prepareStatement(FIND_ALL);

			rs = psFindAll.executeQuery();

			Producto producto;

			while (rs.next()) {
				// System.out.println(rs.getString("username"));
				producto = new Producto();

				producto.setGroupId(rs.getInt("groupId"));
				producto.setNombre(rs.getString("nombre"));
				producto.setDescripcion(rs.getString("descripcion"));
				producto.setPrecio(rs.getDouble("precio"));

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
				producto.setPrecio(rs.getDouble("precio"));
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
				producto.setPrecio(rs.getDouble("precio"));
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

			psInsert.setString(1, producto.getNombre());
			psInsert.setString(2, producto.getDescripcion());
			psInsert.setDouble(3, producto.getPrecio());
			psInsert.setInt(4, producto.getGroupId());

			int res = psInsert.executeUpdate();

			if (res != 1)
				throw new DAOException("La inserción ha devuelto un valor " + res);

			generatedKeys = psInsert.getGeneratedKeys();

			if (generatedKeys.next())
				return generatedKeys.getInt(1);
			else
				throw new DAOException("No se ha recibido la clave generada");

		} catch (Exception e) {
			throw new DAOException("Error en insert", e);
		} finally {
			cerrar(psInsert, generatedKeys);
		}
	}

	public void update(Producto producto) {
		try {
			psUpdate = con.prepareStatement(UPDATE);

			psUpdate.setString(1, producto.getNombre());
			psUpdate.setString(2, producto.getDescripcion());
			psUpdate.setDouble(3, producto.getPrecio());
			psUpdate.setInt(4, producto.getGroupId());

			psUpdate.setString(5, producto.getNombre());

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
		delete(producto.getNombre());
	}

	public void delete(String nombre) {
		try {
			psDelete = con.prepareStatement(DELETE);

			psDelete.setString(1, nombre);

			int res = psDelete.executeUpdate();

			if (res != 1)
				throw new DAOException("La actualización ha devuelto un valor " + res);

		} catch (Exception e) {
			throw new DAOException("Error en update", e);
		} finally {
			cerrar(psDelete);
		}

	}

	public boolean validar(Producto producto) {

		this.abrir();
		Producto[] ProductosArr = this.findAll();
		this.cerrar();

		for (Producto p : ProductosArr) {
			if (p.getNombre().equals(producto.getNombre())) {
				return true;
			}
		}
		return false;
	}

	public boolean validarNombre(Producto producto) {

		this.abrir();
		Producto[] productosArr = this.findAll();
		this.cerrar();

		if (producto.getNombre() != null) {

			for (Producto s : productosArr) {

				if (producto.getNombre().equals(s.getNombre())) {

					return true;
				}
			}
		}
		return false;
	}

}
