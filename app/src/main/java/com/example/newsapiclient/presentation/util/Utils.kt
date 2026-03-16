package com.example.newsapiclient.presentation.util

import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.ExperimentalTime
import java.time.Instant
import java.time.ZoneOffset

object Utils {

    @OptIn(ExperimentalTime::class)
    fun timeAgo(publishedAt: String?): String? {
        return try {
            val instant = Instant.parse(publishedAt)
            val now = Instant.now()

            val duration = Duration.between(instant, now)
            val seconds = duration.seconds

            when {
                seconds < 60 -> "$seconds sec ago"
                seconds < 3600 -> "${seconds / 60} min ago"
                seconds < 86400 -> "${seconds / 3600} hr ago"
                seconds < 604800 -> { // less than 7 days
                    val days = seconds / 86400
                    "$days day${if (days > 1) "s" else ""} ago"
                }
                seconds < 2419200 -> { // less than 4 weeks
                    val weeks = seconds / 604800
                    "$weeks week${if (weeks > 1) "s" else ""} ago"
                }
                else -> {
                    val date = ZonedDateTime.ofInstant(instant, ZoneOffset.systemDefault())
                    val months = java.time.temporal.ChronoUnit.MONTHS.between(
                        date.withDayOfMonth(1),
                        ZonedDateTime.now(ZoneOffset.systemDefault()).withDayOfMonth(1)
                    )
                    if (months < 12) {
                        "$months month${if (months > 1) "s" else ""} ago"
                    } else {
                        // fallback to full date
                        date.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
                    }
                }
            }
        } catch (e: Exception) {
            // fallback in case of parsing error
            e.printStackTrace()
            publishedAt
        }
    }

    fun getCategoryList(): List<String> {
        return listOf(
            "Top Headlines",
            "Business",
            "Entertainment",
            "General",
            "Health",
            "Science",
            "Sport",
            "Technology"
        )
    }
}