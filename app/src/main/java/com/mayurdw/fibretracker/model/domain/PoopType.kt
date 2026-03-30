package com.mayurdw.fibretracker.model.domain

import com.mayurdw.fibretracker.R

enum class PoopType(val image: Int, val title: String, val desc: String) {
    TYPE_7(
        R.drawable.type_7, "Type 7", "Separate Hard Lumps, like little pebbles (Hard to pass)"
    ),
    TYPE_6(
        R.drawable.type_6, "Type 6", "Hard and lumpy and starting to resemble a sausage"
    ),
    TYPE_5(
        R.drawable.type_5, "Type 5", "Sausage-shaped with cracks on the surface"
    ),
    TYPE_4(
        R.drawable.type_4, "Type 4", "Thinner and more snakelike, plus smooth and soft"
    ),
    TYPE_3(
        R.drawable.type_3, "Type 3", "Soft blobs with clear cut edges\n"
    ),
    TYPE_2(
        R.drawable.type_2, "Type 2", "Fluffy, mushy pieces with ragged edges"
    ),
    TYPE_1(
        R.drawable.type_1, "Type 1", "Watery with no solid pieces\n"
    )
}