package uniandes.dpoo.aerolinea.persistencia;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uniandes.dpoo.aerolinea.exceptions.AeropuertoDuplicadoException;
import uniandes.dpoo.aerolinea.exceptions.InformacionInconsistenteException;
import uniandes.dpoo.aerolinea.modelo.Aerolinea;
import uniandes.dpoo.aerolinea.modelo.Aeropuerto;
import uniandes.dpoo.aerolinea.modelo.Avion;
import uniandes.dpoo.aerolinea.modelo.Ruta;
import uniandes.dpoo.aerolinea.modelo.Vuelo;

public class PersistenciaAerolineaJson implements IPersistenciaAerolinea
{
	
	public PersistenciaAerolineaJson() {
		
	}
	
	@Override
	public void cargarAerolinea(String archivo, Aerolinea aerolinea) throws IOException, InformacionInconsistenteException {
        
		
		File file = new File(archivo);
		if (!file.exists()) {
			throw new IOException("El archivo no existe en la ruta especificada: " + archivo);
		} 
		
		String jsonCompleto = new String( Files.readAllBytes( new File( archivo ).toPath( ) ) );
        
		try {
			
			JSONObject raiz = new JSONObject( jsonCompleto );
			
			JSONArray jAeropuertos = raiz.getJSONArray("Aeropuertos");
			JSONArray jAviones = raiz.getJSONArray("Aviones");
			JSONArray jRutas = raiz.getJSONArray("Rutas");
			JSONArray jVuelos = raiz.getJSONArray("Vuelos");
			
			Map<String, Aeropuerto> aeropuertos = new HashMap<>();
			Map<String, Avion> aviones = new HashMap<>();
			
	        // =========================
	        // AEROPUERTOS
	        // =========================
			
			for (int i=0; i<jAeropuertos.length(); i++ ) {
				
				JSONObject jAeropuerto = jAeropuertos.getJSONObject(i);
				
				String nombre = jAeropuerto.getString("nombre");
				String codigo = jAeropuerto.getString("codigo");
				String nombreCiudad = jAeropuerto.getString("nombreCiudad");
				double latitud = jAeropuerto.getDouble("latitud");
				double longitud = jAeropuerto.getDouble("longitud");
						
				try {
					
					Aeropuerto aeropuerto = new Aeropuerto(nombre, codigo, nombreCiudad, latitud, longitud);
					aeropuertos.put(codigo, aeropuerto);
					
				} catch (AeropuertoDuplicadoException e) {
					throw new InformacionInconsistenteException(e.getMessage());
				}			
            
			}
			
			// =========================
	        // AVIONES
	        // =========================
			
			for (int i=0;i<jAviones.length(); i++) {
				
				JSONObject jAvion = jAviones.getJSONObject(i);
				
				String nombreAvion = jAvion.getString("nombre");
				int capacidadAvion  = jAvion.getInt("capacidad");
				

				Avion avion = new Avion(nombreAvion, capacidadAvion);
				aviones.put(nombreAvion, avion);
				aerolinea.agregarAvion(avion);
				
			}
			
			// =========================
	        // RUTAS
	        // =========================
			
			for(int i=0; i<jRutas.length(); i++) {
				
				JSONObject jRuta = jRutas.getJSONObject(i);
				
				String horaSalida = jRuta.getString("horaSalida");
				String horaLlegada = jRuta.getString("horaLlegada");
				String codigoRuta = jRuta.getString("codigoRuta");
				
				String codigoOrigen = jRuta.getString("origen");
				String codigoDestino = jRuta.getString("destino");
				
				Aeropuerto origen = aeropuertos.get(codigoOrigen);
				Aeropuerto destino = aeropuertos.get(codigoDestino);
				
				if (origen == null) {
	                throw new InformacionInconsistenteException("El aeropuerto de origen '" + codigoOrigen + "' no existe.");
	            } if (destino == null) {
	            	throw new InformacionInconsistenteException("El aeropuerto de destino '" + codigoDestino + "' no existe.");
	            }
				
				Ruta ruta = new Ruta(origen, destino, horaSalida, horaLlegada, codigoRuta);
				aerolinea.agregarRuta(ruta);
				
			}
			
			// =========================
	        // VUELOS
	        // =========================
			
			for(int i=0; i<jVuelos.length(); i++) {
				
				JSONObject jVuelo = jVuelos.getJSONObject(i);

	            String codigoRuta = jVuelo.getString("codigoRuta");
	            String fecha = jVuelo.getString("fecha");
	            String nombreAvion = jVuelo.getString("avion");

	            Ruta ruta = aerolinea.getRuta(codigoRuta);
	            Avion avion = aviones.get(nombreAvion);

	            if (ruta == null) {
	                throw new InformacionInconsistenteException("La ruta '" + codigoRuta + "' no existe.");
	            } if (avion == null) {
	                throw new InformacionInconsistenteException("El avión '" + nombreAvion + "' no existe.");
	            }

	            Vuelo vuelo = new Vuelo(ruta, fecha, avion);
	            aerolinea.getVuelos().add(vuelo);	
				
			}
			
		} catch (JSONException e) {
			throw new InformacionInconsistenteException("El formato del archivo de aerolíneas es inválido: " + e.getMessage());
		}
	}
	
