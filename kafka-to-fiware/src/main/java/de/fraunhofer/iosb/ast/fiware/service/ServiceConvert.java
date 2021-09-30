package de.fraunhofer.iosb.ast.fiware.service;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import de.fraunhofer.iosb.ast.fiware.dto.FiwareCreateEntityDto;
import de.fraunhofer.iosb.ast.fiware.dto.FiwareOrionExportDto;
import de.fraunhofer.iosb.ast.fiware.dto.FiwareUpdateEntityDto;
import de.fraunhofer.iosb.ast.fiware.dto.Values;
import de.fraunhofer.iosb.ast.fiware.dto.ZuluTimeIntervalFiware;
import de.fraunhofer.iosb.ast.fiware.dto.quantumleap.Condition;
import de.fraunhofer.iosb.ast.fiware.dto.quantumleap.Entities;
import de.fraunhofer.iosb.ast.fiware.dto.quantumleap.FiwareCreateSubscription;
import de.fraunhofer.iosb.ast.fiware.dto.quantumleap.Http;
import de.fraunhofer.iosb.ast.fiware.dto.quantumleap.Notification;
import de.fraunhofer.iosb.ast.fiware.dto.quantumleap.Subject;
import de.fraunhofer.iosb.ast.prophet.timeseries.dto.equidistant.EquidistantTimeSeries;
import de.fraunhofer.iosb.ast.prophet.timeseries.dto.TimeSeriesType;

@Service
public class ServiceConvert {

	/**
	 * Diese Methode baut aus der Zeitreihe welche aus dem Kafka empfangen wurde mittels
	 * eines Builders das Dto zusammen, welches nach Fiware geschickt wird um eine neue Entitaet anzulegen.
	 * Dabei wird auch ermittelt, ob die empfangene Zeitreihe eine Prognose, Messung oder ein Fahrplan ist,
	 * um den Typ der Entitaet zu bestimmen 
	 * @param timeSeries empfangene Zeitreihe aus Kafka
	 * @return FiwareCreateEntityDto welches nach Fiware geschickt wird um eine neue Entitaet anzulegen
	 */
	public FiwareCreateEntityDto convert(EquidistantTimeSeries timeSeries) {

		Values value = Values.builder().unit(timeSeries.getUnit())
				.timeSeriesName(timeSeries.getTimeSeriesName())
				.intervals(convertTime(timeSeries)).type(timeSeries.getType()).build();

		if (timeSeries.getType() == TimeSeriesType.MESSUNG) {
			FiwareOrionExportDto fiwareOrionExportDto = FiwareOrionExportDto.builder().type("TimeSeries").value(value)
					.build();

			FiwareCreateEntityDto fiwareCreateEntityDto = FiwareCreateEntityDto.builder()
					.id(timeSeries.getTimeSeriesName()).type("Messung")
					.fiwareOrionExportDto(fiwareOrionExportDto).build();

			return fiwareCreateEntityDto;

		} else if (timeSeries.getType() == TimeSeriesType.PROGNOSE) {
			FiwareOrionExportDto fiwareOrionExportDto = FiwareOrionExportDto.builder().type("TimeSeries").value(value)
					.build();

			FiwareCreateEntityDto fiwareCreateEntityDto = FiwareCreateEntityDto.builder()
					.id(timeSeries.getTimeSeriesName()).type("Prognose")
					.fiwareOrionExportDto(fiwareOrionExportDto).build();

			return fiwareCreateEntityDto;

		} else {
			FiwareOrionExportDto fiwareOrionExportDto = FiwareOrionExportDto.builder().type("TimeSeries").value(value)
					.build();

			FiwareCreateEntityDto fiwareCreateEntityDto = FiwareCreateEntityDto.builder()
					.id(timeSeries.getTimeSeriesName()).type("Fahrplan")
					.fiwareOrionExportDto(fiwareOrionExportDto).build();

			return fiwareCreateEntityDto;

		}

	}

