package catalogo.tipos;

import java.io.Serializable;

public class Producto implements Serializable {

	private static final long serialVersionUID = -6288123677827033015L;

	private int id;
	private int groupId;
	private String nombre, descripcion, errores;
	private double precio;
	private int imagen;

	public Producto() {
		this.groupId = 0;
		this.nombre = "";
		this.descripcion = "";
		this.precio = 0.0;
		this.imagen = groupId;
	}

	public Producto(int groupId, String nombre, String descripcion, double precio) {
		this.groupId = groupId;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.imagen = groupId;
	}

	public Producto(int id, int groupId, String nombre, String descripcion, double precio) {
		this.id = id;
		this.groupId = groupId;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
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

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getErrores() {
		return errores;
	}

	public void setErrores(String errores) {
		this.errores = errores;
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
