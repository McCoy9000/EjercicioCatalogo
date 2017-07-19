package catalogo.tipos;

import java.io.Serializable;

public class Usuario implements Serializable {

	private static final long serialVersionUID = 4770875332940984231L;

	// Constructores, getters y setters, hashCode y equals y toString
	private int id;
	private int id_roles;
	private String nombre_completo;
	private String apellidos;
	private String password;
	private String username;
	private String documento;
	private String telefono;
	private String direccion;
	private String codigoPostal;
	private String ciudad;
	private String region;
	private String pais;
	private String empresa;

	public Usuario(int id, int id_roles, String nombre_completo, String apellidos, String username, String password) {
		super();
		this.id = id;
		this.id_roles = id_roles;
		this.nombre_completo = nombre_completo;
		this.apellidos = apellidos;
		this.password = password;
		this.username = username;
	}

	public Usuario(int id_roles, String nombre_completo, String apellidos, String username, String password) {
		super();

		this.id_roles = id_roles;
		this.nombre_completo = nombre_completo;
		this.apellidos = apellidos;
		this.password = password;
		this.username = username;
	}

	public Usuario(String username, String password) {
		super();
		this.id_roles = 2;
		this.nombre_completo = username;
		this.password = password;
		this.username = username;
	}

	public Usuario() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_roles() {
		return id_roles;
	}

	public void setId_roles(int id_roles) {
		this.id_roles = id_roles;
	}

	public String getNombre_completo() {
		return nombre_completo;
	}

	public void setNombre_completo(String nombre_completo) {
		this.nombre_completo = nombre_completo;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", id_roles=" + id_roles + ", nombre_completo=" + nombre_completo + ", apellidos=" + apellidos + ", password=" + password + ", username=" + username
				+ ", documento=" + documento + ", telefono=" + telefono + ", direccion=" + direccion + ", codigoPostal=" + codigoPostal + ", ciudad=" + ciudad + ", region=" + region + ", pais="
				+ pais + ", empresa=" + empresa + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apellidos == null) ? 0 : apellidos.hashCode());
		result = prime * result + ((ciudad == null) ? 0 : ciudad.hashCode());
		result = prime * result + ((codigoPostal == null) ? 0 : codigoPostal.hashCode());
		result = prime * result + ((direccion == null) ? 0 : direccion.hashCode());
		result = prime * result + ((documento == null) ? 0 : documento.hashCode());
		result = prime * result + ((empresa == null) ? 0 : empresa.hashCode());
		result = prime * result + id;
		result = prime * result + id_roles;
		result = prime * result + ((nombre_completo == null) ? 0 : nombre_completo.hashCode());
		result = prime * result + ((pais == null) ? 0 : pais.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((region == null) ? 0 : region.hashCode());
		result = prime * result + ((telefono == null) ? 0 : telefono.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		Usuario other = (Usuario) obj;
		if (apellidos == null) {
			if (other.apellidos != null)
				return false;
		} else if (!apellidos.equals(other.apellidos))
			return false;
		if (ciudad == null) {
			if (other.ciudad != null)
				return false;
		} else if (!ciudad.equals(other.ciudad))
			return false;
		if (codigoPostal == null) {
			if (other.codigoPostal != null)
				return false;
		} else if (!codigoPostal.equals(other.codigoPostal))
			return false;
		if (direccion == null) {
			if (other.direccion != null)
				return false;
		} else if (!direccion.equals(other.direccion))
			return false;
		if (documento == null) {
			if (other.documento != null)
				return false;
		} else if (!documento.equals(other.documento))
			return false;
		if (empresa == null) {
			if (other.empresa != null)
				return false;
		} else if (!empresa.equals(other.empresa))
			return false;
		if (id != other.id)
			return false;
		if (id_roles != other.id_roles)
			return false;
		if (nombre_completo == null) {
			if (other.nombre_completo != null)
				return false;
		} else if (!nombre_completo.equals(other.nombre_completo))
			return false;
		if (pais == null) {
			if (other.pais != null)
				return false;
		} else if (!pais.equals(other.pais))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (region == null) {
			if (other.region != null)
				return false;
		} else if (!region.equals(other.region))
			return false;
		if (telefono == null) {
			if (other.telefono != null)
				return false;
		} else if (!telefono.equals(other.telefono))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}
