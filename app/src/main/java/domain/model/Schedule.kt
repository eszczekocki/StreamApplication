package domain.model

import java.time.LocalDateTime


data class Schedule(
    val date: LocalDateTime,
    var id: Long,
    val imageUrl: String,
    val subtitle: String,
    val title: String
)