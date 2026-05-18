package com.shypotato.tracker.model.domain

import com.shypotato.tracker.model.entity.FoodEntity

val CommonFoods = listOf(
    FoodEntity("Almonds", 10, 123 * 1_000).apply { id = 1 },
    FoodEntity("Banana", 120, 26 * 1_000).apply { id = 2 },
    FoodEntity("Beans", 50, 34 * 1_000).apply { id = 3 },
    FoodEntity("Blueberries", 50, 24 * 1_000).apply { id = 4 },
    FoodEntity("Broccoli", 80, 32 * 1_000).apply { id = 5 },
    FoodEntity("Brussels Sprouts", 50, 44 * 1_000).apply { id = 6 },
    FoodEntity("Carrot", 100, 28 * 1_000).apply { id = 7 },
    FoodEntity("Ceres Baked Peas", 20, 61 * 1_000).apply { id = 8 },
    FoodEntity("Chia", 10, 344 * 1_000).apply { id = 9 },
    FoodEntity("Choco Spread", 20, 100 * 1_000).apply { id = 10 },
    FoodEntity("Freya's Soy and Linseed", 84, 50 * 1_000).apply { id = 11 },
    FoodEntity("GF Bread (Seeded)", 70, 154 * 1_000).apply { id = 12 },
    FoodEntity("GF Bread (Wholemeal)", 70, 135 * 1_000).apply { id = 13 },
    FoodEntity("Mandarin", 120, 18 * 1_000).apply { id = 14 },
    FoodEntity("Oats", 50, 120 * 1_000).apply { id = 15 },
    FoodEntity("Peanut Butter", 30, 90 * 1_000).apply { id = 16 },
    FoodEntity("Ploughman's Soy & Linseed", 100, 58 * 1_000).apply { id = 17 },
    FoodEntity("Potato", 110, 22 * 1_000).apply { id = 18 },
    FoodEntity("Psyllium Husk", 2, 800 * 1_000).apply { id = 19 },
    FoodEntity("Rice Cake", 35, 35 * 1_000).apply { id = 20 },
    FoodEntity("Wholemeal Pasta", 100, 75 * 1_000).apply { id = 21 },
)
