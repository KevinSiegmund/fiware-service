package de.fraunhofer.iosb.ast.fiware.dto;


import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import de.fraunhofer.iosb.ast.prophet.timeseries.dto.TimeSeriesType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Values {
	  
    @NonNull
    @NotNull
    private  String unit;
    
    @NotNull
    @NonNull
    private  String timeSeriesName;
    
    @NotNull
    @NonNull
    private  TimeSeriesType type;
    
    @NotNull
    @NotEmpty
    @Singular
    private List<ZuluTimeIntervalFiware> intervals;

}