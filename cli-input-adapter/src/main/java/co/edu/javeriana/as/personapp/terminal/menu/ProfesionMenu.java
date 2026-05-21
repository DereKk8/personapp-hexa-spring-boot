package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.terminal.adapter.ProfesionInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.ProfesionModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProfesionMenu {

	private static final int OPCION_REGRESAR = 0;
	private static final int OPCION_VER_TODO = 1;
	private static final int OPCION_CREAR = 2;
	private static final int OPCION_BUSCAR = 3;
	private static final int OPCION_ACTUALIZAR = 4;
	private static final int OPCION_ELIMINAR = 5;

	public void iniciarMenu(ProfesionInputAdapterCli profesionInputAdapterCli, Scanner keyboard) {
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
					profesionInputAdapterCli.historial();
					break;
				case OPCION_CREAR:
					crearProfesion(profesionInputAdapterCli, keyboard);
					break;
				case OPCION_BUSCAR:
					buscarProfesion(profesionInputAdapterCli, keyboard);
					break;
				case OPCION_ACTUALIZAR:
					actualizarProfesion(profesionInputAdapterCli, keyboard);
					break;
				case OPCION_ELIMINAR:
					eliminarProfesion(profesionInputAdapterCli, keyboard);
					break;
				default:
					log.warn("La opcion elegida no es valida.");
				}
			} catch (InputMismatchException e) {
				log.warn("Solo se permiten numeros.");
			}
		} while (!isValid);
	}

	private void mostrarMenuOpciones() {
		System.out.println("----------------------");
		System.out.println(OPCION_VER_TODO + " para ver todas las profesiones");
		System.out.println(OPCION_CREAR + " para crear una profesion");
		System.out.println(OPCION_BUSCAR + " para buscar una profesion");
		System.out.println(OPCION_ACTUALIZAR + " para actualizar una profesion");
		System.out.println(OPCION_ELIMINAR + " para eliminar una profesion");
		System.out.println(OPCION_REGRESAR + " para regresar");
	}

	private void crearProfesion(ProfesionInputAdapterCli adapter, Scanner keyboard) {
		keyboard.nextLine();
		System.out.print("Ingrese nombre: ");
		String nombre = keyboard.nextLine();
		System.out.print("Ingrese descripcion: ");
		String desc = keyboard.nextLine();
		adapter.crearProfesion(new ProfesionModelCli(null, nombre, desc));
	}

	private void buscarProfesion(ProfesionInputAdapterCli adapter, Scanner keyboard) {
		System.out.print("Ingrese id: ");
		Integer id = keyboard.nextInt();
		adapter.obtenerProfesion(id);
	}

	private void actualizarProfesion(ProfesionInputAdapterCli adapter, Scanner keyboard) {
		System.out.print("Ingrese id de la profesion a actualizar: ");
		Integer id = keyboard.nextInt();
		keyboard.nextLine();
		System.out.print("Ingrese nombre: ");
		String nombre = keyboard.nextLine();
		System.out.print("Ingrese descripcion: ");
		String desc = keyboard.nextLine();
		adapter.actualizarProfesion(id, new ProfesionModelCli(id, nombre, desc));
	}

	private void eliminarProfesion(ProfesionInputAdapterCli adapter, Scanner keyboard) {
		System.out.print("Ingrese id: ");
		Integer id = keyboard.nextInt();
		adapter.eliminarProfesion(id);
	}

	private int leerOpcion(Scanner keyboard) {
		try {
			System.out.print("Ingrese una opcion: ");
			return keyboard.nextInt();
		} catch (InputMismatchException e) {
			log.warn("Solo se permiten numeros.");
			return leerOpcion(keyboard);
		}
	}
}
