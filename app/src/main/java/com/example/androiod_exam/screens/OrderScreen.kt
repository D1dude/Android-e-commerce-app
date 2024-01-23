package com.example.androiod_exam.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.androiod_exam.CustomTopAppBar
import com.example.androiod_exam.DataClass.entity.OrderEntity
import com.example.androiod_exam.viewModel.OrderViewModel
import androidx.compose.runtime.LaunchedEffect as LaunchedEffect1

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable

fun OrderScreen(
    orderViewModel: OrderViewModel,
    navController: NavController
) {// Start oppkall for å hente alle bestillinger når skjermen startes
    LaunchedEffect1(orderViewModel) {
        orderViewModel.getAllOrders()
    }
    // observere bestillingselementene og filtrerte bestillingene
    val orderItems by orderViewModel.orderItems.observeAsState(emptyList())
    val filteredOrderItems by orderViewModel.filteredOrderItems.observeAsState(emptyList())

    // Kombiner listene
    val combinedOrderItems = (orderItems + filteredOrderItems)
    // oversikt over boksene som er togglet
    var isBoxToggled by remember { mutableStateOf<Map<String, Boolean?>>(emptyMap()) }

    Scaffold(
        topBar = {
            CustomTopAppBar(navController = navController)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Order List",
                style = typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (combinedOrderItems.isEmpty()) {
                Text(text = "No orders available.")
            } else {
                LazyColumn {
                    // grupper ordrene etter cartId
                    combinedOrderItems.groupBy { it.cartId }.forEach { (cartId, groupedOrderItems) ->
                        item {
                            // tilstand for å se om boksen er åpen eller lukket
                            val isToggled = isBoxToggled[cartId.toString()] ?: false

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .border(1.dp, Color.Black)
                                    .let {
                                        if (isToggled) {
                                            it
                                        } else {
                                            it.height(150.dp)
                                        }
                                    }
                                    .clickable {
                                        // Oppdater boksen sin tilstand når den klikkes
                                        isBoxToggled = isBoxToggled.toMutableMap().apply {
                                            this[cartId.toString()] = !isToggled
                                        }
                                    }
                            ) {
                                // Log titles for each order item inside the box
                                groupedOrderItems.forEach { orderItem ->
                                    Log.d("OrderScreen", "Title: ${orderItem.title}")
                                    Log.d("OrderScreen", "ID: ${orderItem.cartId}")
                                }
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                ) {
                                    Text(text = "Order ID: $cartId")
                                    groupedOrderItems.forEach { orderItem ->
                                        Text(text = "Title: ${orderItem.title}")
                                        Text(text = "Quantity: ${orderItem.quantity}")
                                        Text(text = "Price: ${orderItem.price} $")
                                        Spacer(modifier = Modifier.height(10.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}