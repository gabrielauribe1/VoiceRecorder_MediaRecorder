package com.example.audio_mediarecorder2

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import java.io.File


class MainActivity : AppCompatActivity() {

    lateinit var button: Button
    lateinit var button2: Button
    lateinit var button3: Button
    lateinit var mr: MediaRecorder

    @RequiresApi(Build.VERSION_CODES.R)
    //@SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById<View>(R.id.button) as Button
        button2 = findViewById<View>(R.id.button2) as Button
        button3 = findViewById<View>(R.id.button3) as Button

        button.isEnabled=false
        button2.isEnabled=false

        mr= MediaRecorder()

        val audioPath = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File(audioPath, "recording1.3gp")
        val fileProvider = FileProvider.getUriForFile( this, "${ BuildConfig.APPLICATION_ID }.fileprovider", file)


        /*Permiso para acceder a microfono*/
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE),111)

        button.isEnabled=true

        /*Start Recording*/
        button.setOnClickListener{
            mr.setAudioSource(MediaRecorder.AudioSource.MIC)
            mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mr.setOutputFile(file.absolutePath)
            mr.prepare()
            mr.start()
            button2.isEnabled=true
            button.isEnabled=false
        }

        /*Stop Recording*/
        button2.setOnClickListener{
            mr.stop()
            button.isEnabled=true
            button2.isEnabled=false
        }

        /*Play Recording*/
        button3.setOnClickListener{
            var mp = MediaPlayer()
            mp.setDataSource(file.absolutePath)
            mp.prepare()
            mp.start()
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==111 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            button.isEnabled=true
    }
}