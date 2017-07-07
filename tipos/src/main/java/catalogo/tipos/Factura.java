package catalogo.tipos;

import java.io.Serializable;
import java.util.Date;

import catalogo.constantes.Constantes;

public class Factura implements Serializable {
	
	private static final long serialVersionUID = 7877238780760817273L;
	
	// Constructores, getters y setters, hashCode y equals y toString
	private int id;
	private String numero_factura;
	private int id_usuarios;
	private Date fecha;

	public Factura(String número_factura, int id_usuarios, Date fecha) {
		super();
		this.numero_factura = número_factura;
		this.id_usuarios = id_usuarios;
		this.fecha = fecha;
	}

	public Factura(int id, String número_factura, int id_usuarios, Date fecha) {
		super();
		this.id = id;
		this.numero_factura = número_factura;
		this.id_usuarios = id_usuarios;
		this.fecha = fecha;
		Constantes.siguienteFactura++;
	}

	public Factura(int id_usuarios, Date fecha) {
		this.numero_factura = String.format("DRV%09d", Constantes.siguienteFactura);
		this.id_usuarios = id_usuarios;
		this.fecha = fecha;
		Constantes.siguienteFactura++;
	}

	public Factura() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNumero_factura() {
		return numero_factura;
	}

	public void setNumero_factura(String número_factura) {
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
		result = prime * result
				+ ((numero_factura == null) ? 0 : numero_factura.hashCode());
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
		if (numero_factura == null) {
			if (other.numero_factura != null)
				return false;
		} else if (!numero_factura.equals(other.numero_factura))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Factura [id=" + id + ", numero_factura=" + numero_factura
				+ ", id_usuarios=" + id_usuarios + ", fecha=" + fecha + "]";
	}


}
