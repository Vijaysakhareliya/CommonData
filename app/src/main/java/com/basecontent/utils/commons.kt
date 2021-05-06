package com.basecontent.utils

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.ClickableSpan
import android.util.Log
import android.util.Patterns
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.basecontent.R
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import com.basecontent.BuildConfig


const val GROUP_MODEL = "groupModel"

/**
 * This function used for check device is Tablet or Mobile
 * isTabletMode() == true : Tablet
 * isTabletMode() == false : Mobile
 * For enable and disable tablet mode just change isTablet Param
 */
const val isTablet = true
fun Context.isTabletMode(): Boolean {
    return if (isTablet) {
        resources.getBoolean(R.bool.isTablet)
    } else {
        false
    }
}

fun checkStringValue(stValue: String?): Boolean {
    return stValue != null && stValue.isNotEmpty() && stValue != "null"
}

fun checkStringValueReturn(stValue: String?, stReturnString: String = ""): String {
    return if (stValue != null && stValue.isNotEmpty() && stValue != "null") {
        stValue
    } else {
        stReturnString
    }
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

/**
 * For show dialog
 *
 * @param title - title which shown in dialog (application name)
 * @param msg - message which shown in dialog
 * @param positiveText - positive button text
 * @param listener - positive button listener
 * @param negativeText - negative button text
 * @param negativeListener - negative button listener
 * @param icon - drawable icon which shown is dialog
 */
fun Context.showDialog(
    title: String? = this.resources.getString(R.string.app_name),
    msg: String,
    positiveText: String? = this.resources.getString(R.string.labelOk),
    listener: DialogInterface.OnClickListener? = null,
    negativeText: String? = this.resources.getString(R.string.labelCancel),
    negativeListener: DialogInterface.OnClickListener? = null,
    icon: Int? = null
) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setMessage(msg)
    builder.setCancelable(false)
    builder.setPositiveButton(positiveText) { dialog, which ->
        listener?.onClick(dialog, which)
    }
    if (negativeListener != null) {
        builder.setNegativeButton(negativeText) { dialog, which ->
            negativeListener.onClick(dialog, which)
        }
    }
    if (icon != null) {
        builder.setIcon(icon)
    }
    builder.create().show()
}

/**
 * Get String from AppCompactEditText
 */
fun AppCompatEditText.getStringFromEditText(): String {
    return this.editableText.toString()
}

/**
 * Get String from AppCompact TextView
 */
fun AppCompatTextView.getStringFromTextView(): String {
    return this.text.toString()
}

/**
 * prevent double click
 */
fun View.setOnSafeClickListener(
    onSafeClick: (View) -> Unit
) {
    setOnClickListener(SafeClickListener { v ->
        onSafeClick(v)
    })
}

/**
 * For launch other activity
 */
inline fun <reified T : Any> Activity.launchActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {

    val intent = newIntent<T>(this)
    intent.init()
    startActivityForResult(intent, requestCode, options)
}

inline fun <reified T : Any> Context.launchActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {

    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent, options)
}

inline fun <reified T : Any> newIntent(context: Context): Intent =
    Intent(context, T::class.java)

fun getUserType(currentType: String): Int {
    return 0
}

fun pullLinkFromString(text: String?): ArrayList<String> {
    val links = ArrayList<String>()
    if (checkStringValue(text)) {

        //String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
        val regex =
            "\\(?\\b(https?://|www[.]|ftp://)[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]"
        val p: Pattern = Pattern.compile(regex)
        val m: Matcher = p.matcher(text)
        while (m.find()) {
            var urlStr: String = m.group()
            if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
                urlStr = urlStr.substring(1, urlStr.length - 1)
            }
            links.add(urlStr)
        }
    }
    return links
}


fun showLog(message: String) {
    if (BuildConfig.DEBUG) {
        Log.e("AppName", message)
    }
}

fun showLog(tag: String, message: String) {
    if (BuildConfig.DEBUG) {
        Log.e(tag, message)
    }
}

fun String.formatMobileNumber(): String {
    return this.replace("(", "")
        .replace(")", "")
        .replace("-", "")
        .replace(" ", "")
}

fun isValidMobile(phone: String): Boolean {
    return Patterns.PHONE.matcher(phone).matches()
}