	/**
	 * Diese Methode baut aus der Zeitreihe welche aus dem Kafka empfangen wurde mittels
	 * eines Builders das Dto zusammen, welches nach Fiware geschickt wird um eine Entitaet zu updaten 
	 * @param timeSeries empfangene Zeitreihe aus Kafka
	 * @return FiwareUpdateEntityDto welches nach Fiware geschickt wird um eine Entitaet zu updaten
	 */
	public FiwareUpdateEntityDto updateConvert(EquidistantTimeSeries timeSeries) {

		Values value = Values.builder().unit(timeSeries.getUnit())
				.timeSeriesName(timeSeries.getTimeSeriesName())
				.intervals(convertTime(timeSeries)).type(timeSeries.getType()).build();

		FiwareOrionExportDto fiwareOrionExportDto = FiwareOrionExportDto.builder().type("TimeSeries").value(value)
				.build();

		FiwareUpdateEntityDto fiwareUpdateEntityDto = FiwareUpdateEntityDto.builder()
				.fiwareOrionExportDto(fiwareOrionExportDto).build();

		return fiwareUpdateEntityDto;

	}

	/**
	 * Diese Methode baut aus der Zeitreihe welche aus dem Kafka empfangen wurde mittels
	 * eines Builders das Dto zusammen, welches nach Fiware geschickt wird um eine Subscription anzulegen
	 * @param timeSeries empfangene Zeitreihe aus Kafka
	 * @return FiwareCreateSubscription welches nach Fiware geschickt wird um eine Subscription anzulegen
	 */
	public FiwareCreateSubscription createSubscription(EquidistantTimeSeries timeSeries) {

		Condition condition = Condition.builder().attrs(attrs()).build();
		Subject subject = Subject.builder().entities(createEntities(timeSeries)).condition(condition)
				.build();
		Http http = Http.builder().url("http://quantumleap:8668/v2/notify").build();

		Notification notification = Notification.builder().http(http).attrs(attrs()).metadata(metadata()).build();
		FiwareCreateSubscription fiwareCreateSubscription = FiwareCreateSubscription.builder()
				.description("Benachrichtigt Quantumleap über neue Daten").subject(subject).notification(notification)
				.build();

		return fiwareCreateSubscription;
	}

	/**
	 * Methode, welche eine Arrayiste aus einem String erstellt.
	 * @return gibt die Arrayliste zurück.
	 */
	public List<String> attrs() {

		ArrayList<String> attrs = new ArrayList<String>();
		String attribute = "fiwareOrionExportDto";
		attrs.add(attribute);

		return attrs;

	}

	/**
	 * Methode, welche eine Arrayliste aus einem String erstellt
	 * @return gibt die Arrayliste zurück
	 */
	public List<String> metadata() {

		ArrayList<String> metadata = new ArrayList<String>();
		String dateCreated = "dateCreated";
		String dateModified = "dateModified";
		metadata.add(dateModified);
		metadata.add(dateCreated);

		return metadata;

	}

	/**
	 * Methode, welche einem String den Namen der empfangenen Zeitreihe zuweist
	 * @param timeSeries
	 * @return gibt den String zurück
	 */
	public String getEntityID(EquidistantTimeSeries timeSeries) {

		String entityId = timeSeries.getTimeSeriesName();

		return entityId;

	}

	/**
	 * Diese Methode erstellt eine Liste und fuegt den Namen der empfangenen Zeitreihe ein.
	 * @param timeSeries empfangene Zeitreihe aus dem Kafka
	 * @return gibt eine Liste mit den Entitaeten zurück
	 */
	public List<Entities> createEntities(EquidistantTimeSeries timeSeries) {

		List<Entities> entities = new ArrayList<Entities>();
		for (int i = 0; i < 1; i++) {

			Entities entitie = Entities.builder().idPattern(timeSeries.getTimeSeriesName()).build();

			entities.add(entitie);
		}
		return entities;
	}



	/**
	 * Diese Methode erstellt eine Liste aus den Intervallen der empfangenen Zeitreihe
	 * mit den Werten und des Zeitstempels 
	 * @param timeSeries empfangene Zeitreihe aus dem Kafka
	 * @return gibt die Liste der Zeitintervalle zurück
	 */
	public List<ZuluTimeIntervalFiware> convertTime(EquidistantTimeSeries timeSeries) {

		List<ZuluTimeIntervalFiware> interval = new ArrayList<ZuluTimeIntervalFiware>();

		for (int i = 0; i < timeSeries.getIntervals().size(); i++) {

			ZuluTimeIntervalFiware intervale = ZuluTimeIntervalFiware.builder()
					.endDate(timeSeries.getIntervals().get(i).getEndDate())
					.value(timeSeries.getIntervals().get(i).getValue()).build();

			interval.add(intervale);

		}
		return interval;

	}

}