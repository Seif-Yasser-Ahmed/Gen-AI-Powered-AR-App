package com.example.artestxml
import android.text.Editable
import android.text.TextWatcher
import android.view.animation.AnimationUtils
import android.content.Intent
import android.os.Build
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
    private val apiKey = "msy_T0ootoDAJTwMkWD7sEr5tlccHFFOaa2eTAoI"  // Use secured API key access
    private lateinit var generateButton: Button
    private lateinit var navigateToMainButton: Button
//    private lateinit var progressBar: ProgressBar
    private lateinit var progressTextView: TextView
    private var glb_url = ""

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        window.statusBarColor = Color.parseColor("#FF000000")
        val clearIcon: ImageView = findViewById(R.id.clearIcon)
        val editText: EditText = findViewById(R.id.editTextPrompt)
        generateButton = findViewById(R.id.generateButton)
        navigateToMainButton = findViewById(R.id.navigateToMainButton)
//        progressBar = findViewById(R.id.progressBar) // Make sure you have a ProgressBar in your layout
        progressTextView = findViewById(R.id.progressTextView)

//        // Load the blinking animation
        val blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink)
//
//        // Start the animation on the TextView
//        progressTextView.startAnimation(blinkAnimation)

        // Initially set progressTextView to invisible
        progressTextView.visibility = View.INVISIBLE

        // Request storage permission to save the object file
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }

        // Show the clear icon only when there is text in the EditText
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearIcon.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Clear the EditText when the clear icon is clicked
        clearIcon.setOnClickListener {
            editText.text.clear()
        }

        // Generate object file and save it as .obj
        generateButton.setOnClickListener {
            val prompt = editText.text.toString().trim()
            if (prompt.isNotEmpty()) {
                // Start the animation on the TextView when the button is clicked
                progressTextView.visibility = View.VISIBLE
                progressTextView.startAnimation(blinkAnimation)

                // Proceed with the generation process
                generateObjectFromText(prompt)
                generateButton.isEnabled = false
            } else {
                Toast.makeText(this, "Prompt is empty.", Toast.LENGTH_SHORT).show()
            }
        }


        navigateToMainButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("GLB_URL", glb_url)
            startActivity(intent)
        }
    }

    private fun generateObjectFromText(prompt: String) {
        // Launch coroutine for network operation
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Show progress bar
                withContext(Dispatchers.Main) {

                    progressTextView.visibility = View.INVISIBLE
                    progressTextView.text = "Generating: 0%"
                }

                // Prepare JSON request body
                val json = JSONObject().apply {
                    put("mode", "preview")
                    put("prompt", prompt)
                }

                val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), json.toString())

                val request = Request.Builder()
                    .url("https://api.meshy.ai/v2/text-to-3d")
                    .addHeader("Authorization", "Bearer $apiKey")
                    .post(requestBody)
                    .build()

                var response = client.newCall(request).execute()

                val responseBody = response.body?.string()
                Log.d("Response", response.toString())
                Log.d("ResponseBody", responseBody.toString())
                if (response.isSuccessful) {
                    // Retrieve object file bytes
                    val jsonObject = JSONObject(responseBody!!)
                    val id = jsonObject.getString("result")
                    Log.d("ID", id)
                    val getRequest1 = Request.Builder()
                        .url("https://api.meshy.ai/v2/text-to-3d/$id")
                        .addHeader("Authorization", "Bearer $apiKey")
                        .get()
                        .build()
                    // Simulate progress update (wait for 70 seconds)
                    var getResponse = client.newCall(request).execute()
                    var getResponseBody = getResponse.body?.string()
                    var progress = 0
                    while (progress < 100) {
                        // Wait for 7 seconds
                        withContext(Dispatchers.Main) {
                            progressTextView.visibility = View.VISIBLE
                            progressTextView.text = "Generating: ${progress}%"
                        }
                        delay(2000)
                        response = client.newCall(getRequest1).execute()
                        val newResponseBody = response.body?.string()
                        val newJsonObject = JSONObject(newResponseBody!!)
                        Log.d("NewJsonObject", newJsonObject.toString())
                        progress = newJsonObject.getInt("progress")
                        Log.d("Progress", progress.toString())
                    }

                    val refineJson = JSONObject().apply {
                        put("mode", "refine")
                        put("preview_task_id", id)
                    }
                    val refinerRequestBody = RequestBody.create("application/json".toMediaTypeOrNull(), refineJson.toString())
                    val refineRequest = Request.Builder()
                        .url("https://api.meshy.ai/v2/text-to-3d")
                        .addHeader("Authorization", "Bearer $apiKey")
                        .post(refinerRequestBody)
                        .build()

                    val refineResponse = client.newCall(refineRequest).execute()
                    val refineResponseBody = refineResponse.body?.string()
                    Log.d("RefineResponse", refineResponseBody.toString())
                    if (refineResponse.isSuccessful) {
                        val refineJsonObject = JSONObject(refineResponseBody!!)
                        val refineId = refineJsonObject.getString("result")
                        Log.d("RefineID", refineId)
                        val getRequest = Request.Builder()
                            .url("https://api.meshy.ai/v2/text-to-3d/$refineId")
                            .addHeader("Authorization", "Bearer $apiKey")
                            .get()
                            .build()
                        // Simulate progress update (wait for 180 seconds)
                        getResponse = client.newCall(getRequest).execute()
                        getResponseBody = getResponse.body?.string()
                        var newJsonObject = JSONObject(getResponseBody!!)
                        progress = newJsonObject.getInt("progress")
                        while(progress < 100) {
                             // Wait for 9 seconds
                            withContext(Dispatchers.Main) {
                                progressTextView.visibility = View.VISIBLE
                                progressTextView.text = "Refining: ${progress}%"
                            }
                            delay(2000)
                            response = client.newCall(getRequest).execute()
                            val newResponseBody = response.body?.string()
                            newJsonObject = JSONObject(newResponseBody!!)
                            Log.d("NewJsonObject", newJsonObject.toString())
                            progress = newJsonObject.getInt("progress")
                            Log.d("Progress", progress.toString())
                        }

                        // Add code to make a GET request to the same API


                        // Send the id we got to the API and get the response
                        val getResponse = client.newCall(getRequest).execute()
                        val getResponseBody = getResponse.body?.string()
                        Log.d("GetResponse", getResponseBody.toString())

                        if(getResponse.isSuccessful){
                            val getJson = JSONObject(getResponseBody!!)
                            val model_urls = getJson.getString("model_urls")
                            glb_url = JSONObject(model_urls).getString("glb")
                            Log.d("GLB_URL", glb_url)
                            runOnUiThread {
                                navigateToMainButton.visibility = Button.VISIBLE
                            }
                        }

                        withContext(Dispatchers.Main)
                        {
                            progressTextView.clearAnimation()  // Stop the animation
                            progressTextView.text = "Done: 100%"
                            generateButton.isEnabled = true
                            // Toast.makeText(this@HomeActivity, "Object file retrieved successfully", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else
                    {
                        withContext(Dispatchers.Main) {
                            progressTextView.visibility = View.INVISIBLE
                            generateButton.isEnabled = true
                            Toast.makeText(this@HomeActivity, "Error: ${refineResponse.message}", Toast.LENGTH_SHORT).show()
                        }
                    }

                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    progressTextView.visibility = View.INVISIBLE
//                    progressBar.visibility = View.GONE
                    Toast.makeText(this@HomeActivity, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveObjectAsOBJ(objectBytes: ByteArray) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            return
        }

        val fileName = "generated_object.obj"
        val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "GeneratedObjects")
        if (!dir.exists()) {
            dir.mkdirs()
        }

        val objectFile = File(dir, fileName)

        try {
            val fos = FileOutputStream(objectFile)
            fos.write(objectBytes)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
            runOnUiThread {
                Toast.makeText(this, "Error saving object file: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
