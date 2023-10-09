package domain.model

import android.text.format.DateUtils
import android.util.Log
import java.text.SimpleDateFormat
import java.util.Date


data class Event(
    val date: Date,
    val id: Long,
    val imageUrl: String,
    val subtitle: String,
    val title: String,
    val videoUrl: String
) {
    fun getTime(): String? {
        try {
            var day =  when {
                DateUtils.isToday(date.time) -> {
                    "today"
                }
                DateUtils.isToday(date.time + DateUtils.DAY_IN_MILLIS) -> {
                    "yesterday"
                }
                else -> SimpleDateFormat("dd.MM.yyyy").format(date)
            }
            return "$day ${SimpleDateFormat("HH:mm").format(date)}"
        }catch (ex:Exception){
            ex.message?.let { Log.d("parsing date exception", "error") }
        }
        return ""
    }
}