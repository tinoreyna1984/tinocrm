package com.tinexlab.tinocrm.business.service;

import com.tinexlab.tinocrm.business.entity.Cliente;
import com.tinexlab.tinocrm.business.entity.Producto;
import com.tinexlab.tinocrm.business.entity.Venta;
import com.tinexlab.tinocrm.business.repository.ClienteRepository;
import com.tinexlab.tinocrm.business.repository.ProductoRepository;
import com.tinexlab.tinocrm.business.repository.VentaRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExportXLSService {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private ProductoRepository productoRepository;

    public ByteArrayInputStream exportData(String entity) {
        Workbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            if ("clientes".equals(entity)) {
                exportClientes(workbook);
            } else if ("ventas".equals(entity)) {
                exportVentas(workbook);
            } else if ("productos".equals(entity)) {
                exportProductos(workbook);
            }
            workbook.write(out);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    private void exportClientes(Workbook workbook) {
        Sheet sheet = workbook.createSheet("Clientes");

        // Crea la fila de encabezado
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Nombre", "Apellidos", "Documento", "Tipo Documento", "Estado", "Teléfono", "Email"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Obtiene la lista de clientes
        List<Cliente> clientes = clienteRepository.findAll();

        // Llena las filas con los datos de los clientes
        int rowNum = 1;
        for (Cliente cliente : clientes) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(cliente.getId());
            row.createCell(1).setCellValue(cliente.getNombreCliente());
            row.createCell(2).setCellValue(cliente.getApellidosCliente());
            row.createCell(3).setCellValue(cliente.getDocId());
            row.createCell(4).setCellValue(cliente.getTipoDoc().toString());
            row.createCell(5).setCellValue(cliente.getEstadoCliente().toString());
            row.createCell(6).setCellValue(cliente.getFonoCliente());
            row.createCell(7).setCellValue(cliente.getEmailCliente());
        }
    }

    private void exportVentas(Workbook workbook) {
        Sheet sheet = workbook.createSheet("Ventas");

        // Crea la fila de encabezado
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Orden Venta", "Descripción Venta", "Estado Venta", "Fecha Reserva", "Cliente", "Factura", "Usuario"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Obtiene la lista de ventas
        List<Venta> ventas = ventaRepository.findAll();

        // Llena las filas con los datos de las ventas
        int rowNum = 1;
        for (Venta venta : ventas) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(venta.getId());
            row.createCell(1).setCellValue(venta.getOrdenVenta());
            row.createCell(2).setCellValue(venta.getDescVenta());
            row.createCell(3).setCellValue(venta.getEstadoVenta().toString());
            row.createCell(4).setCellValue(venta.getFechaReserva().toString());
            row.createCell(5).setCellValue(venta.getCliente().toString());
            row.createCell(6).setCellValue(venta.getFactura().toString());
            row.createCell(7).setCellValue(venta.getUser().getUsername());
        }
    }

    private void exportProductos(Workbook workbook){
        Sheet sheet = workbook.createSheet("Productos");

        // Crea la fila de encabezado
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Nombre Producto", "Descripción Producto", "Precio Producto"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Obtiene la lista de ventas
        List<Producto> productos = productoRepository.findAll();

        // Llena las filas con los datos de las ventas
        int rowNum = 1;
        for (Producto producto : productos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(producto.getId());
            row.createCell(1).setCellValue(producto.getNombreProducto());
            row.createCell(2).setCellValue(producto.getDescProducto());
            row.createCell(3).setCellValue(producto.getPrecioProducto());
        }
    }
}
