package uniandes.dpoo.aerolinea.modelo.tarifas;

import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.modelo.cliente.ClienteCorporativo;
import uniandes.dpoo.aerolinea.modelo.cliente.ClienteNatural;

public class CalculadoraTarifasTemporadaBaja extends CalculadoraTarifas {
	
	protected final int COSTO_POR_KM_NATURAL = 600;
	protected final int COSTO_POR_KM_CORPORATIVO = 900;
	protected final double DESCUENTO_PEQ = 0.02;
	protected final double DESCUENTO_MEDIANAS = 0.1;
	protected final double DESCUENTO_GRANDES = 0.2;
	
	public CalculadoraTarifasTemporadaBaja() {
		
	}
	
	@Override
	public int calcularCostoBase(Vuelo vuelo, Cliente cliente) {
		if (cliente.getTipoCliente().equals(ClienteNatural.NATURAL)) {
			return COSTO_POR_KM_NATURAL * calcularDistanciaVuelo(vuelo.getRuta());
		} else {
			return COSTO_POR_KM_CORPORATIVO * calcularDistanciaVuelo(vuelo.getRuta());
		}
	}
	
	@Override
	public double calcularPorcentajeDescuento(Cliente cliente) {
		if (cliente.getTipoCliente().equals(ClienteCorporativo.CORPORATIVO)) {
			ClienteCorporativo clienteCorporativo = (ClienteCorporativo) cliente;
			if (clienteCorporativo.getTamanoEmpresa() == ClienteCorporativo.GRANDE) {
				return DESCUENTO_GRANDES;
			} else if (clienteCorporativo.getTamanoEmpresa() == ClienteCorporativo.MEDIANA) {
				return DESCUENTO_MEDIANAS;
			} else {
				return DESCUENTO_PEQ;
			}
		} return 0;
	}

}
