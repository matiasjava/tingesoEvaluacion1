package kartingRM.Backend.Services;

import kartingRM.Backend.Entities.ReserveDetailsEntity;
import kartingRM.Backend.Entities.ReserveEntity;
import kartingRM.Backend.Repositories.ReserveRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.ss.usermodel.*;

import java.io.*;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.io.IOException;


@Service
public class ReserveService {

    @Autowired
    private UserService userService;

    @Autowired
    private ReserveRepository reserveRepository;

    @Autowired
    private JavaMailSender mailSender;

    public List<ReserveEntity> getAllReserves() {
        return reserveRepository.findAll();
    }

    public ReserveEntity getReserveById(Long id) {
        Optional<ReserveEntity> optionalReserve = reserveRepository.findById(id);
        if (optionalReserve.isPresent()) {
            return optionalReserve.get();
        } else {
            throw new RuntimeException("Reserva no encontrada con ID: " + id);
        }
    }

    public String convertirExcelAPdf(String excelPath) throws IOException {
        // Crear un documento PDF
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
    
        // Crear un flujo de contenido para escribir en el PDF
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
    
        // Leer el archivo Excel
        FileInputStream excelFile = new FileInputStream(new File(excelPath));
        Workbook workbook = WorkbookFactory.create(excelFile);
        Sheet sheet = workbook.getSheetAt(0);
    
        // Configuración inicial para escribir en el PDF
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        float yPosition = 750; // Posición inicial en el eje Y
        float margin = 50; // Margen izquierdo
        float lineHeight = 15; // Altura de cada línea
    

        for (Row row : sheet) {
            StringBuilder rowContent = new StringBuilder();
    

            for (Cell cell : row) {
                rowContent.append(cell.toString()).append(" | ");
            }
    

            contentStream.beginText();
            contentStream.newLineAtOffset(margin, yPosition);
            contentStream.showText(rowContent.toString());
            contentStream.endText();
    
            yPosition -= lineHeight;
    

            if (yPosition < 50) {
                contentStream.close();
                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                yPosition = 750; // Reiniciar la posición Y
            }
        }
    

        contentStream.close();
        workbook.close();
        excelFile.close();
        String pdfPath = excelPath.replace(".xlsx", ".pdf");
        document.save(pdfPath);
        document.close();
    
        return pdfPath;
    }

    public String generarComprobantePdf(ReserveEntity reserve) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
    
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
    
        // Configuración inicial
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        float yPosition = 750; // Posición inicial en el eje Y
        float margin = 50; // Margen izquierdo
        float lineHeight = 15; // Altura de cada línea
    
        // Información de la Reserva
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.showText("Comprobante de Reserva");
        contentStream.endText();
    
        yPosition -= lineHeight;
    
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.showText("Código de Reserva: " + reserve.getCodigo_reserva());
        contentStream.newLineAtOffset(0, -lineHeight);
        contentStream.showText("Fecha y Hora de la Reserva: " + reserve.getFecha_uso() + " " + reserve.getHora_inicio() + " - " + reserve.getHora_fin());
        contentStream.newLineAtOffset(0, -lineHeight);
        contentStream.showText("Número de Vueltas o Tiempo Máximo: " + reserve.getVueltas_o_tiempo());
        contentStream.newLineAtOffset(0, -lineHeight);
        contentStream.showText("Cantidad de Personas: " + reserve.getCantidad_personas());
        contentStream.newLineAtOffset(0, -lineHeight);
        contentStream.showText("Nombre del Cliente: " + reserve.getDetalles().get(0).getMemberName());
        contentStream.endText();
    
        yPosition -= (lineHeight * 5);
    
        // Detalle de Pago (Tabla)
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.showText("Detalle de Pago:");
        contentStream.endText();
    
        yPosition -= lineHeight;
    
        // Dibujar encabezados de la tabla
        float tableWidth = 500;
        float[] columnWidths = {100, 80, 80, 80, 80, 80}; // Ancho de cada columna
        String[] headers = {"Nombre", "Tarifa Base", "Descuento", "Monto Final", "IVA", "Total con IVA"};
    
        double totalReservaConIva = 0.0; // Variable para almacenar la suma total

        
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
        float xPosition = margin;
        for (String header : headers) {
            contentStream.beginText();
            contentStream.newLineAtOffset(xPosition, yPosition);
            contentStream.showText(header);
            contentStream.endText();
            xPosition += columnWidths[headers.length - headers.length + 1]; // Ajustar posición X
        }
        yPosition -= lineHeight; // Mover hacia abajo después de los encabezados

        // Dibujar filas de la tabla
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        for (ReserveDetailsEntity detalle : reserve.getDetalles()) {
            double tarifaBase = detalle.getMontoFinal() / (1 - detalle.getDiscount());
            double iva = detalle.getMontoFinal() * 0.19; // 19% de IVA
            double totalConIva = detalle.getMontoFinal() + iva;

            totalReservaConIva += totalConIva; // Sumar el total con IVA

            xPosition = margin;
            String[] row = {
                detalle.getMemberName(),
                String.format("%.2f", tarifaBase),
                String.format("%.2f", 100 * detalle.getDiscount()) + " %",
                String.format("%.2f", detalle.getMontoFinal()),
                String.format("%.2f", iva),
                String.format("%.2f", totalConIva)
            };

            for (String cell : row) {
                contentStream.beginText();
                contentStream.newLineAtOffset(xPosition, yPosition);
                contentStream.showText(cell);
                contentStream.endText();
                xPosition += columnWidths[row.length - row.length + 1]; // Ajustar posición X
            }

            yPosition -= lineHeight;

            // para agregar otra pagina
            if (yPosition < 50) {
                contentStream.close();
                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                yPosition = 750; // Reiniciar la posición Y
            }
        }

