package com.espe.ListoEgsi.service.phase.impl;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.espe.ListoEgsi.domain.model.entity.Inplantation.phase1.ProcessEgsi;
import com.espe.ListoEgsi.domain.model.entity.phase.EgsiAnswer;
import com.espe.ListoEgsi.domain.model.entity.phase.EgsiPhase;
import com.espe.ListoEgsi.domain.model.entity.phase.EgsiQuestion;
import com.espe.ListoEgsi.domain.model.entity.phase.EgsiSection;
import com.espe.ListoEgsi.domain.model.entity.question.PhaseApprovalRequest;
import com.espe.ListoEgsi.exception.ResourceNotFoundException;
import com.espe.ListoEgsi.repository.Inplantation.phase1.ProcessRepository;
import com.espe.ListoEgsi.repository.phase.EgsiAnswerRepository;
import com.espe.ListoEgsi.repository.phase.EgsiPhaseRepository;
import com.espe.ListoEgsi.repository.phase.PhaseApprovalRepository;
import com.espe.ListoEgsi.service.phase.PhaseReportService;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.io.font.constants.StandardFonts;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio para generar reportes PDF de fases.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PhaseReportServiceImpl implements PhaseReportService {

    private final ProcessRepository processRepository;
    private final EgsiPhaseRepository phaseRepository;
    private final EgsiAnswerRepository answerRepository;
    private final PhaseApprovalRepository approvalRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Colores corporativos
    private static final DeviceRgb PRIMARY_COLOR = new DeviceRgb(124, 58, 237); // Purple
    private static final DeviceRgb SECONDARY_COLOR = new DeviceRgb(79, 70, 229); // Indigo
    private static final DeviceRgb SUCCESS_COLOR = new DeviceRgb(34, 197, 94); // Green
    private static final DeviceRgb LIGHT_GRAY = new DeviceRgb(243, 244, 246);
    private static final DeviceRgb DARK_GRAY = new DeviceRgb(75, 85, 99);

    @Override
    public byte[] generatePhaseReport(UUID processId, String phaseId) {
        log.info("Generating PDF report for process {} phase {}", processId, phaseId);

        // Obtener datos
        ProcessEgsi process = processRepository.findById(processId)
                .orElseThrow(() -> new ResourceNotFoundException("Proceso no encontrado"));

        EgsiPhase phase = phaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Fase no encontrada"));

        // Verificar que la fase esté aprobada
        List<PhaseApprovalRequest> approvals = approvalRepository.findByProcessAndPhase(processId, phaseId);
        PhaseApprovalRequest approval = approvals.stream()
                .filter(a -> "APPROVED".equals(a.getStatus()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("La fase debe estar aprobada para generar el reporte"));

        // Obtener respuestas
        List<EgsiAnswer> answers = answerRepository.findByProcessAndPhase(processId, phaseId);
        Map<String, EgsiAnswer> answersMap = new java.util.HashMap<>();
        for (EgsiAnswer answer : answers) {
            answersMap.put(answer.getQuestion().getIdQuestion(), answer);
        }

        // Generar PDF
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc, PageSize.A4);
            document.setMargins(40, 40, 40, 40);

            PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont regularFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            // === HEADER ===
            addHeader(document, process, phase, boldFont, regularFont);

            // === INFORMACIÓN DE APROBACIÓN ===
            addApprovalInfo(document, approval, boldFont, regularFont);

            // === CONTENIDO DE LA FASE ===
            addPhaseContent(document, phase, answersMap, boldFont, regularFont);

            // === FOOTER ===
            addFooter(document, regularFont);

            document.close();
            
            log.info("PDF report generated successfully for process {} phase {}", processId, phaseId);
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Error generating PDF report: {}", e.getMessage(), e);
            throw new RuntimeException("Error al generar el reporte PDF: " + e.getMessage(), e);
        }
    }

    private void addHeader(Document document, ProcessEgsi process, EgsiPhase phase, 
                          PdfFont boldFont, PdfFont regularFont) {
        // Título principal
        Paragraph title = new Paragraph("INFORME DE FASE APROBADA")
                .setFont(boldFont)
                .setFontSize(20)
                .setFontColor(PRIMARY_COLOR)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);
        document.add(title);

        // Subtítulo
        Paragraph subtitle = new Paragraph("Sistema de Gestión EGSI - ESPE")
                .setFont(regularFont)
                .setFontSize(12)
                .setFontColor(DARK_GRAY)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
        document.add(subtitle);

        // Información del proceso
        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginBottom(20);

        addInfoRow(infoTable, "Proceso:", process.getName(), boldFont, regularFont);
        addInfoRow(infoTable, "Descripción:", process.getDescription() != null ? process.getDescription() : "N/A", boldFont, regularFont);
        addInfoRow(infoTable, "Fase:", "Fase " + phase.getPhaseOrder() + ": " + phase.getTitle(), boldFont, regularFont);
        
        document.add(infoTable);

        // Línea separadora
        document.add(new Paragraph("")
                .setBorderBottom(new SolidBorder(PRIMARY_COLOR, 2))
                .setMarginBottom(20));
    }

    private void addApprovalInfo(Document document, PhaseApprovalRequest approval, 
                                 PdfFont boldFont, PdfFont regularFont) {
        Paragraph sectionTitle = new Paragraph("INFORMACIÓN DE APROBACIÓN")
                .setFont(boldFont)
                .setFontSize(14)
                .setFontColor(SUCCESS_COLOR)
                .setMarginBottom(10);
        document.add(sectionTitle);

        // Caja de estado aprobado
        Table statusTable = new Table(UnitValue.createPercentArray(new float[]{1}))
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginBottom(15);

        Cell statusCell = new Cell()
                .add(new Paragraph("✓ FASE APROBADA")
                        .setFont(boldFont)
                        .setFontSize(16)
                        .setFontColor(ColorConstants.WHITE)
                        .setTextAlignment(TextAlignment.CENTER))
                .setBackgroundColor(SUCCESS_COLOR)
                .setPadding(15)
                .setBorder(Border.NO_BORDER);
        statusTable.addCell(statusCell);
        document.add(statusTable);

        // Detalles de aprobación
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        Table detailsTable = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginBottom(20);

        addInfoRow(detailsTable, "Solicitado por:", approval.getRequestedBy(), boldFont, regularFont);
        addInfoRow(detailsTable, "Fecha de solicitud:", 
                approval.getRequestedAt() != null ? approval.getRequestedAt().format(formatter) : "N/A", 
                boldFont, regularFont);
        addInfoRow(detailsTable, "Aprobado por:", approval.getReviewedBy(), boldFont, regularFont);
        addInfoRow(detailsTable, "Fecha de aprobación:", 
                approval.getReviewedAt() != null ? approval.getReviewedAt().format(formatter) : "N/A", 
                boldFont, regularFont);
        
        if (approval.getComments() != null && !approval.getComments().isBlank()) {
            addInfoRow(detailsTable, "Comentarios:", approval.getComments(), boldFont, regularFont);
        }

        document.add(detailsTable);
    }

    private void addPhaseContent(Document document, EgsiPhase phase, Map<String, EgsiAnswer> answersMap,
                                 PdfFont boldFont, PdfFont regularFont) {
        Paragraph sectionTitle = new Paragraph("CONTENIDO DE LA FASE")
                .setFont(boldFont)
                .setFontSize(14)
                .setFontColor(PRIMARY_COLOR)
                .setMarginBottom(15);
        document.add(sectionTitle);

        // Descripción de la fase
        if (phase.getDescription() != null && !phase.getDescription().isBlank()) {
            Paragraph phaseDesc = new Paragraph(phase.getDescription())
                    .setFont(regularFont)
                    .setFontSize(11)
                    .setFontColor(DARK_GRAY)
                    .setMarginBottom(15);
            document.add(phaseDesc);
        }

        // Iterar por secciones
        List<EgsiSection> sections = phase.getSections().stream()
                .sorted((a, b) -> Integer.compare(a.getSectionOrder(), b.getSectionOrder()))
                .toList();

        int sectionNumber = 1;
        for (EgsiSection section : sections) {
            // Título de sección
            Paragraph sectionHeader = new Paragraph(sectionNumber + ". " + section.getTitle())
                    .setFont(boldFont)
                    .setFontSize(12)
                    .setFontColor(SECONDARY_COLOR)
                    .setMarginTop(15)
                    .setMarginBottom(10);
            document.add(sectionHeader);

            if (section.getDescription() != null && !section.getDescription().isBlank()) {
                document.add(new Paragraph(section.getDescription())
                        .setFont(regularFont)
                        .setFontSize(10)
                        .setFontColor(DARK_GRAY)
                        .setMarginBottom(10));
            }

            // Iterar por preguntas
            List<EgsiQuestion> questions = section.getQuestions().stream()
                    .sorted((a, b) -> Integer.compare(a.getQuestionOrder(), b.getQuestionOrder()))
                    .toList();

            for (EgsiQuestion question : questions) {
                EgsiAnswer answer = answersMap.get(question.getIdQuestion());
                addQuestionAndAnswer(document, question, answer, boldFont, regularFont);
            }

            sectionNumber++;
        }
    }

    private void addQuestionAndAnswer(Document document, EgsiQuestion question, EgsiAnswer answer,
                                      PdfFont boldFont, PdfFont regularFont) {
        // Contenedor de pregunta
        Table qaTable = new Table(UnitValue.createPercentArray(new float[]{1}))
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginBottom(10);

        // Pregunta con tipo de input
        String questionTitle = question.getTitle();
        if (question.getIsRequired() != null && question.getIsRequired()) {
            questionTitle += " *";
        }
        
        Cell questionCell = new Cell()
                .add(new Paragraph(questionTitle)
                        .setFont(boldFont)
                        .setFontSize(11)
                        .setFontColor(DARK_GRAY))
                .setBackgroundColor(LIGHT_GRAY)
                .setPadding(8)
                .setBorder(Border.NO_BORDER);
        qaTable.addCell(questionCell);

        // Respuesta
        if (answer != null && answer.getAnswerValue() != null && !answer.getAnswerValue().isBlank()) {
            String inputType = question.getInputType();
            
            // Si es una tabla, mostrar los datos en formato tabla
            if ("TABLA".equalsIgnoreCase(inputType) || "TABLE".equalsIgnoreCase(inputType)) {
                qaTable.addCell(createTableAnswerCell(answer, question, boldFont, regularFont));
            } else {
                // Respuesta de texto normal
                Cell answerCell = new Cell()
                        .add(new Paragraph("Respuesta: " + formatTextAnswer(answer.getAnswerValue()))
                                .setFont(regularFont)
                                .setFontSize(10))
                        .setPadding(8)
                        .setBorder(new SolidBorder(LIGHT_GRAY, 1));
                qaTable.addCell(answerCell);
            }
            
            // Información de auditoría de la respuesta
            Cell auditCell = createAuditInfoCell(answer, regularFont);
            qaTable.addCell(auditCell);
        } else {
            Cell noAnswerCell = new Cell()
                    .add(new Paragraph("Sin respuesta")
                            .setFont(regularFont)
                            .setFontSize(10)
                            .setFontColor(DARK_GRAY)
                            .setItalic())
                    .setPadding(8)
                    .setBorder(new SolidBorder(LIGHT_GRAY, 1));
            qaTable.addCell(noAnswerCell);
        }

        document.add(qaTable);
    }

    private Cell createTableAnswerCell(EgsiAnswer answer, EgsiQuestion question, 
                                       PdfFont boldFont, PdfFont regularFont) {
        Cell containerCell = new Cell()
                .setPadding(8)
                .setBorder(new SolidBorder(LIGHT_GRAY, 1));

        try {
            JsonNode tableData = objectMapper.readTree(answer.getAnswerValue());
            
            if (tableData.isArray() && tableData.size() > 0) {
                // Obtener las columnas del primer elemento
                JsonNode firstRow = tableData.get(0);
                java.util.Iterator<String> fieldNames = firstRow.fieldNames();
                java.util.List<String> columns = new java.util.ArrayList<>();
                while (fieldNames.hasNext()) {
                    columns.add(fieldNames.next());
                }

                if (!columns.isEmpty()) {
                    // Crear tabla con las columnas
                    float[] columnWidths = new float[columns.size()];
                    java.util.Arrays.fill(columnWidths, 1f);
                    Table dataTable = new Table(UnitValue.createPercentArray(columnWidths))
                            .setWidth(UnitValue.createPercentValue(100));

                    // Header de la tabla
                    for (String col : columns) {
                        Cell headerCell = new Cell()
                                .add(new Paragraph(formatColumnName(col))
                                        .setFont(boldFont)
                                        .setFontSize(9))
                                .setBackgroundColor(SECONDARY_COLOR)
                                .setFontColor(ColorConstants.WHITE)
                                .setPadding(5)
                                .setTextAlignment(TextAlignment.CENTER);
                        dataTable.addHeaderCell(headerCell);
                    }

                    // Filas de datos
                    for (JsonNode row : tableData) {
                        for (String col : columns) {
                            String value = row.has(col) && !row.get(col).isNull() 
                                    ? row.get(col).asText() : "";
                            Cell dataCell = new Cell()
                                    .add(new Paragraph(value)
                                            .setFont(regularFont)
                                            .setFontSize(9))
                                    .setPadding(4)
                                    .setBorder(new SolidBorder(LIGHT_GRAY, 0.5f));
                            dataTable.addCell(dataCell);
                        }
                    }

                    containerCell.add(new Paragraph("Datos de la tabla (" + tableData.size() + " registros):")
                            .setFont(boldFont)
                            .setFontSize(10)
                            .setMarginBottom(5));
                    containerCell.add(dataTable);
                }
            } else {
                containerCell.add(new Paragraph("Tabla vacía - Sin registros")
                        .setFont(regularFont)
                        .setFontSize(10)
                        .setItalic());
            }
        } catch (Exception e) {
            log.warn("Error parsing table data: {}", e.getMessage());
            containerCell.add(new Paragraph("Respuesta: " + answer.getAnswerValue())
                    .setFont(regularFont)
                    .setFontSize(10));
        }

        return containerCell;
    }

    private Cell createAuditInfoCell(EgsiAnswer answer, PdfFont regularFont) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        StringBuilder auditInfo = new StringBuilder();
        auditInfo.append("Estado: ").append(formatStatus(answer.getStatus()));
        
        if (answer.getCreatedBy() != null && !answer.getCreatedBy().isBlank()) {
            auditInfo.append(" | Creado por: ").append(answer.getCreatedBy());
        }
        if (answer.getCreatedAt() != null) {
            auditInfo.append(" (").append(answer.getCreatedAt().format(formatter)).append(")");
        }
        if (answer.getUpdatedBy() != null && !answer.getUpdatedBy().isBlank() 
                && !answer.getUpdatedBy().equals(answer.getCreatedBy())) {
            auditInfo.append(" | Modificado por: ").append(answer.getUpdatedBy());
            if (answer.getUpdatedAt() != null) {
                auditInfo.append(" (").append(answer.getUpdatedAt().format(formatter)).append(")");
            }
        }

        return new Cell()
                .add(new Paragraph(auditInfo.toString())
                        .setFont(regularFont)
                        .setFontSize(8)
                        .setFontColor(DARK_GRAY)
                        .setItalic())
                .setPadding(5)
                .setBackgroundColor(new DeviceRgb(249, 250, 251))
                .setBorder(Border.NO_BORDER);
    }

    private String formatStatus(String status) {
        if (status == null) return "Pendiente";
        return switch (status.toUpperCase()) {
            case "PENDING" -> "Pendiente";
            case "IN_PROGRESS" -> "En Progreso";
            case "COMPLETED" -> "Completado";
            default -> status;
        };
    }

    private String formatColumnName(String columnName) {
        // Convertir camelCase o snake_case a texto legible
        String formatted = columnName
                .replaceAll("([a-z])([A-Z])", "$1 $2")
                .replaceAll("_", " ");
        return formatted.substring(0, 1).toUpperCase() + formatted.substring(1);
    }

    private String formatTextAnswer(String answer) {
        // Limpiar HTML si existe
        String cleaned = answer.replaceAll("<[^>]*>", "").trim();
        return cleaned.isEmpty() ? answer : cleaned;
    }

    private void addInfoRow(Table table, String label, String value, PdfFont boldFont, PdfFont regularFont) {
        Cell labelCell = new Cell()
                .add(new Paragraph(label).setFont(boldFont).setFontSize(10))
                .setBorder(Border.NO_BORDER)
                .setPadding(5);
        table.addCell(labelCell);

        Cell valueCell = new Cell()
                .add(new Paragraph(value != null ? value : "N/A").setFont(regularFont).setFontSize(10))
                .setBorder(Border.NO_BORDER)
                .setPadding(5);
        table.addCell(valueCell);
    }

    private void addFooter(Document document, PdfFont regularFont) {
        document.add(new Paragraph("")
                .setBorderTop(new SolidBorder(LIGHT_GRAY, 1))
                .setMarginTop(30));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String generationDate = java.time.LocalDateTime.now().format(formatter);

        Paragraph footer = new Paragraph("Documento generado automáticamente por SIEGSI el " + generationDate)
                .setFont(regularFont)
                .setFontSize(8)
                .setFontColor(DARK_GRAY)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(10);
        document.add(footer);

        Paragraph disclaimer = new Paragraph("Este documento es un comprobante oficial de la fase aprobada.")
                .setFont(regularFont)
                .setFontSize(8)
                .setFontColor(DARK_GRAY)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(disclaimer);
    }
}
