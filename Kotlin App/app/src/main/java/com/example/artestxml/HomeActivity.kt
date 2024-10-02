// app/src/main/java/com/example/artestxml/HomeActivity.kt
package com.example.artestxml

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class HomeActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var progressBar: ProgressBar
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        val navigateButton: Button = findViewById(R.id.button)
        val generateButton: Button = findViewById(R.id.generateButton)
        val editText: EditText = findViewById(R.id.editTextPrompt)
        imageView = findViewById(R.id.imageView)
        progressBar = findViewById(R.id.progressBar)

        // Navigate to MainActivity
        navigateButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Generate image using the model
        generateButton.setOnClickListener {
            val prompt = editText.text.toString().trim()
            if (prompt.isNotEmpty()) {
                generateImageFromText(prompt)
            } else {
                Toast.makeText(this, "Please enter a prompt", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateImageFromText(prompt: String) {
        val apiKey = "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" // Securely access the API key

        // Show ProgressBar
        progressBar.visibility = View.VISIBLE

        // Launch a coroutine on IO dispatcher for network operations
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Prepare the JSON request body
                val json = JSONObject().apply {
                    put("inputs", prompt)
                    // Optional: Add parameters for customization
                    put(
                        "parameters",
                        JSONObject().apply {
                            put("width", 512)
                            put("height", 512)
                            put("num_inference_steps", 50)
                            put("guidance_scale", 7.5)
                        }
                    )
                }

                val requestBody = RequestBody.create(
                    "application/json".toMediaTypeOrNull(),
                    json.toString()
                )

                val request = Request.Builder()
                    .url("https://api-inference.huggingface.co/models/black-forest-labs/FLUX.1-dev") // Replace with desired model
                    .addHeader("Authorization", "Bearer $apiKey")
                    .post(requestBody)
                    .build()

                // Execute the request
                val response = client.newCall(request).execute()

                if (!response.isSuccessful) {
                    // Handle unsuccessful response
                    val errorBody = response.body?.string()
                    val errorMessage = if (errorBody != null) {
                        try {
                            val jsonResponse = JSONObject(errorBody)
                            jsonResponse.getJSONObject("error").getString("message")
                        } catch (e: Exception) {
                            "Unknown error occurred."
                        }
                    } else {
                        "Unknown error occurred."
                    }

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@HomeActivity, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
                    }
                    return@launch
                }

                // Parse the binary image data
                val imageBytes = response.body?.bytes()

                if (imageBytes != null) {
                    // Convert bytes to Bitmap
                    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                    withContext(Dispatchers.Main) {
                        // Display the image using Glide
                        Glide.with(this@HomeActivity)
                            .load(bitmap)
//                            .placeholder(R.drawable.placeholder) // Optional: placeholder image
//                            .error(R.drawable.error) // Optional: error image
                            .into(imageView)

                        // Hide ProgressBar
                        progressBar.visibility = View.GONE
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@HomeActivity, "Failed to retrieve image.", Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@HomeActivity, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
            }
        }
    }
}
