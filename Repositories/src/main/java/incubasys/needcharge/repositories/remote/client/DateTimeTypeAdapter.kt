package incubasys.needcharge.repositories.remote.client

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.ISODateTimeFormat

import java.io.IOException

/**
 * Gson TypeAdapter for Joda DateTime type
 */
class DateTimeTypeAdapter : TypeAdapter<DateTime>() {

    //private final DateTimeFormatter parseFormatter = ISODateTimeFormat.dateOptionalTimeParser();
    private val printFormatter = ISODateTimeFormat.dateTime()

    @Throws(IOException::class)
    override fun write(out: JsonWriter, date: DateTime?) {
        if (date == null) {
            out.nullValue()
        } else {
            out.value(printFormatter.print(date))
        }
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): DateTime? {
        when (`in`.peek()) {
            JsonToken.NULL -> {
                `in`.nextNull()
                return null
            }
            else -> {
                val date = `in`.nextString()
                return printFormatter.parseDateTime(date).toDateTime(DateTimeZone.getDefault())
            }
        }
    }
}
