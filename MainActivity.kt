package com.project.lidor_amraby

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import android.app.TimePickerDialog
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Button
import android.widget.Toast
import java.util.Calendar
import org.json.JSONObject
import java.net.URL
import android.widget.TextView
import com.project.lidor_amraby.R.*
import android.widget.EditText
import com.project.lidor_amraby.PhoneNumberDialog
import com.project.lidor_amraby.R.id.*


class MainActivity : AppCompatActivity(), PhoneNumberDialog.PhoneNumberDialogListener {

    // Declare variables
    private lateinit var numSeatsSpinner : Spinner
    private lateinit var paymentMethodSpinner : Spinner
    private lateinit var veganCheckbox : CheckBox
    private lateinit var timeButton : Button
    private lateinit var reserveButton : Button
    private var numSeatsText = ""
    private var paymentMethodText = ""
    private var veganText = ""
    private var timeText = ""
    private var yes = ""
    private var no = ""
    private var phone = ""
    private var selectedHour = 0
    private var selectedMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        // Initialize views
        numSeatsSpinner = findViewById<Spinner>(num_seats_spinner)
        paymentMethodSpinner = findViewById<Spinner>(payment_method_spinner)
        veganCheckbox = findViewById<CheckBox>(vegan_checkbox)
        timeButton = findViewById<Button>(time_button)
        reserveButton = findViewById<Button>(reserve_button)

        // Initialize string resources
        numSeatsText = getString(string.num_seats_text)
        paymentMethodText = getString(string.payment_method_text)
        veganText = getString(string.vegan_checkbox_text)
        timeText = getString(string.time_button_text)
        yes = getString(string.yes)
        no = getString(string.no)
        phone = getString(string.phone_show)

        // Initialize image views
        val gyros = findViewById<ImageView>(gyros_img)
        val tacos = findViewById<ImageView>(tacos_img)
        val cake = findViewById<ImageView>(cake_img)
        val tea = findViewById<ImageView>(tea_img)
        val lemonade = findViewById<ImageView>(lemonade_img)
        val coffee = findViewById<ImageView>(coffee_img)

        // Initialize animations
        val fadeIn =
            ObjectAnimator.ofFloat(reserveButton, "alpha", 0f, 1f).setDuration(400)
        val shakeRight =
            ObjectAnimator.ofFloat(reserveButton, "translationX", -30f, 0f).setDuration(200)
        val shakeBack =
            ObjectAnimator.ofFloat(reserveButton, "translationX", 30f, 0f).setDuration(200)
        val animatorSetBtn = AnimatorSet()

        val rotateLeft = AnimationUtils.loadAnimation(applicationContext, anim.left_rotate)
        val rotateRight = AnimationUtils.loadAnimation(applicationContext, anim.right_rotate)
        val rotate = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate)
        val shake = AnimationUtils.loadAnimation(applicationContext, anim.shake)

        // Create a blink animation
        val blinkAnimation = AlphaAnimation(0.4f, 1.0f)
        blinkAnimation.duration = 1700
        blinkAnimation.repeatMode = Animation.REVERSE
        blinkAnimation.repeatCount = Animation.INFINITE

        // Apply the animation to the button
        reserveButton.startAnimation(blinkAnimation)

        // Play animations
        animatorSetBtn.playSequentially(fadeIn, shakeRight, shakeBack)
        animatorSetBtn.start()

        // Set click listeners
        timeButton.setOnClickListener {
            showTimePickerDialog()
        }
        reserveButton.setOnClickListener {
            showReserveButtonDialog(phone)
        }
        gyros.setOnClickListener {
            it.startAnimation(shake)
        }
        tacos.setOnClickListener {
            it.startAnimation(rotate)
        }
        cake.setOnClickListener {
            it.startAnimation(shake)
        }
        tea.setOnClickListener {
            it.startAnimation(rotateRight)
        }
        lemonade.setOnClickListener {
            it.startAnimation(rotateLeft)
        }
        coffee.setOnClickListener {
            it.startAnimation(rotateRight)
        }

        showPhoneNumberDialog()

    }

    // Function to show the TimePickerDialog
    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val min = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                selectedHour = hourOfDay// Update hour
                selectedMinute = minute// Update minute
                val selectedTime = "$selectedHour:$selectedMinute"
                // Display the selected time or use it for further processing
                Toast.makeText(this, "Reservation time: $selectedTime", Toast.LENGTH_SHORT)
                    .show()
            },
            hour,
            min,
            true
        )
        timePickerDialog.show()
    }

    // Function to show the ReserveButtonDialog
    private fun showReserveButtonDialog(phoneNumber: String) {
        // Create an AlertDialog.Builder with the activity context
        val dialogBuilder = AlertDialog.Builder(this)
        // Inflate the dialog layout
        val dialogView = LayoutInflater.from(this).inflate(layout.dialog_reservation, null)
        // Set the inflated view as the dialog's content view
        dialogBuilder.setView(dialogView)
        // Create the AlertDialog
        val dialog = dialogBuilder.create()
        // Set the background of the dialog window to transparent
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Set a click listener for the close button in the dialog
        dialogView.findViewById<Button>(close_button).setOnClickListener {
            dialog.dismiss()
        }

        // Set text in the dialog for different fields
        // Number of seats
        "$numSeatsText ${numSeatsSpinner.selectedItem ?: ""}".also {
            dialogView.findViewById<TextView>(num_seats_textview).text = it
        }
        // Payment method
        "$paymentMethodText ${paymentMethodSpinner.selectedItem ?: ""}".also {
            dialogView.findViewById<TextView>(payment_method_textview).text = it
        }
        // Vegan option
        "$veganText: ${if (veganCheckbox.isChecked) 
            "$yes" else "$no"}".also {
            dialogView.findViewById<TextView>(vegan_textview).text = it
        }
        // Selected time
        "$timeText: ${if (selectedMinute < 10) 
            "$selectedHour:0$selectedMinute" else "$selectedHour:$selectedMinute"}".also {
            dialogView.findViewById<TextView>(time_button_textview).text = it
        }
        // Display the entered phone number
        (getString(R.string.phone_show) + phoneNumber).also {
            dialogView.findViewById<TextView>(display_phone_number_textview).text = it
        }

        // Set the background of the dialog layout
        dialogView.findViewById<LinearLayout>(dialog_layout).background =
            ContextCompat.getDrawable(this, drawable.green_rounded_bg)
        dialog.show()
    }

    // Function to show the PhoneNumberDialog
    private fun showPhoneNumberDialog() {
        val phoneNumberDialog = PhoneNumberDialog()
        phoneNumberDialog.show(supportFragmentManager, "PhoneNumberDialog")
    }

    // Implement the onPhoneNumberEntered method to receive the entered phone number
    override fun onPhoneNumberEntered(phoneNumber: String) {
        phone = phoneNumber
    }

}