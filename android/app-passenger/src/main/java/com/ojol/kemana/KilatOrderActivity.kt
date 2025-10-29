
package com.ojol.kemana

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.utsman.kemana.remote.merchant.MerchantInstance
import kotlinx.coroutines.launch

class KilatOrderActivity : AppCompatActivity() {

    // UI Elements
    private lateinit var pickupAddressEditText: EditText
    private lateinit var pickupPhoneEditText: EditText
    private lateinit var destinationAddressEditText: EditText
    private lateinit var destinationPhoneEditText: EditText
    private lateinit var packageWeightSpinner: Spinner
    private lateinit var packageDescriptionEditText: EditText
    private lateinit var motorboxSwitch: Switch
    private lateinit var smsSwitch: Switch
    private lateinit var promoCodeEditText: EditText
    private lateinit var createOrderButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kilat_order)
        title = "Pesan Kilat"

        // Initialize UI Elements
        pickupAddressEditText = findViewById(R.id.edit_text_pickup_address)
        pickupPhoneEditText = findViewById(R.id.edit_text_pickup_phone)
        destinationAddressEditText = findViewById(R.id.edit_text_destination_address)
        destinationPhoneEditText = findViewById(R.id.edit_text_destination_phone)
        packageWeightSpinner = findViewById(R.id.spinner_package_weight)
        packageDescriptionEditText = findViewById(R.id.edit_text_package_description)
        motorboxSwitch = findViewById(R.id.switch_motorbox)
        smsSwitch = findViewById(R.id.switch_sms)
        promoCodeEditText = findViewById(R.id.edit_text_promo_code)
        createOrderButton = findViewById(R.id.button_create_order)

        // Set OnClickListener for the create order button
        createOrderButton.setOnClickListener {
            createKilatOrder()
        }
    }

    private fun createKilatOrder() {
        // 1. Gather data from UI
        val pickupAddress = Address(
            address = pickupAddressEditText.text.toString(),
            phoneNumber = pickupPhoneEditText.text.toString(),
            pickupTime = "Sekarang" // Simplified for now
        )

        val destinationAddress = Address(
            address = destinationAddressEditText.text.toString(),
            phoneNumber = destinationPhoneEditText.text.toString(),
            pickupTime = "-" // Not applicable for destination
        )

        val kilatOrder = KilatOrder(
            pickupAddress = pickupAddress,
            destinationAddress = destinationAddress,
            vehicle = "Motorbike", // Simplified for now
            packageType = packageDescriptionEditText.text.toString(),
            useMotorbox = motorboxSwitch.isChecked,
            sendSms = smsSwitch.isChecked,
            paymentMethod = "Cash", // Simplified for now
            promoCode = promoCodeEditText.text.toString().ifBlank { null }
        )

        // 2. Validate data (basic validation)
        if (pickupAddress.address.isBlank() || destinationAddress.address.isBlank()) {
            Toast.makeText(this, "Alamat penjemputan dan tujuan tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        // 3. Call API
        lifecycleScope.launch {
            try {
                val response = MerchantInstance.create().createKilatOrder(kilatOrder)
                if (response.success == true) {
                    Toast.makeText(this@KilatOrderActivity, "Pesanan Kilat berhasil dibuat!", Toast.LENGTH_LONG).show()
                    // TODO: Navigate to Tracking Screen
                    finish()
                } else {
                    Toast.makeText(this@KilatOrderActivity, "Gagal membuat pesanan: ${response.message}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@KilatOrderActivity, "Gagal membuat pesanan: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
