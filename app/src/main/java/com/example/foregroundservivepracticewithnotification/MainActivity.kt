package com.example.foregroundservivepracticewithnotification

import android.Manifest
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var myService: MyService
//    private lateinit var receiver: BroadcastReceiver
    private var editText:EditText?=null
    private var startButton: Button?=null
    private var stopButton: Button?=null
    private var textView:TextView?=null
    private val NOTIFICATION_PERMISSION_REQUEST_CODE = 123
    private val  receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val message = intent?.getStringExtra("MESSAGE")
            if (message != null) {
              Log.d("alsaa","INCREMENTED")
                counter++
                textView?.text = counter.toString()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editText=findViewById(R.id.edit_text)
        startButton=findViewById(R.id.startBtn)
        stopButton=findViewById(R.id.stopBtn)
        textView=findViewById(R.id.text_view)
//        val message2 = intent.getStringExtra("MESSAGE")
//        textView?.text = counter.toString()
//        counter++
//        receiver = object : BroadcastReceiver() {
//            override fun onReceive(context: Context, intent: Intent) {
//                val message = intent.getStringExtra("MESSAGE")
//                textView?.text = "89"
//            }
//        }
        if(!isNotificationPermissionGranted()){
            Log.d("permmm","not granted if block running")
            requestNotificationPermission()

        }else{
            Log.d("permmm"," granted else block running")
        }
        startButton?.setOnClickListener {
            if(isNotificationPermissionGranted()){
            val intent= Intent(this,MyService::class.java)
            intent.putExtra("key",true)
            var input=editText!!.text.toString()
            intent.putExtra("key2",input)
            ContextCompat.startForegroundService(this,intent)
        }else{

                val builder= AlertDialog.Builder(this)
                builder.setTitle("GIVE PERMISSION From Setting")
                builder.setMessage("You have give permission from the settings to show notifications ")
                builder.setPositiveButton("Settings"){dialog,_->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)

                    }
                val alert=builder.create()
                alert.show()
        }


        }
        stopButton?.setOnClickListener {
            val intent= Intent(this,MyService::class.java)
            intent.putExtra("key",false)
            ContextCompat.startForegroundService(this,intent)
        }
        val filter = IntentFilter("ACTION_INCREMENT")
        registerReceiver(receiver, filter)

    }

    override fun onResume() {
        super.onResume()
//        registerReceiver(receiver, IntentFilter(MyService::class.java.name))
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()

    }
    override fun onPause() {
        super.onPause()
//        unregisterReceiver(receiver)
    }
    companion object{
        var counter = 0
    }




    // Check if the notification permission is granted
    private fun isNotificationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
    }


    // Request the notification permission at runtime
    private fun requestNotificationPermission() {
        Log.d("permmm","REQUESTING PERMISSION GRANTINGG" )
        if (Build.VERSION.SDK_INT > 32){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
            NOTIFICATION_PERMISSION_REQUEST_CODE
        )}
    }

    // Handle the result of the permission request

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                Log.d("aaa", "Permission granted.")
                Toast.makeText(this, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("aaa", "Permission denied.")
                Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_SHORT).show()
                checkUserRequestedDontAskAgain()
            }
        }
    }
    private fun checkUserRequestedDontAskAgain() {
        Log.d("aaa", "Checking outside of if user denied permission forcefully...")

        if (!shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            Log.d("aaa", "inside of if User denied permission forcefully.")

            val builder=AlertDialog.Builder(this)
            builder.setTitle("PERMISSION DENIED FOR ALWAYS")
            builder.setMessage("You have denied the permission you will not be able to to see notificatons from this app")
            builder.setPositiveButton("OK"){dialog,_->dialog.cancel()}
            val alert=builder.create()
            alert.show()
            Toast.makeText(this, "PERMISSION DENIED FORCEFULLY", Toast.LENGTH_SHORT).show()

        }
    }


}