fun Context.showMessage(stMsg: String) = Toast.makeText(this, stMsg, Toast.LENGTH_SHORT).show()

fun String.isValidFormat(format: String?): Boolean {
    var date: Date? = null
    try {
        val sdf = SimpleDateFormat(format, Locale.US)
        date = sdf.parse(this)
        if (this != sdf.format(date)) {
            date = null
        }
    } catch (ex: ParseException) {
//        ex.printStackTrace()
    } catch (ex: Exception) {
//        ex.printStackTrace()
    }
    return date != null
}

fun <T> List<T>.toArrayList(): ArrayList<T> {
    return ArrayList(this)
}

fun Context.deleteCache() {
    try {
        val dir: File = applicationContext.cacheDir
        deleteDir(dir)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun deleteDir(dir: File?): Boolean {
    return if (dir != null && dir.isDirectory) {
        val children: Array<String> = dir.list()!!
        for (i in children.indices) {
            val success = deleteDir(File(dir, children[i]))
            if (!success) {
                return false
            }
        }
        dir.delete()
    } else if (dir != null && dir.isFile) {
        dir.delete()
    } else {
        false
    }
}

fun Context.setClickableString(
    mainString: String,
    selectedString: String,
    onClickText: () -> Unit
): SpannableString {
    val ss = SpannableString(mainString)
    val start = mainString.indexOf(selectedString)
    val end = start + selectedString.length
    ss.setSpan(object : ClickableSpan() {
        override fun onClick(widget: View) {
            onClickText()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = true
            ds.color = ContextCompat.getColor(this@setClickableString, R.color.purple_200)
        }
    }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return ss
}

fun Context.showListingDialog(
    list: ArrayList<String>,
    selectedData: (Int, String) -> Unit
): AlertDialog {
    val alertDialog = AlertDialog.Builder(this)
    val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
    alertDialog.setAdapter(adapter) { _, position ->
        //listener.onItemClick(position, list[position])
        selectedData.invoke(position, list[position])
    }

    return alertDialog.create()
}

fun Context.showListingDialogMultiSelection(
    list: ArrayList<String>,
    selectedItemsString: String,
    selectedItemsPositions: ArrayList<String>,
    selectedData: (String, String) -> Unit
): AlertDialog {
    val alertDialog = AlertDialog.Builder(this)

//    val selectedItems = selectedItemsString.split(",")
    val selectedItemsBoolean = arrayListOf<Boolean>()

    list.forEachIndexed { index, data ->
        selectedItemsBoolean.add(selectedItemsPositions.contains("$index"))
    }

    alertDialog.setMultiChoiceItems(
        list.toTypedArray(),
        selectedItemsBoolean.toBooleanArray()
    ) { _, which, selected ->
        selectedItemsBoolean[which] = selected
    }

    alertDialog.setPositiveButton(this.resources.getString(R.string.labelOk)) { _, _ ->
        val listSelected = ArrayList<String>()
        val listSelectedInt = hashSetOf<String>()
        selectedItemsBoolean.forEachIndexed { position, _ ->
            if (selectedItemsBoolean[position]) {
                listSelected.add(list[position])
                listSelectedInt.add("$position")
            }
        }

        if (listSelected.size > 0) {
            selectedData.invoke(
                TextUtils.join(",", listSelectedInt),
                TextUtils.join(", ", listSelected)
            )
            //listener.onItemClick(0, TextUtils.join(", ", listSelected))
        }
    }

    return alertDialog.create()
}

const val ABSENCE_DATE_FORMAT = "EEEE, dd MMM, yyyy"
const val MEETING_FORMAT = "EEE, dd MMMM, yyyy"
const val BIRTH_DAY_FORMAT = "dd MMM, yyyy"
const val SEND_SERVER_FORMAT = "yyyy-MM-dd"
const val SEND_SERVER_FORMAT_APPOINTMENT_DATE = "yyyy-MM"
const val DAY_FORMAT = "EEEE"
const val FROM_SERVER_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzzz yyyy"
const val INSERT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm"
const val INSERT_DATE_TIME_FORMAT_ABSENCE = "yyyy-MM-dd hh:mm a"
const val TIME_FORMAT = "HH:mm"
const val TIME_LOWER_FORMAT = "hh:mm a"
const val TIME_FORMAT_SECOND_UPPER = "HH:mm"
const val INSERT_DATE_TIME_FORMAT_SECOND = "EEE, dd MMMM, yyyy HH:mm"

fun Date.getDateForWeekPage(): String {
    return SimpleDateFormat(SEND_SERVER_FORMAT, Locale.ENGLISH).format(this)
}

fun String.convertStringToDateForStartTime(): Date {
    val dateFormat = SimpleDateFormat(INSERT_DATE_TIME_FORMAT, Locale.ENGLISH)
    val date = dateFormat.parse(this)
    return Date(date!!.time)
}

fun String.convertStringToDateForStartTimeAbsence(): Date {
    val dateFormat = SimpleDateFormat(INSERT_DATE_TIME_FORMAT_ABSENCE, Locale.ENGLISH)
    val date = dateFormat.parse(this)
    return Date(date!!.time)
}

fun String.convertStringToDateForStartTimeSecond(): Date {
    val dateFormat = SimpleDateFormat(INSERT_DATE_TIME_FORMAT_SECOND, Locale.ENGLISH)
    val date = dateFormat.parse(this)
    return Date(date!!.time)
}

fun convertDateForServer(milliSecond: Long): String {
    val date = Date(milliSecond)
    return SimpleDateFormat(SEND_SERVER_FORMAT, Locale.ENGLISH).format(date)
}

fun Date.convertDateForAppointmentMonth(): String {
    return SimpleDateFormat(SEND_SERVER_FORMAT_APPOINTMENT_DATE, Locale.ENGLISH).format(this)
}

fun Context.convertDateToTimeFormat(milliSecond: Long): String {
    val mCalendar = Calendar.getInstance()
    mCalendar.timeInMillis = milliSecond
    return SimpleDateFormat(TIME_FORMAT, Locale.ENGLISH).format(mCalendar.time)
}

fun Date.convertDateToShowMeetingFormat(): String {
    return SimpleDateFormat(MEETING_FORMAT, Locale.ENGLISH).format(this)
}

fun Date.convertDateForServer(): String {
    return SimpleDateFormat(SEND_SERVER_FORMAT, Locale.ENGLISH).format(this)
}

fun String.convertServerToApp(): String {
    val dateFormat = SimpleDateFormat(SEND_SERVER_FORMAT, Locale.ENGLISH)
    val date = dateFormat.parse(this)
    return SimpleDateFormat(MEETING_FORMAT, Locale.ENGLISH).format(date!!)
}

fun String.convertServerToAppAbsence(): String {
    val dateFormat = SimpleDateFormat(SEND_SERVER_FORMAT, Locale.ENGLISH)
    val date = dateFormat.parse(this)
    return SimpleDateFormat(ABSENCE_DATE_FORMAT, Locale.ENGLISH).format(date!!)
}

fun getCurrentDateWithoutTime(): Date {
    val date = Date()
    val dateFormat = SimpleDateFormat(BIRTH_DAY_FORMAT, Locale.ENGLISH)
    val currentDate = dateFormat.format(date)
    val dateFormatDate = SimpleDateFormat(BIRTH_DAY_FORMAT, Locale.ENGLISH)
    return dateFormatDate.parse(currentDate)!!
}

fun String.convertServerToAppDate(): Date {
    val dateFormat = SimpleDateFormat(MEETING_FORMAT, Locale.ENGLISH)
    return dateFormat.parse(this)!!
}

fun String.convertUpperToLowerTime(): String {
    val dateFormat = SimpleDateFormat(TIME_FORMAT, Locale.ENGLISH)
    val date = dateFormat.parse(this)
    return SimpleDateFormat(TIME_LOWER_FORMAT, Locale.ENGLISH).format(date!!)
}

fun String.convertUpperToLowerTimeSecond(): String {
    val dateFormat = SimpleDateFormat(TIME_FORMAT_SECOND_UPPER, Locale.ENGLISH)
    val date = dateFormat.parse(this)
    return SimpleDateFormat(TIME_LOWER_FORMAT, Locale.ENGLISH).format(date!!)
}

fun String.convertLowerToUpperTimeSecond(): String {
    val dateFormat = SimpleDateFormat(TIME_LOWER_FORMAT, Locale.ENGLISH)
    val date = dateFormat.parse(this)
    return SimpleDateFormat(TIME_FORMAT_SECOND_UPPER, Locale.ENGLISH).format(date!!)
}

fun String.convertDateForServer(): String {
    val dateFormat = SimpleDateFormat(MEETING_FORMAT, Locale.ENGLISH)
    val date = dateFormat.parse(this)
    return SimpleDateFormat(SEND_SERVER_FORMAT, Locale.ENGLISH).format(date!!)
}

fun String.getDayFromDate(): String {
    val dateFormat = SimpleDateFormat(MEETING_FORMAT, Locale.ENGLISH)
    val date = dateFormat.parse(this)
    return SimpleDateFormat(DAY_FORMAT, Locale.ENGLISH).format(date!!)
}

/**
 * Date Picker
 */
fun Context.callDatePicker(
    dateFormat: String,
    forBirthDate: Boolean = false,
    datePicker: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    val dateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            try {
                val calendarOne = GregorianCalendar(year, monthOfYear, dayOfMonth)
                val sdf = SimpleDateFormat(dateFormat, Locale.ENGLISH)
                datePicker.invoke(sdf.format(calendarOne.time))
            } catch (e: Exception) {
                showLog("" + e.localizedMessage!!)
            }
        }

    try {

        var year = calendar.get(Calendar.YEAR)
        if (forBirthDate) {
            year -= 25
        }
        val datePickerDialog = DatePickerDialog(
            this,
            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
            dateSetListener,
            year,
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val cal = Calendar.getInstance()
        if (forBirthDate) {
            cal.add(Calendar.YEAR, -16)
            datePickerDialog.datePicker.maxDate = cal.timeInMillis
        } else {
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 10000
        }

        datePickerDialog.show()
    } catch (e: Exception) {
        var year = calendar.get(Calendar.YEAR)
        if (forBirthDate) {
            year -= 25
        }

        val datePickerDialog = DatePickerDialog(
            this,
            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
            dateSetListener,
            year,
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val cal = Calendar.getInstance()
        if (forBirthDate) {
            cal.add(Calendar.YEAR, -16)
            datePickerDialog.datePicker.maxDate = cal.timeInMillis
        } else {
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 10000
        }

        datePickerDialog.show()
    }
}

/**
 * Time Picker
 */
fun Context.callTimePicker(selectedTime: String, timePicker: (String) -> Unit) {
    val mCurrentTime = Calendar.getInstance()
    var hour = mCurrentTime[Calendar.HOUR_OF_DAY]
    var minute = mCurrentTime[Calendar.MINUTE]
    if (checkStringValue(selectedTime)) {
        val s = selectedTime.split(":".toRegex()).toTypedArray()
        hour = s[0].toInt()
        //minute = s[1].split(" ".toRegex())[0].toInt()
        minute = s[1].toInt()
    }
    val mTimePicker: TimePickerDialog
    val wrapper = ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog)
    mTimePicker = TimePickerDialog(
        wrapper, { _: TimePicker?, hourOfDay: Int, minute1: Int ->

            val datetime = Calendar.getInstance()
            datetime[Calendar.HOUR_OF_DAY] = hourOfDay
            datetime[Calendar.MINUTE] = minute1

            /*val amPm = if (hourOfDay < 12) {
                "AM"
            } else {
                "PM"
            }*/

            var strHrsToShow = datetime[Calendar.HOUR_OF_DAY].toString() + ""
                //if (datetime[Calendar.HOUR] == 0) "12" else datetime[Calendar.HOUR].toString() + ""

            /*if (strHrsToShow.length == 1) {
                strHrsToShow = "0$strHrsToShow"
            }*/

            /*if (minute1 < 10) {
                timePicker.invoke("$strHrsToShow:0${datetime.get(Calendar.MINUTE)} $amPm")
            } else {
                timePicker.invoke("$strHrsToShow:${datetime.get(Calendar.MINUTE)} $amPm")
            }*/
            if (minute1 < 10) {
                timePicker.invoke("$strHrsToShow:0${datetime.get(Calendar.MINUTE)}")
            } else {
                timePicker.invoke("$strHrsToShow:${datetime.get(Calendar.MINUTE)}")
            }
        }, hour, minute, true
    ) //True for 24 hour time
    mTimePicker.setTitle(this.resources.getString(R.string.app_name))
    mTimePicker.show()
}