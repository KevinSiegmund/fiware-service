package de.fraunhofer.iosb.ast.fiware.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fraunhofer.iosb.ast.prophet.timeseries.dto.equidistant.EquidistantTimeSeries;


@EnableKafka
@Service
public class KafkaConsumerService {
	
	 private final static Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
	    

	    
	    private ObjectMapper mapper;
	    
	    @Autowired
	    ServiceFunction serviceFunction;
	    
	    @Autowired
	    public KafkaConsumerService ( ObjectMapper mapper) {
	      
	        this.mapper = mapper;
	    }
	    
	    
	    /**
	     * KafkaListener, welcher auf das definierte Topic hört und bei neuem Eintrag in
	     * das Topic die Daten empfängt. Wurden neue Daten empfangen, werden sie auf ein Object gemappt
	     * und an eine weitere Methode übermittelt
	     * @param jsonMessage
	     * @param ack
	     */
	    @KafkaListener(topics = "#{'${kafka.connection.topicname}'}")
	    public void consume(String jsonMessage, Acknowledgment ack){
	        
	        boolean trigger = true;
	        
	       
	        EquidistantTimeSeries exportDto = null;
	        
	        try {
	       
	        	exportDto = this.mapper.readValue(jsonMessage,  EquidistantTimeSeries.class);
	      
	        } catch (JsonProcessingException e) {
	            logger.info("deserialisation failed: "+e.getMessage());
	        }
	        
	        if(exportDto != null) {
	            
	            
	            try {

	            	serviceFunction.sendFunction(exportDto);
	            } catch (Exception e) {
	                logger.info("serialisation failed: "+e.getMessage());
	            }
	            
	        
	        if(trigger) {
	            ack.acknowledge();
	            logger.info("message was successfully processed ");
	        } else {
	            logger.info("message processing failed");
	        }
	        
	    }
	    }
	

}