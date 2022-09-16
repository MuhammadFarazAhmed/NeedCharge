package incubasys.needcharge.repositories.remote.client

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import org.joda.time.LocalDate
import org.joda.time.format.ISODateTimeFormat

import java.io.IOException

/**
 * Gson TypeAdapter for Joda LocalDate type
 */
class LocalDateTypeAdapter : TypeAdapter<LocalDate>() {

    private val formatter = ISODateTimeFormat.date()

    @Throws(IOException::class)
    override fun write(out: JsonWriter, date: LocalDate?) {
        if (date == null) {
            out.nullValue()
        } else {
            out.value(formatter.print(date))
        }
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): LocalDate? {
        when (`in`.peek()) {
            JsonToken.NULL -> {
                `in`.nextNull()
                return null
            }
            else -> {
                val date = `in`.nextString()
                return formatter.parseLocalDate(date)
            }
        }
    }
}
