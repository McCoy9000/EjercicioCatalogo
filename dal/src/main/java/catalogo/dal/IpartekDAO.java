package catalogo.dal;

public interface IpartekDAO {
	public void abrir();
	public void cerrar();
	
	public void iniciarTransaccion();
	public void confirmarTransaccion();
	public void deshacerTransaccion();
	public void reutilizarConexion(IpartekDAO dao);
}
