package com.mayurdw.fibretracker.ui.destinations

import androidx.annotation.StringRes
import com.mayurdw.fibretracker.R
import kotlinx.serialization.Serializable

sealed interface Destinations {
    val bottomBarVisible: Boolean
        get() = false

    val backButtonVisible: Boolean
        get() = true
}

@Serializable
object Home : Destinations {
    override val bottomBarVisible: Boolean
        get() = true
    override val backButtonVisible: Boolean
        get() = false
}


@Serializable
object EditMenu : Destinations {
    override val bottomBarVisible: Boolean
        get() = true
    override val backButtonVisible: Boolean
        get() = false
}


@Serializable
object Plan : Destinations {
    override val bottomBarVisible: Boolean
        get() = true
    override val backButtonVisible: Boolean
        get() = false
}

@Serializable
object Chart : Destinations {
    override val bottomBarVisible: Boolean
        get() = true
    override val backButtonVisible: Boolean
        get() = false
}

@Serializable
object Setting : Destinations

@Serializable
object ChooseEntry : Destinations


@Serializable
object SelectFoodToEdit : Destinations

@Serializable
data class EnterEditedFood(
    val selectedFoodId: Int
) : Destinations

@Serializable
object AddFoodItem : Destinations

@Serializable
object AddNewFoodItem : Destinations

@Serializable
data class AddAmountItem(
    val selectedFoodId: Int
) : Destinations

@Serializable
data class EditEntry(
    val selectedEntryId: Int
) : Destinations

@Serializable
object PoopQuality : Destinations

@StringRes
fun getTitle(destinations: Destinations): Int {
    return when (destinations) {
        is Home -> R.string.home
        is AddFoodItem -> R.string.add_food
        is AddNewFoodItem -> R.string.add_new_food
        is EditMenu -> R.string.edit_menu
        is SelectFoodToEdit -> R.string.select_food_to_edit
        is AddAmountItem -> R.string.add_amount
        is EditEntry -> R.string.edit_food_entry
        is EnterEditedFood -> R.string.edit_existing_food
        is Chart -> R.string.chart
        is Plan -> R.string.plan
        is Setting -> R.string.settings
        is ChooseEntry -> R.string.choose_entry
        is PoopQuality -> R.string.poop_quality
    }
}

fun getDestination(routeName: String?): Destinations {
    routeName?.let {
        val screens: List<Destinations> = listOf(
            Home,
            AddNewFoodItem,
            AddFoodItem,
            EditMenu,
            AddAmountItem(-1),
            EditEntry(-1),
            SelectFoodToEdit,
            EnterEditedFood(-1),
            Chart,
            Plan,
            Setting,
            ChooseEntry,
            PoopQuality
        )

        for (screen in screens) {
            if (it.contains(screen::class.java.simpleName, ignoreCase = true)) {
                return screen
            }
        }
        throw IllegalStateException("Screen Name not found $routeName")
    }

    return Home
}
