package catalogo.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class IpartekDAOMySQL implements IpartekDAO {

	protected Connection con;

	private String url = "jdbc:postgresql://ec2-50-17-217-166.compute-1.amazonaws.com:5432/dalspvc80kevn7";
	private String mysqlUser = "yxmlvawotaeszy";
	private String mysqlPass = "e9d296f2a4ae6c0a6d3a2df66754faf2c52d05617ed971707b970f0e579fc6c4";

	public IpartekDAOMySQL(String url, String mysqlUser, String mysqlPass) {
		this();
		this.url = url;
		this.mysqlUser = mysqlUser;
		this.mysqlPass = mysqlPass;
	}

	public IpartekDAOMySQL(String url) {

		this.url = url;

	}

	public IpartekDAOMySQL() {
		try {
			Class.forName("org.postgresql.Driver").newInstance();
		} catch (InstantiationException e) {
			throw new DAOException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new DAOException(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			throw new DAOException("No se ha encontrado el driver de SQLite", e);
		} catch (Exception e) {
			throw new DAOException("ERROR NO ESPERADO", e);
		}
	}

	public void abrir() {
		try {
			con = DriverManager.getConnection(url, mysqlUser, mysqlPass);
		} catch (SQLException e) {
			throw new DAOException("Error de conexión a la base de datos", e);
		} catch (Exception e) {
			throw new DAOException("ERROR NO ESPERADO", e);
		}
	}

	public void cerrar() {
		try {
			if (con != null && !con.isClosed()) {
				con.close();
			}
			con = null;
		} catch (SQLException e) {
			throw new DAOException("Error de cierre de conexión a la base de datos", e);
		} catch (Exception e) {
			throw new DAOException("ERROR NO ESPERADO", e);
		}
	}

	public void iniciarTransaccion() {
		try {
			con.setAutoCommit(false);
		} catch (SQLException e) {
			throw new DAOException("Error al desactivar AutoCommit", e);
		}
	}

	public void confirmarTransaccion() {
		try {
			con.commit();
			con.setAutoCommit(true);
		} catch (SQLException e) {
			throw new DAOException("Error al confirmar transacción", e);
		}
	}

	public void deshacerTransaccion() {
		try {
			con.rollback();
			con.setAutoCommit(true);
		} catch (SQLException e) {
			throw new DAOException("Error al deshacer transacción", e);
		}
	}

	public void reutilizarConexion(IpartekDAO dao) {
		con = ((IpartekDAOMySQL) dao).con;
	}

}
