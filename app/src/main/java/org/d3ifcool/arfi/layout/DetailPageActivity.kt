package org.d3ifcool.arfi.layout

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import org.d3ifcool.arfi.R
import org.d3ifcool.arfi.cameraKit.CameraActivity
import org.d3ifcool.arfi.databinding.ActivityDetailPageBinding
import org.d3ifcool.arfi.model.DataWorkout
import org.d3ifcool.arfi.viewModel.DetailViewModel

class DetailPageActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailPageBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var cameraKit: ExtendedFloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = DetailViewModel()
        val data = intent.extras?.getInt("ID")
        buttonBack()
        val dataWorkout = viewModel.getDataById(data!!)
        setContentDetail(dataWorkout)
        cameraKit = binding.fabMulaiWorkout
        cameraKit.setOnClickListener{
            showDialog(dataWorkout)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables", "SetJavaScriptEnabled")
    private fun setContentDetail(data: DataWorkout?) {
        binding.apply {
            if (data != null) {
                Glide.with(this@DetailPageActivity)
                    .load(data.img_detail_page)
                    .into(imgDetailPage)
                titleWorkout.text = resources.getStringArray(R.array.title_workout)[data.title_detail_page]

                Glide.with(this@DetailPageActivity)
                    .load(getIconSaran(data.desc_saran))
                    .into(iconSaran)
                descSaran.text = resources.getStringArray(R.array.kategori_alat)[data.desc_saran]
                descDetailWorkout.text = resources.getStringArray(R.array.description)[data.id]
                webView.settings.javaScriptEnabled = true
                val videoId = resources.getStringArray(R.array.id_video_workout)[data.id_video]
                val html = """
                                <!DOCTYPE html>
                                <html>
                                <body style="margin: 0">
                                    <iframe
                                        width="100%"
                                        height="200px"
                                        src="https://www.youtube.com/embed/$videoId?autoplay=1&controls=1&showinfo=0&autohide=1"
                                        frameborder="0"
                                        allowfullscreen
                                    ></iframe>
                                </body>
                                </html>
                            """
                webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null)
            }
        }
    }

    @SuppressLint("InflateParams", "UseCompatLoadingForDrawables")
    private fun showDialog(data: DataWorkout?){
        if (data != null) {
            MaterialAlertDialogBuilder(this)
                .setIcon(resources.getDrawable(R.drawable.notification))
                .setTitle(resources.getString(R.string.pemberitahuan))
                .setMessage(resources.getStringArray(R.array.dialog_workout)[data.id])
                .setNegativeButton(resources.getString(R.string.batal)) { dialog, which ->
                    // Respond to negative button press
                }
                .setPositiveButton(resources.getString(R.string.mulai)) { dialog, which ->
                    val intent = Intent(this, CameraActivity::class.java)
                    startActivity(intent)
                }
                .show()
        }
    }

    private fun getIconSaran(descSaran: Int): Int {
        return when(descSaran){
            4 -> R.drawable.cable_crossover
            3 -> R.drawable.machine_gym
            1 -> R.drawable.dumbell_icon
            0 -> R.drawable.dumbell_icon
            else -> 0
        }
    }

    private fun buttonBack(){
        binding.backButtonDetailPage.setOnClickListener{
            finish()
        }
    }
}