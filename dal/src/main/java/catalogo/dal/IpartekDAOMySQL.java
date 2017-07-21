package catalogo.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class IpartekDAOMySQL implements IpartekDAO {

	protected Connection con;
	
	private String url = "jdbc:postgres://fvgnqfvntiqegx:0dd45f5b83cc46dd88c3b86820627e3e9f9794cf44924f14810d332b4ca4f01f@ec2-107-20-250-195.compute-1.amazonaws.com:5432/db7mlk8ek0n021_=/usr/bin/env";
	private String mysqlUser = "Mikel";
	private String mysqlPass = "tuput4m4dr3";
	
	
	public IpartekDAOMySQL(String url, String mysqlUser, String mysqlPass) {
		this();
		this.url = url;
		this.mysqlUser = mysqlUser;
		this.mysqlPass = mysqlPass;
	}
	
	
	public IpartekDAOMySQL(String url) {
		this();
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
			con = DriverManager.getConnection(url);
		} catch (SQLException e) {
			throw new DAOException("Error de conexión a la base de datos", e);
		} catch (Exception e) {
			throw new DAOException("ERROR NO ESPERADO", e);
		}
	}

	public void cerrar() {
		try {
			if(con != null && !con.isClosed()) {
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
		con = ((IpartekDAOMySQL)dao).con;
	}
	
}

	

