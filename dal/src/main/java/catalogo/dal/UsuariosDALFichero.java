package catalogo.dal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.TreeMap;

import catalogo.tipos.Usuario;



public class UsuariosDALFichero implements UsuariosDAL {

	private TreeMap<String, Usuario> usuarios = new TreeMap<String, Usuario>();;
	
	private File fichero = new File("usuarios.dat");;
	
	
	public UsuariosDALFichero() {

		super();

		if (!fichero.exists()) {

			escribirDatabase();
			
		} else {
			
			leerDatabase();
		}
	}

	
	public void alta(Usuario usuario) {

		if (usuarios.containsKey(usuario.getNombre()))
			throw new UsuarioYaExistenteDALException("Ya existe el usuario");
		
			usuarios.put(usuario.getNombre(), usuario);
		
		escribirDatabase();
	}

	public boolean validar(Usuario usuario) {

		return usuarios.containsValue(usuario);
	}
	
	public boolean validarNombre(Usuario usuario) {
		
		String[] usuariosArr = usuarios.keySet().toArray(new String[usuarios.keySet().size()]);
		
		if (usuario.getNombre() != null){
		
			for (String s : usuariosArr) {
				
				if (usuario.getNombre().equals(s)){
					
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void modificar(Usuario usuario) {
		if (!usuarios.containsKey(usuario.getNombre()))
			throw new DALException("Intento de modificar producto no existente " + usuarios);

		usuarios.put(usuario.getNombre(), usuario);
		escribirDatabase();
	}
	
	@Override
	public void borrar(Usuario usuario) {
		usuarios.remove(usuario.getNombre());
		escribirDatabase();
	}

	@Override
	public Usuario buscarPorId(String id) {
		return usuarios.get(id);
	}

	@Override
	public Usuario[] buscarTodosLosUsuarios() {
		
		return usuarios.values().toArray(new Usuario[usuarios.size()]);
	}

	public TreeMap<String, Usuario> getDatabase() {
		return (TreeMap<String, Usuario>) usuarios;
	}

	public void setDatabase(TreeMap<String, Usuario> database) {
		this.usuarios = database;

		escribirDatabase();
	}

	
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
	
	@SuppressWarnings("unchecked")
	private void leerDatabase() {
		
		try {
			FileInputStream fis = new FileInputStream(fichero);
			ObjectInputStream ois = new ObjectInputStream(fis);
			usuarios = (TreeMap<String, Usuario>) ois.readObject();
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
