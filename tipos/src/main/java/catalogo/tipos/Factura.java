package catalogo.tipos;

import java.io.Serializable;
import java.util.Date;

public class Factura implements Serializable {
	
	private static final long serialVersionUID = 7877238780760817273L;
	public static int siguienteFactura = 0;
	
	// Constructores, getters y setters, hashCode y equals y toString
	private int id;
	private int numero_factura;
	private int id_usuarios;
	private Date fecha;

	public Factura(int número_factura, int id_usuarios, Date fecha) {
		super();
		this.numero_factura = número_factura;
		this.id_usuarios = id_usuarios;
		this.fecha = fecha;
	}

	public Factura(int id, int número_factura, int id_usuarios, Date fecha) {
		super();
		this.id = id;
		this.numero_factura = número_factura;
		this.id_usuarios = id_usuarios;
		this.fecha = fecha;
	}

	public Factura(int id_usuarios, Date fecha) {
		this.numero_factura = siguienteFactura;
		this.id_usuarios = id_usuarios;
		this.fecha = fecha;
		siguienteFactura++;
	}

	public Factura() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumero_factura() {
		return numero_factura;
	}

	public void setNumero_factura(int número_factura) {
		this.numero_factura = número_factura;
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + id;
		result = prime * result + id_usuarios;
		result = prime * result + numero_factura;
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
		if (numero_factura != other.numero_factura)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Factura [id=" + id + ", número_factura=" + numero_factura + ", id_usuarios=" + id_usuarios + ", fecha=" + fecha + "\n" + "]";
	}

}
