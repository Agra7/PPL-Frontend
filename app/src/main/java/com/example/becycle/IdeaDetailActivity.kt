package com.example.becycle

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class IdeaDetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_idea_detail)

        val imageView = findViewById<ImageView>(R.id.idea_image)
        val titleText = findViewById<TextView>(R.id.idea_title)
        val descText = findViewById<TextView>(R.id.idea_description)
        val materialsText = findViewById<TextView>(R.id.idea_materials)
        val stepsText = findViewById<TextView>(R.id.idea_steps)
        val refText = findViewById<TextView>(R.id.idea_reference)

        val uploadBtn = findViewById<Button>(R.id.upload_button)

        // Get idea info from intent (this could be passed from adapter)
        val title = intent.getStringExtra("idea_title") ?: "Pot Planter"
        val desc = intent.getStringExtra("idea_description") ?: "No description"
        val materials = intent.getStringExtra("idea_materials") ?: ""
        val steps = intent.getStringExtra("idea_steps") ?: ""
        val references = intent.getStringExtra("idea_references") ?: ""

        titleText.text = title
        descText.text = desc
        materialsText.text = materials
        stepsText.text = steps
        refText.text = references

        // Optional: load a sample image
        imageView.setImageResource(R.drawable.plant_placeholder)

        uploadBtn.setOnClickListener {
            Toast.makeText(this, "Upload coming soon!", Toast.LENGTH_SHORT).show()
        }

        findViewById<ImageView>(R.id.back_button).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.upload_button).setOnClickListener {
            val uploadSheet = UploadBottomSheet()
            uploadSheet.show(supportFragmentManager, "UploadBottomSheet")
        }

    }
}
