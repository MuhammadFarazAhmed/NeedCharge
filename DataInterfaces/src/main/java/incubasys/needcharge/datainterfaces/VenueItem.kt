package incubasys.needcharge.datainterfaces

import android.os.Parcelable
import android.text.TextUtils
import com.google.android.gms.maps.model.LatLng
import incubasys.needcharge.datainterfaces.models.Schedule
import kotlinx.android.parcel.Parcelize
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import java.util.*

@Parcelize
data class VenueItem(var id: Int, var name: String, var image: String?, var location: LatLng, var isOpen: Boolean, var todayTiming: String?, var timings: MutableList<BusinessTimings>, var type: Int, var distance: Double, var duration: Double) : Parcelable {

    fun setBusinessHours(businessHoursList: Array<Schedule>) {
        val timings = ArrayList<BusinessTimings>()

        val date = LocalDateTime.now()
        val time = date.toLocalTime()
        var intCurrentDay = date.dayOfWeek
        if (intCurrentDay == 7) {
            intCurrentDay = 0
        }
        for (j in businessHoursList.indices) {
            val intDay = businessHoursList[j].day
            // val intEndDay = null
            val localStartTime = businessHoursList[j].startTime
            val localEndTime = businessHoursList[j].endTime

            // converting day int to string
            val dayBuilder = StringBuilder()
            val day = intDayToString(intDay)
            //  val endDay = intDayToString(intEndDay)
            dayBuilder.append(day)
            /* if (!TextUtils.isEmpty(endDay)) {
                 dayBuilder.append(" - ")
                 dayBuilder.append(endDay)
             }*/

            // converting localDateTime int to string
            val timeBuilder = StringBuilder()
            val startTime = localTimeToString(localStartTime)
            val endTime = localTimeToString(localEndTime)
            timeBuilder.append(startTime)
            if (!TextUtils.isEmpty(endTime)) {
                timeBuilder.append(" - ")
                timeBuilder.append(endTime)
            }
            val businessTimings = BusinessTimings(dayBuilder.toString(), timeBuilder.toString())
            if (todayTiming == null) {
                if ((intCurrentDay == intDay) &&
                        time.isAfter(localStartTime.toLocalTime())
                        && time.isBefore(localEndTime.toLocalTime())) {
                    isOpen = true
                    todayTiming = "Until ${localEndTime.toLocalTime().toString("hh:mm a")}"
                } else if (intCurrentDay == intDay) {
                    isOpen = false
                    todayTiming = "Opens at ${localStartTime.toLocalTime().toString("hh:mm a")}"
                } else {
                    isOpen = false
                    val index = if (j + 1 < businessHoursList.size) {
                        j + 1
                    } else {
                        j - 1
                    }
                    if (index > 0 && index < businessHoursList.size) {
                        val nextOpeningDay = intDayToString(businessHoursList[index].day)
                        val nextOpeningTime = businessHoursList[index].startTime.toLocalTime().toString("hh:mm a")
                        todayTiming = "Opens $nextOpeningDay at $nextOpeningTime"
                    }
                }
            }
            timings.add(businessTimings)
        }
        this.timings = timings
    }

    private fun localTimeToString(localDateTime: DateTime?): String {
        if (localDateTime == null) return ""
        val time = localDateTime.toLocalTime()
        return time.toString("hh:mm aa")
    }

    private fun intDayToString(day: Int?): String {
        // "0 => sunday 1 => monday 2 => tuesday 3 => wednesday 4 => thursday 5 => friday 6 => saturday"
        if (day == null) {
            return ""
        }
        var d = ""
        when (day) {
            0 -> d = "Sun"
            1 -> d = "Mon"
            2 -> d = "Tue"
            3 -> d = "Wed"
            4 -> d = "Thurs"
            5 -> d = "Fri"
            6 -> d = "Sat"
        }
        return d
    }
}
