
package com.utsman.kemana.driver

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseUser
import com.utsman.easygooglelogin.EasyGoogleLogin
import com.utsman.easygooglelogin.LoginResultListener
import com.utsman.kemana.base.RxAppCompatActivity
import com.utsman.kemana.base.intentTo
import com.utsman.kemana.remote.driver.Driver
import com.utsman.kemana.remote.driver.LoginRequest
import com.utsman.kemana.remote.driver.RemoteInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthActivity : RxAppCompatActivity(), LoginResultListener {

    private lateinit var googleLogin: EasyGoogleLogin
    private val activityScope = CoroutineScope(Dispatchers.Main)

    private var debugTapCount = 0
    private val DEBUG_TAP_TARGET = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        googleLogin = EasyGoogleLogin(this)
        val token = getString(R.string.default_web_client_id)
        googleLogin.initGoogleLogin(token, this)

        val inpUserId = findViewById<EditText>(R.id.inp_user_id)
        val inpPassword = findViewById<EditText>(R.id.inp_password)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val btnGoogleSign = findViewById<SignInButton>(R.id.btn_google_sign)

        btnLogin.setOnClickListener {
            val userId = inpUserId.text.toString()
            val password = inpPassword.text.toString()

            if (userId == "debug" && password == "debug") {
                debugTapCount++
                if (debugTapCount >= DEBUG_TAP_TARGET) {
                    showToast("Masuk mode debug offline...")
                    loginWithMockDriver("debug")
                }
                return@setOnClickListener
            }

            debugTapCount = 0 // Reset jika bukan debug

            if (userId.isBlank() || password.isBlank()) {
                showToast("User ID dan Password tidak boleh kosong")
                return@setOnClickListener
            }

            activityScope.launch {
                try {
                    val loginRequest = LoginRequest(id = userId, password = password)
                    val response = RemoteInstance.api.login(loginRequest)

                    if (response.isSuccessful && response.body()?.status == "OK") {
                        val driver = response.body()?.payload?.firstOrNull()
                        if (driver != null) {
                            navigateToMain(driver)
                        } else {
                            showError("Gagal mendapatkan data driver.")
                        }
                    } else {
                        val errorMsg = response.body()?.message ?: "Login gagal, coba lagi."
                        // Jika login gagal (misal: password salah) & ini mode demo, coba offline
                        if (userId == "000001" && password == "demo") {
                            showToast("Login online gagal, masuk mode demo offline...")
                            loginWithMockDriver("demo")
                        } else {
                            showError(errorMsg)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("AuthActivity", "Login error", e)
                    // JIKA KONEKSI GAGAL & ini adalah mode demo, masuk mode offline
                    if (userId == "000001" && password == "demo") {
                        showToast("Koneksi gagal, masuk mode demo offline...")
                        loginWithMockDriver("demo")
                    } else {
                        showError("Tidak dapat terhubung ke server.")
                    }
                }
            }
        }

        btnGoogleSign.setOnClickListener {
            googleLogin.signIn(this)
        }
    }

    private fun loginWithMockDriver(mode: String) {
        val mockDriver = if (mode == "debug") {
            Driver(
                id = "-1", name = "Debug User", email = "debug@kemana.com",
                vehiclePlate = "B 1234 DBG", vehicleType = "Car", photoUrl = null
            )
        } else { // mode == "demo"
            Driver(
                id = "000001-offline", name = "Demo User (Offline)", email = "demo-offline@kemana.com",
                vehiclePlate = "B 0001 DEM", vehicleType = "Motor", photoUrl = null
            )
        }
        navigateToMain(mockDriver)
    }

    private fun navigateToMain(driver: Driver) {
        val bundle = bundleOf("driver" to driver)
        intentTo(MainActivity::class.java, bundle)
        finish()
    }

    private suspend fun showError(message: String) {
        withContext(Dispatchers.Main) {
            showToast(message)
        }
    }
    
    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    // --- Metode Google Login lainnya ---
    override fun onStart() {
        super.onStart()
        googleLogin.initOnStart()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        googleLogin.onActivityResult(this, requestCode, data)
    }

    override fun onLoginSuccess(user: FirebaseUser) {
        showToast("Login Google berhasil! (Implementasi backend belum siap)")
    }

    override fun onLoginFailed(exception: Exception?) {
        showToast("Login Google Gagal: ${exception?.message}")
    }

    override fun onLogoutSuccess(task: com.google.android.gms.tasks.Task<Void>?) {}
    override fun onLogoutError(exception: Exception?) {}
}
