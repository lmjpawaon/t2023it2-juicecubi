package t2023.it2.juicecubi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

class AddItemActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnBack = findViewById<Button>(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }

        btnSubmit.setOnClickListener {
            // Gather information from EditText fields
            val name = findViewById<EditText>(R.id.editTextName).text.toString()
            val description = findViewById<EditText>(R.id.editTextDescription).text.toString()
            val price = findViewById<EditText>(R.id.editTextPrice).text.toString().toDoubleOrNull()
            val imageUrl = findViewById<EditText>(R.id.editTextImageUrl).text.toString()

            // Validate the input
            if (name.isNotEmpty() && description.isNotEmpty() && price != null && imageUrl.isNotEmpty()) {
                // Create a map to represent the item
                val newItem = mapOf(
                    "name" to name,
                    "description" to description,
                    "price" to price,
                    "photoUrl" to imageUrl,
                    "dateCreated" to Date() // Add the current date/time
                )

                // Save the record to the "menu" collection in Firestore
                db.collection("menu").add(newItem)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show()

                        // Redirect back to MainActivity
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish() // Optional: Close the AddItemActivity
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error adding item: $e", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}