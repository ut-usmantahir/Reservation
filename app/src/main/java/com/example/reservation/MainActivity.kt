package com.example.reservation

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.webkit.PermissionRequest
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.reservation.signin.Login
import com.example.reservation.signin.SignUp
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener

//import com.example.reservation.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), PermissionListener {

//    lateinit var mainBiniding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(R.layout.activity_main)

        requestPermissions()


    }

    fun funCreateAccount(view: View){
        intent = Intent(this, SignUp::class.java)

        startActivity(intent)
        finish()
    }
    fun funLoginAccount(view: View){
        intent = Intent(this, Login::class.java)

        startActivity(intent)
        finish()
    }



    private fun requestPermissions(){

        val permissions = listOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).toString()
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(this)
            .check()
    }


    override fun onPermissionGranted(response: PermissionGrantedResponse?) {

//        Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show()
    }


    override fun onPermissionRationaleShouldBeShown(
        permission: com.karumi.dexter.listener.PermissionRequest?,
        token: PermissionToken?
    ) {
        token?.continuePermissionRequest()
        Toast.makeText(
            this,
            "Permission onPermissionRationaleShouldBeShown",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        showSettingsDialog()
        Toast.makeText(
            this,
            "Permission required to use gallery storage",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showSettingsDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton("GOTO SETTINGS"
        ) { dialog, which ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton("Cancel"
        ) { dialog, which -> dialog.cancel() }
        builder.show()
    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }


}
