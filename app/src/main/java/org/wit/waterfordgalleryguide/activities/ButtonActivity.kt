package org.wit.waterfordgalleryguide.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import org.wit.waterfordgalleryguide.R
import org.wit.waterfordgalleryguide.databinding.ActivityButtonBinding
import org.wit.waterfordgalleryguide.views.login.LoginView


class ButtonActivity: AppCompatActivity() {

    private lateinit var binding: ActivityButtonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_button)

        supportActionBar?.hide()

        binding = ActivityButtonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnScanQR.setOnClickListener {
            val scanner = IntentIntegrator(this)
            scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            scanner.setBeepEnabled(false)
            scanner.initiateScan()
        }

        binding.btnScanQR.setOnClickListener {
            val scanner = IntentIntegrator(this)
            scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            scanner.setBeepEnabled(false)
            scanner.initiateScan()
        }

        binding.btnLogout.setOnClickListener {
            val intent = Intent(this@ButtonActivity, LoginView::class.java)
            startActivity(intent)
        }


        binding.btnLogout.setOnClickListener {
            val intent = Intent(this@ButtonActivity, LoginView::class.java)
            startActivity(intent)
         }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null && result.contents != null) {
            val qrResult = result.contents
            if (qrResult.contains("https://") || qrResult.contains("http://")) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(qrResult))
                startActivity(intent)
            } else {
                binding.btnScanQR.text = qrResult
            }
        }
    }
}