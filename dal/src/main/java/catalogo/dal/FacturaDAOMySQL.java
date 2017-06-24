package catalogo.dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import catalogo.tipos.Factura;
import catalogo.tipos.Producto;

public class FacturaDAOMySQL extends IpartekDAOMySQL implements FacturaDAO {
	private final static String FIND_ALL = "SELECT * FROM facturas";
	private final static String FIND_BY_ID = "SELECT * FROM facturas WHERE id = ?";
	private final static String INSERT = "INSERT INTO facturas (numero_factura, id_usuarios, fecha)" + " VALUES (?, ?, ?)";
	private final static String UPDATE = "UPDATE facturas " + "SET numero_factura = ?, id_usuarios = ?,fecha = ?" + "WHERE id = ?";
	private final static String DELETE = "DELETE FROM facturas WHERE id = ?";
	private final static String FIND_PROD_BY_FACTURA_ID = "SELECT * FROM productos_vendidos as pv, facturas_productos as fp WHERE fp.id_facturas = ? AND pv.id = fp.id_productos";
	private final static String DELETE_TABLE_FACTURAS = "DELETE FROM facturas";
	private final static String REGISTER_PRODUCTS = "INSERT INTO facturas_productos (id_facturas, id_productos) VALUES (?, ?)";
	private final static String GET_MAX_ID = "SELECT MAX(ID) FROM facturas";
	
	private PreparedStatement psFindAll, psFindById, psInsert, psUpdate, psDelete, psFindProdByFacturaId, psRegister, psGetMaxId;

	public Factura[] findAll() {
		ArrayList<Factura> facturas = new ArrayList<Factura>();
		ResultSet rs = null;

		try {
			psFindAll = con.prepareStatement(FIND_ALL);

			rs = psFindAll.executeQuery();

			Factura factura;

			while (rs.next()) {
				factura = new Factura();

				factura.setId(rs.getInt("id"));
				factura.setNumero_factura(rs.getInt("numero_factura"));
				factura.setId_usuarios(rs.getInt("id_usuarios"));
				factura.setFecha(rs.getDate("fecha"));

				facturas.add(factura);
			}

		} catch (SQLException e) {
			throw new DAOException("Error en findAll", e);
		} finally {
			cerrar(psFindAll, rs);
		}
		return facturas.toArray(new Factura[facturas.size()]);
	}

	public Factura findById(int id) {
		Factura factura = null;
		ResultSet rs = null;

		try {
			psFindById = con.prepareStatement(FIND_BY_ID);

			psFindById.setInt(1, id);
			rs = psFindById.executeQuery();

			if (rs.next()) {
				factura = new Factura();

				factura.setId(rs.getInt("id"));
				factura.setNumero_factura(rs.getInt("numero_factura"));
				factura.setId_usuarios(rs.getInt("id_usuarios"));
				factura.setFecha(rs.getDate("fecha"));
			}

		} catch (SQLException e) {
			throw new DAOException("Error en FindById", e);
		} finally {
			cerrar(psFindById, rs);
		}
		return factura;
	}

	public int insert(Factura factura) {
		ResultSet generatedKeys = null;

		try {
			psInsert = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

			psInsert.setInt(1, factura.getNumero_factura());
			psInsert.setInt(2, factura.getId_usuarios());
			psInsert.setDate(3, new java.sql.Date(factura.getFecha().getTime()));

			int res = psInsert.executeUpdate();

			if (res != 1) {
				throw new DAOException("La inserción ha devuelto un valor " + res);
			}
			generatedKeys = psInsert.getGeneratedKeys();

			if (generatedKeys.next()) {
				return generatedKeys.getInt(1);
			} else {
				throw new DAOException("No se ha recibido la clave generada");
			}

		} catch (SQLException e) {
			throw new DAOException("Error en el insert", e);
		} finally {
			cerrar(psInsert, generatedKeys);
		}

	}

