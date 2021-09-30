package de.fraunhofer.iosb.ast.fiware.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import de.fraunhofer.iosb.ast.fiware.dto.FiwareCreateEntityDto;
import de.fraunhofer.iosb.ast.fiware.dto.FiwareUpdateEntityDto;
import de.fraunhofer.iosb.ast.fiware.dto.quantumleap.FiwareCreateSubscription;
/**
 * 
 * @author Kevin Siegmund
 *
 */
@Service
public class SendToFiware {

	@Autowired
	private RestTemplate restTemplate;

	
	@Value("${service.url.postDto}")
	private String postUrl;


	/**
	 * Diese Methode legt mittels eines HTTP POST Aufrufs eine neue Entitaet in Fiware an
	 * @param fiwareExportDto Objekt mit den Werten
	 * @return Gibt einen Http Statuscode zurueck
	 */
	public ResponseEntity<?> postDto(FiwareCreateEntityDto fiwareExportDto) {

		try {
			System.out.println(fiwareExportDto);
			HttpHeaders headers = new HttpHeaders();
			headers.set("fiware-service", "Fiware");
			headers.set("fiware-servicepath", "/Test");
			HttpEntity<FiwareCreateEntityDto> entity = new HttpEntity<FiwareCreateEntityDto>(fiwareExportDto, headers);
			System.out.println(entity);
			restTemplate.exchange(postUrl, HttpMethod.POST, entity, String.class);

			return ResponseEntity.status(HttpStatus.OK).body("Entity successfully created");
		} catch (Exception e) {
			System.out.println("Fehler");
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}
	}

	/**
	 * Diese Methode erstellt duech einen HTTP POST Aufruf eine neue Subscription fuer eine Entitaet
	 * @param fiwareCreateSubscription Objekt mit den benoetigten Werten
	 * @return Gibt den HTTP Statuscode zurueck
	 */
	public ResponseEntity<?> createSubscription(FiwareCreateSubscription fiwareCreateSubscription) {

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("fiware-service", "Fiware");
			headers.set("fiware-servicepath", "/Test");
			HttpEntity<FiwareCreateSubscription> entity = new HttpEntity<FiwareCreateSubscription>(
					fiwareCreateSubscription, headers);
			System.out.println(entity);
			restTemplate.exchange("http://172.17.0.1:1026/v2/subscriptions/", HttpMethod.POST, entity, String.class);

			return ResponseEntity.status(HttpStatus.OK).body("entity successfully created");
		} catch (Exception e) {
			System.out.println("Fehler");
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

		}

	}

	/**
	 * Diese Methode updatet die Attribute einer vorhandenen Entitaet in Fiware
	 * @param fiwareOrionDto Objekt mit den zu aktualisierenden Werten
	 * @param entityId Name der Entitaet
	 * @return Gibt den HTTP Statuscode zurueck
	 */
	public ResponseEntity<?> updateDto(FiwareUpdateEntityDto fiwareOrionDto, String entityId) {
		System.out.println(fiwareOrionDto);
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("fiware-service", "Fiware");
			headers.set("fiware-servicepath", "/Test");
			HttpEntity<FiwareUpdateEntityDto> entity = new HttpEntity<FiwareUpdateEntityDto>(fiwareOrionDto, headers);
			restTemplate.exchange("http://172.17.0.1:1026/v2/entities/" + entityId + "/attrs", HttpMethod.POST, entity,
					String.class);
			return ResponseEntity.status(HttpStatus.OK).body("entity successfully updated");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	/**
	 * Diese Methode ueberprueft durch eine HTTP GET Abfrage, ob eine Entitaet schon in Fiware vorhanden ist
	 * @param entityId Name der Entitaet
	 * @return Gibt den HTTP Statuscode zurueck
	 */
	public ResponseEntity<?> getDto(String entityId) {

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("fiware-service", "Fiware");
			headers.set("fiware-servicepath", "/Test");
			HttpEntity entity = new HttpEntity(headers);
			restTemplate.exchange("http://172.17.0.1:1026/v2/entities/" + entityId + "/attrs", HttpMethod.GET, entity,
					String.class);

			return ResponseEntity.status(HttpStatus.OK).body("entity in fiware");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("entity doesnt exist");
		}
	}

}