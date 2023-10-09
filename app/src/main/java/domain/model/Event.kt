package domain.model

import java.time.LocalDateTime


data class Event(
    val date: LocalDateTime,
    var id: Long,
    val imageUrl: String,
    val subtitle: String,
    val title: String,
    val videoUrl: String
)