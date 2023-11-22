package t2023.it2.juicecubi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.bumptech.glide.Glide
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import com.google.type.Date
import t2023.it2.juicecubi.LoginActivity
import t2023.it2.juicecubi.R

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: YourAdapter
    private lateinit var btnAdd: Button
    private lateinit var btnLogout: Button
    private val db = FirebaseFirestore.getInstance() // Initialize Firestore

    data class YourData(
        var dateCreated: Timestamp? = null,
        var description: String = "",
        var name: String = "",
        var photoUrl: String = "",
        var price: Double? = null
    )

    class YourAdapter(private val data: List<YourData>) :
        RecyclerView.Adapter<YourAdapter.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val photoImageView: ImageView = itemView.findViewById(R.id.photoImageView)
            val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
            val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
            val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
            val btnView: Button = itemView.findViewById(R.id.btnView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.cardview, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = data[position]

            // Load the image using Glide
            Glide.with(holder.itemView)
                .load(item.photoUrl)
                .placeholder(R.drawable.logo) // Placeholder image while loading
//                .error(R.drawable.error_image) // Image to show in case of an error
                .into(holder.photoImageView)

            holder.nameTextView.text = item.name
            holder.descriptionTextView.text = item.description
            holder.priceTextView.text = "Php ${item.price}"

            // Handle "View" button click if needed
            holder.btnView.setOnClickListener {
                val intent = Intent(holder.itemView.context, ViewItemActivity::class.java).apply {
                    // Pass data as extras to the intent
                    putExtra("name", item.name)
                    putExtra("description", item.description)
                    putExtra("price", item.price)
                    putExtra("photoUrl", item.photoUrl)
                }

                // Start ViewItemActivity
                holder.itemView.context.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return data.size
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        Log.d("Authentication", "User is ${if (auth.currentUser != null) "signed in" else "not signed in"}")

        // Set up the RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch data from Firestore
        db.collection("menu") // Replace with your collection name
            .get()
            .addOnSuccessListener { result ->
                val dataList = mutableListOf<YourData>()
                for (document in result) {
                    val yourData = document.toObject(YourData::class.java)
                    dataList.add(yourData)
                }

                // Set up the RecyclerView adapter
                adapter = YourAdapter(dataList)
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error Loading Menu: $e", Toast.LENGTH_SHORT).show()
            }

        // Logout and Add Button Declaration
        val logoutButton = findViewById<Button>(R.id.btnLogout)
        val addButton = findViewById<Button>(R.id.btnAdd)

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

        addButton.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
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

    }
}
