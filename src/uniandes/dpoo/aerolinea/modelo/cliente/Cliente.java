package uniandes.dpoo.aerolinea.modelo.cliente;

import java.util.ArrayList;
import java.util.List;

import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

public abstract class Cliente {
	
	private List<Tiquete> tiquetesSinUsar;
	private List<Tiquete> tiquetesUsados;
	
	public Cliente() {
		this.tiquetesSinUsar = new ArrayList<Tiquete>();
		this.tiquetesUsados = new ArrayList<Tiquete>();
	}
	
	public abstract String getTipoCliente();

	public abstract String getIdentificador();
	
	public void agregarTiquete(Tiquete tiquete) {
		tiquetesSinUsar.add(tiquete);
	}
	
	public int calcularValorTotalTiquetes() {
		int totalComprados = 0;
		for (Tiquete tiquete: tiquetesSinUsar) {
			totalComprados += tiquete.getTarifa();
		} for (Tiquete tiquete: tiquetesUsados) {
			totalComprados += tiquete.getTarifa();
		}
		return totalComprados;
	}
	
	public void  usarTiquetes(Vuelo vuelo) {
		for (int i=tiquetesSinUsar.size()-1; i>=0; i--) {
			Tiquete tiquete = tiquetesSinUsar.get(i);
			if (tiquete.getVuelo().equals(vuelo)) {
				tiquetesUsados.add(tiquete);
				tiquete.marcarComoUsado();
				tiquetesSinUsar.remove(i);
			}
		}
	}

}
