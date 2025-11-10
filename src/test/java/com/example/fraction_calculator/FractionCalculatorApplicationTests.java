package com.example.fraction_calculator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.fraction_calculator.dto.FractionRequest;
import com.example.fraction_calculator.dto.FractionResponse;
import com.example.fraction_calculator.model.Fraction;
import com.example.fraction_calculator.service.FractionService;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FractionCalculatorApplicationTests {

	private final FractionService fractionService = new FractionService();
	private final Fraction fraction = new Fraction();

	// TEST: --CLass Fraction-- Khởi tạo phân số với mẫu số bằng 0
	@ParameterizedTest
    @CsvSource({
        "1, 0",
        "5, 0",
        "-3, 0"
    })
	void InvalidFractionWithZeroDenom(int num, int denom) {
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        	() -> new Fraction(num, denom));
	}

	// TEST --METHOD Fraction.gcd-- Rút gọn phân số
	@ParameterizedTest
	@CsvSource({
		"1, 2, 1, 2",
		"4, 2, 2, 1",
		"4, 12, 1, 3"
	})
	void testGCD(int num, int denom, int gcdNum, int gcDenom) {
		Fraction frac = new Fraction(num, denom);
		assertEquals(gcdNum, frac.getNumerator());
		assertEquals(gcDenom, frac.getDenominator());
	}

	// TEST: --METHOD Fraction.Divide-- Chia cho số 0 
	@ParameterizedTest
	@CsvSource({
		"0, 1"
	})
	void testDivideByZero(int num, int denom) {
		Fraction frac = new Fraction(num, denom);
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        	() -> fraction.divide(frac));
	}

	// TEST: --METHOD add, subtract, multiply, divide-- Kết quả tính toán (bao gồm: "+", "add", "-", "subtract", "*", "multiply", "/", "divide")
	@ParameterizedTest
	@CsvSource({
		"+, 1, 2, 1, 3, 5, 6, 5/6",
    	"add, 1, 2, 1, 3, 5, 6, 5/6",
		"-, 1, 2, 1, 3, 1, 6, 1/6",
		"subtract, 1, 2, 1, 3, 1, 6, 1/6",
		"*, 1, 2, 1, 3, 1, 6, 1/6",
		"multiply, 1, 2, 1, 3, 1, 6, 1/6",
		"/, 1, 2, 1, 3, 3, 2, 3/2",
		"divide, 1, 2, 1, 3, 3, 2, 3/2",
	})
	void testCalculate(String operator, int num1, int denom1, int num2, int denom2, int expNum, int expDenom, String expFraction) {
		FractionRequest req = new FractionRequest();
		req.setNum1(String.valueOf(num1));
		req.setDenom1(String.valueOf(denom1));
		req.setNum2(String.valueOf(num2));
		req.setDenom2(String.valueOf(denom2));
		req.setOperation(operator);

		FractionResponse res = fractionService.calculate(req);
		assertEquals(expNum, res.getNumerator());
		assertEquals(expDenom, res.getDenominator());
		assertEquals(expFraction, res.getFraction());
		double expected = (double) res.getNumerator() / res.getDenominator();
		assertEquals(expected, res.getDecimal(), 1e-12);
	}

	// TEST: --METHOD FractionService.calculate-- Mẫu số bằng 0 (bao gồm: denom1, denom2); Phép toán không hợp lệ (chỉ làm tượng trưng, vì đã có method validateOperationInput)
	@ParameterizedTest
	@CsvSource({
		"+, 1, 0, 1, 3",
    	"+, 1, 2, 1, 0",
		"?@, 1, 2, 1, 3"
	})
	void testDenomZero(String operator, int num1, int denom1, int num2, int denom2) {
		FractionRequest req = new FractionRequest();
		req.setNum1(String.valueOf(num1));
		req.setDenom1(String.valueOf(denom1));
		req.setNum2(String.valueOf(num2));
		req.setDenom2(String.valueOf(denom2));
		req.setOperation(operator);

		FractionService.ValidationException ex = assertThrows(FractionService.ValidationException.class,
			() -> fractionService.calculate(req));
	}	

	// TEST: --METHOD FractionService.calculate-- Chứa giá trị null
	@ParameterizedTest
	@CsvSource({
		"null, null, null, null, null"
	})
	void testAllNull(String num1, String denom1, String num2, String denom2, String operator) {
		FractionRequest req = new FractionRequest();
		req.setNum1(num1.equals("null") ? null : num1);
		req.setDenom1(denom1.equals("null") ? null : denom1);
		req.setNum2(num2.equals("null") ? null : num2);
		req.setDenom2(denom2.equals("null") ? null : denom2);
		req.setOperation(operator.equals("null") ? null : operator);

		FractionService.ValidationException ex = assertThrows(FractionService.ValidationException.class,
			() -> fractionService.calculate(req));
	}

	// TEST: --METHOD FractionService.validateNumericInput-- Nhận giá trị null, empty, "   ", số thực, chữ, kí tự đặc biệt
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {"   ", "3.14", "abc", "?@"})
	void testValidateNumericInput(String value) {
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
			() -> fractionService.validateNumericInput(value, "field"));
	}

	// TEST: --METHOD FractionService.validateOperationInput-- Phép toán không hợp lệ (bao gồm: null, empty, "   ", số, kí tự đặc biệt, chữ)
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {"   ", "123", "?@", "abc"})
	void testInvalidOperation(String operator) {
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
			() -> fractionService.validateOperationInput(operator));
	}

	// TEST: --METHOD FractionService.parseToLong-- Nhận giá trị null, empty, "   ", số thực, chữ, kí tự đặc biệt
	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = {"   ", "3.14", "abc", "?@"})
	void testParseToLong(String value) {
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
			() -> fractionService.parseToLong(value, "field"));
	}

	// TEST: --METHOD FractionService.parseLong-- Nhận giá trị null
	@ParameterizedTest
	@ValueSource(strings = {"null"})
	void testInvalidateNumericInput(String value) {
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
			() -> fractionService.parseLong(value.equals("null") ? null : value, "field"));
	}

	// TEST: --METHOD FractionService.parseOperation-- Phép toán null, empty
	@ParameterizedTest
	@ValueSource(strings = {"null", ""})
	void testInvalidateOperation(String operator) {
		IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
			() -> fractionService.parseOperation(operator.equals("null") ? null : operator));
	}
}
