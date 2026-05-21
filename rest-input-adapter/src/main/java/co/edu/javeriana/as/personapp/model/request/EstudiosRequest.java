package co.edu.javeriana.as.personapp.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudiosRequest {
	private Integer idProf;
	private Integer ccPer;
	private String graduationDate;
	private String universityName;
}
