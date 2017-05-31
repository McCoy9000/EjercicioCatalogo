package org.tipos;

import java.io.Serializable;

public class Usuario implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1091638722883386161L;
	
	private String nombre, pass, errores;
	private boolean admin = false;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((pass == null) ? 0 : pass.hashCode());
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
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (pass == null) {
			if (other.pass != null)
				return false;
		} else if (!pass.equals(other.pass))
			return false;
		return true;
	}

	public Usuario(String nombre, String pass, boolean esAdmin) {
		super();
		this.nombre = nombre;
		this.pass = pass;
		this.admin = esAdmin;
	}
	
	public Usuario(String nombre, String pass) {
		super();
		this.nombre = nombre;
		this.pass = pass;
	}

	public Usuario() {
		this("", "");
	}

	@Override
	public String toString() {
		return "Usuario [nombre=" + nombre + ", pass=" + pass + "]";
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getErrores() {
		return errores;
	}

	public void setErrores(String errores) {
		this.errores = errores;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean esAdmin) {
		this.admin = esAdmin;
	}
}
