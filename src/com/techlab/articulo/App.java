package com.techlab.articulo;

import com.techlab.articulo.model.Articulo;
import com.techlab.articulo.model.ArticuloAlimenticio;
import com.techlab.articulo.model.ArticuloElectronico;
import com.techlab.articulo.model.Categoria;

import java.util.ArrayList;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Articulo> articulos = new ArrayList<>();
        ArrayList<Categoria> categorias = new ArrayList<>();

        precargarCategorias(categorias);

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero(scanner, "Ingrese una opción: ");

            switch (opcion) {
                case 1:
                    ingresarArticulo(scanner, articulos, categorias);
                    break;
                case 2:
                    listarArticulos(articulos);
                    break;
                case 3:
                    consultarArticulo(scanner, articulos);
                    break;
                case 4:
                    modificarArticulo(scanner, articulos, categorias);
                    break;
                case 5:
                    eliminarArticulo(scanner, articulos);
                    break;
                case 6:
                    listarCategorias(categorias);
                    break;
                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        } while (opcion != 0);

        scanner.close();
    }

    public static void mostrarMenu() {
        System.out.println();
        System.out.println("======================================================");
        System.out.println("SISTEMA DE ARTÍCULOS - CLASE 4 (HERENCIA Y TO_STRING)");
        System.out.println("======================================================");
        System.out.println("1 - Ingresar artículo");
        System.out.println("2 - Listar artículos");
        System.out.println("3 - Consultar un artículo");
        System.out.println("4 - Modificar un artículo");
        System.out.println("5 - Eliminar un artículo");
        System.out.println("6 - Listar categorías");
        System.out.println("0 - Salir");
        System.out.println("======================================================");
    }

    public static void precargarCategorias(ArrayList<Categoria> categorias) {
        categorias.add(new Categoria(1, "Electrónica", "Productos tecnológicos y electrónicos"));
        categorias.add(new Categoria(2, "Periféricos", "Accesorios para computadora"));
        categorias.add(new Categoria(3, "Alimentos", "Productos alimenticios"));
        categorias.add(new Categoria(4, "Limpieza", "Artículos de limpieza del hogar"));
    }

    // ---------------------------------------------------------------
    // Funcionalidades
    // ---------------------------------------------------------------

    public static void ingresarArticulo(Scanner scanner,
                                        ArrayList<Articulo> articulos,
                                        ArrayList<Categoria> categorias) {
        System.out.println("\n--- INGRESAR ARTÍCULO ---");
        System.out.println("1 - Artículo electrónico");
        System.out.println("2 - Artículo alimenticio");

        int tipo;
        do {
            tipo = leerEntero(scanner, "Seleccione el tipo de artículo: ");
            if (tipo != 1 && tipo != 2) {
                System.out.println("Tipo inválido. Debe ingresar 1 o 2.");
            }
        } while (tipo != 1 && tipo != 2);

        int codigo = leerEntero(scanner, "Ingrese el código del artículo: ");
        if (buscarArticuloPorCodigo(articulos, codigo) != null) {
            System.out.println("Ya existe un artículo con ese código. Se cancela el ingreso.");
            return;
        }

        String nombre = leerTextoNoVacio(scanner, "Ingrese el nombre del artículo: ");
        double precio = leerDoubleNoNegativo(scanner, "Ingrese el precio del artículo: ");

        System.out.println("\n--- CATEGORÍAS DISPONIBLES ---");
        for (Categoria c : categorias) {
            System.out.println(c.getCodigo() + " - " + c.getNombre());
        }
        Categoria categoria = pedirCategoriaExistente(scanner, categorias);

        Articulo articulo; // polimorfismo: variable del tipo padre
        if (tipo == 1) {
            int garantia = leerEnteroNoNegativo(scanner, "Ingrese la garantía en meses: ");
            articulo = new ArticuloElectronico(codigo, nombre, precio, categoria, garantia);
        } else {
            int dias = leerEnteroNoNegativo(scanner, "Ingrese los días para el vencimiento: ");
            articulo = new ArticuloAlimenticio(codigo, nombre, precio, categoria, dias);
        }

        articulos.add(articulo);
        System.out.println("\nArtículo ingresado correctamente.");
        System.out.println("Resumen del objeto creado:");
        System.out.println(articulo);
    }

    public static void listarArticulos(ArrayList<Articulo> articulos) {
        if (articulos.isEmpty()) {
            System.out.println("No hay artículos cargados.");
            return;
        }
        for (Articulo articulo : articulos) {
            System.out.println(articulo);
            System.out.println();
        }
    }

    public static void consultarArticulo(Scanner scanner, ArrayList<Articulo> articulos) {
        if (articulos.isEmpty()) {
            System.out.println("No hay artículos cargados.");
            return;
        }
        int codigo = leerEntero(scanner, "Ingrese el código del artículo a consultar: ");
        Articulo articulo = buscarArticuloPorCodigo(articulos, codigo);

        if (articulo == null) {
            System.out.println("El artículo no existe.");
            return;
        }
        System.out.println(articulo);
        System.out.println("Detalle específico: " + articulo.getDetalleEspecifico());
    }

    public static void modificarArticulo(Scanner scanner,
                                         ArrayList<Articulo> articulos,
                                         ArrayList<Categoria> categorias) {
        if (articulos.isEmpty()) {
            System.out.println("No hay artículos cargados.");
            return;
        }
        int codigo = leerEntero(scanner, "Ingrese el código del artículo a modificar: ");
        Articulo articulo = buscarArticuloPorCodigo(articulos, codigo);

        if (articulo == null) {
            System.out.println("El artículo no existe.");
            return;
        }

        String nombre = leerTextoNoVacio(scanner, "Nuevo nombre: ");
        double precio = leerDoubleNoNegativo(scanner, "Nuevo precio: ");

        System.out.println("\n--- CATEGORÍAS DISPONIBLES ---");
        for (Categoria c : categorias) {
            System.out.println(c.getCodigo() + " - " + c.getNombre());
        }
        Categoria categoria = pedirCategoriaExistente(scanner, categorias);

        articulo.setNombre(nombre);
        articulo.setPrecio(precio);
        articulo.setCategoria(categoria);

        if (articulo instanceof ArticuloElectronico) {
            ArticuloElectronico electronico = (ArticuloElectronico) articulo;
            electronico.setGarantiaMeses(
                    leerEnteroNoNegativo(scanner, "Nueva garantía en meses: "));
        } else if (articulo instanceof ArticuloAlimenticio) {
            ArticuloAlimenticio alimenticio = (ArticuloAlimenticio) articulo;
            alimenticio.setDiasParaVencimiento(
                    leerEnteroNoNegativo(scanner, "Nuevos días para el vencimiento: "));
        }

        System.out.println("Artículo modificado correctamente.");
    }

    public static void eliminarArticulo(Scanner scanner, ArrayList<Articulo> articulos) {
        if (articulos.isEmpty()) {
            System.out.println("No hay artículos cargados.");
            return;
        }
        int codigo = leerEntero(scanner, "Ingrese el código del artículo a eliminar: ");
        Articulo articulo = buscarArticuloPorCodigo(articulos, codigo);

        if (articulo == null) {
            System.out.println("El artículo no existe.");
            return;
        }
        articulos.remove(articulo);
        System.out.println("Artículo eliminado correctamente.");
    }

    public static void listarCategorias(ArrayList<Categoria> categorias) {
        for (Categoria categoria : categorias) {
            System.out.println(categoria);
            System.out.println();
        }
    }

    // ---------------------------------------------------------------
    // Métodos auxiliares de búsqueda
    // ---------------------------------------------------------------

    public static Articulo buscarArticuloPorCodigo(ArrayList<Articulo> articulos, int codigo) {
        for (Articulo articulo : articulos) {
            if (articulo.getCodigo() == codigo) {
                return articulo;
            }
        }
        return null;
    }

    public static Categoria buscarCategoriaPorCodigo(ArrayList<Categoria> categorias, int codigo) {
        for (Categoria categoria : categorias) {
            if (categoria.getCodigo() == codigo) {
                return categoria;
            }
        }
        return null;
    }

    public static Categoria pedirCategoriaExistente(Scanner scanner, ArrayList<Categoria> categorias) {
        Categoria categoria;
        do {
            int codigo = leerEntero(scanner, "Ingrese el código de la categoría: ");
            categoria = buscarCategoriaPorCodigo(categorias, codigo);
            if (categoria == null) {
                System.out.println("La categoría no existe. Intente nuevamente.");
            }
        } while (categoria == null);
        return categoria;
    }

    // ---------------------------------------------------------------
    // Métodos auxiliares de lectura y validación
    // ---------------------------------------------------------------

    public static int leerEntero(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Dato inválido. Debe ingresar un número entero.");
            }
        }
    }

    public static int leerEnteroNoNegativo(Scanner scanner, String mensaje) {
        int valor;
        do {
            valor = leerEntero(scanner, mensaje);
            if (valor < 0) {
                System.out.println("El valor no puede ser negativo.");
            }
        } while (valor < 0);
        return valor;
    }

    public static double leerDoubleNoNegativo(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                double valor = Double.parseDouble(scanner.nextLine().trim());
                if (Double.isNaN(valor) || Double.isInfinite(valor)) {
                    System.out.println("El valor debe ser un número finito.");
                } else if (valor < 0) {
                    System.out.println("El valor no puede ser negativo.");
                } else {
                    return valor;
                }
            } catch (NumberFormatException e) {
                System.out.println("Dato inválido. Debe ingresar un número decimal.");
            }
        }
    }

    public static String leerTextoNoVacio(Scanner scanner, String mensaje) {
        String texto;
        do {
            System.out.print(mensaje);
            texto = scanner.nextLine().trim();
            if (texto.isEmpty()) {
                System.out.println("El texto no puede estar vacío.");
            }
        } while (texto.isEmpty());
        return texto;
    }
}
