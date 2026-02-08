package com.wowo.wowo.contexts.transaction.application.enricher

import com.wowo.wowo.contexts.transaction.application.dto.TransactionDTO
import com.wowo.wowo.contexts.transaction.domain.valueobject.TransactionType
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class TransactionEnrichmentService {
    fun buildContext(dtos: List<TransactionDTO>): TransactionEnrichmentContext {
        if (dtos.isEmpty()) {
            return TransactionEnrichmentContext.empty()
        }

        val normalizedDescriptions = mutableMapOf<String, String?>()
        val categories = mutableMapOf<String, String?>()
        val merchantNames = mutableMapOf<String, String?>()
        val merchantCategories = mutableMapOf<String, String?>()
        val geoCountries = mutableMapOf<String, String?>()
        val geoCities = mutableMapOf<String, String?>()
        val riskScores = mutableMapOf<String, Int?>()
        val riskLevels = mutableMapOf<String, String?>()
        val tags = mutableMapOf<String, List<String>>()

        dtos.forEach { dto ->
            val normalizedDescription = normalizeDescription(dto.description)
            val merchantName = inferMerchantName(normalizedDescription, dto.reference)
            val merchantCategory = inferMerchantCategory(normalizedDescription, dto.reference)
            val category = inferTransactionCategory(dto.type, merchantCategory)
            val geo = inferGeo(dto.reference)
            val risk = assessRisk(dto.amount, dto.currency, merchantCategory)
            val tagList = buildTags(dto.type, merchantCategory, risk.level)

            normalizedDescriptions[dto.id] = normalizedDescription
            categories[dto.id] = category
            merchantNames[dto.id] = merchantName
            merchantCategories[dto.id] = merchantCategory
            geoCountries[dto.id] = geo.country
            geoCities[dto.id] = geo.city
            riskScores[dto.id] = risk.score
            riskLevels[dto.id] = risk.level
            tags[dto.id] = tagList
        }

        return TransactionEnrichmentContext(
            normalizedDescriptions = normalizedDescriptions,
            categories = categories,
            merchantNames = merchantNames,
            merchantCategories = merchantCategories,
            geoCountries = geoCountries,
            geoCities = geoCities,
            riskScores = riskScores,
            riskLevels = riskLevels,
            tags = tags
        )
    }

    private fun normalizeDescription(description: String?): String? {
        if (description.isNullOrBlank()) {
            return null
        }
        return description.trim().replace(Regex("\\s+"), " ").uppercase()
    }

    private fun inferMerchantName(normalizedDescription: String?, reference: String?): String? {
        val source = listOfNotNull(reference, normalizedDescription).joinToString(" ")
        if (source.isBlank()) {
            return null
        }

        val patterns = listOf(
            Regex("MCC\\d+\\s+([A-Z0-9 .&-]{3,})"),
            Regex("PAYMENT\\s+TO\\s+([A-Z0-9 .&-]{3,})"),
            Regex("POS\\s+([A-Z0-9 .&-]{3,})"),
            Regex("MERCHANT\\s+([A-Z0-9 .&-]{3,})")
        )

        for (pattern in patterns) {
            val match = pattern.find(source)
            if (match != null) {
                return match.groupValues[1].trim().replace(Regex("\\s+"), " ")
            }
        }

        return null
    }

    private fun inferMerchantCategory(normalizedDescription: String?, reference: String?): String? {
        val source = listOfNotNull(reference, normalizedDescription).joinToString(" ")
        if (source.isBlank()) {
            return null
        }

        return when {
            source.contains("GROCERY") || source.contains("SUPERMARKET") -> "GROCERY"
            source.contains("RESTAURANT") || source.contains("CAFE") || source.contains("FOOD") -> "FOOD_AND_BEVERAGE"
            source.contains("RIDE") || source.contains("TAXI") || source.contains("TRANSPORT") -> "TRANSPORT"
            source.contains("HOTEL") || source.contains("TRAVEL") || source.contains("AIR") -> "TRAVEL"
            source.contains("PHARMACY") || source.contains("MEDICAL") -> "HEALTH"
            source.contains("UTILITY") || source.contains("ELECTRIC") || source.contains("WATER") -> "UTILITIES"
            source.contains("TOPUP") || source.contains("MOBILE") -> "TELECOM"
            source.contains("ONLINE") || source.contains("ECOM") -> "ECOMMERCE"
            else -> null
        }
    }

    private fun inferTransactionCategory(type: String, merchantCategory: String?): String? {
        val typeEnum = runCatching { TransactionType.valueOf(type) }.getOrNull()
        return when (typeEnum) {
            TransactionType.PAYMENT -> merchantCategory ?: "PAYMENT"
            TransactionType.REFUND -> "REFUND"
            TransactionType.TRANSFER -> "TRANSFER"
            TransactionType.CREDIT -> "CREDIT"
            TransactionType.DEBIT -> "DEBIT"
            null -> merchantCategory
        }
    }

    private fun inferGeo(reference: String?): GeoInfo {
        if (reference.isNullOrBlank()) {
            return GeoInfo(null, null)
        }

        val countryMatch = Regex("COUNTRY:([A-Z]{2})").find(reference)
        val cityMatch = Regex("CITY:([A-Z0-9 .-]{2,})").find(reference)

        val country = countryMatch?.groupValues?.get(1)
        val city = cityMatch?.groupValues?.get(1)?.trim()?.replace(Regex("\\s+"), " ")

        return GeoInfo(country, city)
    }

    private fun assessRisk(amount: BigDecimal, currency: String, merchantCategory: String?): RiskInfo {
        val normalizedAmount = amount.abs()
        var score = 10

        if (normalizedAmount >= BigDecimal("10000000")) {
            score += 50
        } else if (normalizedAmount >= BigDecimal("5000000")) {
            score += 40
        } else if (normalizedAmount >= BigDecimal("1000000")) {
            score += 30
        } else if (normalizedAmount >= BigDecimal("100000")) {
            score += 20
        } else if (normalizedAmount >= BigDecimal("10000")) {
            score += 10
        }

        if (currency != "VND") {
            score += 10
        }

        if (merchantCategory == "ECOMMERCE") {
            score += 10
        }

        val level = when {
            score >= 70 -> "HIGH"
            score >= 40 -> "MEDIUM"
            else -> "LOW"
        }

        return RiskInfo(score, level)
    }

    private fun buildTags(type: String, merchantCategory: String?, riskLevel: String): List<String> {
        val tags = mutableListOf<String>()
        tags.add("TYPE_${type}")
        if (!merchantCategory.isNullOrBlank()) {
            tags.add("MCC_${merchantCategory}")
        }
        tags.add("RISK_${riskLevel}")
        return tags
    }

    private data class GeoInfo(
        val country: String?,
        val city: String?
    )

    private data class RiskInfo(
        val score: Int,
        val level: String
    )
}
