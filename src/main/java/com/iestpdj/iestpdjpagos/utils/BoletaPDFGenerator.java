package com.iestpdj.iestpdjpagos.utils;

import com.iestpdj.iestpdjpagos.model.DatosBoleta;
import com.iestpdj.iestpdjpagos.model.DetalleBoletaInfo;
import com.iestpdj.iestpdjpagos.model.NumeroALetras;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

public class BoletaPDFGenerator {

    private static final String NOMBRE_INSTITUCION = "INSTITUTO DE EDUCACIÓN SUPERIOR TECNOLÓGICO PÚBLICO “DIVINO JESÚS”";
    private static final String DIRECCION = "Ub. Los lirios s/n Santo Tomas-Chumbivilcas";
    private static final String TELEFONO = "Tel: (01) 234-5678";
    private static final String CELULAR = "Cel: +51 987 654 321";
    private static final String CORREO = "Correo: divinojesus@iestpdj.edu.pe";
    private static final String PAGINA_WEB = "Sitio web: www.iestpdj.edu.pe";
    private static final String RUC = "20527620148";

    private static final DeviceRgb COLOR_AZUL = new DeviceRgb(41, 98, 155);
    private static final DeviceRgb COLOR_GRIS = new DeviceRgb(240, 240, 240);

    public static File generarBoletaPDF(DatosBoleta datos, List<DetalleBoletaInfo> detalles) throws Exception {

        File directorio = new File("boletas");
        if (!directorio.exists()) directorio.mkdirs();

        String nombreArchivo = "boletas/" + datos.getNumeroBoleta() + ".pdf";
        File archivoPDF = new File(nombreArchivo);

        PdfWriter writer = new PdfWriter(archivoPDF);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.setMargins(30, 30, 30, 30);

        // Encabezado profesional
        agregarEncabezado(document, datos);

        // Datos de pago
        agregarDatosGenerales(document, datos);

        // Tabla dinámica con P. Unit.
        agregarTablaConceptos(document, detalles);

        // Total
        agregarTotal(document, datos.getTotal());

        // Pie
        agregarPiePagina(document);

        document.close();
        return archivoPDF;
    }

    // ================= ENCABEZADO =================
    private static void agregarEncabezado(Document document, DatosBoleta datos) {
        float[] columnas = {20f, 55f, 25f};
        Table encabezado = new Table(columnas).setWidth(UnitValue.createPercentValue(100));

        // Logo izquierdo (instituto)
        try {
            Image logoIzq = new Image(ImageDataFactory.create("src/main/resources/img/logo_MDE.png"));
            logoIzq.setHeight(60);
            encabezado.addCell(new Cell()
                    .add(logoIzq)
                    .setBorder(Border.NO_BORDER)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER));
        } catch (Exception e) {
            encabezado.addCell(new Cell().setBorder(Border.NO_BORDER));
        }

