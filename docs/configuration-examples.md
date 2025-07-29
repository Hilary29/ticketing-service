# Exemples de Configuration JSON pour GenericTicketEngine

## Configuration Cinéma

```json
{
  "pricingRules": {
    "modifiers": [
      {
        "type": "time_based",
        "rules": {
          "timeSlots": [
            {
              "name": "matinee",
              "startTime": "10:00",
              "endTime": "16:00",
              "operation": "multiply",
              "value": 0.7
            },
            {
              "name": "evening",
              "startTime": "18:00",
              "endTime": "23:59",
              "operation": "multiply",
              "value": 1.2
            }
          ]
        }
      },
      {
        "type": "day_based",
        "rules": {
          "dayRules": {
            "SATURDAY": {
              "operation": "multiply",
              "value": 1.2
            },
            "SUNDAY": {
              "operation": "multiply",
              "value": 1.2
            }
          }
        }
      },
      {
        "type": "quantity_based",
        "rules": {
          "thresholds": [
            {
              "minQuantity": 5,
              "maxQuantity": 10,
              "operation": "multiply",
              "value": 0.85
            },
            {
              "minQuantity": 11,
              "maxQuantity": 999,
              "operation": "multiply",
              "value": 0.75
            }
          ]
        }
      }
    ]
  },
  "validationRules": {
    "maxAdvanceBookingDays": 30,
    "requiredFields": ["seatNumber"]
  },
  "displayData": {
    "theater": "Cinéma Central",
    "screenType": "IMAX",
    "features": ["3D", "Dolby Atmos"]
  }
}
```

## Configuration Transport

```json
{
  "pricingRules": {
    "modifiers": [
      {
        "type": "time_based",
        "rules": {
          "timeSlots": [
            {
              "name": "peak_hours",
              "startTime": "07:00",
              "endTime": "09:00",
              "operation": "multiply",
              "value": 1.5
            },
            {
              "name": "peak_hours_evening",
              "startTime": "17:00",
              "endTime": "19:00",
              "operation": "multiply",
              "value": 1.5
            }
          ]
        }
      },
      {
        "type": "quantity_based",
        "rules": {
          "thresholds": [
            {
              "minQuantity": 10,
              "maxQuantity": 999,
              "operation": "multiply",
              "value": 0.9
            }
          ]
        }
      }
    ]
  },
  "validationRules": {
    "maxAdvanceBookingDays": 90
  },
  "displayData": {
    "transportMode": "Bus/Metro",
    "zones": "Toutes zones",
    "validity": "24h"
  }
}
```

## Configuration Événement

```json
{
  "pricingRules": {
    "modifiers": [
      {
        "type": "quantity_based",
        "rules": {
          "thresholds": [
            {
              "minQuantity": 4,
              "maxQuantity": 999,
              "operation": "multiply",
              "value": 0.9
            }
          ]
        }
      }
    ]
  },
  "validationRules": {
    "maxAdvanceBookingDays": 365,
    "minimumAge": 18
  },
  "displayData": {
    "venue": "Palais des Sports",
    "eventType": "Concert",
    "dresscode": "Casual"
  }
}
```

## Configuration Simple (Pas de règles spéciales)

```json
{
  "displayData": {
    "type": "Standard",
    "description": "Billet standard sans règles particulières"
  }
}
```

## Structure Générale

### PricingRules
- **time_based**: Modificateurs basés sur l'heure
- **day_based**: Modificateurs basés sur le jour de la semaine  
- **quantity_based**: Modificateurs basés sur la quantité

### ValidationRules
- **maxAdvanceBookingDays**: Nombre maximum de jours à l'avance
- **requiredFields**: Champs obligatoires
- **minimumAge**: Âge minimum requis

### DisplayData
Données d'affichage spécifiques au type de ticket

### Opérations Supportées
- **multiply**: Multiplier le prix
- **add**: Ajouter au prix
- **subtract**: Soustraire du prix
- **divide**: Diviser le prix