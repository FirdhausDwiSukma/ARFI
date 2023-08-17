package org.d3ifcool.arfi

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import org.d3ifcool.arfi.databinding.ActivitySplashScreenBinding

@Suppress("DEPRECATION")
@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private lateinit var handler: Handler
    private lateinit var binding : ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        
        handler = Handler()
        handler.postDelayed({
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}

//    fun isConnected(): Boolean{
//        var connectivityManager: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        var network: NetworkInfo?= connectivityManager.activeNetworkInfo
//        if (network!= null){
//            if (network.isConnected){
//                return true
//            }
//        }
//        return false
//    }
    //if (isConnected() == false){
    //    var builder = AlertDialog.Builder(applicationContext)
    //
    //    builder.setTitle(getString(R.string.alert_title))
    //    builder.setMessage(getString(R.string.alert_desc))
    //
    //    builder.setPositiveButton(getString(R.string.dialog_button_splash)){dialogInteface, which->
    //        finish()
    //        System.exit(0)
    //    }
    //
    //    builder.setNegativeButton(getString(R.string.dialog_Negbutton_splash)){dialogInteface, which->
    //        startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
    //        finish()
    //        System.exit(0)
    //    }
    //
    //    val alertDialog: AlertDialog = builder.create()
    //    alertDialog.setCancelable(false)
    //    alertDialog.show()
    //}
    //else
    //{
    //    handler = Handler()
    //    handler.postDelayed({
    //        val intent = Intent(applicationContext, MainActivity::class.java)
    //        startActivity(intent)
    //        finish()
    //    }, 2000)
    //}