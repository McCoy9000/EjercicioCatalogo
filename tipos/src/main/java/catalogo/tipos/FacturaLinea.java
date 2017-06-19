package catalogo.tipos;

public class FacturaLinea {
		private Producto producto;
		private int cantidad;
		
		public FacturaLinea(Producto producto, int cantidad) {
			super();
			this.producto = producto;
			this.cantidad = cantidad;
		}
		
		public Producto getProducto() {
			return producto;
		}
		public void setProducto(Producto producto) {
			this.producto = producto;
		}
		public int getCantidad() {
			return cantidad;
		}
		public void setCantidad(int cantidad) {
			this.cantidad = cantidad;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + cantidad;
			result = prime * result + ((producto == null) ? 0 : producto.hashCode());
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
			FacturaLinea other = (FacturaLinea) obj;
			if (cantidad != other.cantidad)
				return false;
			if (producto == null) {
				if (other.producto != null)
					return false;
			} else if (!producto.equals(other.producto))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Linea [producto=" + producto + ", cantidad=" + cantidad + "]";
		}
		
	}