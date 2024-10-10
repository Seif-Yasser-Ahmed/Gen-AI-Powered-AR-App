package com.example.artestxml

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.ArModelNode

class MainActivity : AppCompatActivity() {

    private lateinit var sceneView: ArSceneView
    private lateinit var place: FloatingActionButton
    private lateinit var navigateButton: Button
    private lateinit var modelNode: ArModelNode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)
        sceneView = findViewById(R.id.sceneView)
        place = findViewById(R.id.button)
        navigateButton = findViewById(R.id.navigateButton)
        val glbUrl = intent.getStringExtra("GLB_URL")

        modelNode = ArModelNode().apply {
            loadModelGlbAsync(
                glbFileLocation = glbUrl ?: "kiki.glb" // Use the URL or a default file if URL is null
            ) {
                sceneView.planeRenderer.isVisible = true
            }
            onAnchorChanged = {
                place.visibility = if (anchor != null) View.GONE else View.VISIBLE
            }
        }

        sceneView.addChild(modelNode)

        place.setOnClickListener {
            anchorObject()
        }

        navigateButton.setOnClickListener {
            finish()
        }
    }

    private fun anchorObject() {
        modelNode.anchor()
        sceneView.planeRenderer.isVisible = true
    }
}