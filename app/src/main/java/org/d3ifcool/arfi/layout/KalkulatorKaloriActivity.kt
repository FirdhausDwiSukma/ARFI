package org.d3ifcool.arfi.layout

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.textfield.TextInputEditText
import org.d3ifcool.arfi.R
import org.d3ifcool.arfi.databinding.ActivityKalkulatorKaloriBinding

class KalkulatorKaloriActivity : AppCompatActivity() {
    private lateinit var binding : ActivityKalkulatorKaloriBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKalkulatorKaloriBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.calculateButton.setOnClickListener {
            kalkulatorKalori()
        }
    }

    @SuppressLint("StringFormatMatches")
    private fun kalkulatorKalori(){

        //val heigtEditText : TextInputEditText = binding.heightEditText

        val weightEditText: TextInputEditText = binding.weightEditText

        //val repetitionsEditText : TextInputEditText = binding.repetitionsEditText

        val setsEditText : TextInputEditText = binding.setsEditText

        val radioGroupGender = binding.radioGroupGender
        val radioGroupOlahraga = binding.radioGroupOlahraga

        val genderId = radioGroupGender.checkedRadioButtonId
        val gender = when (genderId){
            R.id.radioMale -> "Pria"
            R.id.radioFemale -> "Wanita"
            else -> ""
        }

        val olahragaId = radioGroupOlahraga.checkedRadioButtonId
        val olahragaRadio = when (olahragaId){
            R.id.radioBicep -> "Bicep Curl"
            R.id.radioCrossover -> "Crossover Chest"
            R.id.radioDumbellOverhead -> "Dumbell Overhead"
            R.id.radioGerakanSenam -> "Gerakan Senam"
            R.id.radioJumpingJack -> "Jumping Jack"
            R.id.radioPullDownLow -> "Pull Down Low"
            else -> ""
        }
        //val height = heigtEditText.text.toString().toFloatOrNull()
        val weight = weightEditText.text.toString().toFloatOrNull()
        //val repetitions = repetitionsEditText.text.toString().toIntOrNull()
        val sets = setsEditText.text.toString().toIntOrNull()

        if (olahragaRadio.isNotEmpty() && gender.isNotEmpty() && weight != null && sets != null) {
            val caloriesBurned = calculateCaloriesBurned(olahragaRadio, gender, weight, sets)
            binding.caloriesBurnedTextView.text =
                getString(R.string.calories_burned, caloriesBurned)
        } else {
            binding.caloriesBurnedTextView.text = getString(R.string.invalid_input)
        }
    }
    private fun calculateCaloriesBurned(olahragaRadio: String, gender: String, weight: Float, sets: Int): Double {

        var MET = 0.0
        var minute = 0.0

        if(olahragaRadio == "Bicep Curl"){
            MET = if (gender == "Pria") 4.5 else 4.0
            minute = 0.083//5
        }
        if(olahragaRadio == "Crossover Chest"){
            MET = if (gender == "Pria") 5.0 else 4.5
            minute = 0.167
        }
        if(olahragaRadio == "Dumbell Overhead"){
            MET = if (gender == "Pria") 4.5 else 4.0
            minute = 0.067
        }
        if(olahragaRadio == "Gerakan Senam"){
            MET = if (gender == "Pria") 3.0 else 2.5
            minute = 0.25
        }
        if(olahragaRadio == "Jumping Jack"){
            MET = if (gender == "Pria") 8.0 else 7.5
            minute = 0.083
        }
        if(olahragaRadio == "Pull Down Low"){
            MET = if (gender == "Pria") 5.0 else 4.5
            minute = 0.067
        }
        val totalCalories = MET * weight * minute * sets
        return totalCalories
    }
//      val totalCalories = (MET * weight * (repetitions * sets)) / minute
//      val totalCalories = MET * weight * minute * repetitions * sets
//      val totalCalories = ((MET * weight) * repetitions * sets).toInt()
//      private fun calculateCaloriesBurned(weight: Double, repetitions: Int, sets: Int): Double {
//      val caloriesPerRepetition = 0.1 // Anggap 0.1 kalori per repetisi (angka sembarang)
//      return weight * repetitions * sets * caloriesPerRepetition
//    }
//    private fun calculateCaloriesBurned(height: Float, weight: Float, repetitions: Int, sets: Int): Int {
//
//        val MET = 3.5
//
//        val totalCalories = ((MET * weight) + (MET * height) * repetitions * sets) / 1000
//        return totalCalories.toInt()
//    }
}