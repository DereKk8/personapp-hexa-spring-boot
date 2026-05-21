package co.edu.javeriana.as.personapp.model.response;

import co.edu.javeriana.as.personapp.model.request.EstudiosRequest;

public class EstudiosResponse extends EstudiosRequest {

	private String status;

	public EstudiosResponse(Integer idProf, Integer ccPer, String graduationDate, String universityName, String status) {
		super(idProf, ccPer, graduationDate, universityName);
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
