package catalogo.dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import catalogo.tipos.Usuario;

public class UsuarioDAOMySQL extends IpartekDAOMySQL implements UsuarioDAO {

	private final static String FIND_ALL = "SELECT * FROM usuarios";
	private final static String FIND_BY_ID = "SELECT * FROM usuarios WHERE id = ?";
	private final static String INSERT = "INSERT INTO usuarios (username, password, nombre_completo, id_roles)" + " VALUES (?, ?, ?, ?)";
	private final static String FIND_BY_NAME = "SELECT * FROM usuarios WHERE username = ?";
	private final static String UPDATE = "UPDATE usuarios " + "SET username = ?, password = ?, nombre_completo = ?, id_roles = ? " + "WHERE username = ?";
	private final static String DELETE = "DELETE FROM usuarios WHERE username = ?";
	private final static String DELETE_TABLE_USUARIOS = "DELETE FROM usuarios";
	private PreparedStatement psFindAll, psFindById, psFindByName, psInsert, psUpdate, psDelete;

	public UsuarioDAOMySQL(String url, String mysqlUser, String mysqlPass) {
		super(url, mysqlUser, mysqlPass);
	}

	public UsuarioDAOMySQL() {

	}

	public Usuario[] findAll() {

		ArrayList<Usuario> usuarios = new ArrayList<Usuario>();
		ResultSet rs = null;

		try {
			psFindAll = con.prepareStatement(FIND_ALL);

			rs = psFindAll.executeQuery();

			Usuario usuario;

			while (rs.next()) {

				usuario = new Usuario();

				usuario.setId(rs.getInt("id"));
				usuario.setId_roles(rs.getInt("id_roles"));
				usuario.setNombre_completo(rs.getString("nombre_completo"));
				usuario.setPassword(rs.getString("password"));
				usuario.setUsername(rs.getString("username"));

				usuarios.add(usuario);
			}

		} catch (SQLException e) {
			throw new DAOException("Error en findAll", e);
		} finally {
			cerrar(psFindAll, rs);
		}
		return usuarios.toArray(new Usuario[usuarios.size()]);
	}

	public Usuario findById(int id) {
		Usuario usuario = null;
		ResultSet rs = null;

		try {
			psFindById = con.prepareStatement(FIND_BY_ID);

			psFindById.setInt(1, id);
			rs = psFindById.executeQuery();

			if (rs.next()) {
				usuario = new Usuario();

				usuario.setId(rs.getInt("id"));
				usuario.setId_roles(rs.getInt("id_roles"));
				usuario.setNombre_completo(rs.getString("nombre_completo"));
				usuario.setPassword(rs.getString("password"));
				usuario.setUsername(rs.getString("username"));
			}

		} catch (Exception e) {
			throw new DAOException("Error en findById", e);
		} finally {
			cerrar(psFindById, rs);
		}

		return usuario;
	}

	public Usuario findByName(String username) {
		Usuario usuario = null;
		ResultSet rs = null;

		try {
			psFindByName = con.prepareStatement(FIND_BY_NAME);

			psFindByName.setString(1, username);
			rs = psFindByName.executeQuery();

			if (rs.next()) {
				usuario = new Usuario();

				usuario.setId(rs.getInt("id"));
				usuario.setId_roles(rs.getInt("id_roles"));
				usuario.setNombre_completo(rs.getString("nombre_completo"));
				usuario.setPassword(rs.getString("password"));
				usuario.setUsername(rs.getString("username"));
			}

		} catch (Exception e) {
			throw new DAOException("Error en findByName", e);
		} finally {
			cerrar(psFindByName, rs);
		}
		return usuario;
	}

	public int insert(Usuario usuario) {
		ResultSet generatedKeys = null;

		try {
			psInsert = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

			psInsert.setString(1, usuario.getUsername());
			psInsert.setString(2, usuario.getPassword());
			psInsert.setString(3, usuario.getNombre_completo());
			psInsert.setInt(4, usuario.getId_roles());

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

	public void update(Usuario usuario) {
		try {
			psUpdate = con.prepareStatement(UPDATE);

			psUpdate.setString(1, usuario.getUsername());
			psUpdate.setString(2, usuario.getPassword());
			psUpdate.setString(3, usuario.getNombre_completo());
			psUpdate.setInt(4, usuario.getId_roles());

			psUpdate.setString(5, usuario.getUsername());

			int res = psUpdate.executeUpdate();

			if (res != 1)
				throw new DAOException("La actualización ha devuelto un valor " + res);

		} catch (Exception e) {
			throw new DAOException("Error en update", e);
		} finally {
			cerrar(psUpdate);
		}
	}

	public void delete(Usuario usuario) {
		delete(usuario.getUsername());
	}

	public void delete(String username) {
		try {
			psDelete = con.prepareStatement(DELETE);

			psDelete.setString(1, username);

			int res = psDelete.executeUpdate();

			if (res != 1)
				throw new DAOException("La actualización ha devuelto un valor " + res);

		} catch (Exception e) {
			throw new DAOException("Error en delete", e);
		} finally {
			cerrar(psDelete);
		}
	}

	public void deleteUsuarios() {
		try {
			psDelete = con.prepareStatement(DELETE_TABLE_USUARIOS);

			psDelete.executeUpdate();

		} catch (Exception e) {
			throw new DAOException("Error en delete table", e);
		} finally {
			cerrar(psDelete);
		}
	}

	public boolean validar(Usuario usuario) {

		this.abrir();
		Usuario[] usuariosArr = this.findAll();
		this.cerrar();

		for (Usuario u : usuariosArr) {
			if (u.getUsername().equals(usuario.getUsername()) && u.getPassword().equals(usuario.getPassword())) {
				return true;
			}
		}
		return false;
	}

	public boolean validarNombre(Usuario usuario) {

		this.abrir();
		Usuario[] usuariosArr = this.findAll();
		this.cerrar();

		if (usuario.getUsername() != null) {

			for (Usuario s : usuariosArr) {

				if (usuario.getUsername().equals(s.getUsername())) {

					return true;
				}
			}
		}
		return false;
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
