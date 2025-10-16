package com.sistema.puntoventas.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.sistema.puntoventas.entity.Venta;
import com.sistema.puntoventas.repository.VentaRepository;
import com.sistema.puntoventas.util.NumeroALetras;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FacturaService {

    private final VentaRepository ventaRepository;
    private final ConfiguracionFacturaService configuracionFacturaService;

    public byte[] generarFacturaPDF(String ticket) {
        try {
            // Obtener número de factura
            String numeroFactura = configuracionFacturaService.obtenerSiguienteNumeroFactura();
            
            // Obtener ventas por ticket
            List<Venta> ventas = ventaRepository.findByTicket(ticket);
            
            if (ventas.isEmpty()) {
                throw new RuntimeException("No se encontraron ventas para el ticket: " + ticket);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            
            Venta primeraVenta = ventas.get(0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            // ===== SECCIÓN 1: DATOS DEL NEGOCIO =====
            Table headerTable = new Table(UnitValue.createPercentArray(new float[]{2, 1}));
            headerTable.setWidth(UnitValue.createPercentValue(100));
            
            // Datos del negocio (izquierda)
            Cell businessCell = new Cell()
                    .setBorder(new SolidBorder(1))
                    .setPadding(10);
            businessCell.add(new Paragraph("SISTEMA DE PUNTO DE VENTAS")
                    .setBold()
                    .setFontSize(14));
            businessCell.add(new Paragraph("Nombre Comercial: Tu Negocio S.A. de C.V.")
                    .setFontSize(9));
            businessCell.add(new Paragraph("Dirección: Calle Principal #123, Ciudad")
                    .setFontSize(9));
            businessCell.add(new Paragraph("Teléfono: (503) 2222-3333")
                    .setFontSize(9));
            businessCell.add(new Paragraph("Email: ventas@tunegocio.com")
                    .setFontSize(9));
            
            // ===== SECCIÓN 2: NÚMERO DE FACTURA Y DATOS FISCALES =====
            Cell invoiceCell = new Cell()
                    .setBorder(new SolidBorder(1))
                    .setPadding(10)
                    .setTextAlignment(TextAlignment.CENTER);
            invoiceCell.add(new Paragraph("FACTURA")
                    .setBold()
                    .setFontSize(16)
                    .setBackgroundColor(new DeviceRgb(0, 0, 0))
                    .setFontColor(ColorConstants.WHITE)
                    .setPadding(5));
            invoiceCell.add(new Paragraph("No. " + numeroFactura)
                    .setBold()
                    .setFontSize(14)
                    .setMarginTop(5));
            invoiceCell.add(new Paragraph("NIT: 0614-123456-001-2")
                    .setFontSize(9)
                    .setMarginTop(5));
            invoiceCell.add(new Paragraph("N.R.C: 123456-7")
                    .setFontSize(9));
            invoiceCell.add(new Paragraph("Fecha: " + primeraVenta.getFecha().format(formatter))
                    .setFontSize(9)
                    .setMarginTop(5));
            
            headerTable.addCell(businessCell);
            headerTable.addCell(invoiceCell);
            document.add(headerTable);
            
            document.add(new Paragraph("\n").setFontSize(5));

            // ===== SECCIÓN 3: DATOS DEL CLIENTE =====
            Table clientTable = new Table(UnitValue.createPercentArray(new float[]{1, 2, 1}));
            clientTable.setWidth(UnitValue.createPercentValue(100));
            
            String clienteNombre = "Consumidor Final";
            String clienteDUI = "";
            
            if (primeraVenta.getCliente() != null) {
                clienteNombre = primeraVenta.getCliente().getNombre() + " " + 
                               primeraVenta.getCliente().getApellido();
                clienteDUI = primeraVenta.getCliente().getNid() != null ? 
                            primeraVenta.getCliente().getNid() : "";
            }
            
            clientTable.addCell(createBorderedCell("Nombre:", true));
            clientTable.addCell(createBorderedCell(clienteNombre, false));
            clientTable.addCell(createBorderedCell("Venta a cuenta de:", false));
            
            clientTable.addCell(createBorderedCell("Dirección:", true));
            clientTable.addCell(createBorderedCell(
                primeraVenta.getCliente() != null && primeraVenta.getCliente().getDireccion() != null ? 
                primeraVenta.getCliente().getDireccion() : "N/A", false));
            clientTable.addCell(createBorderedCell("", false));
            
            clientTable.addCell(createBorderedCell("D.U.I. ó N.I.T.:", true));
            clientTable.addCell(createBorderedCell(clienteDUI.isEmpty() ? "N/A" : clienteDUI, false));
            clientTable.addCell(createBorderedCell("", false));
            
            document.add(clientTable);
            document.add(new Paragraph("\n").setFontSize(5));

            // ===== SECCIÓN 4: CUERPO DE LA FACTURA =====
            float[] columnWidths = {0.8f, 3, 1.2f, 1.2f, 1.2f, 1.5f};
            Table itemsTable = new Table(UnitValue.createPercentArray(columnWidths));
            itemsTable.setWidth(UnitValue.createPercentValue(100));

            // Encabezados
            itemsTable.addHeaderCell(createHeaderCell("CANT."));
            itemsTable.addHeaderCell(createHeaderCell("DESCRIPCIÓN"));
            itemsTable.addHeaderCell(createHeaderCell("PRECIO\nUNITARIO"));
            itemsTable.addHeaderCell(createHeaderCell("VENTAS\nNO SUJETAS"));
            itemsTable.addHeaderCell(createHeaderCell("VENTAS\nEXENTAS"));
            itemsTable.addHeaderCell(createHeaderCell("VENTAS\nGRAVADAS"));

            // Datos de productos
            BigDecimal IVA_RATE = new BigDecimal("1.13"); // 13% IVA en El Salvador
            BigDecimal totalGravadas = BigDecimal.ZERO;
            BigDecimal totalExentas = BigDecimal.ZERO;
            BigDecimal totalNoSujetas = BigDecimal.ZERO;
            
            for (Venta venta : ventas) {
                itemsTable.addCell(createDataCell(String.valueOf(venta.getCantidad())));
                itemsTable.addCell(createDataCell(venta.getDescripcion()));
                itemsTable.addCell(createDataCell("$" + venta.getPrecio().toString()));
                itemsTable.addCell(createDataCell("")); // Ventas no sujetas
                itemsTable.addCell(createDataCell("")); // Ventas exentas
                itemsTable.addCell(createDataCell("$" + venta.getImporte().toString())); // Ventas gravadas
                
                totalGravadas = totalGravadas.add(venta.getImporte());
            }
            
            // Agregar filas vacías para completar la factura
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 6; j++) {
                    itemsTable.addCell(createDataCell(""));
                }
            }

            document.add(itemsTable);

            // ===== SECCIÓN 5: TOTALES Y SECCIÓN 6: SON =====
            // Calcular subtotal (sin IVA), IVA y total
            BigDecimal subtotalSinIVA = totalGravadas.divide(IVA_RATE, 2, RoundingMode.HALF_UP);
            BigDecimal montoIVA = totalGravadas.subtract(subtotalSinIVA);
            BigDecimal ventaTotal = totalGravadas.add(totalExentas).add(totalNoSujetas);
            
            Table totalsTable = new Table(UnitValue.createPercentArray(new float[]{3, 2}));
            totalsTable.setWidth(UnitValue.createPercentValue(100));
            
            // Columna izquierda: SON
            Cell sonCell = new Cell()
                    .setBorder(new SolidBorder(1))
                    .setPadding(5);
            sonCell.add(new Paragraph("SON: " + NumeroALetras.convertir(ventaTotal).toUpperCase())
                    .setFontSize(9)
                    .setBold());
            
            // Columna derecha: SUMAS
            Cell sumsCell = new Cell()
                    .setBorder(new SolidBorder(1))
                    .setPadding(5);
            
            Table sumsInnerTable = new Table(UnitValue.createPercentArray(new float[]{2, 1}));
            sumsInnerTable.setWidth(UnitValue.createPercentValue(100));
            
            sumsInnerTable.addCell(createTotalCell("SUMAS (Subtotal sin IVA)", false));
            sumsInnerTable.addCell(createTotalCell("$" + String.format("%.2f", subtotalSinIVA), false));
            
            sumsInnerTable.addCell(createTotalCell("IVA (13%)", false));
            sumsInnerTable.addCell(createTotalCell("$" + String.format("%.2f", montoIVA), false));
            
            sumsInnerTable.addCell(createTotalCell("VENTA EXENTA", false));
            sumsInnerTable.addCell(createTotalCell("$" + String.format("%.2f", totalExentas), false));
            
            sumsInnerTable.addCell(createTotalCell("VENTA NO SUJETA", false));
            sumsInnerTable.addCell(createTotalCell("$" + String.format("%.2f", totalNoSujetas), false));
            
            sumsInnerTable.addCell(createTotalCell("(-) IVA RETENIDO", false));
            sumsInnerTable.addCell(createTotalCell("$0.00", false));
            
            sumsInnerTable.addCell(createTotalCell("VENTA TOTAL", true));
            sumsInnerTable.addCell(createTotalCell("$" + String.format("%.2f", ventaTotal), true));
            
            sumsCell.add(sumsInnerTable);
            
            totalsTable.addCell(sonCell);
            totalsTable.addCell(sumsCell);
            
            document.add(totalsTable);

            // ===== SECCIÓN 7: PIE DE PÁGINA =====
            document.add(new Paragraph("\n").setFontSize(8));
            document.add(new Paragraph("Ticket: " + ticket)
                    .setFontSize(8)
                    .setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("¡Gracias por su compra!")
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold());

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF: " + e.getMessage(), e);
        }
    }
    
    private Cell createBorderedCell(String text, boolean bold) {
        Paragraph p = new Paragraph(text).setFontSize(9);
        if (bold) p.setBold();
        return new Cell()
                .add(p)
                .setBorder(new SolidBorder(1))
                .setPadding(3);
    }
    
    private Cell createHeaderCell(String text) {
        return new Cell()
                .add(new Paragraph(text).setBold().setFontSize(8))
                .setTextAlignment(TextAlignment.CENTER)
                .setBackgroundColor(new DeviceRgb(220, 220, 220))
                .setBorder(new SolidBorder(1))
                .setPadding(3);
    }
    
    private Cell createDataCell(String text) {
        return new Cell()
                .add(new Paragraph(text).setFontSize(9))
                .setBorder(new SolidBorder(1))
                .setPadding(3);
    }
    
    private Cell createTotalCell(String text, boolean bold) {
        Paragraph p = new Paragraph(text).setFontSize(9);
        if (bold) p.setBold();
        return new Cell()
                .add(p)
                .setBorder(Border.NO_BORDER)
                .setPadding(2);
    }
}
