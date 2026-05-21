package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.as.personapp.adapter.ProfesionInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/profesion")
public class ProfesionControllerV1 {

	@Autowired
	private ProfesionInputAdapterRest profesionInputAdapterRest;

	@ResponseBody
	@GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProfesionResponse> profesiones() {
		log.info("Into profesiones REST API");
		return profesionInputAdapterRest.historial();
	}

	@ResponseBody
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ProfesionResponse crearProfesion(@RequestBody ProfesionRequest request) {
		log.info("Into crearProfesion REST API");
		return profesionInputAdapterRest.crearProfesion(request);
	}

	@ResponseBody
	@GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ProfesionResponse obtenerProfesion(@PathVariable Integer id) {
		log.info("Into obtenerProfesion REST API");
		return profesionInputAdapterRest.obtenerProfesion(id);
	}

	@ResponseBody
	@PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ProfesionResponse actualizarProfesion(@PathVariable Integer id, @RequestBody ProfesionRequest request) {
		log.info("Into actualizarProfesion REST API");
		return profesionInputAdapterRest.actualizarProfesion(id, request);
	}

	@ResponseBody
	@DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ProfesionResponse eliminarProfesion(@PathVariable Integer id) {
		log.info("Into eliminarProfesion REST API");
		return profesionInputAdapterRest.eliminarProfesion(id);
	}
}