        // Centro con información institucional
        Cell centro = new Cell().setBorder(Border.NO_BORDER)
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);

        centro.add(new Paragraph("MINISTERIO DE EDUCACIÓN")
                .setFontSize(9).setBold().setFontColor(COLOR_AZUL));
        centro.add(new Paragraph(NOMBRE_INSTITUCION)
                .setFontSize(10).setBold().setFontColor(COLOR_AZUL));
        centro.add(new Paragraph(DIRECCION)
                .setFontSize(8).setFontColor(ColorConstants.DARK_GRAY));
        centro.add(new Paragraph(TELEFONO + " | " + CELULAR)
                .setFontSize(8).setFontColor(ColorConstants.DARK_GRAY));
        centro.add(new Paragraph(CORREO + " | " + PAGINA_WEB)
                .setFontSize(8).setFontColor(ColorConstants.DARK_GRAY));
        encabezado.addCell(centro);

        // Derecha con cuadro y logo MDE al costado
        Table contenedorDerecha = new Table(UnitValue.createPercentArray(new float[]{70, 30}))
                .setWidth(UnitValue.createPercentValue(100))
                .setBorder(Border.NO_BORDER);

        // --- Subtabla con RUC y número de boleta ---
        Table cuadroBoleta = new Table(1);
        cuadroBoleta.setWidth(UnitValue.createPercentValue(100));

        cuadroBoleta.addCell(new Cell()
                .add(new Paragraph("R.U.C. " + RUC)
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(9))
                .setBorder(new SolidBorder(ColorConstants.BLACK, 1)));

        cuadroBoleta.addCell(new Cell()
                .add(new Paragraph("RECIBO DE TESORERÍA")
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(9)
                        .setFontColor(COLOR_AZUL))
                .setBackgroundColor(COLOR_GRIS)
                .setBorder(new SolidBorder(ColorConstants.BLACK, 1)));

        cuadroBoleta.addCell(new Cell()
                .add(new Paragraph("N.º " + datos.getNumeroBoleta())
                        .setFontSize(14)
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontColor(COLOR_AZUL))
                .setBorder(new SolidBorder(ColorConstants.BLACK, 1))
                .setPadding(5));

        contenedorDerecha.addCell(new Cell()
                .add(cuadroBoleta)
                .setBorder(Border.NO_BORDER));

        // --- Logo MDE al costado derecho ---
        try {
            Image logoMDE = new Image(ImageDataFactory.create("src/main/resources/img/logo_istp.png"));
            logoMDE.setHeight(60);
            logoMDE.setAutoScale(true);
            contenedorDerecha.addCell(new Cell()
                    .add(logoMDE)
                    .setBorder(Border.NO_BORDER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setTextAlignment(TextAlignment.CENTER));
        } catch (Exception e) {
            contenedorDerecha.addCell(new Cell().setBorder(Border.NO_BORDER));
        }

        encabezado.addCell(new Cell()
                .add(contenedorDerecha)
                .setBorder(Border.NO_BORDER));

        document.add(encabezado);
        document.add(new Paragraph("\n"));
    }


    // ================= DATOS DE PAGO =================
    private static void agregarDatosGenerales(Document document, DatosBoleta datos) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Table datosPago = new Table(UnitValue.createPercentArray(new float[]{30, 70}))
                .setWidth(UnitValue.createPercentValue(100));

        datosPago.addCell(celda("FECHA:", sdf.format(datos.getFechaEmision()), true));
        //datosPago.addCell(celda("CAJERO:", datos.getCajero(), true));
        datosPago.addCell(celda("RECIBÍ DE:", datos.getNombreEstudiante(), false));
        datosPago.addCell(celda("MÉTODO DE PAGO:", datos.getMetodoPago(), false));

        document.add(datosPago);
        document.add(new Paragraph("\n"));
    }

    private static Cell celda(String label, String valor, boolean fondo) {
        Paragraph contenido = new Paragraph(label + " " + valor)
                .setFontSize(9)
                .setFontColor(ColorConstants.BLACK);
        Cell cell = new Cell().add(contenido)
                .setBorder(Border.NO_BORDER)
                .setPadding(3);

        if (fondo)
            cell.setBackgroundColor(COLOR_GRIS);

        return cell;
    }

    // ================= TABLA DE CONCEPTOS =================
    private static void agregarTablaConceptos(Document document, List<DetalleBoletaInfo> detalles) {
        float[] columnas = {8, 52, 20, 20};
        Table tabla = new Table(columnas).setWidth(UnitValue.createPercentValue(100));

        tabla.addHeaderCell(celdaHeader("N°"));
        tabla.addHeaderCell(celdaHeader("DESCRIPCIÓN"));
        tabla.addHeaderCell(celdaHeader("P. Unit."));
        tabla.addHeaderCell(celdaHeader("TOTAL"));

        int index = 1;
        for (DetalleBoletaInfo d : detalles) {
            tabla.addCell(celdaDato(String.valueOf(index++), TextAlignment.CENTER));
            tabla.addCell(celdaDato(d.getConcepto(), TextAlignment.LEFT));
            tabla.addCell(celdaDato(String.format("S/ %.2f", d.getPrecioUnitario()), TextAlignment.RIGHT));
            tabla.addCell(celdaDato(String.format("S/ %.2f", d.getSubtotal()), TextAlignment.RIGHT));
        }

        document.add(tabla);
        document.add(new Paragraph("\n"));
    }

    private static Cell celdaHeader(String texto) {
        return new Cell()
                .add(new Paragraph(texto).setBold().setFontSize(9)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(COLOR_AZUL)
                .setBorder(new SolidBorder(ColorConstants.BLACK, 0.5f))
                .setPadding(5);
    }

    private static Cell celdaDato(String texto, TextAlignment align) {
        return new Cell()
                .add(new Paragraph(texto).setFontSize(9).setTextAlignment(align))
                .setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f))
                .setPadding(4);
    }

    // ================= TOTAL =================
    private static void agregarTotal(Document document, double total) {

        // ==== Tabla original del total ====
        Table totales = new Table(UnitValue.createPercentArray(new float[]{80, 20}))
                .setWidth(UnitValue.createPercentValue(100));

        totales.addCell(new Cell()
                .add(new Paragraph("TOTAL A PAGAR:")
                        .setBold()
                        .setFontSize(10)
                        .setTextAlignment(TextAlignment.RIGHT))
                .setBackgroundColor(COLOR_GRIS)
                .setBorder(new SolidBorder(ColorConstants.BLACK, 0.5f))
                .setPadding(6));

        totales.addCell(new Cell()
                .add(new Paragraph(String.format("S/ %.2f", total))
                        .setBold()
                        .setFontSize(10)
                        .setTextAlignment(TextAlignment.CENTER))
                .setBorder(new SolidBorder(ColorConstants.BLACK, 0.5f))
                .setPadding(6));

        document.add(totales);

        // ==== NUEVO: TOTAL EN LETRAS ====
        String montoLetras = convertirNumeroALetras(total);
        document.add(new Paragraph("SON: " + montoLetras)
                .setFontSize(9)
                .setItalic()
                .setMarginTop(5));

        document.add(new Paragraph("\n"));
    }

    // ================= CONVERTIR NÚMERO A LETRAS =================
    private static String convertirNumeroALetras(double monto) {
        long parteEntera = (long) monto;
        int decimales = (int) Math.round((monto - parteEntera) * 100);

        return NumeroALetras.convertir(parteEntera) + " CON " +
                String.format("%02d", decimales) + "/100 SOLES";
    }

    // ================= PIE DE PÁGINA =================
    private static void agregarPiePagina(Document document) {
        document.add(new Paragraph("\n\n\n")); // <-- MÁS ESPACIO ANTES DEL PIE

        document.add(new LineSeparator(new com.itextpdf.kernel.pdf.canvas.draw.SolidLine()));

        document.add(new Paragraph("TESORERÍA - IESTP “DIVINO JESÚS”")
                .setFontSize(10)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(12)  // <-- MÁS ABAJO
                .setFontColor(COLOR_AZUL));

        document.add(new Paragraph("Conserve esta boleta como comprobante de pago oficial.")
                .setFontSize(8)
                .setItalic()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.GRAY));
    }
}
