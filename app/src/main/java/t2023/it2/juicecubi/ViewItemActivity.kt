package t2023.it2.juicecubi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class ViewItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_item)

        // Retrieve data from the intent
        val name = intent.getStringExtra("name")
        val description = intent.getStringExtra("description")
        val price = intent.getDoubleExtra("price", 0.0)
        val photoUrl = intent.getStringExtra("photoUrl")

        // Update the UI with the item details
        val nameTextView = findViewById<TextView>(R.id.nameTextView)
        val descriptionTextView = findViewById<TextView>(R.id.descriptionTextView)
        val priceTextView = findViewById<TextView>(R.id.priceTextView)
        val photoImageView = findViewById<ImageView>(R.id.photoImageView)

        nameTextView.text = name
        descriptionTextView.text = description
        priceTextView.text = "Php $price"

        // Load the image using Glide
        Glide.with(this)
            .load(photoUrl)
            .into(photoImageView)

        //Back To Main
        val backButton = findViewById<Button>(R.id.btnBack)

        backButton.setOnClickListener {
            finish()
        }
    }


}