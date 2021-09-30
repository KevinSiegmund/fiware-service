package de.fraunhofer.iosb.ast.fiware.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import de.fraunhofer.iosb.ast.fiware.dto.FiwareCreateEntityDto;
import de.fraunhofer.iosb.ast.fiware.dto.FiwareUpdateEntityDto;
import de.fraunhofer.iosb.ast.fiware.dto.quantumleap.FiwareCreateSubscription;
import de.fraunhofer.iosb.ast.prophet.timeseries.dto.equidistant.EquidistantTimeSeries;


/**
 * 
 * @author Kevin Siegmund
 *
 */
@Service
public class ServiceFunction {

	@Autowired
	private ServiceConvert serviceConvert;

	@Autowired
	private SendToFiware sendToFiware;

	/**
	 * Methode, welche aus dem Namen der empfangenen Zeitreihe den Namen der in Fiware anzulegenden Entität ermittelt.
	 * Danach wird geprüft, ob es schon eine angelegte Entität mit dem Namen in Fiware gibt.
	 * Wenn diese Entität schon existiert wird eine weitere Methode augerufen, welche die Werte Entität updatet.
	 * Sollte es noch keine Entität mit diesem Namen geben, so wird eine Methode aufgerufen, welche eine neue Entität anlegt,
	 * zusätzlich wird eine Methode aufgerufen, welche in QuantumLeap eine Subscriptiion erstellt 
	 * @param fiwareTimeSeriesExportDto
	 */
	public void sendFunction(EquidistantTimeSeries timeSeries) {

		try {
			String entityId = serviceConvert.getEntityID(timeSeries);
			ResponseEntity<?> result = sendToFiware.getDto(entityId);
			System.out.println(result.getStatusCodeValue());
	

			if (result.getStatusCode() == HttpStatus.OK) {
				FiwareUpdateEntityDto fiwareUpdateEntityDto = serviceConvert.updateConvert(timeSeries);
				System.out.println(fiwareUpdateEntityDto);
				sendToFiware.updateDto(fiwareUpdateEntityDto, entityId);

			} else if (result.getStatusCode() == HttpStatus.NOT_FOUND) {
				FiwareCreateEntityDto fiwareCreateEntityDto = serviceConvert.convert(timeSeries);
				System.out.println(fiwareCreateEntityDto);
				FiwareCreateSubscription fiwareCreateSubscription = serviceConvert
						.createSubscription(timeSeries);
				System.out.println(fiwareCreateSubscription);
				sendToFiware.createSubscription(fiwareCreateSubscription);
				sendToFiware.postDto(fiwareCreateEntityDto);
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

}