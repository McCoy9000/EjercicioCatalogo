package catalogo.tipos;

public class FacturaMascara {

	private int id;
	private String numero_factura;
	private String usuario;
	private String fecha;
	
	public FacturaMascara() {

	}

	public FacturaMascara(int id, String numero_factura, String usuario, String fecha) {
		this.id = id;
		this.numero_factura = numero_factura;
		this.usuario = usuario;
		this.fecha = fecha;
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

	public void setNumero_factura(String numero_factura) {
		this.numero_factura = numero_factura;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	@Override
	public String toString() {
		return "FacturaMascara [id=" + id + ", numero_factura="
				+ numero_factura + ", usuario=" + usuario + ", fecha=" + fecha
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + id;
		result = prime * result
				+ ((numero_factura == null) ? 0 : numero_factura.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
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
		FacturaMascara other = (FacturaMascara) obj;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		if (id != other.id)
			return false;
		if (numero_factura == null) {
			if (other.numero_factura != null)
				return false;
		} else if (!numero_factura.equals(other.numero_factura))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}
	
	
	
}
