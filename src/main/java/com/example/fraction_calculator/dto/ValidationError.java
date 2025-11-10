package com.example.fraction_calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;

@Schema(description = "Response khi có lỗi validation")
public class ValidationError {
    @Schema(
        description = "Map của các field và danh sách lỗi tương ứng",
        example = "{\"num1\": [\"Tử số thứ nhất không được null\"], \"denom1\": [\"Mẫu số thứ nhất không được bằng 0\"]}"
    )
    private Map<String, List<String>> errors;

    public ValidationError(Map<String, List<String>> errors) {
        this.errors = errors;
    }

    public Map<String, List<String>> getErrors() { return errors; }
    public void setErrors(Map<String, List<String>> errors) { this.errors = errors; }
}
