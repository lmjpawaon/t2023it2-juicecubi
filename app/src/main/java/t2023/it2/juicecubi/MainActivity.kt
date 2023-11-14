package t2023.it2.juicecubi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Test Firestore connection by retrieving data
        retrieveDataFromFirestore()
    }

    private fun retrieveDataFromFirestore() {
        db.collection("menu")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    // Enumerate all fields in the document
                    for ((field, value) in document.data) {
                        Log.d("FirestoreTest", "Document ID: ${document.id}, Field: $field, Value: $value")

//                        // Update TextView with retrieved data
//                        textView.append("$field: $value\n")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreTest", "Error getting documents: ", exception)
            }
    }
}