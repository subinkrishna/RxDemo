package com.subinkrishna.rxdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.toast
import com.subinkrishna.rxdemo.github.GithubUserActivity

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
                toast("Please enter a valid username")
            }
        }

    }
}
