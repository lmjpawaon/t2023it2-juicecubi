package t2023.it2.juicecubi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class RegistrationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // Initialize Firestore
        db = FirebaseFirestore.getInstance()

        // Initialize UI components
        val displayNameEditText = findViewById<EditText>(R.id.displayNameEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val registerButton = findViewById<Button>(R.id.registerButton)

        // Set a click listener for the register button
        registerButton.setOnClickListener {
            val displayName = displayNameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (displayName.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Register the user and save credentials
            registerUser(displayName, email, password)
        }
    }

    private fun registerUser(displayName: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registration success, update UI or navigate to another activity
                    val user = auth.currentUser

                    // For later - To display displayName in MainActivity
                    /*val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                Log.d("RegistrationActivity", "User profile updated.")
                            } else {
                                Log.e("RegistrationActivity", "Failed to update user profile.", updateTask.exception)
                            }
                        }*/
                    saveUserToFirestore(user?.uid, displayName, email)

                    // Navigate to another activity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Optional: Close the registration activity
                } else {
                    // If registration fails, display a message to the user.
                    Toast.makeText(this, "Registration failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserToFirestore(userId: String?, displayName: String, email: String) {
        if (userId != null) {
            val user = hashMapOf(
                "displayName" to displayName,
                "email" to email
            )

            db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener {
                    Log.d("RegistrationActivity", "User saved to Firestore successfully.")
                }
                .addOnFailureListener { e ->
                    Log.e("RegistrationActivity", "Error saving user to Firestore: $e")
                }
        }
    }
}