package com.shypotato.tracker.model.domain

import com.shypotato.tracker.model.entity.FoodEntity

val CommonFoods = listOf(
    FoodEntity("Oats", 50, 120 * 1_000).apply { id = 1 },
    FoodEntity("Chia", 10, 344 * 1_000).apply { id = 2 },
    FoodEntity("GF Bread", 70, 154 * 1_000).apply { id = 3 },
    FoodEntity("Rice Cake", 35, 35 * 1_000).apply { id = 4 },
    FoodEntity("Banana", 120, 26 * 1_000).apply { id = 5 },
    FoodEntity("Potato", 110, 22 * 1_000).apply { id = 6 },
    FoodEntity("Beans", 50, 34 * 1_000).apply { id = 7 },
    FoodEntity("Broccoli", 80, 32 * 1_000).apply { id = 8 },
    FoodEntity("Psyllium Husk", 2, 800 * 1_000).apply { id = 9 }
)
