package com.example.fraction_calculator.dto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    description = "Response body cho phép tính toán phân số - kết quả luôn được rút gọn tối giản",
    example = "{\"numerator\": 5, \"denominator\": 6, \"fraction\": \"5/6\", \"decimal\": 0.8333333333333334}"
)
public class FractionResponse {
    @Schema(
        description = "Tử số của kết quả (có thể âm, dấu âm luôn ở tử)",
        example = "5",
        type = "integer"
    )
    private Long numerator;

    @Schema(
        description = "Mẫu số của kết quả (luôn dương)",
        example = "6",
        type = "integer",
        minimum = "1"
    )
    private Long denominator;

    @Schema(
        description = "Phân số dưới dạng chuỗi tối giản. Nếu chia hết thì chỉ hiển thị tử (không có /1)",
        example = "5/6",
        type = "string"
    )
    private String fraction;

    @Schema(
        description = "Giá trị thập phân của phân số",
        example = "0.8333333333333334",
        type = "number"
    )
    private Double decimal;

    public FractionResponse(Long numerator, Long denominator, String fraction, Double decimal) {
        this.numerator = numerator;
        this.denominator = denominator;
        this.fraction = fraction;
        this.decimal = decimal;
    }

    public Long getNumerator() { return numerator; }
    public Long getDenominator() { return denominator; }
    public String getFraction() { return fraction; }
    public Double getDecimal() { return decimal; }
}
