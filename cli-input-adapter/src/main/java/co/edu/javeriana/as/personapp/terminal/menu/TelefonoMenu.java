package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.terminal.adapter.TelefonoInputAdapterCli;
import co.edu.javeriana.as.personapp.terminal.model.TelefonoModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TelefonoMenu {

	private static final int OPCION_REGRESAR = 0;
	private static final int OPCION_VER_TODO = 1;
	private static final int OPCION_CREAR = 2;
	private static final int OPCION_BUSCAR = 3;
	private static final int OPCION_ACTUALIZAR = 4;
	private static final int OPCION_ELIMINAR = 5;

	public void iniciarMenu(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
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
					telefonoInputAdapterCli.historial();
					break;
				case OPCION_CREAR:
					crearTelefono(telefonoInputAdapterCli, keyboard);
					break;
				case OPCION_BUSCAR:
					buscarTelefono(telefonoInputAdapterCli, keyboard);
					break;
				case OPCION_ACTUALIZAR:
					actualizarTelefono(telefonoInputAdapterCli, keyboard);
					break;
				case OPCION_ELIMINAR:
					eliminarTelefono(telefonoInputAdapterCli, keyboard);
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
		System.out.println("----------------------");
		System.out.println(OPCION_VER_TODO + " para ver todos los telefonos");
		System.out.println(OPCION_CREAR + " para crear un telefono");
		System.out.println(OPCION_BUSCAR + " para buscar un telefono");
		System.out.println(OPCION_ACTUALIZAR + " para actualizar un telefono");
		System.out.println(OPCION_ELIMINAR + " para eliminar un telefono");
		System.out.println(OPCION_REGRESAR + " para regresar");
	}

	private void crearTelefono(TelefonoInputAdapterCli adapter, Scanner keyboard) {
		System.out.print("Ingrese numero: ");
		String num = keyboard.nextLine();
		System.out.print("Ingrese operador: ");
		String oper = keyboard.nextLine();
		Integer duenio = leerEntero(keyboard, "Ingrese cc del duenio: ");
		adapter.crearTelefono(new TelefonoModelCli(num, oper, duenio));
	}

	private void buscarTelefono(TelefonoInputAdapterCli adapter, Scanner keyboard) {
		System.out.print("Ingrese numero: ");
		String num = keyboard.nextLine();
		adapter.obtenerTelefono(num);
	}

	private void actualizarTelefono(TelefonoInputAdapterCli adapter, Scanner keyboard) {
		System.out.print("Ingrese numero del telefono a actualizar: ");
		String num = keyboard.nextLine();
		System.out.print("Ingrese operador: ");
		String oper = keyboard.nextLine();
		Integer duenio = leerEntero(keyboard, "Ingrese cc del duenio: ");
		adapter.actualizarTelefono(num, new TelefonoModelCli(num, oper, duenio));
	}

	private void eliminarTelefono(TelefonoInputAdapterCli adapter, Scanner keyboard) {
		System.out.print("Ingrese numero: ");
		String num = keyboard.nextLine();
		adapter.eliminarTelefono(num);
	}

	private int leerOpcion(Scanner keyboard) {
		try {
			System.out.print("Ingrese una opcion: ");
			return keyboard.nextInt();
		} catch (InputMismatchException e) {
			log.warn("Solo se permiten numeros.");
			keyboard.next();
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
}
