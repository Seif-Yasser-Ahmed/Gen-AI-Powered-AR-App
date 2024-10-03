package com.example.artestxml
import android.os.Build
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class HomeActivity : AppCompatActivity() {

    private val client = OkHttpClient()
    private val apiKey = "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"  // Use secured API key access

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor("#FF000000")
        }

        val generateButton: Button = findViewById(R.id.generateButton)
        val editText: EditText = findViewById(R.id.editTextPrompt)

        // Request storage permission to save the image
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }

        // Generate image and save it as PNG
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
        // Launch coroutine for network operation
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Prepare JSON request body
                val json = JSONObject().apply {
                    put("inputs", prompt)
                    put("parameters", JSONObject().apply {
                        put("width", 512)
                        put("height", 512)
                        put("num_inference_steps", 50)
                        put("guidance_scale", 7.5)
                    })
                }

                val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), json.toString())

                val request = Request.Builder()
                    .url("https://api-inference.huggingface.co/models/black-forest-labs/FLUX.1-dev")
                    .addHeader("Authorization", "Bearer $apiKey")
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    // Retrieve image bytes
                    val imageBytes = response.body?.bytes()
                    if (imageBytes != null) {
                        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        // Save the image as PNG
                        saveImageAsPNG(bitmap)
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@HomeActivity, "Failed to retrieve image", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@HomeActivity, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@HomeActivity, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveImageAsPNG(bitmap: Bitmap) {
        val fileName = "generated_image.png"
        val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "GeneratedImages")
        if (!dir.exists()) {
            dir.mkdirs()
        }

        val imageFile = File(dir, fileName)

        try {
            val fos = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()

            runOnUiThread {
                Toast.makeText(this, "Image saved at: ${imageFile.absolutePath}", Toast.LENGTH_LONG).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            runOnUiThread {
                Toast.makeText(this, "Error saving image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
