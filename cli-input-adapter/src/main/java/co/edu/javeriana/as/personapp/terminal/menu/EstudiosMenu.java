package co.edu.javeriana.as.personapp.terminal.menu;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.terminal.adapter.EstudiosInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.EstudiosModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EstudiosMenu {

	private static final int OPCION_REGRESAR = 0;
	private static final int OPCION_VER_TODO = 1;
	private static final int OPCION_CREAR = 2;
	private static final int OPCION_BUSCAR = 3;
	private static final int OPCION_ACTUALIZAR = 4;
	private static final int OPCION_ELIMINAR = 5;
	private static final int OPCION_CONTAR = 6;

	public void iniciarMenu(EstudiosInputAdapterCli estudiosInputAdapterCli, Scanner keyboard) {
		boolean isValid = false;
		do {
			try {
				mostrarMenuOpciones();
				int opcion = leerOpcion(keyboard);
				switch (opcion) {
				case OPCION_REGRESAR:
					isValid = true;
					break;
				case OPCION_VER_TODO:
					estudiosInputAdapterCli.historial();
					break;
				case OPCION_CREAR:
					crearEstudio(estudiosInputAdapterCli, keyboard);
					break;
				case OPCION_BUSCAR:
					buscarEstudio(estudiosInputAdapterCli, keyboard);
					break;
				case OPCION_ACTUALIZAR:
					actualizarEstudio(estudiosInputAdapterCli, keyboard);
					break;
				case OPCION_ELIMINAR:
					eliminarEstudio(estudiosInputAdapterCli, keyboard);
					break;
				case OPCION_CONTAR:
					estudiosInputAdapterCli.contar();
					break;
				default:
					log.warn("La opcion elegida no es valida.");
				}
			} catch (InputMismatchException e) {
				log.warn("Solo se permiten numeros.");
				keyboard.next();
			}
		} while (!isValid);
	}

	private void mostrarMenuOpciones() {
		System.out.println("\n----- Modulo Estudios (MariaDB) -----");
		System.out.println(OPCION_VER_TODO + " para ver todos los estudios");
		System.out.println(OPCION_CREAR + " para crear un estudio");
		System.out.println(OPCION_BUSCAR + " para buscar un estudio");
		System.out.println(OPCION_ACTUALIZAR + " para actualizar un estudio");
		System.out.println(OPCION_ELIMINAR + " para eliminar un estudio");
		System.out.println(OPCION_CONTAR + " para contar los estudios");
		System.out.println(OPCION_REGRESAR + " para regresar");
	}

	private void crearEstudio(EstudiosInputAdapterCli adapter, Scanner keyboard) {
		Integer idProf = leerEntero(keyboard, "Ingrese id de la profesion: ");
		Integer ccPer = leerEntero(keyboard, "Ingrese cc de la persona: ");
		String fecha = leerFecha(keyboard);
		System.out.print("Ingrese universidad: ");
		String univer = keyboard.nextLine();
		adapter.crearEstudio(new EstudiosModelCli(idProf, ccPer, fecha, univer));
	}

	private void buscarEstudio(EstudiosInputAdapterCli adapter, Scanner keyboard) {
		Integer idProf = leerEntero(keyboard, "Ingrese id de la profesion: ");
		Integer ccPer = leerEntero(keyboard, "Ingrese cc de la persona: ");
		adapter.obtenerEstudio(idProf, ccPer);
	}

	private void actualizarEstudio(EstudiosInputAdapterCli adapter, Scanner keyboard) {
		Integer idProf = leerEntero(keyboard, "Ingrese id de la profesion: ");
		Integer ccPer = leerEntero(keyboard, "Ingrese cc de la persona: ");
		String fecha = leerFecha(keyboard);
		System.out.print("Ingrese universidad: ");
		String univer = keyboard.nextLine();
		adapter.actualizarEstudio(idProf, ccPer, new EstudiosModelCli(idProf, ccPer, fecha, univer));
	}

	private void eliminarEstudio(EstudiosInputAdapterCli adapter, Scanner keyboard) {
		Integer idProf = leerEntero(keyboard, "Ingrese id de la profesion: ");
		Integer ccPer = leerEntero(keyboard, "Ingrese cc de la persona: ");
		adapter.eliminarEstudio(idProf, ccPer);
	}

	private int leerOpcion(Scanner keyboard) {
		System.out.print("Ingrese una opcion: ");
		try {
			return Integer.parseInt(keyboard.nextLine().trim());
		} catch (NumberFormatException e) {
			log.warn("Solo se permiten numeros.");
			return leerOpcion(keyboard);
		}
	}

	private static Integer leerEntero(Scanner keyboard, String prompt) {
		while (true) {
			System.out.print(prompt);
			try {
				return Integer.valueOf(keyboard.nextLine().trim());
			} catch (NumberFormatException e) {
				log.warn("Solo se permiten números.");
			}
		}
	}

	private static String leerFecha(Scanner keyboard) {
		while (true) {
			System.out.print("Ingrese fecha (YYYY-MM-DD): ");
			String fecha = keyboard.nextLine().trim();
			if (fecha.isEmpty()) {
				return null;
			}
			try {
				java.time.LocalDate.parse(fecha);
				return fecha;
			} catch (java.time.format.DateTimeParseException e) {
				log.warn("Formato de fecha inválido. Use YYYY-MM-DD (ej: 2024-01-15).");
			}
		}
	}
}
