package catalogo.dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import catalogo.tipos.Usuario;
import catalogo.tipos.UsuarioMascara;

public class UsuarioDAOMySQL extends IpartekDAOMySQL implements UsuarioDAO {

	private final static String FIND_ALL = "SELECT * FROM usuarios WHERE NOT id_roles = '3'";
	private final static String FIND_BY_ID = "SELECT * FROM usuarios WHERE id = ?";
	private final static String FIND_MASK_BY_ID = "SELECT usuarios.id, username, password, nombre_completo, apellidos, documento, telefono, empresa, direccion, codigo_postal, ciudad, region, pais FROM usuarios WHERE usuarios.id=?";
	private final static String INSERT = "INSERT INTO usuarios (username, password, nombre_completo, apellidos, id_roles)" + " VALUES (?, ?, ?, ?, ?)";
	private final static String FIND_BY_NAME = "SELECT * FROM usuarios WHERE username = ?";
	private final static String UPDATE = "UPDATE usuarios "
			+ "SET username = ?, password = ?, nombre_completo = ?, apellidos = ?, id_roles = ? , documento = ?, telefono = ?, direccion = ?, codigo_postal = ?, ciudad = ? , region = ?, pais = ?, empresa = ? "
			+ "WHERE id = ?";
	private final static String DELETE = "DELETE FROM usuarios WHERE id = ?";
	private final static String DELETE_TABLE_USUARIOS = "DELETE FROM usuarios";
	private final static String FIND_ALL_MASKS = "SELECT usuarios.id, username, password, nombre_completo, apellidos, rol FROM usuarios, roles WHERE roles.id=usuarios.id_roles AND NOT id_roles = '3'";
	private PreparedStatement psFindAll, psFindById, psFindMaskById, psFindByName, psInsert, psUpdate, psDelete, psDeleteUsers, psFindAllMasks;

	public UsuarioDAOMySQL(String url) {
		super(url);
	}

	public UsuarioDAOMySQL() {
		super();
	}

	public UsuarioMascara[] findAllMasks() {

		ArrayList<UsuarioMascara> usuarios = new ArrayList<>();
		ResultSet rs = null;

		try {
			psFindAllMasks = con.prepareStatement(FIND_ALL_MASKS);

			rs = psFindAllMasks.executeQuery();

			UsuarioMascara usuario;

			while (rs.next()) {
				usuario = new UsuarioMascara();

				usuario.setId(rs.getInt("id"));
				usuario.setRol(rs.getString("rol"));
				usuario.setNombre_completo(rs.getString("nombre_completo"));
				usuario.setApellidos(rs.getString("apellidos"));
				usuario.setPassword(rs.getString("password"));
				usuario.setUsername(rs.getString("username"));

				usuarios.add(usuario);
			}
		} catch (SQLException e) {
			throw new DAOException("Error en findAllMasks", e);
		} finally {
			cerrar(psFindAllMasks, rs);
		}

		return usuarios.toArray(new UsuarioMascara[usuarios.size()]);

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
				usuario.setApellidos(rs.getString("apellidos"));
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
				usuario.setApellidos(rs.getString("apellidos"));
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

	public UsuarioMascara findMaskById(int id) {
		UsuarioMascara usuario = null;
		ResultSet rs = null;

		try {
			psFindMaskById = con.prepareStatement(FIND_MASK_BY_ID);

			psFindMaskById.setInt(1, id);
			rs = psFindMaskById.executeQuery();

			if (rs.next()) {
				usuario = new UsuarioMascara();

				usuario.setId(rs.getInt("id"));
				usuario.setNombre_completo(rs.getString("nombre_completo"));
				usuario.setApellidos(rs.getString("apellidos"));
				usuario.setPassword(rs.getString("password"));
				usuario.setUsername(rs.getString("username"));
				usuario.setDocumento(rs.getString("documento"));
				usuario.setTelefono(rs.getString("telefono"));
				usuario.setDireccion(rs.getString("direccion"));
				usuario.setCodigoPostal(rs.getString("codigo_postal"));
				usuario.setCiudad(rs.getString("ciudad"));
				usuario.setRegion(rs.getString("region"));
				usuario.setPais(rs.getString("pais"));
				usuario.setEmpresa(rs.getString("empresa"));
			}

		} catch (Exception e) {
			throw new DAOException("Error en findMaskById", e);
		} finally {
			cerrar(psFindMaskById, rs);
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
				usuario.setApellidos(rs.getString("apellidos"));
				usuario.setPassword(rs.getString("password"));
				usuario.setUsername(rs.getString("username"));
				usuario.setDocumento(rs.getString("documento"));
				usuario.setTelefono(rs.getString("telefono"));
				usuario.setDireccion(rs.getString("direccion"));
				usuario.setCodigoPostal(rs.getString("codigo_postal"));
				usuario.setCiudad(rs.getString("ciudad"));
				usuario.setRegion(rs.getString("region"));
				usuario.setPais(rs.getString("pais"));
				usuario.setEmpresa(rs.getString("empresa"));
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
			psInsert.setString(4, usuario.getApellidos());
			psInsert.setInt(5, usuario.getId_roles());

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
			psUpdate.setString(4, usuario.getApellidos());
			psUpdate.setInt(5, usuario.getId_roles());
			psUpdate.setString(6, usuario.getDocumento());
			psUpdate.setString(7, usuario.getTelefono());
			psUpdate.setString(8, usuario.getDireccion());
			psUpdate.setString(9, usuario.getCodigoPostal());
			psUpdate.setString(10, usuario.getCiudad());
			psUpdate.setString(11, usuario.getRegion());
			psUpdate.setString(12, usuario.getPais());
			psUpdate.setString(13, usuario.getEmpresa());

			psUpdate.setInt(14, usuario.getId());

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
		delete(usuario.getId());
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

	public void deleteUsuarios() {
		try {
			psDeleteUsers = con.prepareStatement(DELETE_TABLE_USUARIOS);

			psDeleteUsers.executeUpdate();

		} catch (Exception e) {
			throw new DAOException("Error en delete table", e);
		} finally {
			cerrar(psDeleteUsers);
		}
	}

	public boolean validar(Usuario usuario) {

		Usuario[] usuariosArr = null;

		try {
			usuariosArr = this.findAll();
		} catch (Exception e) {
			throw new DAOException("Error al validar usuario", e);
		}

		for (Usuario u : usuariosArr) {
			if (u.getUsername().equals(usuario.getUsername()) && u.getPassword().equals(usuario.getPassword())) {
				return true;
			}
		}
		return false;
	}

	public boolean validarNombre(Usuario usuario) {

		Usuario[] usuariosArr = null;

		try {
			usuariosArr = this.findAll();
		} catch (Exception e) {
			throw new DAOException("Error al validar usuario, e");
		}

		if (usuario.getUsername() != null) {

			for (Usuario u : usuariosArr) {
				if (usuario.getUsername().equals(u.getUsername())) {
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
