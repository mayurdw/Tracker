package com.shypotato.tracker.destinations

import com.shypotato.tracker.ui.destinations.AddAmountItem
import com.shypotato.tracker.ui.destinations.AddFoodItem
import com.shypotato.tracker.ui.destinations.AddNewFoodItem
import com.shypotato.tracker.ui.destinations.EditEntry
import com.shypotato.tracker.ui.destinations.EditMenu
import com.shypotato.tracker.ui.destinations.EnterEditedFood
import com.shypotato.tracker.ui.destinations.Home
import com.shypotato.tracker.ui.destinations.SelectFoodToEdit
import com.shypotato.tracker.ui.destinations.getDestination
import org.junit.Assert.assertEquals
import org.junit.Test

class GetDestinationTest {

    @Test
    fun `Null Value returns Default`() {
        assertEquals(Home, getDestination(null))
    }

    @Test(expected = IllegalStateException::class)
    fun `Unknown Name throws error`() {
        getDestination("")
    }

    @Test
    fun `Home Returns Home`() {
        assertEquals(Home, getDestination("home"))
    }

    @Test
    fun `Add New Food Returns Add New Food`() {
        assertEquals(AddNewFoodItem, getDestination(AddNewFoodItem::class.java.simpleName))
    }

    @Test
    fun `Add Amount Item Returns Add New Item`() {
        assertEquals(AddAmountItem(-1), getDestination("addamountitem"))
    }

    @Test
    fun `Select Food To Edit`() {
        assertEquals(SelectFoodToEdit, getDestination("selectfoodtoedit"))
    }

    @Test
    fun `Enter Edited Food`() {
        assertEquals(EnterEditedFood(-1), getDestination("enterEditedFood"))
    }

    @Test
    fun `Add Food Item`() {
        assertEquals(AddFoodItem, getDestination("AddFoodItem"))
    }

    @Test
    fun `Edit Entry`() {
        assertEquals(EditEntry(-1), getDestination("EditEntry"))
    }

    @Test
    fun `Edit Menu`() {
        assertEquals(EditMenu, getDestination("EditMenu"))
    }
}
