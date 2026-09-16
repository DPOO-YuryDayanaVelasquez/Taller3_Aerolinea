package uniandes.dpoo.aerolinea.modelo;

import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.modelo.tarifas.CalculadoraTarifas;
import uniandes.dpoo.aerolinea.tiquetes.GeneradorTiquetes;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;
import uniandes.dpoo.aerolinea.exceptions.VueloSobrevendidoException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Vuelo{
	
	private String fecha;
	private Ruta ruta;
	private Avion avion;
	private Map<String, Tiquete> tiquetes;

	public Vuelo(Ruta ruta, String fecha, Avion avion) {
		
		this.ruta = ruta;
		this.avion = avion;
		this.fecha = fecha;
		this.tiquetes = new HashMap<String, Tiquete>();
	}

	
	public Ruta getRuta() {
		return ruta;
	}
	
	public String getFecha() {
		return fecha;
	}
	
	public Avion getAvion() {
		return avion;
	}
	
	public Collection<Tiquete> getTiquetes() {
		return tiquetes.values();
	}
	
	public int venderTiquetes(Cliente cliente, CalculadoraTarifas calculadora, int cantidad) throws VueloSobrevendidoException {
		if ((this.avion.getCapacidad() - tiquetes.size())< cantidad) {
			throw new VueloSobrevendidoException(this);
		} 
			int cantidadComprada = 0;
			int totalValor = 0;
			int tarifa = calculadora.calcularTarifa(this, cliente);
			while (cantidadComprada < cantidad) {
				Tiquete tiquete = GeneradorTiquetes.generarTiquete(this, cliente, tarifa);
				GeneradorTiquetes.registrarTiquete(tiquete);
				registrarTiquete(tiquete);
				cantidadComprada +=1;
				totalValor += tiquete.getTarifa();
			}
			return totalValor;
	}

	public void registrarTiquete(Tiquete tiquete) {
		this.tiquetes.put(tiquete.getCodigo(), tiquete);
	}
	
	public boolean equals(Object obj){
		if (obj instanceof Vuelo) {
			Vuelo otroVuelo = (Vuelo) obj;			
			if (fecha.equals(otroVuelo.getFecha()) && ruta.getCodigoRuta().equals(otroVuelo.getRuta().getCodigoRuta())) {
				return true;
			}
		} return false;
	}
	
	
	
	
}
