package com.neero.ticketing_service.engine;

import com.neero.ticketing_service.entity.Ticket;
import com.neero.ticketing_service.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Moteur générique simplifié pour la gestion des tickets
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GenericEngine {

    /**
     * Calcule le prix d'un ticket basé sur la configuration
     */
    public BigDecimal calculatePrice(BigDecimal basePrice, 
                                   Map<String, Object> ticketData,
                                   Map<String, Object> typeConfig) {
        
        Map<String, Object> pricingRules = (Map<String, Object>) 
            typeConfig.getOrDefault("pricing", Collections.emptyMap());
        
        BigDecimal finalPrice = basePrice;
        
        // Appliquer les modificateurs de prix
        List<Map<String, Object>> modifiers = (List<Map<String, Object>>) 
            pricingRules.getOrDefault("modifiers", Collections.emptyList());
            
        for (Map<String, Object> modifier : modifiers) {
            finalPrice = applyModifier(finalPrice, modifier, ticketData);
        }
        
        return finalPrice.setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Valide un ticket selon la configuration
     */
    public void validate(Ticket ticket, Map<String, Object> typeConfig) {
        Map<String, Object> validationRules = (Map<String, Object>) 
            typeConfig.getOrDefault("validation", Collections.emptyMap());
        
        List<String> errors = new ArrayList<>();
        
        // Valider les champs requis
        List<String> requiredFields = (List<String>) 
            validationRules.getOrDefault("requiredFields", Collections.emptyList());
            
        for (String field : requiredFields) {
            if (!ticket.getData().containsKey(field)) {
                errors.add("Missing required field: " + field);
            }
        }
        
        // Valider les contraintes personnalisées
        Map<String, Object> constraints = (Map<String, Object>) 
            validationRules.getOrDefault("constraints", Collections.emptyMap());
            
        for (Map.Entry<String, Object> constraint : constraints.entrySet()) {
            String field = constraint.getKey();
            Map<String, Object> rules = (Map<String, Object>) constraint.getValue();
            
            Object value = ticket.getData().get(field);
            if (value != null) {
                validateFieldConstraints(field, value, rules, errors);
            }
        }
        
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
    
    /**
     * Calcule la date de validité basée sur la configuration
     */
    public LocalDateTime calculateValidity(Map<String, Object> typeConfig) {
        Map<String, Object> validityConfig = (Map<String, Object>) 
            typeConfig.getOrDefault("validity", Collections.emptyMap());
            
        int days = (Integer) validityConfig.getOrDefault("daysFromNow", 30);
        return LocalDateTime.now().plusDays(days);
    }
    
    private BigDecimal applyModifier(BigDecimal price, Map<String, Object> modifier, Map<String, Object> ticketData) {
        String type = (String) modifier.get("type");
        
        switch (type) {
            case "percentage":
                double percentage = (Double) modifier.getOrDefault("value", 0.0);
                return price.multiply(BigDecimal.valueOf(1 + percentage / 100));
                
            case "fixed":
                double fixed = (Double) modifier.getOrDefault("value", 0.0);
                return price.add(BigDecimal.valueOf(fixed));
                
            case "conditional":
                String field = (String) modifier.get("field");
                Object expectedValue = modifier.get("expectedValue");
                Object actualValue = ticketData.get(field);
                
                if (Objects.equals(expectedValue, actualValue)) {
                    double value = (Double) modifier.getOrDefault("value", 0.0);
                    String operation = (String) modifier.getOrDefault("operation", "add");
                    
                    return switch (operation) {
                        case "add" -> price.add(BigDecimal.valueOf(value));
                        case "multiply" -> price.multiply(BigDecimal.valueOf(value));
                        default -> price;
                    };
                }
                break;
                
            default:
                log.warn("Unknown modifier type: {}", type);
        }
        
        return price;
    }
    
    private void validateFieldConstraints(String field, Object value, Map<String, Object> rules, List<String> errors) {
        // Validation de type
        String expectedType = (String) rules.get("type");
        if (expectedType != null && !isValidType(value, expectedType)) {
            errors.add(field + " must be of type " + expectedType);
        }
        
        // Validation de plage (pour les nombres)
        if (value instanceof Number number) {
            Number min = (Number) rules.get("min");
            Number max = (Number) rules.get("max");
            
            if (min != null && number.doubleValue() < min.doubleValue()) {
                errors.add(field + " must be at least " + min);
            }
            if (max != null && number.doubleValue() > max.doubleValue()) {
                errors.add(field + " must be at most " + max);
            }
        }
        
        // Validation de longueur (pour les chaînes)
        if (value instanceof String str) {
            Integer minLength = (Integer) rules.get("minLength");
            Integer maxLength = (Integer) rules.get("maxLength");
            
            if (minLength != null && str.length() < minLength) {
                errors.add(field + " must be at least " + minLength + " characters long");
            }
            if (maxLength != null && str.length() > maxLength) {
                errors.add(field + " must be at most " + maxLength + " characters long");
            }
        }
    }
    
    private boolean isValidType(Object value, String expectedType) {
        return switch (expectedType.toLowerCase()) {
            case "string" -> value instanceof String;
            case "number" -> value instanceof Number;
            case "boolean" -> value instanceof Boolean;
            case "array" -> value instanceof List;
            case "object" -> value instanceof Map;
            default -> true;
        };
    }
}