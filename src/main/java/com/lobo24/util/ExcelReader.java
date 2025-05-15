package com.lobo24.util;

import com.lobo24.models.Producto;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.math.BigDecimal;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelReader {

    public static void leerYGuardarProductos(String rutaArchivo, Connection connection)
            throws IOException, SQLException {

        FileInputStream fis = new FileInputStream(new File(rutaArchivo));
        Workbook workbook = null;

        try {
            if (rutaArchivo.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else if (rutaArchivo.endsWith(".xls")) {
                workbook = new HSSFWorkbook(fis);
            } else {
                throw new IllegalArgumentException("Formato de archivo no soportado.");
            }

            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new IllegalArgumentException("El archivo no contiene cabecera.");
            }

            // Mapear nombres de columna a índices
            int colCodigo = -1, colNombre = -1, colDescripcion = -1,
                    colPrecioCompra = -1, colPrecioVenta = -1, colPorcentaje = -1,
                    colStock = -1, colStockMinimo = -1, colCategoria = -1;

            for (Cell cell : headerRow) {
                String colName = getCellStringValue(cell).toLowerCase();
                switch (colName) {
                    case "código":
                    case "codigo":
                    case "código de barras":
                    case "codigo de barras":
                        colCodigo = cell.getColumnIndex();
                        break;
                    case "nombre":
                        colNombre = cell.getColumnIndex();
                        break;
                    case "descripción":
                    case "descripcion":
                        colDescripcion = cell.getColumnIndex();
                        break;
                    case "precio compra":
                    case "precio de compra":
                        colPrecioCompra = cell.getColumnIndex();
                        break;
                    case "precio venta":
                    case "precio de venta":
                        colPrecioVenta = cell.getColumnIndex();
                        break;
                    case "porcentaje incremento":
                    case "incremento":
                        colPorcentaje = cell.getColumnIndex();
                        break;
                    case "stock":
                        colStock = cell.getColumnIndex();
                        break;
                    case "stock mínimo":
                    case "stock minimo":
                        colStockMinimo = cell.getColumnIndex();
                        break;
                    case "categoría":
                    case "categoria":
                        colCategoria = cell.getColumnIndex();
                        break;
                }
            }

            if (colCodigo == -1 || (colNombre == -1 && colDescripcion == -1)) {
                throw new IllegalArgumentException("Faltan columnas esenciales: código y nombre/descripción.");
            }

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Saltar cabecera

                String codigo = getCellStringValue(row.getCell(colCodigo));
                String nombre = getCellStringValue(row.getCell(colNombre != -1 ? colNombre : colDescripcion));
                String descripcion = getCellStringValue(row.getCell(colDescripcion));
                String categoria = colCategoria != -1 ? getCellStringValue(row.getCell(colCategoria)) : "";

                BigDecimal precioCompra = colPrecioCompra != -1 ?
                        BigDecimal.valueOf(getCellNumericValue(row.getCell(colPrecioCompra))) : BigDecimal.ZERO;

                BigDecimal precioVenta = colPrecioVenta != -1 ?
                        BigDecimal.valueOf(getCellNumericValue(row.getCell(colPrecioVenta))) : BigDecimal.ZERO;

                BigDecimal porcentajeIncremento = colPorcentaje != -1 ?
                        BigDecimal.valueOf(getCellNumericValue(row.getCell(colPorcentaje))) : BigDecimal.ZERO;

                int stock = colStock != -1 ? (int) getCellNumericValue(row.getCell(colStock)) : 0;
                int stockMinimo = colStockMinimo != -1 ? (int) getCellNumericValue(row.getCell(colStockMinimo)) : 0;

                if (codigo == null || codigo.isEmpty() || (nombre == null || nombre.isEmpty()) && (descripcion == null || descripcion.isEmpty())) {
                    System.err.println("Fila " + (row.getRowNum() + 1) + " omitida: código o nombre/descripción faltante");
                    continue;
                }

                Producto producto = new Producto();
                producto.setCodigoBarras(codigo);
                producto.setNombre(nombre != null ? nombre : descripcion);
                producto.setDescripcion(descripcion != null ? descripcion : nombre);
                producto.setCategoria(categoria);
                producto.setPrecioCoste(precioCompra);
                producto.setPrecioVenta(precioVenta);
                producto.setIncremento(porcentajeIncremento);
                producto.setStock(stock);
                producto.setStockMinimo(stockMinimo);
                producto.setAutomatico(false); // Valor por defecto

                guardarProductoEnBaseDeDatos(producto, connection);
            }

        } catch (Exception e) {
            System.err.println("Error al procesar el archivo: " + e.getMessage());
            throw e;
        } finally {
            if (workbook != null) workbook.close();
            fis.close();
        }
    }

    private static String getCellStringValue(Cell cell) {
        if (cell == null) return null;
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }

    private static double getCellNumericValue(Cell cell) {
        if (cell == null) return 0.0;
        switch (cell.getCellType()) {
            case NUMERIC:
                return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            default:
                return 0.0;
        }
    }

    public static void guardarProductoEnBaseDeDatos(Producto producto, Connection connection) throws SQLException {
        String sql = "INSERT INTO productos (codigo_barras, nombre, descripcion, categoria, " +
                "precio_compra, precio_venta, porcentaje_incremento, stock, stock_minimo, automatico) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, producto.getCodigoBarras());
            preparedStatement.setString(2, producto.getNombre());
            preparedStatement.setString(3, producto.getDescripcion());
            preparedStatement.setString(4, producto.getCategoria());
            preparedStatement.setBigDecimal(5, producto.getPrecioCoste());
            preparedStatement.setBigDecimal(6, producto.getPrecioVenta());
            preparedStatement.setBigDecimal(7, producto.getIncremento());
            preparedStatement.setInt(8, producto.getStock());
            preparedStatement.setInt(9, producto.getStockMinimo());
            preparedStatement.setBoolean(10, producto.isAutomatico());

            preparedStatement.executeUpdate();
        }
    }
}