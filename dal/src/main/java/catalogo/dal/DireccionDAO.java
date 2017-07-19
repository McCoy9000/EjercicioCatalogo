package catalogo.dal;

import catalogo.tipos.Direccion;

public interface DireccionDAO extends IpartekDAO {
	
	public Direccion[] findAll();
	
	public Direccion findById(int id);

	public int insert(Direccion direccion);

	public void update(Direccion direccion);

	public void delete(Direccion direccion);

	public void delete(int id);


}
