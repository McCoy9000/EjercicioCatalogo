package org.dal;


public class UsuariosDALFactory {

	public static UsuariosDAL getUsuariosDAL() {
		// return new UsuariosDALUsuarioUnico();
		UsuariosDAL usuarios = new UsuariosDALFichero();

		return usuarios;

	}
}
