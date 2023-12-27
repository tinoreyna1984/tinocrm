package com.tinexlab.tinocrm.controller;

import com.tinexlab.tinocrm.service.ExportXLSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/export")
public class ExportController {

    @Autowired
    private ExportXLSService exportXLSService;

    @GetMapping("/xls/{entity}")
    public ResponseEntity<?> exportXLSData(@PathVariable String entity) throws IOException {
        ByteArrayInputStream stream = exportXLSService.exportData(entity);

        // Convertir el InputStream a byte[]
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int reads;
        byte[] buffer = new byte[1024];
        while ((reads = stream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, reads);
        }
        byteArrayOutputStream.flush();

        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + entity + "-" + formattedDate + ".xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(byteArrayOutputStream.toByteArray());
    }

}
