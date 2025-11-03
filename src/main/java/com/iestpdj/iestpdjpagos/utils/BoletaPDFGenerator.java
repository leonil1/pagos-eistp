package com.iestpdj.iestpdjpagos.utils;

import com.iestpdj.iestpdjpagos.model.DatosBoleta;
import com.iestpdj.iestpdjpagos.model.DetalleBoletaInfo;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

public class BoletaPDFGenerator {

    private static final String NOMBRE_INSTITUCION = "INSTITUCIÓN EDUCATIVA";
    private static final String RUC_INSTITUCION = "20123456789";
    private static final String DIRECCION = "Av. Principal 123, Lima, Perú";
    private static final String TELEFONO = "(01) 234-5678";

    public static File generarBoletaPDF(DatosBoleta datos,
                                        List<DetalleBoletaInfo> detalles) throws Exception {

        // Crear directorio si no existe
        File directorio = new File("boletas");
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        // Nombre del archivo
        String nombreArchivo = "boletas/" + datos.getNumeroBoleta() + ".pdf";
        File archivoPDF = new File(nombreArchivo);

        // Crear documento PDF
        PdfWriter writer = new PdfWriter(archivoPDF);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Configurar márgenes
        document.setMargins(20, 40, 20, 40);

        // ===== ENCABEZADO =====
        agregarEncabezado(document);

        // ===== INFORMACIÓN DE LA BOLETA =====
        agregarInformacionBoleta(document, datos);

        // ===== INFORMACIÓN DEL ESTUDIANTE =====
        agregarInformacionEstudiante(document, datos);

        // ===== TABLA DE CONCEPTOS =====
        agregarTablaConceptos(document, detalles);

        // ===== TOTALES =====
        agregarTotales(document, datos.getTotal());

        // ===== PIE DE PÁGINA =====
        agregarPiePagina(document, datos);

        document.close();
        return archivoPDF;
    }

    private static void agregarEncabezado(Document document) {
        // Título de la institución
        Paragraph titulo = new Paragraph(NOMBRE_INSTITUCION)
                .setFontSize(18)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);
        document.add(titulo);

        // RUC
        Paragraph ruc = new Paragraph("RUC: " + RUC_INSTITUCION)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(2);
        document.add(ruc);

        // Dirección y teléfono
        Paragraph info = new Paragraph(DIRECCION + " | Tel: " + TELEFONO)
                .setFontSize(9)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(15);
        document.add(info);

