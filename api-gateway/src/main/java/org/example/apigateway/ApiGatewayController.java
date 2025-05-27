package org.example.apigateway;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@Tag(name = "API Gateway", description = "Отвечает за роутинг запросов к микросервисам")
public class ApiGatewayController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Operation(summary = "Загрузить файл", description = "Загружает файл в хранилище")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Файл успешно загружен"),
            @ApiResponse(responseCode = "500", description = "Не удалось загрузить файл")
    })
    @PostMapping(value = "/file/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            String targetUrl = "http://file-storing-service:8080/file/upload";
            ResponseEntity<String> response = restTemplate.postForEntity(targetUrl, requestEntity, String.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }

    @Operation(summary = "Проанализировать файл", description = "Исследует файл(работа с облаком слов не была реализована)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Анализ проведён успешно"),
            @ApiResponse(responseCode = "404", description = "Файл не найден"),
            @ApiResponse(responseCode = "500", description = "Не удалось проанализировать файл")
    })
    @GetMapping("/analysis/{id}")
    public ResponseEntity<String> getAnalysis(
            @Parameter(description = "ID файла", required = true) @PathVariable String id) {
        String targetUrl = "http://file-analysis-service:8080/analysis/" + id;
        return restTemplate.getForEntity(targetUrl, String.class);
    }

    @Operation(summary = "Скачать файл", description = "В ответе на запрос будет содержимое файла")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Анализ скачан успешно"),
            @ApiResponse(responseCode = "404", description = "Файл не найден"),
            @ApiResponse(responseCode = "500", description = "Не удалось скачать файл")
    })
    @GetMapping("/file/download/{id}")
    public ResponseEntity<String> downloadFile(
            @Parameter(description = "ID файла", required = true) @PathVariable String id) {
        String targetUrl = "http://file-storing-service:8080/file/download/" + id;
        ResponseEntity<String> response = restTemplate.getForEntity(targetUrl, String.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}