	@Override
	public void salvarAerolinea(String archivo, Aerolinea aerolinea) throws IOException {

	    JSONObject raiz = new JSONObject();

	    // =========================
	    // AEROPUERTOS
	    // =========================

	    JSONArray jAeropuertos = new JSONArray();
	    Map<String, Aeropuerto> aeropuertos = new HashMap<>();

	    for (Ruta ruta : aerolinea.getRutas()) {

	        Aeropuerto origen = ruta.getOrigen();
	        Aeropuerto destino = ruta.getDestino();

	        aeropuertos.put(origen.getCodigo(), origen);
	        aeropuertos.put(destino.getCodigo(), destino);
	    }

	    for (Aeropuerto aeropuerto : aeropuertos.values()) {

	        JSONObject jAeropuerto = new JSONObject();

	        jAeropuerto.put("nombre", aeropuerto.getNombre());
	        jAeropuerto.put("codigo", aeropuerto.getCodigo());
	        jAeropuerto.put("nombreCiudad", aeropuerto.getNombreCiudad());
	        jAeropuerto.put("latitud", aeropuerto.getLatitud());
	        jAeropuerto.put("longitud", aeropuerto.getLongitud());

	        jAeropuertos.put(jAeropuerto);
	    }

	    raiz.put("Aeropuertos", jAeropuertos);

	    // =========================
	    // AVIONES
	    // =========================

	    JSONArray jAviones = new JSONArray();

	    for (Avion avion : aerolinea.getAviones()) {

	        JSONObject jAvion = new JSONObject();

	        jAvion.put("nombre", avion.getNombre());
	        jAvion.put("capacidad", avion.getCapacidad());

	        jAviones.put(jAvion);
	    }

	    raiz.put("Aviones", jAviones);

	    // =========================
	    // RUTAS
	    // =========================

	    JSONArray jRutas = new JSONArray();

	    for (Ruta ruta : aerolinea.getRutas()) {

	        JSONObject jRuta = new JSONObject();

	        jRuta.put("horaSalida", ruta.getHoraSalida());
	        jRuta.put("horaLlegada", ruta.getHoraLlegada());
	        jRuta.put("codigoRuta", ruta.getCodigoRuta());
	        jRuta.put("origen", ruta.getOrigen().getCodigo());
	        jRuta.put("destino", ruta.getDestino().getCodigo());

	        jRutas.put(jRuta);
	    }

	    raiz.put("Rutas", jRutas);

	    // =========================
	    // VUELOS
	    // =========================

	    JSONArray jVuelos = new JSONArray();

	    for (Vuelo vuelo : aerolinea.getVuelos()) {

	        JSONObject jVuelo = new JSONObject();

	        jVuelo.put("codigoRuta",vuelo.getRuta().getCodigoRuta());
	        jVuelo.put("avion",vuelo.getAvion().getNombre());
	        jVuelo.put("fecha",vuelo.getFecha());
	        jVuelos.put(jVuelo);
	    }

	    raiz.put("Vuelos", jVuelos);

	    // =========================
	    // ESCRIBIR ARCHIVO
	    // =========================

	    PrintWriter pw = new PrintWriter(archivo);
	    raiz.write(pw, 2, 0);
	    pw.close();
	}
}
