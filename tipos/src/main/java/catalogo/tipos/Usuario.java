package catalogo.tipos;

import java.io.Serializable;
import java.time.LocalDate;

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
	private int direccion;
	private LocalDate fechaDeNacimiento;
	private int empresa;


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

	public int getDireccion() {
		return direccion;
	}

	public void setDireccion(int direccion) {
		this.direccion = direccion;
	}

	public LocalDate getFechaDeNacimiento() {
		return fechaDeNacimiento;
	}

	public void setFechaDeNacimiento(LocalDate fechaDeNacimiento) {
		this.fechaDeNacimiento = fechaDeNacimiento;
	}

	public int getEmpresa() {
		return empresa;
	}

	public void setEmpresa(int empresa) {
		this.empresa = empresa;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", id_roles=" + id_roles
				+ ", nombre_completo=" + nombre_completo + ", apellidos="
				+ apellidos + ", password=" + password + ", username="
				+ username + ", documento=" + documento + ", direccion="
				+ direccion + ", fechaDeNacimiento=" + fechaDeNacimiento
				+ ", empresa=" + empresa + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((apellidos == null) ? 0 : apellidos.hashCode());
		result = prime * result + direccion;
		result = prime * result
				+ ((documento == null) ? 0 : documento.hashCode());
		result = prime * result + empresa;
		result = prime
				* result
				+ ((fechaDeNacimiento == null) ? 0 : fechaDeNacimiento
						.hashCode());
		result = prime * result + id;
		result = prime * result + id_roles;
		result = prime * result
				+ ((nombre_completo == null) ? 0 : nombre_completo.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
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
		if (direccion != other.direccion)
			return false;
		if (documento == null) {
			if (other.documento != null)
				return false;
		} else if (!documento.equals(other.documento))
			return false;
		if (empresa != other.empresa)
			return false;
		if (fechaDeNacimiento == null) {
			if (other.fechaDeNacimiento != null)
				return false;
		} else if (!fechaDeNacimiento.equals(other.fechaDeNacimiento))
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
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}