        // Línea separadora
        document.add(new Paragraph("\n")
                .setMarginBottom(5)
                .setBorderBottom(new com.itextpdf.layout.borders.SolidBorder(
                        ColorConstants.BLACK, 1)));
    }

    private static void agregarInformacionBoleta(Document document, DatosBoleta datos) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Paragraph tituloBoleta = new Paragraph("BOLETA DE PAGO")
                .setFontSize(16)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(10)
                .setMarginBottom(15)
                .setFontColor(new DeviceRgb(0, 51, 102));
        document.add(tituloBoleta);

        // Información en dos columnas
        Table infoTable = new Table(2);
        infoTable.setWidth(UnitValue.createPercentValue(100));

        infoTable.addCell(crearCeldaInfo("N° Boleta:", datos.getNumeroBoleta()));
        infoTable.addCell(crearCeldaInfo("Fecha:", sdf.format(datos.getFechaEmision())));
        infoTable.addCell(crearCeldaInfo("Cajero:", datos.getCajero()));
        infoTable.addCell(crearCeldaInfo("Método Pago:", datos.getMetodoPago()));

        document.add(infoTable);
        document.add(new Paragraph("\n").setMarginBottom(5));
    }

    private static void agregarInformacionEstudiante(Document document, DatosBoleta datos) {
        Paragraph subtitulo = new Paragraph("DATOS DEL ESTUDIANTE")
                .setFontSize(12)
                .setBold()
                .setMarginBottom(10)
                .setFontColor(new DeviceRgb(0, 51, 102));
        document.add(subtitulo);

        Table estudianteTable = new Table(2);
        estudianteTable.setWidth(UnitValue.createPercentValue(100));

        estudianteTable.addCell(crearCeldaInfo("Nombre:", datos.getNombreEstudiante()));
        estudianteTable.addCell(crearCeldaInfo("DNI:", datos.getDniEstudiante()));

        document.add(estudianteTable);
        document.add(new Paragraph("\n").setMarginBottom(10));
    }

    private static void agregarTablaConceptos(Document document,
                                              List<DetalleBoletaInfo> detalles) {
        Paragraph subtitulo = new Paragraph("DETALLE DE PAGO")
                .setFontSize(12)
                .setBold()
                .setMarginBottom(10)
                .setFontColor(new DeviceRgb(0, 51, 102));
        document.add(subtitulo);

        // Crear tabla con 4 columnas
        float[] columnWidths = {50f, 10f, 20f, 20f};
        Table table = new Table(columnWidths);
        table.setWidth(UnitValue.createPercentValue(100));

        // Encabezados
        DeviceRgb colorEncabezado = new DeviceRgb(0, 51, 102);
        table.addHeaderCell(crearCeldaEncabezado("Concepto", colorEncabezado));
        table.addHeaderCell(crearCeldaEncabezado("Cant.", colorEncabezado));
        table.addHeaderCell(crearCeldaEncabezado("P. Unit.", colorEncabezado));
        table.addHeaderCell(crearCeldaEncabezado("Subtotal", colorEncabezado));

        // Datos
        for (DetalleBoletaInfo detalle : detalles) {
            table.addCell(crearCeldaDato(detalle.getConcepto(), TextAlignment.LEFT));
            table.addCell(crearCeldaDato(String.valueOf(detalle.getCantidad()),
                    TextAlignment.CENTER));
            table.addCell(crearCeldaDato(String.format("S/ %.2f",
                            detalle.getPrecioUnitario()),
                    TextAlignment.RIGHT));
            table.addCell(crearCeldaDato(String.format("S/ %.2f",
                            detalle.getSubtotal()),
                    TextAlignment.RIGHT));
        }

        document.add(table);
    }

    private static void agregarTotales(Document document, double total) {
        document.add(new Paragraph("\n").setMarginBottom(10));

        Table totalesTable = new Table(new float[]{70f, 30f});
        totalesTable.setWidth(UnitValue.createPercentValue(100));

        // Celda vacía a la izquierda
        totalesTable.addCell(new Cell().setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));

        // Total
        Table totalInner = new Table(2);
        totalInner.setWidth(UnitValue.createPercentValue(100));

        Cell labelTotal = new Cell().add(new Paragraph("TOTAL A PAGAR:")
                        .setBold()
                        .setFontSize(12))
                .setBackgroundColor(new DeviceRgb(0, 51, 102))
                .setFontColor(ColorConstants.WHITE)
                .setPadding(8)
                .setTextAlignment(TextAlignment.RIGHT);

        Cell valorTotal = new Cell().add(new Paragraph(String.format("S/ %.2f", total))
                        .setBold()
                        .setFontSize(12))
                .setBackgroundColor(new DeviceRgb(0, 102, 204))
                .setFontColor(ColorConstants.WHITE)
                .setPadding(8)
                .setTextAlignment(TextAlignment.RIGHT);

        totalInner.addCell(labelTotal);
        totalInner.addCell(valorTotal);

        totalesTable.addCell(new Cell().add(totalInner)
                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER));

        document.add(totalesTable);
    }

    private static void agregarPiePagina(Document document, DatosBoleta datos) {
        document.add(new Paragraph("\n\n").setMarginBottom(10));

        Paragraph nota = new Paragraph("Nota: Conserve esta boleta como comprobante de pago.")
                .setFontSize(9)
                .setItalic()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);
        document.add(nota);

        Paragraph gracias = new Paragraph("¡Gracias por su pago!")
                .setFontSize(10)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(10);
        document.add(gracias);

        // Línea final
        document.add(new Paragraph("\n")
                .setMarginTop(15)
                .setBorderTop(new com.itextpdf.layout.borders.SolidBorder(
                        ColorConstants.LIGHT_GRAY, 0.5f)));

        Paragraph footer = new Paragraph("Documento generado electrónicamente - " +
                new SimpleDateFormat("dd/MM/yyyy HH:mm").format(
                        datos.getFechaEmision()))
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.GRAY);
        document.add(footer);
    }

    private static Cell crearCeldaInfo(String label, String valor) {
        Paragraph p = new Paragraph()
                .add(new Text(label + " ").setBold().setFontSize(10))
                .add(new Text(valor).setFontSize(10));

        return new Cell().add(p)
                .setPadding(5)
                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER);
    }

    private static Cell crearCeldaEncabezado(String texto, DeviceRgb color) {
        return new Cell().add(new Paragraph(texto)
                        .setBold()
                        .setFontColor(ColorConstants.WHITE)
                        .setFontSize(10))
                .setBackgroundColor(color)
                .setPadding(8)
                .setTextAlignment(TextAlignment.CENTER);
    }

    private static Cell crearCeldaDato(String texto, TextAlignment alineacion) {
        return new Cell().add(new Paragraph(texto).setFontSize(9))
                .setPadding(6)
                .setTextAlignment(alineacion);
    }
}