        // Agregar el total de la reserva al final
        yPosition -= lineHeight;
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.showText("Total Reserva: " + "$" + String.format("%.2f", totalReservaConIva));
        contentStream.endText();
    
        contentStream.close();
    
        // guardar el pdf
        String pdfPath = "comprobantes/comprobante_" + reserve.getId() + ".pdf";
        File comprobantesDir = new File("comprobantes");
        if (!comprobantesDir.exists()) {
            comprobantesDir.mkdir();
        }
        document.save(pdfPath);
        document.close();
    
        return pdfPath;
    }

    public void enviarComprobantePorCorreo(String[] destinatarios, String filePath, String reserveCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(destinatarios);
        helper.setSubject("Comprobante de Reserva " + reserveCode);
        helper.setText("hola");
        File file = new File(filePath);
        helper.addAttachment("comprobante.pdf", file);
    
        mailSender.send(message);
    }

    public ReserveEntity saveReserve(ReserveEntity reserve) {
        if (reserve.getDetalles() != null) {
            int cantidadPersonas = reserve.getCantidad_personas();
            int maxCumpleanos = calcularMaxCumpleanos(cantidadPersonas);
            int cumpleanosAplicados = 0;
            double montoTotalReserva = 0.0;

            for (ReserveDetailsEntity detalle : reserve.getDetalles()) {
                detalle.setReserve(reserve);
                double descuentoCumpleanos = 0.0;
                if (cumpleanosAplicados < maxCumpleanos &&
                        detalle.getDateBirthday().equals(reserve.getFecha_uso())) {
                    descuentoCumpleanos = 0.50;
                    cumpleanosAplicados++;
                }

                double descuentoCliente = userService.obtenerDescuentoPorCategoria(detalle.getUserId());
                double descuentoGrupo = calcularDescuentoGrupo(cantidadPersonas);

                double descuentoFinal = Math.max(descuentoCumpleanos, Math.max(descuentoCliente, descuentoGrupo));
                detalle.setDiscount(descuentoFinal);

                double tarifaBase = calcularTarifaBase(reserve.getVueltas_o_tiempo(), reserve.getFecha_uso(), cantidadPersonas, detalle.getUserId());
                double montoFinal = tarifaBase * (1 - descuentoFinal);
                detalle.setMontoFinal(montoFinal);
                montoTotalReserva += montoFinal;
            }

            reserve.setMontoFinal(montoTotalReserva);
        }

        ReserveEntity savedReserve = reserveRepository.save(reserve);

        try {
            String pdfPath = generarComprobantePdf(savedReserve); // Generar el PDF
            String[] emails = savedReserve.getDetalles().stream()
                    .map(detalle -> userService.findUserById(detalle.getUserId()).getEmail())
                    .toArray(String[]::new);
            enviarComprobantePorCorreo(emails, pdfPath, savedReserve.getCodigo_reserva()); // Enviar el PDF por correo
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al generar o enviar el comprobante.");
        }

        return savedReserve;
    }

    private int calcularMaxCumpleanos(int cantidadPersonas) {
        if (cantidadPersonas >= 6 && cantidadPersonas <= 10) {
            return 2; 
        } else if (cantidadPersonas >= 3 && cantidadPersonas <= 5) {
            return 1; 
        } else {
            return 0; 
        }
    }

    public ReserveEntity updateReserve(Long id, ReserveEntity reserve) {
        ReserveEntity existingReserve = getReserveById(id);
        existingReserve.setHora_inicio(reserve.getHora_inicio());
        existingReserve.setHora_fin(reserve.getHora_fin());
        existingReserve.setCantidad_personas(reserve.getCantidad_personas());
        return reserveRepository.save(existingReserve);
    }

    public void deleteReserve(Long id) {
        reserveRepository.deleteById(id);
    }

    private boolean esFinDeSemana(LocalDate fecha) {
        DayOfWeek dia = fecha.getDayOfWeek();
        return dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY;
    }

    private boolean esDiaFeriado(LocalDate fecha) {
        Set<LocalDate> diasFeriados = Set.of(
            LocalDate.of(2025, 9, 18), // Fiestas Patrias
            LocalDate.of(2025, 12, 25) // Navidad
        ); //dejo pendiente agregar más feriados
        return diasFeriados.contains(fecha);
    } 

    public double calcularTarifaBase(String vueltasOTiempo, LocalDate fechaUso, int cantidadPersonas, Long userId) {
        double tarifaBase;
        int duracionMaxima;
    
        // Determinar tarifa base y duración máxima
        switch (vueltasOTiempo) {
            case "10 vueltas":
            case "10 minutos":
                tarifaBase = 15000;
                duracionMaxima = 30; // minutos
                break;
            case "15 vueltas":
            case "15 minutos":
                tarifaBase = 20000;
                duracionMaxima = 35; // minutos
                break;
            case "20 vueltas":
            case "20 minutos":
                tarifaBase = 25000;
                duracionMaxima = 40; // minutos
                break;
            default:
                throw new IllegalArgumentException("Vueltas o tiempo no válido");
        }
        return tarifaBase;
    }

    
    public double calcularDescuentoGrupo(int cantidadPersonas) {
        if (cantidadPersonas >= 11) {
            return 0.30; // 30%
        } else if (cantidadPersonas >= 6) {
            return 0.20; // 20%
        } else if (cantidadPersonas >= 3) {
            return 0.10; // 10%
        } else {
            return 0.0; // 0%
        }
    }
}