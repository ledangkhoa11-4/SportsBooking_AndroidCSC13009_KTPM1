package com.example.sportbooking

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.sportbooking.DTO.BookingHistory
import com.example.sportbooking.Ultils.CreateToast
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.Order
import com.paypal.checkout.order.PurchaseUnit
import com.paypal.checkout.paymentbutton.PaymentButtonContainer
import com.shawnlin.numberpicker.NumberPicker
import nl.joery.timerangepicker.TimeRangePicker
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Booking : AppCompatActivity() {
    lateinit var hourServiceTv: TextView
    lateinit var courtNameTv: TextView
    lateinit var courtLocation: TextView
    lateinit var sportType: TextView
    lateinit var yardNum: TextView
    lateinit var dateTv: TextView
    lateinit var timeTv: TextView
    lateinit var court: Court
    lateinit var timeRangePicker: TimeRangePicker
    lateinit var timeStart: TextView
    lateinit var timeEnd: TextView
    lateinit var priceBooking: TextView
    lateinit var checkoutBtn: PaymentButtonContainer
    lateinit var loadingIndicator: CircularProgressIndicator
    lateinit var warningTV: TextView
    var durationBooking: Int = 0;
    var index = -1
    var date = -1L
    var yard = -1
    var start = -1L
    var end = -1L
    var bookList: kotlin.collections.ArrayList<BookingHistory> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)
        loadingIndicator = findViewById(R.id.loadingBooking)
        val formatter = java.text.DecimalFormat("#,###")
        val intent = intent
        index = intent.getIntExtra("index", 0)
        date = intent.getLongExtra("date", -1)
        yard = intent.getIntExtra("yard", -1)
        var hour = intent.getIntExtra("hour", -1)
        court = MainActivity.listCourt[index]
        loadBookingList()
        courtNameTv = findViewById(R.id.courtNameBooking)
        courtLocation = findViewById(R.id.courtLocationDetailBooking)
        sportType = findViewById(R.id.typeSport)
        yardNum = findViewById(R.id.yardNumChoose)
        dateTv = findViewById(R.id.dateChoose)
        timeTv = findViewById(R.id.timeChoose)
        priceBooking = findViewById(R.id.priceBooking)

        courtNameTv.setText(court.Name)
        courtLocation.setText(court.location!!.addressName)
        sportType.setText(court.Type)

        hourServiceTv = findViewById(R.id.hourServiceTv)
        hourServiceTv.setText(
            convertTimestampToTime(court.ServiceHour[0]) + " - " + convertTimestampToTime(
                court.ServiceHour[1]
            )
        )

        val pickTimeView: View =
            LayoutInflater.from(this).inflate(R.layout.time_picker_layout, null);
        timeRangePicker = pickTimeView.findViewById(R.id.timeRangePicker)
        timeRangePicker.startTime = TimeRangePicker.Time(0, 0)
        timeStart = pickTimeView.findViewById(R.id.startTimeTv)
        timeEnd = pickTimeView.findViewById(R.id.endTimeTv)
        warningTV = pickTimeView.findViewById(R.id.warningTV)
        timeRangePicker.setOnTimeChangeListener(object : TimeRangePicker.OnTimeChangeListener {
            override fun onStartTimeChange(startTime: TimeRangePicker.Time) {
                timeStart.text =
                    startTime.hour.toString().padStart(2, '0') + ":" + startTime.minute.toString()
                        .padStart(2, '0')
                start = timeStringToTimestamp(timeStart.text.toString())

                if (yard == -1 || date == -1L || start == -1L || end == -1L) {
                    warningTV.visibility = View.VISIBLE
                    warningTV.text = "Please choose yard, date and time"
                }
                val opening = Date(court.ServiceHour[0])
                val closing = Date(court.ServiceHour[1])
                val startBook = Date(start)
                val endBook = Date(end)

                if (startBook.after(endBook)) {
                    warningTV.visibility = View.VISIBLE
                    warningTV.text = "Start time must be before end time"
                } else if (startBook.before(opening) || endBook.after(closing)) {
                    warningTV.visibility = View.VISIBLE
                    warningTV.text = "Time must be in working time"
                } else {
                    warningTV.visibility = View.GONE
                }

                for (booking in bookList) {
                    if (date == booking.Date && yard == booking.Yard) {
                        val anotherStartBook = Date(booking.Time[0])
                        val anotherEndBook = Date(booking.Time[1])

                        if (checkTimeConflict(
                                startBook,
                                endBook,
                                anotherStartBook,
                                anotherEndBook
                            )
                        ) {
                            warningTV.visibility = View.VISIBLE
                            warningTV.text = "Time is not available"
                            break
                        } else {
                            warningTV.visibility = View.GONE
                        }
                    }
                }
            }
            override fun onDurationChange(duration: TimeRangePicker.TimeDuration) {
                durationBooking = duration.durationMinutes;
            }

            override fun onEndTimeChange(endTime: TimeRangePicker.Time) {
                timeEnd.text =
                    endTime.hour.toString().padStart(2, '0') + ":" + endTime.minute.toString()
                        .padStart(2, '0')
                end = timeStringToTimestamp(timeEnd.text.toString())

                if (yard == -1 || date == -1L || start == -1L || end == -1L) {
                    warningTV.visibility = View.VISIBLE
                    warningTV.text = "Please choose yard, date and time"
                }
                val opening = Date(court.ServiceHour[0])
                val closing = Date(court.ServiceHour[1])
                val startBook = Date(start)
                val endBook = Date(end)

                if (startBook.after(endBook)) {
                    warningTV.visibility = View.VISIBLE
                    warningTV.text = "Start time must be before end time"
                } else if (startBook.before(opening) || endBook.after(closing)) {
                    warningTV.visibility = View.VISIBLE
                    warningTV.text = "Time must be in working time"
                } else {
                    warningTV.visibility = View.GONE
                }

                for (booking in bookList) {
                    if (date == booking.Date && yard == booking.Yard) {
                        val anotherStartBook = Date(booking.Time[0])
                        val anotherEndBook = Date(booking.Time[1])

                        if (checkTimeConflict(
                                startBook,
                                endBook,
                                anotherStartBook,
                                anotherEndBook
                            )
                        ) {
                            warningTV.visibility = View.VISIBLE
                            warningTV.text = "Time is not available"
                            break
                        } else {
                            warningTV.visibility = View.GONE
                        }
                    }
                }
            }
        })
        if (hour != -1) {
            timeStart.text = hour.toString().padStart(2, '0') + ":" + 0.toString().padStart(2, '0')
            timeEnd.text =
                (hour + 1).toString().padStart(2, '0') + ":" + 0.toString().padStart(2, '0')
            start = timeStringToTimestamp(timeStart.text.toString())
            end = timeStringToTimestamp(timeEnd.text.toString())
            durationBooking = 60
            timeRangePicker.startTimeMinutes = hour * 60
            timeRangePicker.endTimeMinutes = (hour + 1) * 60
            timeTv.setText(timeStart.text.toString() + " - " + timeEnd.text.toString())
            priceBooking.setText(formatter.format(court.Price) + "đ")
        }

        timeTv.setOnClickListener {
            if (pickTimeView.parent != null) {
                var vg = pickTimeView.parent as ViewGroup
                vg.removeView(pickTimeView)
            }
            MaterialAlertDialogBuilder(this)
                .setTitle("Pick booking time")
                .setView(pickTimeView)
                .setPositiveButton("Apply") { dialog, which ->
                    timeStart.text = timeRangePicker.startTime.hour.toString()
                        .padStart(2, '0') + ":" + timeRangePicker.startTime.minute.toString()
                        .padStart(2, '0')
                    start = timeStringToTimestamp(timeStart.text.toString())
                    timeEnd.text = timeRangePicker.endTime.hour.toString()
                        .padStart(2, '0') + ":" + timeRangePicker.endTime.minute.toString()
                        .padStart(2, '0')
                    end = timeStringToTimestamp(timeEnd.text.toString())
                    timeTv.setText(timeStart.text.toString() + " - " + timeEnd.text.toString())
                    priceBooking.setText(formatter.format((court.Price * durationBooking) / 60) + "đ")
                    checkoutBtn.visibility = View.INVISIBLE
                    findViewById<Button>(R.id.BookBtn).visibility = View.VISIBLE
                }.show()
        }

        val today = Calendar.getInstance()
        val constraintsBuilder = CalendarConstraints.Builder()
        constraintsBuilder.setStart(today.timeInMillis)
        constraintsBuilder.setValidator(DateValidator(court.ServiceWeekdays))
        val datePickerBuilder =
            MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(constraintsBuilder.build())
                .setTitleText("Select date")

        val datePicker: MaterialDatePicker<Long>
        if (date != -1L) {
            datePicker = datePickerBuilder.setSelection(date).build()
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dateTv.setText(sdf.format(Date(date)))
        } else {
            datePicker = datePickerBuilder.build()
        }
        dateTv.setOnClickListener {
            datePicker.show(supportFragmentManager, "tag");
        }
        datePicker.addOnPositiveButtonClickListener {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dateTv.setText(sdf.format(Date(it)))
            date = it
            checkoutBtn.visibility = View.INVISIBLE
            findViewById<Button>(R.id.BookBtn).visibility = View.VISIBLE
        }

        val numberPickerView: View = LayoutInflater.from(this).inflate(R.layout.yard_picker, null);
        val numberPicker: NumberPicker = numberPickerView.findViewById(R.id.yardNumberPicker)
        numberPicker.minValue = 1
        numberPicker.maxValue = court.numOfYards
        val pickYardDialog = MaterialAlertDialogBuilder(this)
            .setTitle("Pick yard number")
            .setView(numberPickerView)
            .setPositiveButton("Apply") { dialog, which ->
                yardNum.setText("Field " + numberPicker.value)
                yard = numberPicker.value;
                checkoutBtn.visibility = View.INVISIBLE
                findViewById<Button>(R.id.BookBtn).visibility = View.VISIBLE
            }
        yardNum.setOnClickListener {
            if (numberPickerView.parent != null) {
                var vg = numberPickerView.parent as ViewGroup
                vg.removeView(numberPickerView)
            }
            pickYardDialog.show()
        }
        if (yard != -1) {
            yardNum.setText("Field " + yard)
        }
        findViewById<ImageButton>(R.id.backButtonRating).setOnClickListener {
            finish()
        }
        checkoutBtn = findViewById(R.id.paybtn)
        checkoutBtn.setup(
            createOrder =
            CreateOrder { createOrderActions ->
                val order =
                    Order(
                        intent = OrderIntent.CAPTURE,
                        appContext = AppContext(userAction = UserAction.PAY_NOW),
                        purchaseUnitList =
                        listOf(
                            PurchaseUnit(
                                amount =
                                Amount(currencyCode = CurrencyCode.USD, value = "10.00")
                            )
                        )
                    )
                createOrderActions.create(order)
            },
            onApprove =
            OnApprove { approval ->
                loadingIndicator.visibility=View.VISIBLE
                approval.orderActions.capture { captureOrderResult ->
                    val calendar = Calendar.getInstance()
                    val newBooking = BookingHistory(
                        "",
                        court.CourtID,
                        MainActivity.user.id,
                        date,
                        yard,
                        arrayListOf(start, end),
                        court.Price * durationBooking / 60,
                        null,
                        calendar.timeInMillis,
                        false
                    )
                    uploadNewBooking(newBooking)
                }
            },
            onCancel = OnCancel {
                CreateToast.createToast(this, "Error","Payment was canceled", false)
            },
            onError = OnError { errorInfo ->
                Log.d("OnError", "Error: $errorInfo")
            }
        )
        findViewById<Button>(R.id.BookBtn).setOnClickListener {
            if (yard == -1 || date == -1L || start == -1L || end == -1L) {
                Log.i("A", yard.toString())
                Log.i("A", date.toString())
                Log.i("A", start.toString())
                Log.i("A", end.toString())
                createToast("Sorry", "Please enter full information", false);
                return@setOnClickListener
            }
            val working = Date(court.ServiceHour[0])
            val closing = Date(court.ServiceHour[1])
            val startBook = Date(start)
            val endBook = Date(end)

            if (startBook.after(endBook)) {
                createToast("Oops", "Do not book over the day!", false);
                return@setOnClickListener
            }
            if (startBook.before(working) || endBook.after(closing)) {
                createToast(
                    "Wrong time line",
                    "Booking time is outside of the court's operating hours",
                    false
                );
                return@setOnClickListener
            }
            for (booking in bookList) {
                if (date == booking.Date && yard == booking.Yard) {
                    val another_start_book = Date(booking.Time[0])
                    val another_end_book = Date(booking.Time[1])
                    if (checkTimeConflict(
                            startBook,
                            endBook,
                            another_start_book,
                            another_end_book
                        )
                    ) {
                        createToast("Sorry", " This time range is already booked", false);
                        return@setOnClickListener
                    }
                }
            }
            createToast("Horray", "You can proceed to payment", true);
            checkoutBtn.visibility = View.VISIBLE
            findViewById<Button>(R.id.BookBtn).visibility = View.INVISIBLE
        }
    }

    private fun uploadNewBooking(newBooking: BookingHistory) {
        loadingIndicator.visibility=View.VISIBLE
        val bookingRef = MainActivity.database.getReference("Booking")
        bookingRef.push().setValue(newBooking).addOnSuccessListener {
            loadingIndicator.visibility=View.INVISIBLE
            CreateToast.createToast(
                this,
                "Booking successfully",
                "You can check it in My Booking",
                true
            )
            finish()
        }
    }

    fun checkTimeConflict(x1: Date, x2: Date, y1: Date, y2: Date): Boolean {
        if (x1.before(y1) && (x2.before(y1) || x2 == y1))
            return false
        if ((x1.after(y2) || x1 == y2) && x2.after(y2))
            return false
        return true
    }

    fun createToast(title: String, message: String, isSuccess: Boolean) {
        var style: MotionToastStyle
        if (isSuccess)
            style = MotionToastStyle.SUCCESS
        else
            style = MotionToastStyle.ERROR
        MotionToast.createToast(
            this,
            title,
            message,
            style,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.SHORT_DURATION,
            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular)
        )
    }

    fun timeStringToTimestamp(timeString: String): Long {
        val format = SimpleDateFormat("hh:mm", Locale.getDefault())
        val date = format.parse(timeString)
        return date?.time ?: 0
    }

    fun convertTimestampToTime(timestamp: Long): String {
        val date = Date(timestamp)
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formatter.format(date)
    }

    fun loadBookingList() {
        val bookingRef = MainActivity.database.getReference("Booking");
        val queryRef = bookingRef.orderByChild("courtID").equalTo(court.CourtID)
        queryRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookList.clear()
                for (ds in snapshot.children) {
                    val bookHistory = ds.getValue(BookingHistory::class.java)
                    bookList.add(bookHistory!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}

