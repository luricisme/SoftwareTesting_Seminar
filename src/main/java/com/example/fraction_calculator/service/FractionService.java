package com.example.fraction_calculator.service;

import com.example.fraction_calculator.model.Fraction;
import com.example.fraction_calculator.dto.FractionRequest;
import com.example.fraction_calculator.dto.FractionResponse;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FractionService {

    public FractionResponse calculate(FractionRequest request) {
        Map<String, List<String>> fieldErrors = validateRequest(request);
        
        if (!fieldErrors.isEmpty()) {
            throw new ValidationException(fieldErrors);
        }

        long num1 = parseLong(request.getNum1(), "num1");
        long denom1 = parseLong(request.getDenom1(), "denom1");
        long num2 = parseLong(request.getNum2(), "num2");
        long denom2 = parseLong(request.getDenom2(), "denom2");

        Fraction frac1 = new Fraction(num1, denom1);
        Fraction frac2 = new Fraction(num2, denom2);

        Fraction result;
        String op = parseOperation(request.getOperation());

        switch (op) {
            case "add":
            case "+":
                result = frac1.add(frac2);
                break;
            case "subtract":
            case "-":
                result = frac1.subtract(frac2);
                break;
            case "multiply":
            case "*":
                result = frac1.multiply(frac2);
                break;
            case "divide":
            case "/":
                result = frac1.divide(frac2);
                break;
            default:
                throw new IllegalArgumentException("Phép toán không hợp lệ: " + request.getOperation() + 
                    ". Hỗ trợ: add, +, subtract, -, multiply, *, divide, /");
        }

        return new FractionResponse(
            result.getNumerator(),
            result.getDenominator(),
            result.toString(),
            result.toDecimal()
        );
    }

    public Map<String, List<String>> validateRequest(FractionRequest request) {
        Map<String, List<String>> fieldErrors = new HashMap<>();

        // Validate num1
        List<String> num1Errors = new ArrayList<>();
        if (request.getNum1() == null) {
            num1Errors.add("Tử số thứ nhất không được null");
        } else {
            try {
                validateNumericInput(request.getNum1(), "num1");
            } catch (IllegalArgumentException e) {
                num1Errors.add(e.getMessage());
            }
        }
        if (!num1Errors.isEmpty()) {
            fieldErrors.put("num1", num1Errors);
        }

        // Validate denom1
        List<String> denom1Errors = new ArrayList<>();
        if (request.getDenom1() == null) {
            denom1Errors.add("Mẫu số thứ nhất không được null");
        } else {
            try {
                long denom1 = parseToLong(request.getDenom1(), "denom1");
                if (denom1 == 0) {
                    denom1Errors.add("Mẫu số thứ nhất không được bằng 0");
                }
            } catch (IllegalArgumentException e) {
                denom1Errors.add(e.getMessage());
            }
        }
        if (!denom1Errors.isEmpty()) {
            fieldErrors.put("denom1", denom1Errors);
        }

        // Validate num2
        List<String> num2Errors = new ArrayList<>();
        if (request.getNum2() == null) {
            num2Errors.add("Tử số thứ hai không được null");
        } else {
            try {
                validateNumericInput(request.getNum2(), "num2");
            } catch (IllegalArgumentException e) {
                num2Errors.add(e.getMessage());
            }
        }
        if (!num2Errors.isEmpty()) {
            fieldErrors.put("num2", num2Errors);
        }

        // Validate denom2
        List<String> denom2Errors = new ArrayList<>();
        if (request.getDenom2() == null) {
            denom2Errors.add("Mẫu số thứ hai không được null");
        } else {
            try {
                long denom2 = parseToLong(request.getDenom2(), "denom2");
                if (denom2 == 0) {
                    denom2Errors.add("Mẫu số thứ hai không được bằng 0");
                }
            } catch (IllegalArgumentException e) {
                denom2Errors.add(e.getMessage());
            }
        }
        if (!denom2Errors.isEmpty()) {
            fieldErrors.put("denom2", denom2Errors);
        }

        // Validate operation
        List<String> operationErrors = new ArrayList<>();
        if (request.getOperation() == null) {
            operationErrors.add("Phép toán không được null");
        } else {
            try {
                validateOperationInput(request.getOperation());
            } catch (IllegalArgumentException e) {
                operationErrors.add(e.getMessage());
            }
        }
        if (!operationErrors.isEmpty()) {
            fieldErrors.put("operation", operationErrors);
        }

        return fieldErrors;
    }

    public void validateNumericInput(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " không được null");
        }
        if (value instanceof Number) {
            return;
        }
        if (value instanceof String) {
            String str = ((String) value).trim();
            if (str.isEmpty()) {
                throw new IllegalArgumentException("Không được để trống");
            }
            if (!str.matches("-?\\d+")) {
                throw new IllegalArgumentException("Phải là số nguyên, nhận được: " + value);
            }
            return;
        }
        throw new IllegalArgumentException("Phải là số hoặc chuỗi số, nhận được kiểu: " + value.getClass().getSimpleName());
    }

    public void validateOperationInput(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Không được để trống");
        }
        String op;
        if (value instanceof String) {
            op = ((String) value).trim();
        } else {
            op = value.toString().trim();
        }

        if (op.isEmpty()) {
            throw new IllegalArgumentException("Không được để trống");
        }

        if (!op.matches("[a-zA-Z+\\-*/]+")) {
            throw new IllegalArgumentException("Chứa ký tự không hợp lệ: " + value);
        }

        String opLower = op.toLowerCase();
        if (!opLower.equals("add") && !opLower.equals("subtract") && 
            !opLower.equals("multiply") && !opLower.equals("divide") &&
            !op.equals("+") && !op.equals("-") && !op.equals("*") && !op.equals("/")) {
            throw new IllegalArgumentException("Không hợp lệ: " + value + 
                ". Hỗ trợ: add, +, subtract, -, multiply, *, divide, /");
        }
    }

    public long parseToLong(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " không được null");
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof String) {
            String str = ((String) value).trim();
            if (str.isEmpty()) {
                throw new IllegalArgumentException("Không được để trống");
            }
            if (!str.matches("-?\\d+")) {
                throw new IllegalArgumentException("Phải là số nguyên, nhận được: " + value);
            }
            return Long.parseLong(str);
        }
        throw new IllegalArgumentException("Phải là số hoặc chuỗi số, nhận được kiểu: " + value.getClass().getSimpleName());
    }

    public long parseLong(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " không được null");
        }
        return parseToLong(value, fieldName);
    }

    public String parseOperation(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Phép toán không được null");
        }

        String op;
        if (value instanceof String) {
            op = ((String) value).trim();
        } else {
            op = value.toString().trim();
        }

        if (op.isEmpty()) {
            throw new IllegalArgumentException("Phép toán không được để trống");
        }

        return op.toLowerCase();
    }

    public static class ValidationException extends RuntimeException {
        private final Map<String, List<String>> fieldErrors;

        public ValidationException(Map<String, List<String>> fieldErrors) {
            super("Validation failed");
            this.fieldErrors = fieldErrors;
        }

        public Map<String, List<String>> getFieldErrors() {
            return fieldErrors;
        }
    }
}
