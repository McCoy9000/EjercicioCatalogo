package catalogo.dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import catalogo.tipos.Direccion;

public class DireccionDAOMySQL extends IpartekDAOMySQL implements DireccionDAO {

	private final static String FIND_ALL = "SELECT * FROM direcciones";
	private final static String FIND_BY_ID = "SELECT * FROM direcciones WHERE id = ?";
	private final static String INSERT = "INSERT INTO direcciones (direccion, codigoPostal, ciudad, region, pais)" + " VALUES (?, ?, ?, ?, ?)";
	private final static String UPDATE = "UPDATE direcciones " + "SET direccion = ?, codigoPostal = ?, ciudad = ?, region = ?, pais = ? " + "WHERE id = ?";
	private final static String DELETE = "DELETE FROM direcciones WHERE id = ?";
	private PreparedStatement psFindAll, psFindById, psInsert, psUpdate, psDelete;

	public DireccionDAOMySQL(String url) {
		super(url);
	}

	public DireccionDAOMySQL() {

	}

	
	public Direccion[] findAll() {

		ArrayList<Direccion> direcciones = new ArrayList<Direccion>();
		ResultSet rs = null;

		try {
			psFindAll = con.prepareStatement(FIND_ALL);

			rs = psFindAll.executeQuery();

			Direccion direccion;

			while (rs.next()) {

				direccion = new Direccion();

				direccion.setId(rs.getInt("id"));
				direccion.setDireccion(rs.getString("direccion"));
				direccion.setCodigoPostal(rs.getString("codigo_postal"));
				direccion.setCiudad(rs.getString("ciudad"));
				direccion.setRegion(rs.getString("region"));
				direccion.setPais(rs.getString("pais"));

				direcciones.add(direccion);
			}

		} catch (SQLException e) {
			throw new DAOException("Error en findAll", e);
		} finally {
			cerrar(psFindAll, rs);
		}
		return direcciones.toArray(new Direccion[direcciones.size()]);
	}

	public Direccion findById(int id) {
		Direccion direccion = null;
		ResultSet rs = null;

		try {
			psFindById = con.prepareStatement(FIND_BY_ID);

			psFindById.setInt(1, id);
			rs = psFindById.executeQuery();

			if (rs.next()) {
				direccion = new Direccion();

				direccion.setId(rs.getInt("id"));
				direccion.setDireccion(rs.getString("direccion"));
				direccion.setCodigoPostal(rs.getString("codigo_postal"));
				direccion.setCiudad(rs.getString("ciudad"));
				direccion.setRegion(rs.getString("region"));
				direccion.setPais(rs.getString("pais"));;
			}

		} catch (Exception e) {
			throw new DAOException("Error en findById", e);
		} finally {
			cerrar(psFindById, rs);
		}

		return direccion;
	}

	public int insert(Direccion direccion) {
		ResultSet generatedKeys = null;

		try {
			psInsert = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

			psInsert.setString(1, direccion.getDireccion());
			psInsert.setString(2, direccion.getCodigoPostal());
			psInsert.setString(3, direccion.getCiudad());
			psInsert.setString(4, direccion.getRegion());
			psInsert.setString(5, direccion.getPais());

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

	public void update(Direccion direccion) {
		try {
			psUpdate = con.prepareStatement(UPDATE);

			psInsert.setString(1, direccion.getDireccion());
			psInsert.setString(2, direccion.getCodigoPostal());
			psInsert.setString(3, direccion.getCiudad());
			psInsert.setString(4, direccion.getRegion());
			psInsert.setString(5, direccion.getPais());

			psUpdate.setInt(6, direccion.getId());

			int res = psUpdate.executeUpdate();

			if (res != 1)
				throw new DAOException("La actualización ha devuelto un valor " + res);

		} catch (Exception e) {
			throw new DAOException("Error en update", e);
		} finally {
			cerrar(psUpdate);
		}
	}

	public void delete(Direccion direccion) {
		delete(direccion.getId());
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
