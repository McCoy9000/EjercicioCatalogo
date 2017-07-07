package catalogo.encriptacion;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Encriptador {

	String Key = "todostusmuertos";
	byte[] KeyData = Key.getBytes();

	public SecretKeySpec KS = new SecretKeySpec(KeyData, "Blowfish");

	public Cipher cipher = Cipher.getInstance("Blowfish");

	public Encriptador() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {

		cipher.init(Cipher.ENCRYPT_MODE, KS);

	}

}
