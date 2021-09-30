package de.fraunhofer.iosb.ast.fiware.dto;

import java.time.Instant;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ZuluTimeIntervalFiware {
	

	    private double value;
	    
	    @NonNull
	    @NotNull
	    private Instant endDate;

}