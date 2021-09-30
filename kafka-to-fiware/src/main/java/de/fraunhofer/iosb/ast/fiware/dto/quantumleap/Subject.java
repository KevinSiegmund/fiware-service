package de.fraunhofer.iosb.ast.fiware.dto.quantumleap;

import java.util.List;

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
public class Subject {

	@NonNull
    @NotNull
	private List<Entities> entities;
	
	@NonNull
    @NotNull
	private Condition condition;
}