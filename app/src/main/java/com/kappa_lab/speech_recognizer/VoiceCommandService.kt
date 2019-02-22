package com.kappa_lab.speech_recognizer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import java.util.*

class VoiceCommandService{
    companion object {
        const val tag: String = "VoiceCommandService"
    }

    private val srIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "com.kappa_lab.speech_recognizer")
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH.toString())
    }

    private var matcher: List<String> = listOf()
    private var callback: (List<String>) -> Unit = {}
    private val recognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(bundle: Bundle) {
            Log.d(tag, "onReadyForSpeech")
        }

        override fun onBeginningOfSpeech() {
            Log.d(tag, "onBeginningOfSpeech")
        }

        override fun onRmsChanged(v: Float) {
//            Log.d(tag, "onRmsChanged")
        }

        override fun onBufferReceived(bytes: ByteArray) {
            Log.d(tag, "onBufferReceived")
        }

        override fun onEndOfSpeech() {
            Log.d(tag, "onEndOfSpeech")
        }

        override fun onError(i: Int) {
            SpeechRecognizer.ERROR_AUDIO
            Log.d(tag, "onError $i")
            when(i){
//                SpeechRecognizer.ERROR_NO_MATCH -> message.text = "SpeechRecognizer.ERROR_NO_MATCH"
            }
        }

        override fun onResults(bundle: Bundle) {
            val key = SpeechRecognizer.RESULTS_RECOGNITION
            val result = bundle.getStringArrayList(key)
            val hit= mutableListOf<String>()

            result?.let {
                result.forEach { r ->
                    matcher.forEach { m ->
                        if (!hit.contains(m) && r.contains(m, true)) hit.add(m)
                    }
                }
            }
            Log.d(tag, "onResults raw:$result, hit:$hit")
            callback(hit)
        }

        override fun onPartialResults(bundle: Bundle) {}

        override fun onEvent(i: Int, bundle: Bundle) {}
    }
    private val recognizer:SpeechRecognizer

    constructor(context: Context){
        recognizer = SpeechRecognizer.createSpeechRecognizer(context)
        recognizer.setRecognitionListener(recognitionListener)
    }
    
    fun recognize(matcher: List<String>, callback:(List<String>)->Unit){
        this.matcher = matcher
        this.callback = callback
        recognizer.startListening(srIntent)
    }
}