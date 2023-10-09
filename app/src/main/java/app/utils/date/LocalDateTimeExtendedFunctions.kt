package app.utils.date

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object LocalDateTimeExtendedFunctions {
    fun LocalDateTime.parseWithTodayAndYesterdayNaming(): ParsedDate? {
        var dateFromNow = when {
            LocalDate.now().compareTo(this.toLocalDate()) == 0 -> {
                DateFromNow.TODAY
            }
            LocalDate.now().minusDays(1)
                .compareTo(this.toLocalDate()) == 0 -> {
                DateFromNow.YESTERDAY
            }
            LocalDate.now().plusDays(1)
                .compareTo(this.toLocalDate()) == 0 -> {
                DateFromNow.TOMORROW
            }
            else -> DateFromNow.NONE
        }
        return ParsedDate(
            this.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
            this.format(DateTimeFormatter.ofPattern("HH:mm")),
            dateFromNow
        )
    }
}