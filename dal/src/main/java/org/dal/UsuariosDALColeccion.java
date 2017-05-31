package org.dal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.tipos.Usuario;

public class UsuariosDALColeccion implements UsuariosDAL {

	private Map<String, Usuario> usuarios = new HashMap<String, Usuario>();
	
	File fichero = new File("usuarios.dat");
	
	
	public void alta(Usuario usuario) {
		if (usuarios.containsKey(usuario.getNombre()))
			throw new UsuarioYaExistenteDALException("Ya existe el usuario " + usuario.getNombre());

		usuarios.put(usuario.getNombre(), usuario);
	}

	public boolean validar(Usuario usuario) {
		return usuarios.containsValue(usuario);
	}

	public void modificar(Usuario usuario) {
		if (!usuarios.containsKey(usuario.getNombre()))
			throw new DALException("Intento de modificar usuario no existente " + usuario);

		usuarios.put(usuario.getNombre(), usuario);
	}

	public void borrar(Usuario usuario) {
		usuarios.remove(usuario.getNombre());
	}

	public Usuario buscarPorId(String id) {
		return usuarios.get(id);
	}

	public Usuario[] buscarTodosLosUsuarios() {
		// Usuario[] arr = new Usuario[usuarios.size()];
		//
		// int i = 0;
		//
		// for(Usuario usuario : usuarios.values())
		// arr[i++] = usuario;
		//
		// return arr;

		return usuarios.values().toArray(new Usuario[usuarios.size()]);
	}
	
	public boolean validarNombre(Usuario usuario) {
		
		Collection<Usuario> users = usuarios.values();
		
		if (usuario.getNombre() != null){
		
			for (Usuario u : users) {
				
				if (usuario.getNombre().equals(u.getNombre())){
					
					return true;
					
				}
				
			}
		}
		
		return false;
	}
	
	
	@SuppressWarnings("unused")
	private void escribirDatabase() {
			
			try {
				FileOutputStream fos = new FileOutputStream(fichero);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(usuarios);
				oos.close();
				fos.close();
			} catch (FileNotFoundException e) {
				System.out.println("FileNotFoundException");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("IOException");
				e.printStackTrace();
			}
			
		}
		
		@SuppressWarnings({ "unchecked", "unused" })
		private void leerDatabase() {
			
			try {
				FileInputStream fis = new FileInputStream(fichero);
				ObjectInputStream ois = new ObjectInputStream(fis);
				usuarios = (Map<String, Usuario>) ois.readObject();
				ois.close();
				fis.close();
			} catch (IOException e) {
				System.out.println("IOException reading");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				System.out.println("Class not found Exception reading");
				e.printStackTrace();
			}
			
		}
}

	