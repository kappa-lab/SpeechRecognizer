package com.kappa_lab.speech_recognizer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.Manifest.permission.RECORD_AUDIO
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat


/**
 * SpeechRecognizerを使って任意の文言が含まれているか判定する
 */
class MainActivity : AppCompatActivity() {

    companion object {
        const val tag: String = "SpRec"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Permission
        if (ContextCompat.checkSelfPermission(this, RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, RECORD_AUDIO)) {
                // reject
            } else {
                // accept
                ActivityCompat.requestPermissions(this, arrayOf(RECORD_AUDIO), 1)
            }
        }

        val voiceCommandService = VoiceCommandService(this)

        button.setOnClickListener {
            message.text = "Ready..."
            voiceCommandService.recognize(listOf("PLAY","STOP","PAUSE")) { s ->
                onRecognizedCommand(s)
            }
        }
    }

    fun onRecognizedCommand(command: List<String>) {
        if(command.count()==0) {
            message.text = "no hit"
            return
        }
        message.text = command[0]
    }
}
