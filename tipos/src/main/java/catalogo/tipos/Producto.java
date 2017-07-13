package catalogo.tipos;

import java.io.Serializable;
import java.math.BigDecimal;

public class Producto implements Serializable {

	private static final long serialVersionUID = -6288123677827033015L;

	private int id;
	private int groupId;
	private String nombre, descripcion;
	private BigDecimal precio;
	private int imagen;

	public Producto() {
		this.groupId = 0;
		this.nombre = "";
		this.descripcion = "";
		BigDecimal bd = BigDecimal.ZERO;
		bd = bd.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		this.precio = bd;
		this.imagen = groupId;
	}

	public Producto(int groupId, String nombre, String descripcion, BigDecimal precio) {
		this.groupId = groupId;
		this.nombre = nombre;
		this.descripcion = descripcion;
		BigDecimal bd = precio;
		bd = bd.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		this.precio = bd;
		this.imagen = groupId;
	}

	public Producto(int id, int groupId, String nombre, String descripcion, BigDecimal precio) {
		this.id = id;
		this.groupId = groupId;
		this.nombre = nombre;
		this.descripcion = descripcion;
		BigDecimal bd = precio;
		bd = bd.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		this.precio = bd;
		this.imagen = groupId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getImagen() {
		return imagen;
	}

	public void setImagen(int imagen) {
		this.imagen = imagen;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}

	@Override
	public String toString() {
		return groupId + "." + id + ", " + nombre + ", " + descripcion + ", " + precio + " €";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Producto other = (Producto) obj;
		if (groupId != other.groupId)
			return false;
		return true;
	}

}
