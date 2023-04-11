package com.example.sportbooking

import android.os.Parcel
import android.os.Parcelable
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*

class DateValidator(var ServiceWeekdays:String) : CalendarConstraints.DateValidator{
    private val today: Long = MaterialDatePicker.todayInUtcMilliseconds()
    constructor(parcel: Parcel) : this("") {}

    override fun writeToParcel(parcel: Parcel, flags: Int) {}

    override fun isValid(date: Long): Boolean {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = date

        when(calendar.get(Calendar.DAY_OF_WEEK)){
            Calendar.MONDAY -> if(!ServiceWeekdays.contains("Mon",true)) return false
            Calendar.TUESDAY -> if(!ServiceWeekdays.contains("Tue",true)) return false
            Calendar.WEDNESDAY -> if(!ServiceWeekdays.contains("Wed",true)) return false
            Calendar.THURSDAY -> if(!ServiceWeekdays.contains("Thu",true)) return false
            Calendar.FRIDAY -> if(!ServiceWeekdays.contains("Fri",true)) return false
            Calendar.SATURDAY -> if(!ServiceWeekdays.contains("Sat",true)) return false
            Calendar.SUNDAY -> if(!ServiceWeekdays.contains("Sun",true)) return false
        }

        // Disable all dates before today
        if (date < today) {
            return false
        }
        // Allow all other dates
        return true
    }

    override fun describeContents(): Int {
        return 0
    }
    companion object CREATOR : Parcelable.Creator<DateValidator> {
        override fun createFromParcel(parcel: Parcel): DateValidator {
            return DateValidator(parcel)
        }

        override fun newArray(size: Int): Array<DateValidator?> {
            return arrayOfNulls(size)
        }
    }
}