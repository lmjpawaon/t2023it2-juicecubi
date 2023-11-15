package t2023.it2.juicecubi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        Log.d("Authentication", "User is ${if (auth.currentUser != null) "signed in" else "not signed in"}")

        //Initialize Variables
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)


        // Perform login when a button is clicked
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            signIn(email, password)
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signIn(email: String, password: String) {

        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Email and password cannot be empty.", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, navigate to main activity
                    Log.d("LoginActivity", "signInWithEmail:success")
                    Toast.makeText(this, "Authentication succeeded.", Toast.LENGTH_SHORT).show()

                    // Navigate to another activity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Close the login activity

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("LoginActivity", "signInWithEmail:failure", task.exception)

                    // Check specific error cases
                    task.exception?.message?.let { errorMessage ->
                        Log.e("LoginActivity", "Error message: $errorMessage")

                        // You can handle specific error messages here
                        when {
                            errorMessage.contains("INVALID_LOGIN_CREDENTIALS") -> {
                                // Wrong email/password
                                Toast.makeText(this, "Authentication Failed. Please check email or password.", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                // Other authentication failures
                                Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
    }
}