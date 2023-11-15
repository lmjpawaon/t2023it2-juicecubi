package t2023.it2.juicecubi

/* TEMPLATE FOR FIREBASE TEST CONNECT */

//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.util.Log
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.tasks.await
//
//class MainActivity : AppCompatActivity() {
//    private val db = FirebaseFirestore.getInstance()
//    private val collectionName = "menu"
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        // Test Firestore connection by retrieving data and performing field rename
//        GlobalScope.launch(Dispatchers.IO) {
//            renameFieldInCollection()
//        }
//    }
//
//    private suspend fun renameFieldInCollection() {
//        try {
//            val querySnapshot = db.collection(collectionName).get().await()
//
//            for (document in querySnapshot.documents) {
//                // Rename the field in each document
//                val data = document.data
//                if (data != null && data.containsKey("imageUrl")) {
//                    val newValue = data["imageUrl"]
//                    data.remove("imageUrl")
//                    data["photoUrl"] = newValue
//
//                    // Update the document with the new data
//                    db.collection(collectionName).document(document.id).set(data).await()
//                }
//            }
//
//            Log.d("FirestoreTest", "Field rename complete.")
//        } catch (e: Exception) {
//            Log.e("FirestoreTest", "Error during field rename: $e")
//        }
//    }
//}

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        Log.d("Authentication", "User is ${if (auth.currentUser != null) "signed in" else "not signed in"}")

        // Find the logout button by ID
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        /*val greetingTextView = findViewById<TextView>(R.id.greetingTextView)

        // Set the greeting based on the user's display name
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val displayName = currentUser.displayName
            Log.d("CurrentUser", "User is $displayName")
            greetingTextView.text = "Hello, $displayName!"
        }*/

        // Set a click listener for the logout button
        logoutButton.setOnClickListener {
            // Logout the user
            auth.signOut()
            Toast.makeText(this, "User Signed Out.", Toast.LENGTH_SHORT).show()

            // Redirect to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Optional: Close the main activity
        }
    }

    override fun onStart() {
        super.onStart()

        // Check if the user is signed in
        if (auth.currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        // If the user is already signed in, you can optionally perform other actions here
    }
}