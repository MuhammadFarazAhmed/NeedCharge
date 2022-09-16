package incubasys.needcharge.repositories.remote.client

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import org.joda.time.LocalDateTime
import org.joda.time.format.ISODateTimeFormat

import java.io.IOException

/**
 * Gson TypeAdapter for Joda LocalTime type
 */
class LocalDateTimeTypeAdapter : TypeAdapter<LocalDateTime>() {

    private val formatter = ISODateTimeFormat.dateTime()

    @Throws(IOException::class)
    override fun write(out: JsonWriter, time: LocalDateTime?) {
        if (time == null) {
            out.nullValue()
        } else {
            out.value(formatter.print(time))
        }
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): LocalDateTime? {
        when (`in`.peek()) {
            JsonToken.NULL -> {
                `in`.nextNull()
                return null
            }
            else -> {
                val time = `in`.nextString()
                return formatter.parseLocalDateTime(time)
            }
        }
    }
}
