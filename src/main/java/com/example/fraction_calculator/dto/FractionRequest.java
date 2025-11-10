package com.example.fraction_calculator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    description = "Request body cho phép tính toán phân số",
    example = "{\"num1\": 1, \"denom1\": 2, \"num2\": 1, \"denom2\": 3, \"operation\": \"add\"}"
)
public class FractionRequest {
    @JsonProperty("num1")
    @Schema(
        description = "Tử số của phân số thứ nhất - phải là số nguyên",
        example = "1",
        type = "integer"
    )
    private Object num1;

    @JsonProperty("denom1")
    @Schema(
        description = "Mẫu số của phân số thứ nhất - phải là số nguyên và khác 0",
        example = "2",
        type = "integer"
    )
    private Object denom1;

    @JsonProperty("num2")
    @Schema(
        description = "Tử số của phân số thứ hai - phải là số nguyên",
        example = "1",
        type = "integer"
    )
    private Object num2;

    @JsonProperty("denom2")
    @Schema(
        description = "Mẫu số của phân số thứ hai - phải là số nguyên và khác 0",
        example = "3",
        type = "integer"
    )
    private Object denom2;

    @JsonProperty("operation")
    @Schema(
        description = "Phép toán cần thực hiện. Hỗ trợ: add (+), subtract (-), multiply (*), divide (/)",
        example = "add",
        type = "string",
        allowableValues = {"add", "+", "subtract", "-", "multiply", "*", "divide", "/"}
    )
    private Object operation;

    public Object getNum1() { return num1; }
    public void setNum1(Object num1) { this.num1 = num1; }

    public Object getDenom1() { return denom1; }
    public void setDenom1(Object denom1) { this.denom1 = denom1; }

    public Object getNum2() { return num2; }
    public void setNum2(Object num2) { this.num2 = num2; }

    public Object getDenom2() { return denom2; }
    public void setDenom2(Object denom2) { this.denom2 = denom2; }

    public Object getOperation() { return operation; }
    public void setOperation(Object operation) { this.operation = operation; }
}
