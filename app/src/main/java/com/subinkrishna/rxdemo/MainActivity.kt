package com.subinkrishna.rxdemo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.subinkrishna.rxdemo.github.ui.GithubUserActivity

/**
 * Launcher activity
 *
 * @author Subinkrishna Gopi
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.submit).setOnClickListener {
            val username = findViewById<EditText>(R.id.user_id).text
            if (username.isNotEmpty()) {
                startActivity(GithubUserActivity.getStartIntent(this, username.toString()))
            } else {
                Toast.makeText(this, "Please enter a valid username", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
