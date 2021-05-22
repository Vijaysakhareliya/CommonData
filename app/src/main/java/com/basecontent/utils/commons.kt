package com.basecontent.utils

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
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
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.basecontent.BuildConfig
import com.basecontent.R
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import java.io.File
import java.math.RoundingMode
import java.net.URLEncoder
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.ArrayList


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

/**
 * Check Input string is valid or not and return boolean value
 */
fun checkStringValue(stValue: String?): Boolean {
    return stValue != null && stValue.isNotEmpty() && stValue != "null"
}

/**
 * Return input string if input string is valid otherwise return second string which is mention in second param
 */
fun checkStringValueReturn(stValue: String?, stReturnString: String = ""): String {
    return if (stValue != null && stValue.isNotEmpty() && stValue != "null") {
        stValue
    } else {
        stReturnString
    }
}

/**
 * For set visible to View
 */
fun View.visible() {
    this.visibility = View.VISIBLE
}

/**
 * For set gone to View
 */
fun View.gone() {
    this.visibility = View.GONE
}

/**
 * For set invisible to View
 */
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

/**
 * Find URL from given input string
 */
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

/**
 * showLog only debug mode
 */
fun showLog(message: String) {
    if (BuildConfig.DEBUG) {
        Log.e("AppName", message)
    }
}

/**
 * showLog only debug mode
 */
fun showLog(tag: String, message: String) {
    if (BuildConfig.DEBUG) {
        Log.e(tag, message)
    }
}

/**
 * remove unnecessary data from number and format number
 */
fun String.formatMobileNumber(): String {
    return this.replace("(", "")
        .replace(")", "")
        .replace("-", "")
        .replace(" ", "")
}

/**
 * Check mobile is valid or not
 */
fun isValidMobile(phone: String): Boolean {
    return Patterns.PHONE.matcher(phone).matches()
}

/**
 * show Toast for showing message
 */
fun Context.showMessage(stMsg: String) = Toast.makeText(this, stMsg, Toast.LENGTH_SHORT).show()

/**
 * show Snackbar for showing message
 */
fun View.snackbar(message: CharSequence) = Snackbar
    .make(this, message, Snackbar.LENGTH_SHORT)
    .apply { show() }

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
    colorForSelected : Int,
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
            ds.color = ContextCompat.getColor(this@setClickableString, colorForSelected)
        }
    }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return ss
}

/**
 * show Dialog with string type listing
 */
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

/**
 * show Dialog with multiple selecting string type listing
 */
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

/**
 * Convert Date to String with given format
 */
fun Date.convertDateToString(resultDateFormat : String): String {
    return SimpleDateFormat(resultDateFormat, Locale.ENGLISH).format(this)
}

/**
 * Convert String to Date with given format
 */
fun String.convertOneFormatToOtherFormatInDate(inputFormat: String): Date {
    val dateFormat = SimpleDateFormat(inputFormat, Locale.ENGLISH)
    val date = dateFormat.parse(this)
    return Date(date!!.time)
}

/**
 * Convert Date One format to other format in String
 */
fun String.convertOneFormatToOtherFormatInString(inputFormat: String, resultFormat:String): String {
    val dateFormat = SimpleDateFormat(inputFormat, Locale.ENGLISH)
    val dateFormatResult = SimpleDateFormat(resultFormat, Locale.ENGLISH)
    val date = dateFormat.parse(this)
    return dateFormatResult.format(Date(date!!.time))
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

/**
 * Send WhatsApp message to particular User and Message
 */
fun Context.sendWhatsAppMessage(stMobileNo: String, message: String) {
    val url = "https://api.whatsapp.com/send?phone=+91${stMobileNo}&text=" + URLEncoder.encode(
        message,
        "UTF-8"
    )
    val intentWhatsApp = Intent(Intent.ACTION_VIEW)
    intentWhatsApp.data = Uri.parse(url)
    startActivity(intentWhatsApp)
}

/**
 * For validate email-id
 *
 * @return email-id is valid or not
 */
fun String.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

/**
 * For validate phone
 *
 * @return phone is valid or not
 */
fun String.isValidPhone(): Boolean {
    return !TextUtils.isEmpty(this) && Patterns.PHONE.matcher(this).matches()
}

/**
 * isNetworkAvailable - Check if there is a NetworkConnection
 * @return boolean
 */
fun Context.isNetworkAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

/**
 * Get String from strings.xml file using resource
 */
fun Context.getStringValue(resourceId: Int): String {
    return resources.getString(resourceId)
}

/**
 * load image
 */
fun Context.loadImage(imagePath: String, imageView: AppCompatImageView) {
    Glide.with(this)
        .load(imagePath)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(imageView)
}

fun Context.loadRoundedCornerImage(imagePath: String, imageView: AppCompatImageView) {
    val requestOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .priority(Priority.HIGH)

    Glide.with(this).load(imagePath)
        .transform(CenterCrop(), RoundedCorners(10))
        .apply(requestOptions)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(imageView)
}

/**
 * value set in decimal format
 * like 1.1111 to 1.11
 */
fun setValueInDecimalFormat(value: Double): String {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING
    return df.format(value)
}

/**
 * convert object to string
 */
fun convertObjectToString(value: Any): String {
    return Gson().toJson(value)
}

/**
 * get object from string
 */
fun <T> convertStringToObject(value: String?, defValue: Class<T>): T {
    return Gson().fromJson(SharedPrefHelper[value!!, ""], defValue)
}
