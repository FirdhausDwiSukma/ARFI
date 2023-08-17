package org.d3ifcool.arfi.utils

import org.d3ifcool.arfi.model.DataWorkout

class DummyData() {
    private val data = ArrayList<DataWorkout>()

    init {
        data.add(
            DataWorkout(
                0,
                "https://firebasestorage.googleapis.com/v0/b/arfi-f749e.appspot.com/o/Dumbbell%20Overhead%20Press.jpg?alt=media&token=16db385b-c6c7-4e22-aed6-ee329ffc6cc2",
                0,
                0,
                0
            )
        )
        data.add(
            DataWorkout(
                1,
                "https://firebasestorage.googleapis.com/v0/b/arfi-f749e.appspot.com/o/dumbel_arfi.jpg?alt=media&token=bd9f2893-1f4a-4f74-bfef-3db8d9cb5aae&_gl=1*1s1a9d1*_ga*OTY1MTIyMjAzLjE2ODA2NDc5MDY.*_ga_CW55HF8NVT*MTY4NTc1NDQ4Mi45LjEuMTY4NTc1NTg2My4wLjAuMA..",
                1,
                //R.drawable.dumbell_icon,
                1,
                1
            )
        )
        data.add(
            DataWorkout(
                2,
                "https://firebasestorage.googleapis.com/v0/b/arfi-f749e.appspot.com/o/low%20pull.jpg?alt=media&token=0dfa3c27-496b-4fee-a71d-b821a447e822",
                2,
                //R.drawable.dumbell_icon,
                3,
                2
            )
        )
        data.add(
            DataWorkout(
                3,
                "https://firebasestorage.googleapis.com/v0/b/arfi-f749e.appspot.com/o/Jumping%20jack.jpg?alt=media&token=7453612d-4915-49f4-88d4-2542b81351a2",
                3,
                2,
                3
            )
        )
        data.add(
            DataWorkout(
                4,
                "https://firebasestorage.googleapis.com/v0/b/arfi-f749e.appspot.com/o/Gerakan%20Senam.jpg?alt=media&token=e9c2fb69-d4ad-4803-96e8-68a01d0b6678",
                4,
                2,
                4
            )
        )
        data.add(
            DataWorkout(
                5,
                "https://firebasestorage.googleapis.com/v0/b/arfi-f749e.appspot.com/o/cable%20crossover%20gym.jpeg?alt=media&token=891784f0-da4c-4254-888f-47c269cc374f",
                5,
                4,
                5
            )
        )
    }
    fun getAllData():ArrayList<DataWorkout> = data

    fun getDataByIndex(id: Int): DataWorkout = data[id]
}