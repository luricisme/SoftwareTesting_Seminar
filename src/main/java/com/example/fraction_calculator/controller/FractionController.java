package com.example.fraction_calculator.controller;

import com.example.fraction_calculator.service.FractionService;
import com.example.fraction_calculator.dto.FractionRequest;
import com.example.fraction_calculator.dto.FractionResponse;
import com.example.fraction_calculator.dto.ValidationError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fraction")
@Tag(
    name = "Fraction Calculator",
    description = "API tính toán các phép toán với phân số (cộng, trừ, nhân, chia)"
)
public class FractionController {

    @Autowired
    private FractionService fractionService;

    @PostMapping("/calculate")
    @Operation(
        summary = "Tính toán hai phân số",
        description = "Phép cộng, trừ, nhân, chia. Kết quả rút gọn tối giản, dấu âm ở tử, mẫu dương"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Thành công",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FractionResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Validation error - trả về lỗi của từng field",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ValidationError.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Server error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<?> calculate(@RequestBody FractionRequest request) {
        try {
            FractionResponse response = fractionService.calculate(request);
            return ResponseEntity.ok(response);
        } catch (FractionService.ValidationException e) {
            ValidationError error = new ValidationError(e.getFieldErrors());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            ValidationError error = new ValidationError(java.util.Map.of("error", java.util.List.of(e.getMessage())));
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Lỗi: " + e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    @ApiResponse(responseCode = "200", description = "API running")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("API Fraction Calculator is running");
    }

    public static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
    }

    // API test dev
    @GetMapping("/hello-dev")
    @Operation(summary = "Test hello-dev endpoint")
    @ApiResponse(responseCode = "200", description = "Trả về thông điệp hello-dev")
    public ResponseEntity<String> helloDev() {
        return ResponseEntity.ok("Hello Dev! This is the dev environment.");
    }
}
