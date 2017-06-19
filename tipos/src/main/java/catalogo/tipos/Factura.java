package catalogo.tipos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Factura {
	// Constructores, getters y setters, hashCode y equals y toString
	private int id;
	private String número_factura;
	private int id_usuarios;
	private Date fecha;
	
	private List<FacturaLinea> lineas = new ArrayList<FacturaLinea>();

	public Factura(int id, String número_factura, int id_usuarios, Date fecha) {
		super();
		this.id = id;
		this.número_factura = número_factura;
		this.id_usuarios = id_usuarios;
		this.fecha = fecha;
	}

	public Factura() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNúmero_factura() {
		return número_factura;
	}

	public void setNúmero_factura(String número_factura) {
		this.número_factura = número_factura;
	}

	public int getId_usuarios() {
		return id_usuarios;
	}

	public void setId_usuarios(int id_usuarios) {
		this.id_usuarios = id_usuarios;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void addProductoYCantidad(Producto producto, int cantidad){
		lineas.add(new FacturaLinea(producto, cantidad));
	}
	
	public FacturaLinea[] getLineas(){
		return lineas.toArray(new FacturaLinea[lineas.size()]);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + id;
		result = prime * result + id_usuarios;
		result = prime * result + ((número_factura == null) ? 0 : número_factura.hashCode());
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
		Factura other = (Factura) obj;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		if (id != other.id)
			return false;
		if (id_usuarios != other.id_usuarios)
			return false;
		if (número_factura == null) {
			if (other.número_factura != null)
				return false;
		} else if (!número_factura.equals(other.número_factura))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Factura [id=" + id + ", número_factura=" + número_factura + ", id_usuarios=" + id_usuarios + ", fecha="
				+ fecha + "\n" + lineas.toString() + "]";
	}

	
}
