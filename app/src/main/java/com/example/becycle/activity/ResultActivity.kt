package com.example.becycle.activity

import android.os.Bundle
import android.transition.TransitionInflater
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.becycle.adapters.CenterAdapter
import com.example.becycle.items.Idea
import com.example.becycle.adapters.IdeaAdapter
import com.example.becycle.R
import java.io.File

class ResultActivity : BaseActivity() {

    private lateinit var resultImage: ImageView
    private lateinit var labelText: TextView
    private lateinit var ideaList: RecyclerView
    private lateinit var tabIdea: TextView
    private lateinit var tabCenter: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.sharedElementEnterTransition = TransitionInflater.from(this)
            .inflateTransition(android.R.transition.move)

        setContentView(R.layout.activity_result)

        setupBottomNavIfNeeded()

        resultImage = findViewById(R.id.result_image_view)
        labelText = findViewById(R.id.detected_label)
        ideaList = findViewById(R.id.idea_list)
        tabIdea = findViewById(R.id.tab_idea)
        tabCenter = findViewById(R.id.tab_center)

        val imagePath = intent.getStringExtra("image_path")
        val imageUri = intent.getStringExtra("image_uri")

        if (imagePath != null) {
            Glide.with(this)
                .load(File(imagePath))
                .into(resultImage)
            resultImage.transitionName = "shared_image"
        } else if (imageUri != null) {
            Glide.with(this)
                .load(imageUri)
                .into(resultImage)
            resultImage.transitionName = "shared_image"
        }

        labelText.text = "Plastic Bottle"

        ideaList.layoutManager = LinearLayoutManager(this)
        showIdeas()

        tabIdea.setOnClickListener {
            highlightTab(selected = tabIdea, other = tabCenter)
            showIdeas()
        }

        tabCenter.setOnClickListener {
            highlightTab(selected = tabCenter, other = tabIdea)
            showCenters()
        }
    }

    private fun highlightTab(selected: TextView, other: TextView) {
        selected.setBackgroundResource(R.drawable.tab_selected)
        selected.setTextColor(ContextCompat.getColor(this, R.color.green))

        other.setBackgroundResource(android.R.color.transparent)
        other.setTextColor(ContextCompat.getColor(this, R.color.gray))
    }

    private fun showIdeas() {
        val ideas = listOf(
            Idea(
                title = "Pot Planter",
                description = "Turn a plastic bottle into a decorative plant pot. It’s perfect for herbs or succulents.",
                materials = "• Plastic bottle\n• Scissors\n• Paint\n• Soil\n• Seeds or plant",
                steps = "1. Cut the bottle in half\n2. Decorate it with paint\n3. Fill with soil\n4. Add plant\n5. Place on windowsill",
                references = "https://example.com/pot-planter\nhttps://youtube.com/example1"
            ),
            Idea(
                title = "Bird Feeder",
                description = "Create a simple bird feeder to hang in your backyard.",
                materials = "• Plastic bottle\n• Wooden spoons\n• String\n• Birdseed",
                steps = "1. Cut small holes for spoons\n2. Fill with seed\n3. Hang on tree\n4. Watch birds enjoy",
                references = "https://example.com/bird-feeder"
            )
            // Add more ideas here...
        )

        ideaList.adapter = IdeaAdapter(ideas)
    }


    private fun showCenters() {
        val centers = listOf(
            "Pusat Daur Ulang Bandung (3.56 Km Away)\nJl. Buah Batu no.67 Kec. Gede Bage, 157368",
            "Location 2\nJl. lorem ipsum no. 12 kec. lorem ipsum, 12345.",
            "Location 3\nJl. lorem ipsum no. 12 kec. lorem ipsum, 12345.",
            "Location 4\nJl. lorem ipsum no. 12 kec. lorem ipsum, 12345."
        )
        ideaList.adapter = CenterAdapter(centers)
    }
}
