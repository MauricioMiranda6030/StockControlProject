package com.stock.control.util;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.stock.control.dto.ProductSaveDto;
import com.stock.control.dto.SaleReportDTO;
import com.stock.control.dto.SaleViewDTO;
import com.stock.control.front.tools.CurrencyFormater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/*
* Clase que se encarga de la creación de los reportes en PDF
*/
public class PdfGenerator {

    private static final Logger log = LoggerFactory.getLogger(PdfGenerator.class);

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private static final float[] colsForProducts = {50f, 500f, 90f, 110f}, //id, nombre, desc, precio y stock
                                 colsForSales = {30f, 50f, 80f, 150f, 30f, 90f, 50f}, //id, code, client, products, amount, price, date
                                 colsForClient = {200f, 30f, 30f};

    private final static String home = System.getProperty("user.home");
    private final static String downloadsPath = Paths.get(home, "Downloads").toString();
    private static final String pdfName = LocalDate.now().format(formatter) + ".pdf";

    public static void createProductsReportPdf(List<ProductSaveDto> products){
        try {
            buildPdfForProducts(products);
            log.info("Report just created at: {}", downloadsPath + "\\reporte de stock " + pdfName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createSalesReportPdf(List<SaleViewDTO> sales, String totalAmount, String totalCurrency){
        try {
            buildPdfForSales(sales, totalAmount, totalCurrency);
            log.info("Sale report just created at: {}", downloadsPath + "\\reporte de ventas " + pdfName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createClientReportPdf(List<SaleReportDTO> sales, LocalDate dateFrom, LocalDate dateTo){
        try{
            buildPdfForClientReport(sales, dateFrom, dateTo);
            log.info("Client sale report just created at: {}", downloadsPath + "\\reporte de clientes " + pdfName);
        }catch (FileNotFoundException e){
            throw new RuntimeException(e);
        }
    }

    private static void buildPdfForProducts(List<ProductSaveDto> products) throws FileNotFoundException {
        Document doc = createDoc("reporte de stock ");
        setTitle(doc, "Reporte de Stock " + LocalDate.now().format(formatter));
        Table table = createTable(colsForProducts);
        setUpColumnsForProducts(table);
        fillTableProducts(products, table);
        doc.add(table);
        doc.close();
    }

    private static void buildPdfForSales(List<SaleViewDTO> sales, String totalAmount, String totalCurrency) throws FileNotFoundException {
        Document doc = createDoc("reporte de ventas ");
        setTitle(doc, "Reporte de Ventas " + LocalDate.now().format(formatter));
        Table table = createTable(colsForSales);
        setUpColumnsForSale(table);
        fillTableSales(sales, table);
        doc.add(table);
        doc.add(addTableInfo(totalAmount, totalCurrency));
        doc.close();
    }

    private static void buildPdfForClientReport(List<SaleReportDTO> sales, LocalDate dateFrom, LocalDate dateTo) throws FileNotFoundException {
        Document doc = createDoc("reporte de clientes desde " + dateFrom.format(formatter) + " hasta " + dateTo.format(formatter) + " - ");
        setTitle(doc, "Reporte de Clientes Desde " + dateFrom.format(formatter) + " Hasta " + dateTo.format(formatter));
        Table table = createTable(colsForClient);
        setUpColumnsForClients(table);
        fillTableClient(sales, table);
        doc.add(table);
        doc.close();
    }

    private static void setTitle(Document doc, String title) {
        Paragraph par = new Paragraph(title)
                .setFontSize(15f)
                .setBold();

        doc.add(par);
    }

    private static Document createDoc(String name) throws FileNotFoundException {
        PdfWriter pdfWriter = new PdfWriter(downloadsPath + "\\"+name + pdfName);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        return new Document(pdfDocument);
    }

    private static Paragraph addTableInfo(String totalAmount,String totalCurrency){
        Text text = new Text("Total de Productos: " + totalAmount + "\nTotal Recaudado: " + totalCurrency);
        return new Paragraph(text).setFontSize(10);
    }

    private static Table createTable(float[] cols) {
        return new Table(cols);
    }

    private static void setUpColumnsForProducts(Table table) {
        table.addCell(createHeaderCell("id"));
        table.addCell(createHeaderCell("Nombre"));
        table.addCell(createHeaderCell("Stock"));
        table.addCell(createHeaderCell("Precio"));
    }

    private static void setUpColumnsForSale(Table table){
        table.addCell(createHeaderCell("id"));
        table.addCell(createHeaderCell("Código"));
        table.addCell(createHeaderCell("Vendido a"));
        table.addCell(createHeaderCell("Productos"));
        table.addCell(createHeaderCell("Cantidad Total"));
        table.addCell(createHeaderCell("Precio Final"));
        table.addCell(createHeaderCell("Fecha"));
    }

    private static void setUpColumnsForClients(Table table) {
        table.addCell(createHeaderCell("Nombre"));
        table.addCell(createHeaderCell("MT"));
        table.addCell(createHeaderCell("Cantidad de Ventas"));
    }

    private static Cell createHeaderCell(String title){
        Paragraph content = new Paragraph(title).setFontSize(10).setMultipliedLeading(1.2f).setFontColor(Color.WHITE);
        return new Cell().add(content).setBold().setBackgroundColor(Color.GRAY).setTextAlignment(TextAlignment.CENTER).setPadding(5);
    }

    private static void fillTableProducts(List<ProductSaveDto> products, Table table) {
        products.forEach(
                p -> {
                    table.addCell(createBodyCell(String.valueOf(p.getId())));
                    table.addCell(createBodyCell(p.getName()));
                    table.addCell(createBodyCell(String.valueOf(p.getStock())));
                    table.addCell(createBodyCell(CurrencyFormater.getCurrency(p.getPrice())));
                }
        );
    }

    private static void fillTableSales(List<SaleViewDTO> sales, Table table){
        sales.forEach(
                s ->{
                    table.addCell(createBodyCell(String.valueOf(s.getId())));
                    table.addCell(createBodyCell(s.getCode()));
                    table.addCell(createBodyCell(s.getClient()));
                    table.addCell(createBodyCell(s.getProductsDetails()));
                    table.addCell(createBodyCell(String.valueOf(s.getTotalAmount())));
                    table.addCell(createBodyCell(s.getFinalPrice()));
                    table.addCell(createBodyCell(s.getDateOfSale()));
                }
        );
    }

    private static void fillTableClient(List<SaleReportDTO> sales, Table table) {
        sales.forEach(
                s -> {
                    table.addCell(createBodyCell(s.getName()));
                    table.addCell(createBodyCell(s.getDocId()));
                    table.addCell(createBodyCell(String.valueOf(s.getCont())));
                }
        );
    }

    private static Cell createBodyCell(String text){
        Paragraph content = new Paragraph(text).setFontSize(10).setMultipliedLeading(1.2f);
        return new Cell().add(content).setPadding(5);
    }
}