	public void update(Factura factura) {

		try {
			psUpdate = con.prepareStatement(UPDATE);

			psUpdate.setInt(1, factura.getNumero_factura());
			psUpdate.setInt(2, factura.getId_usuarios());
			psUpdate.setDate(3, new java.sql.Date(factura.getFecha().getTime()));
			psUpdate.setInt(4, factura.getId());

			int res = psUpdate.executeUpdate();

			if (res != 1) {
				throw new DAOException("La actualización ha devuelto un valor " + res);
			}
		} catch (Exception e) {
			throw new DAOException("Error en el update", e);
		} finally {
			cerrar(psUpdate);
		}

	}

	public void delete(Factura factura) {
		delete(factura.getId());
	}

	public void delete(int id) {
		try {
			psDelete = con.prepareStatement(DELETE);

			psDelete.setInt(1, id);

			int res = psDelete.executeUpdate();

			if (res != 1) {
				throw new DAOException("La eliminación por ID ha devuelto un valor " + res);
			}
		} catch (Exception e) {
			throw new DAOException("Error en el deleteID", e);
		} finally {
			cerrar(psDelete);
		}

	}

	public Producto[] findProductoByFacturaId(int id) {
		ArrayList<Producto> productos = new ArrayList<>();
		ResultSet rs = null;

		Producto producto;

		try {
			psFindProdByFacturaId = con.prepareStatement(FIND_PROD_BY_FACTURA_ID);
			psFindProdByFacturaId.setInt(1, id);

			rs = psFindProdByFacturaId.executeQuery();

			while (rs.next()) {
				producto = new Producto();

				producto.setId(rs.getInt("id"));
				producto.setGroupId(rs.getInt("groupId"));
				producto.setNombre(rs.getString("nombre"));
				producto.setDescripcion(rs.getString("descripcion"));
				producto.setPrecio(rs.getDouble("precio"));
				producto.setImagen(rs.getInt("imagen"));

				productos.add(producto);
			}
		} catch (Exception e) {
			throw new DAOException("Error al buscar los productos de la factura", e);
		} finally {
			cerrar(psFindProdByFacturaId);
		}

		return productos.toArray(new Producto[productos.size()]);
	}

	public void deleteFacturas() {
		try {
			psDelete = con.prepareStatement(DELETE_TABLE_FACTURAS);

			psDelete.executeUpdate();

		} catch (Exception e) {
			throw new DAOException("Error en delete table", e);
		} finally {
			cerrar(psDelete);
		}

	}

	public int insertFacturaProducto(int id_factura, int id_producto) {

		try {
			psRegister = con.prepareStatement(REGISTER_PRODUCTS);

			psRegister.setInt(1, id_factura);
			psRegister.setInt(2, id_producto);

			int res = psRegister.executeUpdate();

			if (res != 1) {
				throw new DAOException("La inserción ha devuelto un valor " + res);
			}

		} catch (SQLException e) {
			throw new DAOException("Error en el insert", e);
		} finally {
			cerrar(psRegister);
		}

		return 1;

	}
	
	public int getMaxId() {
		
		ResultSet rs;
		int maxId = 0;
		try {
			psGetMaxId = con.prepareStatement(GET_MAX_ID);
			
			rs = psGetMaxId.executeQuery();
			
			while(rs.next()) {
				maxId = rs.getInt(1);
			}
		} catch (SQLException e) {
			throw new DAOException("Error al obtener maxId", e);
		} finally {
			cerrar(psGetMaxId);
		}
		
		return maxId;
	}
	
	@Override
	public Double getPrecioTotal(int id) {
		
		Double precioTotal = 0.0;
		try {
		for (Producto p: this.findProductoByFacturaId(id)){
			precioTotal += p.getPrecio();
		}
		} catch (Exception e) {
			throw new DAOException("Error al obtener el precio total", e);
		}
		return precioTotal;
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

	
}
