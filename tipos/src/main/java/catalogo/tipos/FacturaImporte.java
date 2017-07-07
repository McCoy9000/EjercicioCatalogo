package catalogo.tipos;

import java.util.Date;

import catalogo.constantes.Constantes;

public class FacturaImporte extends Factura {

	private static final long serialVersionUID = -7618354108992118731L;

	Double importe;
	Double ivaFactura = importe * Constantes.IVA;
	Double pvp = importe + ivaFactura;

	public FacturaImporte(String número_factura, int id_usuarios, Date fecha) {
		super(número_factura, id_usuarios, fecha);
		// TODO Auto-generated constructor stub
	}

	public FacturaImporte(int id, String número_factura, int id_usuarios, Date fecha) {
		super(id, número_factura, id_usuarios, fecha);
		// TODO Auto-generated constructor stub
	}

	public FacturaImporte(int id_usuarios, Date fecha) {
		super(id_usuarios, fecha);
		// TODO Auto-generated constructor stub
	}

	public FacturaImporte() {
		// TODO Auto-generated constructor stub
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

}
