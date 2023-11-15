package t2023.it2.juicecubi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private val collectionName = "menu"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Test Firestore connection by retrieving data and performing field rename
        GlobalScope.launch(Dispatchers.IO) {
            renameFieldInCollection()
        }
    }

    private suspend fun renameFieldInCollection() {
        try {
            val querySnapshot = db.collection(collectionName).get().await()

            for (document in querySnapshot.documents) {
                // Rename the field in each document
                val data = document.data
                if (data != null && data.containsKey("imageUrl")) {
                    val newValue = data["imageUrl"]
                    data.remove("imageUrl")
                    data["photoUrl"] = newValue

                    // Update the document with the new data
                    db.collection(collectionName).document(document.id).set(data).await()
                }
            }

            Log.d("FirestoreTest", "Field rename complete.")
        } catch (e: Exception) {
            Log.e("FirestoreTest", "Error during field rename: $e")
        }
    }
}